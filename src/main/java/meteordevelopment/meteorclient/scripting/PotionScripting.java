/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.scripting;

import bsh.EvalError;
import bsh.Interpreter;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.utils.PostInit;
import meteordevelopment.meteorclient.utils.player.ChatUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Loads Potion addons from "<gameDir>/meteor-client/addons/".
 * ".bsh" (BeanShell) addons always work. ".py" (Python) addons only work if the separate
 * "python-addon" companion jar (GraalPy) is also installed alongside Potion Client.
 * Every script gets a "po" variable — see {@link PotionBridge} for what it can do.
 */
public class PotionScripting {
    private static final String NEW_BSH_ADDON_TEMPLATE = """
        myFeatureHandler() {
            run() {
                po.chat("Hello from my new addon!");
            }
            return this;
        }

        po.set("Potion", "my-feature", (Runnable) myFeatureHandler());
        """;

    private PotionScripting() {
    }

    public static File getAddonsDir() {
        return new File(MeteorClient.FOLDER, "addons");
    }

    private static File getPythonLibDir() {
        return new File(getAddonsDir(), ".potion_lib");
    }

    public static File[] listAddonScripts() {
        File[] scripts = getAddonsDir().listFiles((dir, name) -> name.endsWith(".bsh") || name.endsWith(".py"));
        return scripts == null ? new File[0] : scripts;
    }

    private static File getEnabledListFile() {
        return new File(getAddonsDir(), ".enabled.txt");
    }

    /**
     * The set of addon file names that should be loaded on next startup.
     * If no list has been saved yet, every script currently found is considered enabled
     * (so addons work out of the box without needing to open the Potion tab first).
     */
    public static Set<String> getEnabledNames() {
        File file = getEnabledListFile();

        if (!file.exists()) {
            Set<String> all = new LinkedHashSet<>();
            for (File script : listAddonScripts()) all.add(script.getName());
            return all;
        }

        try {
            Set<String> names = new LinkedHashSet<>(Files.readAllLines(file.toPath()));
            names.removeIf(String::isBlank);
            return names;
        } catch (IOException e) {
            MeteorClient.LOG.error("Potion scripting: failed to read the enabled addons list.", e);
            return Set.of();
        }
    }

    public static void setEnabledNames(Set<String> names) {
        try {
            Files.createDirectories(getAddonsDir().toPath());
            Files.write(getEnabledListFile().toPath(), List.copyOf(names));
        } catch (IOException e) {
            MeteorClient.LOG.error("Potion scripting: failed to save the enabled addons list.", e);
        }
    }

    /**
     * Creates a new BeanShell addon script from a template with a unique file name. Note: it is
     * only picked up once the game is restarted, since addons are only loaded once at startup.
     */
    public static File createNewAddon() throws IOException {
        File addonsDir = getAddonsDir();
        Files.createDirectories(addonsDir.toPath());

        String base = "new_addon";
        File file = new File(addonsDir, base + ".bsh");

        for (int i = 1; file.exists(); i++) {
            file = new File(addonsDir, base + i + ".bsh");
        }

        Files.writeString(file.toPath(), NEW_BSH_ADDON_TEMPLATE);
        return file;
    }

    @PostInit
    public static void init() {
        File addonsDir = getAddonsDir();

        try {
            Files.createDirectories(addonsDir.toPath());
        } catch (IOException e) {
            MeteorClient.LOG.error("Potion scripting: failed to set up the addons folder.", e);
            return;
        }

        Set<String> enabled = getEnabledNames();
        List<File> pythonScripts = new ArrayList<>();

        for (File script : listAddonScripts()) {
            if (!enabled.contains(script.getName())) continue;

            if (script.getName().endsWith(".bsh")) {
                loadBeanShell(script);
            } else {
                pythonScripts.add(script);
            }
        }

        if (!pythonScripts.isEmpty()) {
            try {
                PythonRuntime.loadAll(pythonScripts, getPythonLibDir());
            } catch (Throwable t) {
                MeteorClient.LOG.warn("Potion scripting: found .py addons, but the Python runtime (\"python-addon\") isn't installed or failed to start. Install/reinstall it alongside Potion Client to run them.", t);
            }
        }
    }

    private static void loadBeanShell(File script) {
        try {
            Interpreter interpreter = new Interpreter();
            interpreter.set("po", new PotionBridge());
            interpreter.source(script.getAbsolutePath());
            MeteorClient.LOG.info("Potion scripting: loaded addon '{}'.", script.getName());
        } catch (EvalError | IOException e) {
            MeteorClient.LOG.error("Potion scripting: failed to load addon '{}'.", script.getName(), e);
            ChatUtils.error("Failed to load Potion addon '%s': %s", script.getName(), e.getMessage());
        }
    }
}
