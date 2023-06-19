package com.alrex.parcool.proxy;

import com.alrex.parcool.common.network.*;
import me.pepperbell.simplenetworking.SimpleChannel;

public class AllProxy extends CommonProxy {
    @Override
    public void registerMessages(SimpleChannel instance) {
        instance.registerC2SPacket(
                StartBreakfallMessage.class,
                3,
                StartBreakfallMessage::decode
        );
        instance.registerC2SPacket(
                SyncStaminaMessage.class,
                10,
                SyncStaminaMessage::decode
        );
        instance.registerC2SPacket(
                LimitationByServerMessage.class,
                12,
                LimitationByServerMessage::decode
        );
        instance.registerC2SPacket(
                SyncActionStateMessage.class,
                15,
                SyncActionStateMessage::decode
        );
        instance.registerC2SPacket(
                StaminaControlMessage.class,
                16,
                StaminaControlMessage::decode
        );
        instance.registerS2CPacket(
                StartBreakfallMessage.class,
                3,
                StartBreakfallMessage::decode
        );
        instance.registerS2CPacket(
                SyncStaminaMessage.class,
                10,
                SyncStaminaMessage::decode
        );
        instance.registerS2CPacket(
                LimitationByServerMessage.class,
                12,
                LimitationByServerMessage::decode
        );
        instance.registerS2CPacket(
                SyncActionStateMessage.class,
                15,
                SyncActionStateMessage::decode

        );
        instance.registerS2CPacket(
                StaminaControlMessage.class,
                16,
                StaminaControlMessage::decode
        );
    }
}
