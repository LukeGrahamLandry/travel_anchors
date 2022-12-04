package ca.lukegrahamlandry.travelstaff.platform;

import ca.lukegrahamlandry.travelstaff.FabricNetworkHandler;
import ca.lukegrahamlandry.travelstaff.block.TileTravelAnchor;
import ca.lukegrahamlandry.travelstaff.network.AnchorListUpdateSerializer;
import ca.lukegrahamlandry.travelstaff.network.AnchorNameChangeSerializer;
import ca.lukegrahamlandry.travelstaff.network.ClientEventSerializer;
import ca.lukegrahamlandry.travelstaff.network.SyncAnchorTileSerializer;
import ca.lukegrahamlandry.travelstaff.platform.services.INetworkHelper;
import ca.lukegrahamlandry.travelstaff.util.TravelAnchorList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkHelper implements INetworkHelper {
    @Environment(EnvType.CLIENT)
    @Override
    public void sendNameChangeToServer(String name, BlockPos pos) {
        AnchorNameChangeSerializer.AnchorNameChangeMessage msg = new AnchorNameChangeSerializer.AnchorNameChangeMessage(pos, name);
        FriendlyByteBuf data = PacketByteBufs.create();
        AnchorNameChangeSerializer.encode(msg, data);
        ClientPlayNetworking.send(FabricNetworkHandler.ANCHOR_NAME_CHANGE, data);
    }

    @Override
    public void syncAnchorToClients(TileTravelAnchor tile) {
        CompoundTag tag = new CompoundTag();
        tile.saveAdditional(tag);

        ((ServerLevel) tile.getLevel()).getPlayers((p) -> true).forEach((player -> {
            SyncAnchorTileSerializer.AnchorTileMessage msg = new SyncAnchorTileSerializer.AnchorTileMessage(tag, tile.getBlockPos());
            FriendlyByteBuf data = PacketByteBufs.create();
            SyncAnchorTileSerializer.encode(msg, data);
            ServerPlayNetworking.send(player, FabricNetworkHandler.SYNC_ANCHOR_TILE, data);
        }));
    }

    @Override
    public void sendAnchorListToClients(ServerLevel level, TravelAnchorList travelAnchorList) {
        level.getPlayers((p) -> true).forEach(this::sendAnchorListToClient);
    }

    @Override
    public void sendAnchorListToClient(ServerPlayer player) {
        AnchorListUpdateSerializer.AnchorListUpdateMessage msg = new AnchorListUpdateSerializer.AnchorListUpdateMessage(TravelAnchorList.get(player.getCommandSenderWorld()).saveForNetwork());
        FriendlyByteBuf data = PacketByteBufs.create();
        AnchorListUpdateSerializer.encode(msg, data);
        ServerPlayNetworking.send(player, FabricNetworkHandler.SYNC_ANCHOR_LIST, data);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void sendClientEventToServer(ClientEventSerializer.ClientEvent msg) {
        FriendlyByteBuf data = PacketByteBufs.create();
        ClientEventSerializer.encode(msg, data);
        ClientPlayNetworking.send(FabricNetworkHandler.CLIENT_EVENT_TO_SERVER, data);
    }
}
