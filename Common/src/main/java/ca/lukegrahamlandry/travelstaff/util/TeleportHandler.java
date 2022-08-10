package ca.lukegrahamlandry.travelstaff.util;

import ca.lukegrahamlandry.travelstaff.Constants;
import ca.lukegrahamlandry.travelstaff.block.TileTravelAnchor;
import ca.lukegrahamlandry.travelstaff.enchantments.RangeEnchantment;
import ca.lukegrahamlandry.travelstaff.enchantments.TeleportationEnchantment;
import ca.lukegrahamlandry.travelstaff.item.ItemTravelStaff;
import ca.lukegrahamlandry.travelstaff.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Optional;


// TODO: use tags instead of directly comparing?

public class TeleportHandler {

    public static boolean anchorTeleport(Level level, Player player, @Nullable BlockPos except, @Nullable InteractionHand hand) {
        Pair<BlockPos, String> anchor = getAnchorToTeleport(level, player, except);
        return teleportPlayer(player, anchor, hand);
    }

    public static Pair<BlockPos, String> getAnchorToTeleport(Level level, Player player, @Nullable BlockPos except) {
        boolean allow = !player.isShiftKeyDown() || canBlockTeleport(player);
        if (allow) {
            double maxDistance = getMaxDistance(player);
            Vec3 positionVec = player.position().add(0, player.getEyeHeight(), 0);
            Optional<Pair<BlockPos, String>> anchor = TravelAnchorList.get(level).getAnchorsAround(player.position(), Math.pow(maxDistance, 2))
                    .filter(pair -> except == null || !except.equals(pair.getLeft()))
                    .min((p1, p2) -> {
                        double angle1 = Math.abs(getAngleRadians(positionVec, p1.getLeft(), player.getYRot(), player.getXRot()));
                        double angle2 = Math.abs(getAngleRadians(positionVec, p2.getLeft(), player.getYRot(), player.getXRot()));
                        return Double.compare(angle1, angle2);
                    }).filter(p -> Math.abs(getAngleRadians(positionVec, p.getLeft(), player.getYRot(), player.getXRot())) <= Math.toRadians(Services.CONFIG.getMaxAngle()))
                    .filter(p -> canTeleportTo(level, p.getLeft()));
            return anchor.orElse(null);
        } else {
            return null;
        }
    }
    
    public static boolean teleportPlayer(Player player, @Nullable Pair<BlockPos, String> anchor, @Nullable InteractionHand hand) {
        if (anchor != null) {
            if (!player.getLevel().isClientSide) {
                Vec3 teleportVec = checkTeleport(player, anchor.getLeft().above());
                if (teleportVec == null) {
                    return false;
                }
                player.teleportTo(teleportVec.x()+0.5, teleportVec.y(), teleportVec.z()+0.5);
            }
            player.fallDistance = 0;
            if (hand != null) {
                player.swing(hand, true);
            }
            player.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 1F);
            if (!player.level.isClientSide) {
                player.displayClientMessage(new TranslatableComponent("travelstaff.tp.success", anchor.getRight()), true);
            }
            return true;
        } else {
            if (!player.level.isClientSide) {
                player.displayClientMessage(new TranslatableComponent("travelstaff.tp.fail"), true);
            }
            return false;
        }
    }

    public static boolean shortTeleport(Level level, Player player, InteractionHand hand) {
        Vec3 targetVec = player.position().add(0, player.getEyeHeight(), 0);
        Vec3 lookVec = player.getLookAngle();
        BlockPos target = null;
        for (double i = Services.CONFIG.getShortTeleportDistance(); i >= 2; i -= 0.5) {
            Vec3 v3d = targetVec.add(lookVec.multiply(i, i, i));
            target = new BlockPos(Math.round(v3d.x), Math.round(v3d.y), Math.round(v3d.z));
            if (canTeleportTo(level, target.below())) { //to use the same check as the anchors use the position below
                break;
            } else {
                target = null;
            }
        }
        if (target != null) {
            if (!player.getLevel().isClientSide) {
                Vec3 teleportVec = checkTeleport(player, target);
                if (teleportVec == null) {
                    return false;
                }
                player.teleportTo(teleportVec.x()+0.5, teleportVec.y(), teleportVec.z()+0.5);
            }
            player.fallDistance = 0;
            player.swing(hand, true);
            player.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 1F);
            return true;
        } else {
            if (!player.level.isClientSide) {
                player.displayClientMessage(new TranslatableComponent("travelstaff.hop.fail"), true);
            }
            return false;
        }
    }

    public static boolean canTeleportTo(BlockGetter level, BlockPos target) {
        return !fullBlockAt(level, target.immutable().above(1))
                && !fullBlockAt(level, target.immutable().above(2))
                && target.getY() >= level.getMinBuildHeight();
    }

    private static boolean fullBlockAt(BlockGetter level, BlockPos target){
        return Block.isShapeFullBlock(level.getBlockState(target).getShape(level, target));
    }

    public static boolean canPlayerTeleport(Player player, InteractionHand hand) {
        if (player != null) {
            return canItemTeleport(player, hand) || canBlockTeleport(player);
        }
        return false;
    }

    public static BlockPos down(Player player){
        return new BlockPos(player.getBoundingBox().getCenter().x, player.getY(), player.getBoundingBox().getCenter().z).below();
    }

    public static boolean canBlockTeleport(Player player) {
        return player.getLevel().getBlockState(down(player)).getBlock() == Constants.getTravelAnchor();// && !player.isShiftKeyDown();
    }

    public static boolean canItemTeleport(Player player, InteractionHand hand) {
        return player.getItemInHand(hand).getItem() instanceof ItemTravelStaff
                || EnchantmentHelper.getItemEnchantmentLevel(TeleportationEnchantment.INSTANCE, player.getItemInHand(hand)) >= 1;
    }

    private static double getAngleRadians(Vec3 positionVec, BlockPos anchor, float yRot, float xRot) {
        Vec3 blockVec = new Vec3(anchor.getX() + 0.5 - positionVec.x, anchor.getY() + 1.0 - positionVec.y, anchor.getZ() + 0.5 - positionVec.z).normalize();
        Vec3 lookVec = Vec3.directionFromRotation(xRot, yRot).normalize();
        return Math.acos(lookVec.dot(blockVec));
    }

    public static double getMaxDistance(Player player) {
        int mainHandLevel = EnchantmentHelper.getItemEnchantmentLevel(RangeEnchantment.INSTANCE, player.getItemInHand(InteractionHand.MAIN_HAND));
        int offHandLevel = EnchantmentHelper.getItemEnchantmentLevel(RangeEnchantment.INSTANCE, player.getItemInHand(InteractionHand.OFF_HAND));
        int lvl = Math.max(mainHandLevel, offHandLevel);
        return Services.CONFIG.getMaxDistance() * (1 + (lvl / 2d));
    }
    
    public static boolean canElevate(Player player) {
        if (player != null) {
            return player.getLevel().getBlockState(down(player)).getBlock() == Constants.getTravelAnchor();
        }
        return false;
    }
    
    public static boolean elevateUp(Player player) {
        if (!canElevate(player)) {
            return false;
        }
        Level level = player.getLevel();
        BlockPos.MutableBlockPos searchPos = player.blockPosition().immutable().mutable();
        while (!level.isOutsideBuildHeight(searchPos) && (level.getBlockState(searchPos).getBlock() != Constants.getTravelAnchor() || !canTeleportTo(level, searchPos))) {
            searchPos.move(Direction.UP);
        }
        BlockState state = level.getBlockState(searchPos);
        Pair<BlockPos, String> anchor = null;
        if (state.getBlock() == Constants.getTravelAnchor() && canTeleportTo(level, searchPos)) {
            BlockPos target = searchPos.immutable();
            BlockEntity tile = level.getBlockEntity(target);
            if (tile instanceof TileTravelAnchor) {
                String name = ((TileTravelAnchor) tile).getName();
                if (!name.isEmpty()){
                    anchor = Pair.of(target, name);
                }
            }
        }
        return teleportPlayer(player, anchor, null);
    }
    
    public static boolean elevateDown(Player player) {
        if (!canElevate(player)) {
            return false;
        }
        Level level = player.getLevel();
        BlockPos.MutableBlockPos searchPos = player.blockPosition().immutable().below(2).mutable();
        while (!level.isOutsideBuildHeight(searchPos) && (level.getBlockState(searchPos).getBlock() != Constants.getTravelAnchor() || !canTeleportTo(level, searchPos))) {
            searchPos.move(Direction.DOWN);
        }
        BlockState state = level.getBlockState(searchPos);
        Pair<BlockPos, String> anchor = null;
        if (state.getBlock() == Constants.getTravelAnchor() && canTeleportTo(level, searchPos)) {
            BlockPos target = searchPos.immutable();
            BlockEntity tile = level.getBlockEntity(target);
            if (tile instanceof TileTravelAnchor) {
                String name = ((TileTravelAnchor) tile).getName();
                if (!name.isEmpty()){
                    anchor = Pair.of(target, name);
                }
            }
        }
        return teleportPlayer(player, anchor, null);
    }


    @Nullable
    private static Vec3 checkTeleport(Player player, BlockPos target) {
//        EntityTeleportEvent event = new EntityTeleportEvent(player, target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
//        if (MinecraftForge.EVENT_BUS.post(event)) {
//            return null;
//        }
//        return new Vec3(event.getTargetX(), event.getTargetY(), event.getTargetZ());
        return new Vec3(target.getX(), target.getY(), target.getZ());
    }
}
