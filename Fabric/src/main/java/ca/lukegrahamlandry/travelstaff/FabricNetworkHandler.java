package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.travelstaff.network.AnchorListUpdateSerializer;
import ca.lukegrahamlandry.travelstaff.network.AnchorNameChangeSerializer;
import ca.lukegrahamlandry.travelstaff.network.ClientEventSerializer;
import ca.lukegrahamlandry.travelstaff.network.SyncAnchorTileSerializer;
import ca.lukegrahamlandry.travelstaff.util.NetworkEventHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class FabricNetworkHandler {
    public static final ResourceLocation SYNC_ANCHOR_TILE = new ResourceLocation(Constants.MOD_ID, "sync_anchor_tile");
    public static final ResourceLocation SYNC_ANCHOR_LIST = new ResourceLocation(Constants.MOD_ID, "sync_anchor_list");
    public static final ResourceLocation CLIENT_EVENT_TO_SERVER = new ResourceLocation(Constants.MOD_ID, "clientevent_to_server");
    public static final ResourceLocation ANCHOR_NAME_CHANGE = new ResourceLocation(Constants.MOD_ID, "anchor_name_change");

    public static void init(){
        ServerPlayNetworking.registerGlobalReceiver(CLIENT_EVENT_TO_SERVER, (server, player, handler, data, responseSender) -> {
            if (player == null) return;
            ClientEventSerializer.ClientEvent msg = ClientEventSerializer.decode(data);
            server.execute(() -> NetworkEventHandler.handleClientEvent(player, msg));
        });

        ServerPlayNetworking.registerGlobalReceiver(ANCHOR_NAME_CHANGE, (server, player, handler, data, responseSender) -> {
            if (player == null) return;
            AnchorNameChangeSerializer.AnchorNameChangeMessage msg = AnchorNameChangeSerializer.decode(data);
            server.execute(() -> NetworkEventHandler.handleNameChange(player.getLevel(), msg.pos, msg.name));
        });
    }

    public static void initClient(){
        ClientPlayNetworking.registerGlobalReceiver(SYNC_ANCHOR_LIST, (client, handler, data, responseSender) -> {
            AnchorListUpdateSerializer.AnchorListUpdateMessage msg = AnchorListUpdateSerializer.decode(data);
            client.execute(() -> NetworkEventHandler.handleClientUpdateAnchorList(msg.nbt));
        });

        ClientPlayNetworking.registerGlobalReceiver(SYNC_ANCHOR_TILE, (client, handler, data, responseSender) -> {
            SyncAnchorTileSerializer.AnchorTileMessage msg = SyncAnchorTileSerializer.decode(data);
            client.execute(() -> NetworkEventHandler.handleSyncAnchorTile(msg.nbt, msg.pos));
        });
    }
}
