package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.lib.event.fabric.WrapperLibClientInitializer;
import ca.lukegrahamlandry.travelstaff.block.RenderTravelAnchor;
import ca.lukegrahamlandry.travelstaff.network.AnchorListUpdateSerializer;
import ca.lukegrahamlandry.travelstaff.network.SyncAnchorTileSerializer;
import ca.lukegrahamlandry.travelstaff.util.NetworkEventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.Minecraft;

import static ca.lukegrahamlandry.travelstaff.FabricNetworkHandler.SYNC_ANCHOR_LIST;
import static ca.lukegrahamlandry.travelstaff.FabricNetworkHandler.SYNC_ANCHOR_TILE;

@Environment(EnvType.CLIENT)
public class FabricModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new WrapperLibClientInitializer().onInitializeClient();
        BlockEntityRendererRegistry.register(TravelAnchorRegistry.TRAVEL_ANCHOR_TILE.get(), (ctx) -> new RenderTravelAnchor());
        initClientPackets();
    }

    @Environment(EnvType.CLIENT)
    public static void initClientPackets(){
        ClientPlayNetworking.registerGlobalReceiver(SYNC_ANCHOR_LIST, (client, handler, data, responseSender) -> {
            AnchorListUpdateSerializer.AnchorListUpdateMessage msg = AnchorListUpdateSerializer.decode(data);
            client.execute(() -> NetworkEventHandler.handleClientUpdateAnchorList(Minecraft.getInstance().level, msg.nbt));
        });

        ClientPlayNetworking.registerGlobalReceiver(SYNC_ANCHOR_TILE, (client, handler, data, responseSender) -> {
            SyncAnchorTileSerializer.AnchorTileMessage msg = SyncAnchorTileSerializer.decode(data);
            client.execute(() -> NetworkEventHandler.handleSyncAnchorTile(Minecraft.getInstance().level, msg.nbt, msg.pos));
        });
    }
}