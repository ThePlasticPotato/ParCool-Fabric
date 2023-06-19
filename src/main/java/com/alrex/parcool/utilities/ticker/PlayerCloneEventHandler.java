package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.common.event.EventPlayerClone;
import dev.onyxstudios.cca.api.v3.entity.PlayerCopyCallback;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerCloneEventHandler implements PlayerCopyCallback {
    @Override
    public void copyData(ServerPlayerEntity original, ServerPlayerEntity clone, boolean lossless) {
        EventPlayerClone.onClone(original, clone, original.isDead());
    }
}
