package com.codex.memory;

import com.codex.memory.MemoryModData;

import com.codex.memory.config.MemoryClientConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Builds the Cloth Config screen for Memory when the library is present.
 */
public final class MemoryClientClothScreenFactory {
    private MemoryClientClothScreenFactory() {
    }

    public static Screen create(Screen parent) {
        MemoryClientConfig current = MemoryClientConfig.loadOrCreate();

        final int[] intervalSeconds = { current.intervalSeconds() };
        final boolean[] showMessage = { current.showScreenshotMessage() };

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Memory"));
        builder.setSavingRunnable(() -> {
            MemoryClientConfig updated = MemoryClientConfig.overwrite(intervalSeconds[0], showMessage[0]);
            MemoryClientInitializer.reloadClientConfig(updated);
        });

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigEntryBuilder entries = builder.entryBuilder();

        general.addEntry(entries.startIntField(Text.literal("Interval (seconds)"), intervalSeconds[0])
                .setDefaultValue(MemoryModData.DEFAULT_INTERVAL_SECONDS)
                .setTooltip(Text.literal("Minimum 1 second. Default 300 seconds = 5 minutes."))
                .setMin(1)
                .setSaveConsumer(value -> intervalSeconds[0] = value)
                .build());

        general.addEntry(entries.startBooleanToggle(Text.literal("Show Screenshot Message"), showMessage[0])
                .setDefaultValue(MemoryModData.DEFAULT_SHOW_MESSAGE)
                .setTooltip(Text.literal("If enabled, show the vanilla screenshot message in chat."))
                .setSaveConsumer(value -> showMessage[0] = value)
                .build());

        return builder.build();
    }
}
