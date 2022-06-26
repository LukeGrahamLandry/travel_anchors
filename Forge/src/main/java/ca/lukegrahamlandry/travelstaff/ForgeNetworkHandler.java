package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.travelstaff.network.AnchorListUpdateSerializer;
import ca.lukegrahamlandry.travelstaff.network.AnchorNameChangeSerializer;
import ca.lukegrahamlandry.travelstaff.network.ClientEventSerializer;
import ca.lukegrahamlandry.travelstaff.network.SyncAnchorTileSerializer;
import ca.lukegrahamlandry.travelstaff.util.NetworkEventHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class ForgeNetworkHandler {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerPackets(){
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Constants.MOD_ID, "packets"), () -> "1.0", s -> true, s -> true);


        INSTANCE.registerMessage(nextID(), AnchorNameChangeSerializer.AnchorNameChangeMessage.class, AnchorNameChangeSerializer::encode, AnchorNameChangeSerializer::decode, (msg, ctx) -> {
            ServerPlayer player = ctx.get().getSender();
            ctx.get().enqueueWork(() -> {
                if (player != null) {
                    NetworkEventHandler.handleNameChange(player.getLevel(), msg.pos, msg.name);
                }
            });
            ctx.get().setPacketHandled(true);
        }, Optional.of(NetworkDirection.PLAY_TO_SERVER));


        INSTANCE.registerMessage(nextID(), AnchorListUpdateSerializer.AnchorListUpdateMessage.class, AnchorListUpdateSerializer::encode, AnchorListUpdateSerializer::decode, (msg, ctx) -> {
            ctx.get().enqueueWork(() -> {
                ForgeClientHelper.onAnchorListUpdatePacket(msg);
            });
            ctx.get().setPacketHandled(true);
        }, Optional.of(NetworkDirection.PLAY_TO_CLIENT));


        INSTANCE.registerMessage(nextID(), ClientEventSerializer.ClientEvent.class, ClientEventSerializer::encode, ClientEventSerializer::decode, (msg, ctx) -> {
            ServerPlayer player = ctx.get().getSender();
            ctx.get().enqueueWork(() -> {
                if (player != null) {
                    NetworkEventHandler.handleClientEvent(player, msg);
                }
            });
            ctx.get().setPacketHandled(true);
        }, Optional.of(NetworkDirection.PLAY_TO_SERVER));

        INSTANCE.registerMessage(nextID(), SyncAnchorTileSerializer.AnchorTileMessage.class, SyncAnchorTileSerializer::encode, SyncAnchorTileSerializer::decode, (msg, ctx) -> {
            ctx.get().enqueueWork(() -> {
                ForgeClientHelper.onSyncTilePacket(msg);
            });
            ctx.get().setPacketHandled(true);
        }, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }


}
