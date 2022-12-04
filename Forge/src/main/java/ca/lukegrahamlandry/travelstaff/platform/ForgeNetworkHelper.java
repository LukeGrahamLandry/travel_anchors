package ca.lukegrahamlandry.travelstaff.platform;

import ca.lukegrahamlandry.travelstaff.ForgeNetworkHandler;
import ca.lukegrahamlandry.travelstaff.block.TileTravelAnchor;
import ca.lukegrahamlandry.travelstaff.network.AnchorListUpdateSerializer;
import ca.lukegrahamlandry.travelstaff.network.AnchorNameChangeSerializer;
import ca.lukegrahamlandry.travelstaff.network.ClientEventSerializer;
import ca.lukegrahamlandry.travelstaff.network.SyncAnchorTileSerializer;
import ca.lukegrahamlandry.travelstaff.platform.services.INetworkHelper;
import ca.lukegrahamlandry.travelstaff.util.TravelAnchorList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class ForgeNetworkHelper implements INetworkHelper {
    @Override
    public void sendNameChangeToServer(String name, BlockPos pos) {
        ForgeNetworkHandler.INSTANCE.sendToServer(new AnchorNameChangeSerializer.AnchorNameChangeMessage(pos, name));
    }

    @Override
    public void syncAnchorToClients(TileTravelAnchor tile) {
        if (tile.getLevel() == null) return;

        CompoundTag tag = new CompoundTag();
        tile.saveAdditional(tag);
        ForgeNetworkHandler.INSTANCE.send(PacketDistributor.DIMENSION.with(tile.getLevel()::dimension), new SyncAnchorTileSerializer.AnchorTileMessage(tag, tile.getBlockPos()));
    }

    @Override
    public void sendAnchorListToClients(ServerLevel level, TravelAnchorList list) {
        if (list == null) {
            list = TravelAnchorList.get(level);
        }
        ForgeNetworkHandler.INSTANCE.send(PacketDistributor.DIMENSION.with(level::dimension), new AnchorListUpdateSerializer.AnchorListUpdateMessage(list.saveForNetwork()));
    }

    @Override
    public void sendAnchorListToClient(ServerPlayer player) {
        ForgeNetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new AnchorListUpdateSerializer.AnchorListUpdateMessage(TravelAnchorList.get(player.getCommandSenderWorld()).saveForNetwork()));
    }

    @Override
    public void sendClientEventToServer(ClientEventSerializer.ClientEvent event) {
        ForgeNetworkHandler.INSTANCE.sendToServer(event);
    }
}
