/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LogoDrawer.class)
public class LogoDrawerMixin {
    private static final Identifier POTION_FONT = Identifier.of("meteor-client", "potion");
    private static final Text TEXT = Text.literal("Potion").setStyle(Style.EMPTY.withFont(new StyleSpriteSource.Font(POTION_FONT)));
    private static final float TEXT_SCALE = 6.0F;

    @Inject(method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V", at = @At("HEAD"), cancellable = true)
    private void onDraw(DrawContext context, int screenWidth, float alpha, int y, CallbackInfo ci) {
        ci.cancel();

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int color = ColorHelper.getWhite(alpha);

        int textWidth = (int) (textRenderer.getWidth(TEXT) * TEXT_SCALE);
        int startX = screenWidth / 2 - textWidth / 2;

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(startX, y);
        context.getMatrices().scale(TEXT_SCALE, TEXT_SCALE);
        context.drawText(textRenderer, TEXT, 0, 0, color, true);
        context.getMatrices().popMatrix();
    }
}
