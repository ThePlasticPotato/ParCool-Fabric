package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.event.EventOpenSettingsParCool;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;

public class ClientTickHandler implements ClientTickEvents.EndTick, ClientTickEvents.StartTick {

    @Override
    public void onEndTick(MinecraftClient client) {
        ParCool.ACTION_PROCESSOR.onTickInClient();
    }

    @Override
    public void onStartTick(MinecraftClient client) {
        EventOpenSettingsParCool.onTick();
    }
}
