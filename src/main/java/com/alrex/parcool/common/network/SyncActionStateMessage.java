package com.alrex.parcool.common.network;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.capability.impl.Parkourability;
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
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.function.Supplier;

;import static net.fabricmc.api.EnvType.CLIENT;
import static net.fabricmc.api.EnvType.SERVER;

public class SyncActionStateMessage implements S2CPacket, C2SPacket {
    private SyncActionStateMessage() {
    }

    private UUID senderUUID = null;
    private byte[] buffer = null;

    public void encode(PacketByteBuf packetBuffer) {
        packetBuffer
                .writeLong(senderUUID.getMostSignificantBits())
                .writeLong(senderUUID.getLeastSignificantBits())
                .writeInt(buffer.length)
                .writeBytes(buffer);
    }

    public static SyncActionStateMessage decode(PacketByteBuf packetBuffer) {
        SyncActionStateMessage message = new SyncActionStateMessage();
        message.senderUUID = new UUID(packetBuffer.readLong(), packetBuffer.readLong());
        int size = packetBuffer.readInt();
        message.buffer = new byte[size];
        packetBuffer.readBytes(message.buffer);
        return message;
    }

    @Environment(CLIENT)
    public static void sync(PlayerEntity player, Encoder builder) {
        ByteBuffer buffer1 = builder.build();
        if (buffer1.limit() == 0) return;
        SyncActionStateMessage message = new SyncActionStateMessage();
        message.senderUUID = player.getUuid();
        message.buffer = new byte[buffer1.limit()];
        buffer1.get(message.buffer);

        ParCool.CHANNEL_INSTANCE.sendToServer(message);
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler listener, PacketSender responseSender, SimpleChannel channel) {
        ParCool.CHANNEL_INSTANCE.sendToClient(this, player);
        if (player == null) return;

        Parkourability parkourability = Parkourability.get(player);
        if (parkourability == null) return;

        Decoder decoder = new Decoder(this.buffer, parkourability);
        while (decoder.hasNext()) {
            ActionSyncData item = decoder.getItem();
            if (item == null) continue;
            Action action = item.getAction();
            switch (item.getType()) {
                case Start:
                    action.setDoing(true);
                    action.onStartInServer(player, parkourability, item.getBuffer());
                    action.onStart(player, parkourability);
                    break;
                case Finish:
                    action.setDoing(false);
                    action.onStopInServer(player);
                    action.onStop(player);
                    break;
                case Normal:
                    action.restoreSynchronizedState(item.getBuffer());
                    break;
            }
        }
    }
    @Environment(CLIENT)
    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler listener, PacketSender responseSender, SimpleChannel channel) {
        PlayerEntity player;
        boolean clientSide;
        if (listener.getConnection().getSide().equals(NetworkSide.CLIENTBOUND)) {
            World world = MinecraftClient.getInstance().world;
            if (world == null) return;
            player = world.getPlayerByUuid(senderUUID);
            if (player == null || !player.isMainPlayer()) return;
            clientSide = true;
        } else {
            player = listener.getWorld().getPlayerByUuid(senderUUID);
            ParCool.CHANNEL_INSTANCE.sendToServer(this);
            if (player == null) return;
            clientSide = false;
        }

        Parkourability parkourability = Parkourability.get(player);
        if (parkourability == null) return;

        Decoder decoder = new Decoder(this.buffer, parkourability);
        while (decoder.hasNext()) {
            ActionSyncData item = decoder.getItem();
            if (item == null) continue;
            Action action = item.getAction();
            switch (item.getType()) {
                case Start:
                    action.setDoing(true);
                    if (clientSide) {
                        action.onStartInOtherClient(player, parkourability, item.getBuffer());
                    } else {
                        action.onStartInServer(player, parkourability, item.getBuffer());
                    }
                    action.onStart(player, parkourability);
                    break;
                case Finish:
                    action.setDoing(false);
                    if (clientSide) {
                        action.onStopInOtherClient(player);
                    } else {
                        action.onStopInServer(player);
                    }
                    action.onStop(player);
                    break;
                case Normal:
                    action.restoreSynchronizedState(item.getBuffer());
                    break;
            }
        }
    }

    public static class Encoder {
        private static final Encoder instance = new Encoder();

        private Encoder() {
        }

        private final ByteBuffer buffer = ByteBuffer.allocate(1024);

        public static Encoder reset() {
            instance.buffer.clear();
            return instance;
        }

        public Encoder appendSyncData(Parkourability parkourability, Action action, ByteBuffer actionBuffer) {
            return append(DataType.Normal, parkourability, action, actionBuffer);
        }

        public Encoder appendStartData(Parkourability parkourability, Action action, ByteBuffer actionBuffer) {
            return append(DataType.Start, parkourability, action, actionBuffer);
        }

        public Encoder appendFinishMsg(Parkourability parkourability, Action action) {
            short id = parkourability.getActionID(action);
            if (id < 0) return this;
            buffer.putShort(id)
                    .put(DataType.Finish.getCode())
                    .putInt(0);
            return this;
        }

        private Encoder append(DataType type, Parkourability parkourability, Action action, ByteBuffer actionBuffer) {
            short id = parkourability.getActionID(action);
            if (id < 0) return this;
            buffer.putShort(id)
                    .put(type.getCode())
                    .putInt(actionBuffer.limit())
                    .put(actionBuffer);
            return this;
        }

        public ByteBuffer build() {
            buffer.flip();
            return buffer;
        }
    }

    private static class Decoder {
        ByteBuffer buffer;
        Parkourability parkourability;

        Decoder(byte[] buf, Parkourability parkourability) {
            buffer = ByteBuffer.wrap(buf);
            this.parkourability = parkourability;
        }

        public boolean hasNext() {
            return buffer.position() < buffer.limit();
        }

        @Nullable
        public ActionSyncData getItem() {
            Action action = parkourability.getActionFromID(buffer.getShort());
            DataType type = DataType.getFromCode(buffer.get());
            int bufferSize = buffer.getInt();
            if (bufferSize > 1024) {
                StringBuilder msgBuilder = new StringBuilder();
                msgBuilder.append("Synchronization failed. demanded buffer size is too large\n")
                        .append(action)
                        .append(":Sync_Type")
                        .append(type)
                        .append('\n')
                        .append(buffer);
                if (buffer.limit() < 128) {
                    buffer.rewind();
                    msgBuilder.append("->{");
                    while (buffer.hasRemaining()) {
                        msgBuilder.append(Integer.toHexString(buffer.get()))
                                .append(',');
                    }
                    msgBuilder.append('}');
                }
                ParCool.LOGGER.warn(msgBuilder.toString());
                buffer.position(buffer.limit());
                return null;
            }
            byte[] array = new byte[bufferSize];
            buffer.get(array);
            if (action == null) {
                return null;
            }
            ByteBuffer buf = ByteBuffer.wrap(array);
            return new ActionSyncData(action, buf, type);
        }
    }

    private enum DataType {
        Normal, Start, Finish;

        public byte getCode() {
            switch (this) {
                case Start:
                    return 1;
                case Finish:
                    return 2;
                default:
                    return 0;
            }
        }

        public static DataType getFromCode(byte code) {
            switch (code) {
                case 1:
                    return Start;
                case 2:
                    return Finish;
                default:
                    return Normal;
            }
        }
    }

    private static class ActionSyncData {
        Action action;
        ByteBuffer buffer;
        DataType type;

        public ActionSyncData(Action action, ByteBuffer buffer, DataType type) {
            this.action = action;
            this.buffer = buffer;
            this.type = type;
        }

        public DataType getType() {
            return type;
        }

        public Action getAction() {
            return action;
        }

        public ByteBuffer getBuffer() {
            return buffer;
        }
    }
}
