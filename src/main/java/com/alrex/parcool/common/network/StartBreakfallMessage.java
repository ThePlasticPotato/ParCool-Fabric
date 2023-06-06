package com.alrex.parcool.common.network;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.action.impl.BreakfallReady;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Parkourability;
import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;
import java.util.function.Supplier;

;

public class StartBreakfallMessage implements S2CPacket, C2SPacket {
    UUID playerID = null;

    public UUID getPlayerID() {
        return playerID;
    }

    @Override
    public void encode(PacketByteBuf packet) {
        packet.writeLong(playerID.getMostSignificantBits());
        packet.writeLong(playerID.getLeastSignificantBits());
    }

    public static StartBreakfallMessage decode(PacketByteBuf packet) {
        StartBreakfallMessage message = new StartBreakfallMessage();
        message.playerID = new UUID(packet.readLong(), packet.readLong());
        return message;
    }

    public static void send(ServerPlayerEntity player) {
        StartBreakfallMessage message = new StartBreakfallMessage();
        message.playerID = player.getUuid();
        ParCool.CHANNEL_INSTANCE.sendToClient(message, player);
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler listener, PacketSender responseSender, SimpleChannel channel) {

    }
    @Environment(EnvType.CLIENT)
    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler listener, PacketSender responseSender, SimpleChannel channel) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (!playerID.equals(player.getUuid())) return;

        Parkourability parkourability = Parkourability.get(player);
        if (parkourability == null) return;
        IStamina stamina = IStamina.get(player);
        if (stamina == null) return;

        parkourability.get(BreakfallReady.class).startBreakfall(player, parkourability, stamina);
    }
}