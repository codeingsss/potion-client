/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.mixin;

import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.client.util.Icons;
import net.minecraft.client.util.Window;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Mixin(Window.class)
public class WindowMixin {
    @Redirect(method = "setIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Icons;getIcons(Lnet/minecraft/resource/ResourcePack;)Ljava/util/List;"))
    private List<InputSupplier<InputStream>> potionIcons(Icons icons, ResourcePack resourcePack) {
        return List.of(
            supplier("/assets/meteor-client/window_icon/icon_16x16.png"),
            supplier("/assets/meteor-client/window_icon/icon_32x32.png"),
            supplier("/assets/meteor-client/window_icon/icon_48x48.png"),
            supplier("/assets/meteor-client/window_icon/icon_128x128.png"),
            supplier("/assets/meteor-client/window_icon/icon_256x256.png")
        );
    }

    private static InputSupplier<InputStream> supplier(String path) {
        return () -> {
            InputStream stream = MeteorClient.class.getResourceAsStream(path);
            if (stream == null) throw new IOException("Missing Potion window icon resource: " + path);
            return stream;
        };
    }
}
