/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.scripting;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.scripting.natives.NativeAddonMarker;
import meteordevelopment.meteorclient.utils.player.ChatUtils;

import java.io.File;
import java.util.List;

/**
 * Loads native (".dll"/".so"/".dylib") Potion addons via JNI. Isolated in its own class, like
 * {@link PythonRuntime}, so that referencing {@link NativeAddonMarker} — a class that only exists
 * in the separate "native-addon" companion jar — throws a clean NoClassDefFoundError the caller
 * can catch when that jar isn't installed, instead of failing anywhere else.
 *
 * Unlike ".bsh"/".py" addons, native addons run completely unsandboxed native code in-process:
 * they can crash the game or do anything the OS user running it can do. Only load ones you trust.
 *
 * A native addon is a shared library that implements the standard JNI "JNI_OnLoad" entry point
 * (called automatically once the library is loaded). From there it can call back into the game
 * through the plain static methods on {@link PotionNative} to read player/world state or trigger
 * chat/module actions.
 */
class NativeRuntime {
    private NativeRuntime() {
    }

    static void loadAll(List<File> libs) {
        NativeAddonMarker.touch();

        for (File lib : libs) {
            try {
                System.load(lib.getAbsolutePath());
                MeteorClient.LOG.info("Potion scripting: loaded native addon '{}'.", lib.getName());
            } catch (Throwable t) {
                MeteorClient.LOG.error("Potion scripting: failed to load native addon '{}'.", lib.getName(), t);
                ChatUtils.error("Failed to load Potion native addon '%s': %s", lib.getName(), t.getMessage());
            }
        }
    }
}
