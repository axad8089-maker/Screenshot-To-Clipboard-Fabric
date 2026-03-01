package net.user;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenshotClipboardClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("screenshot-clipboard");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Screenshot Clipboard Mod Initialized.");
    }
}
