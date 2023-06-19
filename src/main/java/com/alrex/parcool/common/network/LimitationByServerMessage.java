package com.alrex.parcool.common.network;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.common.action.ActionList;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.common.info.ActionLimitation;
import com.alrex.parcool.common.info.LimitationByServer;
import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

;import static net.fabricmc.api.EnvType.CLIENT;

public class LimitationByServerMessage implements C2SPacket, S2CPacket {
	private boolean forIndividuals = false;
	private boolean enforced = false;
	private int maxStaminaLimitation = Integer.MAX_VALUE;
	private int maxStaminaRecovery = Integer.MAX_VALUE;
	private boolean permissionOfInfiniteStamina = true;
	private final ActionLimitation[] limitations = new ActionLimitation[ActionList.ACTIONS.size()];

	public void setEnforced(boolean enforced) {
		this.enforced = enforced;
	}

	public void setMaxStaminaLimitation(int maxStaminaLimitation) {
		this.maxStaminaLimitation = maxStaminaLimitation;
	}

	public void setPermissionOfInfiniteStamina(boolean permissionOfInfiniteStamina) {
		this.permissionOfInfiniteStamina = permissionOfInfiniteStamina;
	}

	public void setMaxStaminaRecovery(int maxStaminaRecovery) {
		this.maxStaminaRecovery = maxStaminaRecovery;
	}

	public boolean isEnforced() {
		return enforced;
	}

	public ActionLimitation[] getLimitations() {
		return limitations;
	}

	public int getMaxStaminaLimitation() {
		return maxStaminaLimitation;
	}

	public int getMaxStaminaRecovery() {
		return maxStaminaRecovery;
	}

	public boolean getPermissionOfInfiniteStamina() {
		return permissionOfInfiniteStamina;
	}

	public void encode(FriendlyByteBuf packet) {
		packet.writeBoolean(forIndividuals);
		packet.writeBoolean(enforced);
		packet.writeInt(maxStaminaLimitation);
		packet.writeInt(maxStaminaRecovery);
		packet.writeBoolean(permissionOfInfiniteStamina);
		for (ActionLimitation limitation : limitations) {
			packet.writeBoolean(limitation.isPossible())
					.writeInt(limitation.getLeastStaminaConsumption());
		}
	}

	public static LimitationByServerMessage decode(FriendlyByteBuf packet) {
		LimitationByServerMessage message = new LimitationByServerMessage();
		message.forIndividuals = packet.readBoolean();
		message.enforced = packet.readBoolean();
		message.maxStaminaLimitation = packet.readInt();
		message.maxStaminaRecovery = packet.readInt();
		message.permissionOfInfiniteStamina = packet.readBoolean();
		for (int i = 0; i < ActionList.ACTIONS.size(); i++) {
			message.limitations[i]
					= new ActionLimitation(
					packet.readBoolean(),
					packet.readInt()
			);
		}
		return message;
	}

	private static LimitationByServerMessage newInstanceForServerWide() {
		LimitationByServerMessage message = new LimitationByServerMessage();
		ParCoolConfig.Server config = ParCoolConfig.CONFIG_SERVER;

		message.maxStaminaLimitation = config.staminaMax.get();
		message.enforced = ParCoolConfig.CONFIG_SERVER.enforced.get();
		message.permissionOfInfiniteStamina = config.allowInfiniteStamina.get();
		message.maxStaminaRecovery = config.staminaRecoveryMax.get();
		message.forIndividuals = false;
		for (int i = 0; i < ActionList.ACTIONS.size(); i++) {
			message.limitations[i]
					= new ActionLimitation(
					config.getPermissionOf(ActionList.getByIndex(i)),
					config.getLeastStaminaConsumptionOf(ActionList.getByIndex(i))
			);
		}
		return message;
	}

	private static LimitationByServerMessage newInstance(LimitationByServer limitation) {
		LimitationByServerMessage message = new LimitationByServerMessage();

		message.forIndividuals = false;
		limitation.writeSyncData(message);
		return message;
	}

	public static void send(ServerPlayer player) {
		LimitationByServerMessage msg = newInstanceForServerWide();
		msg.forIndividuals = false;
		ParCool.CHANNEL_INSTANCE.sendToClient(msg, player);
	}

	public static void sendIndividualLimitation(ServerPlayer player) {
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null) return;
		LimitationByServerMessage msg = newInstance(parkourability.getActionInfo().getIndividualLimitation());
		msg.forIndividuals = true;
		ParCool.CHANNEL_INSTANCE.sendToClient(msg, player);
	}

	@Override
	public void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel) {

	}
	@Environment(CLIENT)
	@Override
	public void handle(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
			Player player = Minecraft.getInstance().player;
			if (player == null) return;
			Parkourability parkourability = Parkourability.get(player);
			if (parkourability == null) return;
			if (forIndividuals) {
				parkourability.getActionInfo().receiveIndividualLimitation(this);
			} else {
				parkourability.getActionInfo().receiveLimitation(this);
			}
	}
}
