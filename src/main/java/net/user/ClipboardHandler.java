package net.user;

import net.minecraft.client.texture.NativeImage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class ClipboardHandler {

    /**
     * Copy image to system clipboard from a temp file (file is written on game thread so NativeImage is still valid).
     * Optional onSuccess is run on the AWT thread after copy.
     */
    public static void copyImageFromFile(Path tempFile, Runnable onSuccess) {
        new Thread(() -> {
            try {
                ScreenshotClipboardClient.LOGGER.info("Starting copy from: " + tempFile.toString());
                BufferedImage img = ImageIO.read(tempFile.toFile());
                if (img == null) {
                    ScreenshotClipboardClient.LOGGER.error("Failed to read image from temp file!");
                    Files.deleteIfExists(tempFile);
                    return;
                }
                Files.deleteIfExists(tempFile);

                EventQueue.invokeLater(() -> {
                    try {
                        ScreenshotClipboardClient.LOGGER.info("Attempting to access clipboard...");
                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                        Clipboard clipboard = toolkit.getSystemClipboard();
                        clipboard.setContents(new ClipboardImageTransferable(img), null);
                        ScreenshotClipboardClient.LOGGER.info("Successfully set clipboard contents.");
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (Exception e) {
                        ScreenshotClipboardClient.LOGGER.error("Clipboard access failed! This might be due to headless mode or OS restrictions.", e);
                    }
                });
            } catch (Exception e) {
                ScreenshotClipboardClient.LOGGER.error("Threaded copy failed", e);
                try { Files.deleteIfExists(tempFile); } catch (Exception ignored) {}
            }
        }, "Screenshot Clipboard Thread").start();
    }

    /**
     * Copy image to system clipboard. Optional onSuccess is run on the AWT thread after copy.
     * Prefer copyImageFromFile when you can write the image to a file on the game thread first.
     */
    public static void copyImage(NativeImage nativeImage, Runnable onSuccess) {
        try {
            Path tempFile = Files.createTempFile("minecraft_screenshot_clipboard_", ".png");
            nativeImage.writeTo(tempFile);
            copyImageFromFile(tempFile, onSuccess);
        } catch (Exception e) {
            ScreenshotClipboardClient.LOGGER.error("Failed to prepare screenshot for clipboard", e);
        }
    }

    @Deprecated
    public static void copyImage(NativeImage nativeImage) {
        copyImage(nativeImage, null);
    }
}
