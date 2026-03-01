# Screenshot to Clipboard Mod (Fabric 1.21.11)

A lightweight Fabric mod for Minecraft 1.21.11 that automatically copies screenshots to your system clipboard when you press `F2`. 

This codebase provides the source files for creating a screenshot-to-clipboard plugin. Included is a standard Fabric toolchain, ready to build and expand upon.

## How it Works
The mod uses a Mixin hooked into Minecraft's `ScreenshotRecorder`. When the `NativeImage` is captured by the game, the mod converts the image and immediately transfers it to your operating system's clipboard. No additional configuration is required.

## Building from Source

This requires Java 21 to compile correctly for Minecraft 1.21.11.

1. Ensure you have **Java 21** installed (`JAVA_HOME` pointing to your JDK 21 installation).
2. Open a terminal in the project directory.
3. Run the Gradle build command:
   * **Windows:** `gradlew build`
   * **Linux/Mac:** `./gradlew build`
4. The compiled `.jar` file will be located in the `build/libs` directory.

## Contributing / Modifying
The main source files are located in:
* `src/main/java/net/user/ScreenshotClipboardClient.java` - Client entrypoint
* `src/main/java/net/user/ClipboardHandler.java` - AWT Clipboard manipulation logic
* `src/main/java/net/user/mixin/ScreenshotRecorderMixin.java` - The Mixin injection point
