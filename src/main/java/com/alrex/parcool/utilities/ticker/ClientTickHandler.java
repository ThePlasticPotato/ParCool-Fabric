package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.event.EventOpenSettingsParCool;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class ClientTickHandler implements ClientTickEvents.EndTick, ClientTickEvents.StartTick {
    @Environment(EnvType.CLIENT)
    @Override
    public void onEndTick(MinecraftClient client) {
        if (client.world != null) {
            ParCool.ACTION_PROCESSOR.onTickInClient();
            PlayerEntity player = client.player;
            if (player == null) return;
            ParCool.ACTION_PROCESSOR.onTick(player, true);
        }
    }
    @Environment(EnvType.CLIENT)
    @Override
    public void onStartTick(MinecraftClient client) {
        if (client.world != null) {
            EventOpenSettingsParCool.onTick();
        }
    }
}
