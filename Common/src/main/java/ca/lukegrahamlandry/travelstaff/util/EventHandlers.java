package ca.lukegrahamlandry.travelstaff.util;

import ca.lukegrahamlandry.travelstaff.network.ClientEventSerializer;
import ca.lukegrahamlandry.travelstaff.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EventHandlers {
    // not in use on item because has to check for teleport enchant as well
    public static InteractionResult onRightClick(Player player, InteractionHand hand, ItemStack stack) {
        Level level = player.level;
        if (TeleportHandler.canPlayerTeleport(player, hand) && !stack.isEmpty()) {
            if (player.isShiftKeyDown() && TeleportHandler.canItemTeleport(player, hand)) {
                if (TeleportHandler.shortTeleport(level, player, hand)) {
                    player.getCooldowns().addCooldown(stack.getItem(), 30);
                    return InteractionResult.SUCCESS;
                }
            } else {
                if (TeleportHandler.anchorTeleport(level, player, player.blockPosition().immutable().below(), hand)) {
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    public static InteractionResult onEmptyClick(Player player, InteractionHand hand, ItemStack stack) {
        if (TeleportHandler.canBlockTeleport(player) && !player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND && player.getItemInHand(InteractionHand.OFF_HAND).isEmpty() && stack.isEmpty()) {
            Services.NETWORK.sendClientEventToServer(ClientEventSerializer.ClientEvent.EMPTY_HAND_INTERACT);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult emptyBlockClick(Player player, InteractionHand hand, ItemStack stack) {
        if (hand == InteractionHand.MAIN_HAND) {
            // Empty offhand does not count. In that case the main hand will either produce
            // this event or PlayerInteractEvent.RightClickItem
            if (TeleportHandler.canPlayerTeleport(player, hand)) {
                if (!player.isShiftKeyDown()) {
                    if (TeleportHandler.anchorTeleport(player.level, player, player.blockPosition().immutable().below(), hand)) {
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    public static void onJump(Player player) {
        if (Services.CONFIG.disableElevation()) {
            if (TeleportHandler.canBlockTeleport(player) && !player.isShiftKeyDown()) {
                Services.NETWORK.sendClientEventToServer(ClientEventSerializer.ClientEvent.JUMP_TP);
            }
        } else {
            if (TeleportHandler.canElevate(player) && !player.isShiftKeyDown()) {
                Services.NETWORK.sendClientEventToServer(ClientEventSerializer.ClientEvent.JUMP);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onSneak() {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().options.keyShift.consumeClick()) {
            if (!Services.CONFIG.disableElevation()) {
                if (TeleportHandler.canElevate(Minecraft.getInstance().player)) {
                    Services.NETWORK.sendClientEventToServer(ClientEventSerializer.ClientEvent.SNEAK);
                }
            }
        }
    }
}
