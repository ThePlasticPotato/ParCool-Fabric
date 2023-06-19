package com.alrex.parcool;

import com.alrex.parcool.client.input.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ParCoolClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindings.register();
        ModLoadingContext.registerConfig(ParCool.MOD_ID, ModConfig.Type.CLIENT, ParCoolConfig.CLIENT_SPEC);
    }
}
