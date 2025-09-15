package com.codex.memory;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;

/**
 * Registers the Memory config screen with Mod Menu when Cloth Config is available.
 */
public final class MemoryClientModMenuBridge implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
                return MemoryClientClothScreenFactory.create(parent);
            }
            return parent;
        };
    }
}
