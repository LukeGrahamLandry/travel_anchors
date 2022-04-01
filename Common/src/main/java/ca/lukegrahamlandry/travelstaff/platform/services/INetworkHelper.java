package ca.lukegrahamlandry.travelstaff.platform.services;

import ca.lukegrahamlandry.travelstaff.block.TileTravelAnchor;
import ca.lukegrahamlandry.travelstaff.network.ClientEventSerializer;
import ca.lukegrahamlandry.travelstaff.util.TravelAnchorList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public interface INetworkHelper {
    void sendNameChangeToServer(String trim, BlockPos pos);

    void syncAnchorToClients(TileTravelAnchor tile);

    void sendAnchorListToClients(ServerLevel level, TravelAnchorList travelAnchorList);

    void sendAnchorListToClient(ServerPlayer player);

    void sendClientEventToServer(ClientEventSerializer.ClientEvent event);
}
