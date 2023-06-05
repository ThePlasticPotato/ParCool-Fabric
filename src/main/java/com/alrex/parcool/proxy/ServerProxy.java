package com.alrex.parcool.proxy;

import com.alrex.parcool.common.network.*;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public class ServerProxy extends CommonProxy {
	@Override
	public void registerMessages(SimpleChannel instance) {
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
