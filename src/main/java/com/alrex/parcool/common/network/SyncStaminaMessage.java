package com.alrex.parcool.common.network;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.common.capability.IStamina;
import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.UUID;
import java.util.function.Supplier;

import static net.fabricmc.api.EnvType.CLIENT;
import static net.fabricmc.api.EnvType.SERVER;

public class SyncStaminaMessage implements C2SPacket, S2CPacket {

    private int stamina = 0;
    private boolean exhausted = false;
    private int clientDemandedMaxValue = 0;
    private UUID playerID = null;

    public void encode(PacketByteBuf packet) {
        packet.writeInt(this.stamina);
        packet.writeBoolean(this.exhausted);
        packet.writeInt(this.clientDemandedMaxValue);
        packet.writeLong(this.playerID.getMostSignificantBits());
        packet.writeLong(this.playerID.getLeastSignificantBits());
    }

    public static SyncStaminaMessage decode(PacketByteBuf packet) {
        SyncStaminaMessage message = new SyncStaminaMessage();
        message.stamina = packet.readInt();
        message.exhausted = packet.readBoolean();
        message.clientDemandedMaxValue = packet.readInt();
        message.playerID = new UUID(packet.readLong(), packet.readLong());
        return message;
    }

    @Environment(CLIENT)
    public static void sync(PlayerEntity player) {
        IStamina stamina = IStamina.get(player);
        if (stamina == null || !player.isMainPlayer()) return;

        SyncStaminaMessage message = new SyncStaminaMessage();
        message.stamina = stamina.get();
        message.exhausted = stamina.isExhausted();
        message.playerID = player.getUuid();
        message.clientDemandedMaxValue = ParCoolConfig.CONFIG_CLIENT.staminaMax.get();

        ParCool.LOGGER.info("Stamina Synced, " + stamina.get() + " Max Stamina : " + stamina.getMaxStamina());
        ParCool.CHANNEL_INSTANCE.sendToServer(message);
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler listener, PacketSender responseSender, SimpleChannel channel) {
        ParCool.CHANNEL_INSTANCE.sendToClient(this, player);
        if (player == null) return;
        IStamina stamina = IStamina.get(player);
        if (stamina == null) return;
        stamina.set(this.stamina);
        stamina.setExhaustion(exhausted);
        stamina.setMaxStamina(clientDemandedMaxValue);
    }
    @Environment(CLIENT)
    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler listener, PacketSender responseSender, SimpleChannel channel) {
        PlayerEntity player;
        if (listener.getConnection().getSide().equals(NetworkSide.CLIENTBOUND)) {
            World world = MinecraftClient.getInstance().world;
            if (world == null) return;
            player = world.getPlayerByUuid(playerID);
            if (player == null || player.isMainPlayer()) return;
        } else {
            player = (PlayerEntity) responseSender;
            ParCool.CHANNEL_INSTANCE.sendToServer(this);
            if (player == null) return;
        }
        IStamina stamina = IStamina.get(player);
        if (stamina == null) return;
        stamina.set(this.stamina);
        stamina.setExhaustion(exhausted);
        stamina.setMaxStamina(clientDemandedMaxValue);
    }
}
