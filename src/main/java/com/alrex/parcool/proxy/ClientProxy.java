package com.alrex.parcool.proxy;

import com.alrex.parcool.common.network.*;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.Environment;

import static net.fabricmc.api.EnvType.CLIENT;

@Environment(CLIENT)
public class ClientProxy extends CommonProxy {
	@Override
	public void registerMessages(SimpleChannel instance) {
		instance.registerMessage(
				3,
				StartBreakfallMessage.class,
				StartBreakfallMessage::encode,
				StartBreakfallMessage::decode,
				StartBreakfallMessage::handleClient
		);
		instance.registerMessage(
				10,
				SyncStaminaMessage.class,
				SyncStaminaMessage::encode,
				SyncStaminaMessage::decode,
				SyncStaminaMessage::handleClient
		);
		instance.registerMessage(
				12,
				LimitationByServerMessage.class,
				LimitationByServerMessage::encode,
				LimitationByServerMessage::decode,
				LimitationByServerMessage::handle
		);
		instance.registerMessage(
				15,
				SyncActionStateMessage.class,
				SyncActionStateMessage::encode,
				SyncActionStateMessage::decode,
				SyncActionStateMessage::handleClient
		);
		instance.registerC2SPacket(
				StaminaControlMessage.class,
				16,
				StaminaControlMessage::handle
		);
	}
}
