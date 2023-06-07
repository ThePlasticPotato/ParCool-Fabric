package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.ParCool;
import io.github.fabricators_of_create.porting_lib.event.common.PlayerTickEvents;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerTickHandler implements PlayerTickEvents.End {
    @Override
    public void onEndOfPlayerTick(PlayerEntity player) {
        if (player.world.isClient) return;
        ParCool.ACTION_PROCESSOR.onTick(player, false);
    }
}
