/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.scripting.natives;

/**
 * Present only inside "native-addon.jar", never in the main "potion-client.jar". Referencing this
 * class from {@code meteordevelopment.meteorclient.scripting.NativeRuntime} is how that class
 * detects whether the native-addon companion jar is installed: a plain class reference throws
 * NoClassDefFoundError at class-init time when it's missing, which the caller catches cleanly.
 */
public final class NativeAddonMarker {
    private NativeAddonMarker() {
    }

    public static void touch() {
    }
}
