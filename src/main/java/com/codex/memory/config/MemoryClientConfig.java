package com.codex.memory.config;

import com.codex.memory.MemoryModData;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loads and persists the Memory client configuration.
 */
public final class MemoryClientConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("memory-config");
    private static final String FILE_NAME = "memory.toml";
    private static final int MIN_INTERVAL_SECONDS = 1;

    private final int intervalSeconds;
    private final boolean showScreenshotMessage;

    private MemoryClientConfig(int intervalSeconds, boolean showScreenshotMessage) {
        this.intervalSeconds = intervalSeconds;
        this.showScreenshotMessage = showScreenshotMessage;
    }

    public int intervalSeconds() {
        return intervalSeconds;
    }

    public boolean showScreenshotMessage() {
        return showScreenshotMessage;
    }

    public static MemoryClientConfig loadOrCreate() {
        MemoryClientConfig defaults = normalized(MemoryModData.DEFAULT_INTERVAL_SECONDS, MemoryModData.DEFAULT_SHOW_MESSAGE);
        Path path = configPath();
        if (!Files.exists(path)) {
            writeConfig(path, defaults);
            return defaults;
        }

        try (CommentedFileConfig file = openFile(path)) {
            file.load();
            MemoryClientConfig loaded = fromToml(file, defaults);
            writeToFile(file, loaded);
            return loaded;
        } catch (Throwable t) {
            LOGGER.error("Failed to load {}: {}", FILE_NAME, t.toString(), t);
            writeConfig(path, defaults);
            return defaults;
        }
    }

    public static MemoryClientConfig overwrite(int intervalSeconds, boolean showScreenshotMessage) {
        MemoryClientConfig updated = normalized(intervalSeconds, showScreenshotMessage);
        writeConfig(configPath(), updated);
        return updated;
    }

    private static MemoryClientConfig fromToml(CommentedConfig config, MemoryClientConfig defaults) {
        int interval = defaults.intervalSeconds;
        Object rawInterval = config.get("interval_seconds");
        if (rawInterval instanceof Number number) {
            interval = number.intValue();
        }

        boolean showMessage = defaults.showScreenshotMessage;
        Object rawShow = config.get("show_screenshot_message");
        if (rawShow instanceof Boolean bool) {
            showMessage = bool;
        }

        return normalized(interval, showMessage);
    }

    private static void writeConfig(Path path, MemoryClientConfig config) {
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            LOGGER.error("Failed to create config directory for {}", FILE_NAME, e);
            return;
        }

        try (CommentedFileConfig file = openFile(path)) {
            file.load();
            writeToFile(file, config);
        } catch (Throwable t) {
            LOGGER.error("Failed to write {}: {}", FILE_NAME, t.toString(), t);
        }
    }

    private static CommentedFileConfig openFile(Path path) {
        return CommentedFileConfig.builder(path)
                .preserveInsertionOrder()
                .writingMode(WritingMode.REPLACE)
                .build();
    }

    private static void writeToFile(CommentedFileConfig file, MemoryClientConfig config) {
        file.set("#", "Memory - Client Config");
        file.set("#1", "Controls automatic screenshot cadence and notification visibility.");

        file.set("interval_seconds", config.intervalSeconds);
        file.setComment("interval_seconds", "Time between screenshots in seconds (default 300 = 5 minutes). Minimum 1.");

        file.set("show_screenshot_message", config.showScreenshotMessage);
        file.setComment("show_screenshot_message", "If true, show the vanilla chat message after each automatic screenshot.");

        file.save();
    }

    private static MemoryClientConfig normalized(int intervalSeconds, boolean showScreenshotMessage) {
        int sanitizedInterval = Math.max(MIN_INTERVAL_SECONDS, intervalSeconds);
        return new MemoryClientConfig(sanitizedInterval, showScreenshotMessage);
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
    }
}
