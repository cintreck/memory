package com.codex.memory;

import static com.codex.memory.MemoryModData.LOGGER;

import com.codex.memory.config.MemoryClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ScreenshotRecorder;

/**
 * Wires the Memory screenshot scheduler into the client tick loop.
 */
public final class MemoryClientInitializer implements ClientModInitializer {
    private static MemoryClientInitializer instance;

    private MemoryClientConfig config;
    private MemoryClientScreenshotTimer timer;

    @Override
    public void onInitializeClient() {
        instance = this;
        applyConfig(MemoryClientConfig.loadOrCreate());
        ClientTickEvents.END_CLIENT_TICK.register(this::handleTick);
    }

    private void applyConfig(MemoryClientConfig newConfig) {
        this.config = newConfig;
        long intervalMillis = Math.max(1L, newConfig.intervalSeconds()) * 1000L;
        this.timer = new MemoryClientScreenshotTimer(intervalMillis);
        LOGGER.info(
                "Memory configuration applied: automatic screenshots every {} seconds. Chat message {}.",
                newConfig.intervalSeconds(),
                newConfig.showScreenshotMessage() ? "visible" : "hidden"
        );
    }

    private void handleTick(MinecraftClient client) {
        if (client == null || timer == null) {
            return;
        }

        if (client.world == null || client.player == null || client.isPaused()) {
            timer.reset(System.currentTimeMillis());
            return;
        }

        long nowMillis = System.currentTimeMillis();
        if (!timer.shouldCapture(nowMillis)) {
            return;
        }

        captureScreenshot(client, nowMillis);
    }

    private void captureScreenshot(MinecraftClient client, long nowMillis) {
        LOGGER.debug("Capturing Memory screenshot at {}", nowMillis);
        ScreenshotRecorder.saveScreenshot(
                client.runDirectory,
                client.getFramebuffer(),
                text -> {
                    if (config.showScreenshotMessage()) {
                        client.execute(() -> client.inGameHud.getChatHud().addMessage(text));
                    }
                }
        );
        timer.markCaptured(nowMillis);
    }

    public static void reloadClientConfig(MemoryClientConfig newConfig) {
        if (instance != null) {
            instance.applyConfig(newConfig);
        }
    }
}
