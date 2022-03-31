package ca.lukegrahamlandry.travelstaff.util;

import ca.lukegrahamlandry.travelstaff.block.TileTravelAnchor;
import ca.lukegrahamlandry.travelstaff.network.ClientEventSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NetworkEventHandler {
    public static void handleClientUpdateAnchorList(CompoundTag nbt){
        if (Minecraft.getInstance().level == null) return;

        TravelAnchorList.get(Minecraft.getInstance().level).load(nbt);
    }

    public static void handleNameChange(ServerLevel level, BlockPos pos, String name){
        if (level == null) return;

        if (level.hasChunkAt(pos)) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TileTravelAnchor) {
                ((TileTravelAnchor) be).setName(name);
                be.setChanged();
            }
        }
    }
    public static void handleClientEvent(ServerPlayer player, ClientEventSerializer.ClientEvent msg) {
        if (player == null) return;

        switch (msg) {
            case JUMP:
                if (TeleportHandler.canElevate(player)) {
                    if (TeleportHandler.elevateUp(player)) {
                        player.setDeltaMovement(player.getDeltaMovement().multiply(1, 0, 1));
                    }
                }
                break;

            case EMPTY_HAND_INTERACT:
                if (TeleportHandler.canBlockTeleport(player) && !player.isShiftKeyDown()) {
                    TeleportHandler.anchorTeleport(player.getCommandSenderWorld(), player, player.blockPosition().immutable().below(), InteractionHand.MAIN_HAND);
                }
                break;

            case SNEAK:
                if (TeleportHandler.canElevate(player)) {
                    if (TeleportHandler.elevateDown(player)) {
                        player.setDeltaMovement(player.getDeltaMovement().multiply(1, 0, 1));
                    }
                }
                break;

            case JUMP_TP:
                // Client has configured to use jump as telport not elevate
                System.out.println("no staff tele try "  + TeleportHandler.canBlockTeleport(player));
                if (TeleportHandler.canBlockTeleport(player)) {
                    TeleportHandler.anchorTeleport(player.getLevel(), player, TeleportHandler.down(player), null);
                }
                break;
        }
    }

    public static void handleSyncAnchorTile(CompoundTag nbt, BlockPos pos) {
        if (Minecraft.getInstance().level == null) return;

        if (Minecraft.getInstance().level.hasChunkAt(pos)) {
            BlockEntity be = Minecraft.getInstance().level.getBlockEntity(pos);
            if (be instanceof TileTravelAnchor) {
                ((TileTravelAnchor) be).setName(nbt.getString("travel_anchor_name"));
                ((TileTravelAnchor) be).readMimic(nbt);
            }
        }
    }


    // player changes a name on client
    // client -> server handleNameChange
    // then server sends it to all clients
    // server -> client handleSyncAnchorTile
}
