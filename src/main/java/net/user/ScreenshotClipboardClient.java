package net.user;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenshotClipboardClient implements ClientModInitializer {
    public static final String MOD_ID = "screenshot_clipboard";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Screenshot to Clipboard initialized!");
    }
}
