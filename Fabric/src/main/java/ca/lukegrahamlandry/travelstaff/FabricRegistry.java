package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.travelstaff.block.BlockTravelAnchor;
import ca.lukegrahamlandry.travelstaff.block.TileTravelAnchor;
import ca.lukegrahamlandry.travelstaff.enchantments.RangeEnchantment;
import ca.lukegrahamlandry.travelstaff.enchantments.TeleportationEnchantment;
import ca.lukegrahamlandry.travelstaff.item.ItemTravelStaff;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class FabricRegistry {
    public static void init(){
        Registry.register(Registry.ITEM, Constants.TELEPORT_STAFF_KEY, new ItemTravelStaff(new Item.Properties()));
        Block anchor = new BlockTravelAnchor();
        Registry.register(Registry.ITEM, Constants.TRAVEL_ANCHOR_KEY, new BlockItem(anchor, new Item.Properties()));
        Registry.register(Registry.BLOCK, Constants.TRAVEL_ANCHOR_KEY, anchor);

        Registry.register(Registry.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "range"), RangeEnchantment.INSTANCE);
        Registry.register(Registry.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "teleportation"), TeleportationEnchantment.INSTANCE);

        Registry.register(Registry.BLOCK_ENTITY_TYPE, Constants.TRAVEL_ANCHOR_KEY, FabricBlockEntityTypeBuilder.create(TileTravelAnchor::new, anchor).build(null));
    }
}
