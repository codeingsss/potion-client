/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.scripting;

import meteordevelopment.meteorclient.MeteorClient;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * All GraalPy (org.graalvm.polyglot) references live in this one class, isolated from
 * {@link PotionScripting}, so that a missing Python runtime ("python-addon" not installed)
 * fails as a clean NoClassDefFoundError the caller can catch, instead of anywhere else.
 */
class PythonRuntime {
    private static final String POTION_PY = """
        import polyglot

        _bridge = polyglot.import_value("__potion_bridge__")

        set = _bridge.set
        command = _bridge.command
        command_with_args = _bridge.commandWithArgs
        java = _bridge.java
        chat = _bridge.chat
        error = _bridge.error
        log = _bridge.log
        ip = _bridge.ip
        in_singleplayer = _bridge.inSingleplayer
        username = _bridge.username
        health = _bridge.health
        max_health = _bridge.maxHealth
        hunger = _bridge.hunger
        x = _bridge.x
        y = _bridge.y
        z = _bridge.z
        yaw = _bridge.yaw
        pitch = _bridge.pitch
        gamemode = _bridge.gamemode
        dimension = _bridge.dimension
        ping = _bridge.ping
        fps = _bridge.fps
        tps = _bridge.tps
        is_sneaking = _bridge.isSneaking
        is_sprinting = _bridge.isSprinting
        is_on_ground = _bridge.isOnGround
        is_submerged_in_water = _bridge.isSubmergedInWater
        experience_level = _bridge.experienceLevel
        held_item = _bridge.heldItem
        world_time = _bridge.worldTime
        is_raining = _bridge.isRaining
        is_thundering = _bridge.isThundering
        difficulty = _bridge.difficulty
        is_module_active = _bridge.isModuleActive
        toggle_module = _bridge.toggleModule
        enable_module = _bridge.enableModule
        disable_module = _bridge.disableModule
        """;

    private PythonRuntime() {
    }

    static void loadAll(List<File> scripts, File libDir) {
        // Pure-Python packages can be dropped into "site-packages" (e.g. by running
        // "pip install --target <that folder> <package>" on your own machine) and will be
        // importable from addon scripts. Packages needing native/C extensions won't work.
        File sitePackagesDir = new File(libDir, "site-packages");

        try {
            Files.createDirectories(libDir.toPath());
            Files.createDirectories(sitePackagesDir.toPath());
            Files.writeString(new File(libDir, "Potion.py").toPath(), POTION_PY);
        } catch (IOException e) {
            MeteorClient.LOG.error("Potion scripting: failed to set up the Python addon lib folder.", e);
            return;
        }

        String pythonPath = libDir.getAbsolutePath() + File.pathSeparator + sitePackagesDir.getAbsolutePath();

        Context context = Context.newBuilder("python")
            .allowAllAccess(true)
            .option("python.PythonPath", pythonPath)
            .out(System.out)
            .err(System.err)
            .build();

        context.getPolyglotBindings().putMember("__potion_bridge__", new PotionBridge());

        for (File script : scripts) {
            try {
                context.eval(Source.newBuilder("python", script).build());
                MeteorClient.LOG.info("Potion scripting: loaded addon '{}'.", script.getName());
            } catch (Exception e) {
                MeteorClient.LOG.error("Potion scripting: failed to load addon '{}'.", script.getName(), e);
                PotionScripting.reportLoadError(String.format("Failed to load Potion addon '%s': %s", script.getName(), e.getMessage()));
            }
        }
    }
}
