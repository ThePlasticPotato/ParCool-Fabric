package com.alrex.parcool.common.network;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.capability.IStamina;
import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.EnvType;
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

import java.util.function.Supplier;

import static net.fabricmc.api.EnvType.CLIENT;
import static net.fabricmc.api.EnvType.SERVER;

public class StaminaControlMessage implements S2CPacket, C2SPacket {
    private int value = 0;
    private boolean add = false;

    public void encode(PacketByteBuf packet) {
        packet.writeInt(this.value);
        packet.writeBoolean(this.add);
    }

    public static StaminaControlMessage decode(PacketByteBuf packet) {
        StaminaControlMessage message = new StaminaControlMessage();
        message.value = packet.readInt();
        message.add = packet.readBoolean();
        return message;
    }

    public static void sync(ServerPlayerEntity player, int value, boolean add) {
        StaminaControlMessage message = new StaminaControlMessage();
        message.value = value;
        message.add = add;

        ParCool.CHANNEL_INSTANCE.sendToClient(message,player);
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler listener, PacketSender responseSender, SimpleChannel channel) {

    }
    @Environment(CLIENT)
    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler listener, PacketSender responseSender, SimpleChannel channel) {
        PlayerEntity player;
        if (listener.getConnection().getSide().equals(NetworkSide.CLIENTBOUND)) {
            player = MinecraftClient.getInstance().player;
        } else {
            player = (PlayerEntity) responseSender;
        }
        if (player == null) return;
        IStamina stamina = IStamina.get(player);
        if (stamina == null) return;
        if (add) {
            stamina.recover(value);
        } else {
            stamina.set(value);
        }
    }
}
