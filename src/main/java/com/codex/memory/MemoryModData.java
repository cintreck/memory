package com.codex.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shared constants for the Memory mod.
 */
public final class MemoryModData {
    public static final String MOD_ID = "memory";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final int DEFAULT_INTERVAL_SECONDS = 300;
    public static final boolean DEFAULT_SHOW_MESSAGE = false;

    private MemoryModData() {
    }
}
