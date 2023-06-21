package com.alrex.parcool;

import com.alrex.parcool.client.hud.impl.StaminaRenderer;
import com.alrex.parcool.client.input.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ParCoolClient implements ClientModInitializer {

    public static StaminaRenderer staminaRenderer = new StaminaRenderer(MinecraftClient.getInstance());

    @Override
    public void onInitializeClient() {
        KeyBindings.register();
        ModLoadingContext.registerConfig(ParCool.MOD_ID, ModConfig.Type.CLIENT, ParCoolConfig.CLIENT_SPEC);
    }
}
