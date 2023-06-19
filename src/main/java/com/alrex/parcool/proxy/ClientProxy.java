package com.alrex.parcool.proxy;

import com.alrex.parcool.common.network.*;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.Environment;

import static net.fabricmc.api.EnvType.CLIENT;

@Environment(CLIENT)
public class ClientProxy extends CommonProxy {
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
	}
}
