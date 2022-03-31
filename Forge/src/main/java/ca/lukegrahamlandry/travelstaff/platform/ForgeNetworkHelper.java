package ca.lukegrahamlandry.travelstaff.platform;

import ca.lukegrahamlandry.travelstaff.NetworkInit;
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
    public static ForgeNetworkHelper INSTANCE = new ForgeNetworkHelper();


    @Override
    public void sendNameChangeToServer(String name, BlockPos pos) {
        NetworkInit.INSTANCE.sendToServer(new AnchorNameChangeSerializer.AnchorNameChangeMessage(pos, name));
    }

    @Override
    public void syncAnchorToClients(TileTravelAnchor tile) {
        if (tile.getLevel() == null) return;

        CompoundTag tag = new CompoundTag();
        tile.saveAdditional(tag);
        NetworkInit.INSTANCE.send(PacketDistributor.DIMENSION.with(tile.getLevel()::dimension), new SyncAnchorTileSerializer.AnchorTileMessage(tag, tile.getBlockPos()));
    }

    @Override
    public void sendAnchorListToClients(ServerLevel level, TravelAnchorList list) {
        if (list == null) {
            list = TravelAnchorList.get(level);
        }
        NetworkInit.INSTANCE.send(PacketDistributor.DIMENSION.with(level::dimension), new AnchorListUpdateSerializer.AnchorListUpdateMessage(list.save(new CompoundTag())));
    }

    @Override
    public void sendAnchorListToClients(ServerPlayer player) {
        NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new AnchorListUpdateSerializer.AnchorListUpdateMessage(TravelAnchorList.get(player.getCommandSenderWorld()).save(new CompoundTag())));
    }

    @Override
    public void sendClientEventToServer(ClientEventSerializer.ClientEvent event) {
        NetworkInit.INSTANCE.sendToServer(event);
    }
}
