package ca.lukegrahamlandry.travelstaff.platform;

import ca.lukegrahamlandry.travelstaff.block.TileTravelAnchor;
import ca.lukegrahamlandry.travelstaff.network.ClientEventSerializer;
import ca.lukegrahamlandry.travelstaff.platform.services.INetworkHelper;
import ca.lukegrahamlandry.travelstaff.util.TravelAnchorList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkHelper implements INetworkHelper {
    @Override
    public void sendNameChangeToServer(String trim, BlockPos pos) {

    }

    @Override
    public void syncAnchorToClients(TileTravelAnchor tile) {

    }

    @Override
    public void sendAnchorListToClients(ServerLevel level, TravelAnchorList travelAnchorList) {

    }

    @Override
    public void sendAnchorListToClients(ServerPlayer player) {

    }

    @Override
    public void sendClientEventToServer(ClientEventSerializer.ClientEvent event) {

    }
}
