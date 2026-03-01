package net.user.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import net.user.ClipboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Files;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotRecorderMixin {

    private static Runnable makeCopiedToClipboardMessage() {
        return () -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.inGameHud != null) {
                client.execute(() ->
                    client.inGameHud.getChatHud().addMessage(Text.translatable("message.screenshot_clipboard.copied"))
                );
            }
        };
    }

    /**
     * 1.21.1 and earlier: takeScreenshot(Framebuffer) returns NativeImage.
     * Write to temp file on game thread (before caller can close image), then copy to clipboard in background.
     */
    @Inject(
        method = "takeScreenshot(Lnet/minecraft/client/gl/Framebuffer;)Lnet/minecraft/client/texture/NativeImage;",
        at = @At("RETURN"),
        require = 0
    )
    private static void onTakeScreenshotReturn(CallbackInfoReturnable<NativeImage> cir) {
        NativeImage image = cir.getReturnValue();
        if (image == null) return;
        try {
            java.nio.file.Path tempFile = Files.createTempFile("minecraft_screenshot_clipboard_", ".png");
            image.writeTo(tempFile);
            ClipboardHandler.copyImageFromFile(tempFile, makeCopiedToClipboardMessage());
        } catch (Exception e) {
            net.user.ScreenshotClipboardClient.LOGGER.error("Failed to prepare screenshot for clipboard", e);
        }
    }

    /**
     * 1.21.7 / 1.21.11: takeScreenshot(Framebuffer, Consumer<NativeImage>) - wrap callback, write to file on game thread then copy.
     */
    @ModifyVariable(
        method = "takeScreenshot(Lnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V",
        at = @At("HEAD"),
        argsOnly = true,
        require = 0
    )
    private static Consumer<NativeImage> wrapTakeScreenshotCallback(Consumer<NativeImage> callback) {
        if (callback == null) return null;
        return image -> {
            if (image != null) {
                try {
                    java.nio.file.Path tempFile = Files.createTempFile("minecraft_screenshot_clipboard_", ".png");
                    image.writeTo(tempFile);
                    ClipboardHandler.copyImageFromFile(tempFile, makeCopiedToClipboardMessage());
                } catch (Exception e) {
                    net.user.ScreenshotClipboardClient.LOGGER.error("Failed to prepare screenshot for clipboard", e);
                }
            }
            callback.accept(image);
        };
    }

    /**
     * 1.21.7 / 1.21.11: takeScreenshot(Framebuffer, int, Consumer<NativeImage>) - wrap callback.
     */
    @ModifyVariable(
        method = "takeScreenshot(Lnet/minecraft/client/gl/Framebuffer;ILjava/util/function/Consumer;)V",
        at = @At("HEAD"),
        argsOnly = true,
        index = 2,
        require = 0
    )
    private static Consumer<NativeImage> wrapTakeScreenshotWithFactorCallback(Consumer<NativeImage> callback) {
        if (callback == null) return null;
        return image -> {
            if (image != null) {
                try {
                    java.nio.file.Path tempFile = Files.createTempFile("minecraft_screenshot_clipboard_", ".png");
                    image.writeTo(tempFile);
                    ClipboardHandler.copyImageFromFile(tempFile, makeCopiedToClipboardMessage());
                } catch (Exception e) {
                    net.user.ScreenshotClipboardClient.LOGGER.error("Failed to prepare screenshot for clipboard", e);
                }
            }
            callback.accept(image);
        };
    }
}
