package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.ParCool;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;

public class ClientTickHandler implements ClientTickEvents.EndTick {

    @Override
    public void onEndTick(MinecraftClient client) {
        ParCool.ACTION_PROCESSOR.onTickInClient();
    }
}
