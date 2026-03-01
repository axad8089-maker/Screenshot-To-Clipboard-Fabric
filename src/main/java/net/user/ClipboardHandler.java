package net.user;

import net.minecraft.client.texture.NativeImage;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class ClipboardHandler {

    public static void copyImage(NativeImage nativeImage) {
        new Thread(() -> {
            try {
                // The most version-agnostic and reliable way is to let NativeImage write
                // to a temporary file via its standard STB writer (which writes PNG)
                // and then read that PNG into standard Java AWT classes.
                Path tempFile = Files.createTempFile("minecraft_screenshot_clipboard_", ".png");
                nativeImage.writeTo(tempFile);

                BufferedImage img = ImageIO.read(tempFile.toFile());

                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new ClipboardImageTransferable(img), null);

                ScreenshotClipboardClient.LOGGER.info("Copied screenshot to clipboard.");
                Files.deleteIfExists(tempFile);
            } catch (Exception e) {
                ScreenshotClipboardClient.LOGGER.error("Failed to copy screenshot to clipboard", e);
            }
        }, "Screenshot Clipboard Thread").start();
    }
}
