package net.user.mixin;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.client.gl.Framebuffer;
import net.user.ClipboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotRecorderMixin {
    @Inject(method = "takeScreenshot", at = @At("RETURN"), require = 1)
    private static void onTakeScreenshot(Framebuffer framebuffer, CallbackInfoReturnable<NativeImage> cir) {
        NativeImage image = cir.getReturnValue();
        if (image != null) {
            ClipboardHandler.copyImage(image);
        }
    }
}
