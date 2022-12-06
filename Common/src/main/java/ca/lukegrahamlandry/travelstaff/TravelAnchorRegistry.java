package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.lib.registry.RegistryWrapper;
import ca.lukegrahamlandry.travelstaff.block.BlockTravelAnchor;
import ca.lukegrahamlandry.travelstaff.block.TileTravelAnchor;
import ca.lukegrahamlandry.travelstaff.enchantments.RangeEnchantment;
import ca.lukegrahamlandry.travelstaff.enchantments.TeleportationEnchantment;
import ca.lukegrahamlandry.travelstaff.item.ItemTravelStaff;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class TravelAnchorRegistry {
    public static final RegistryWrapper<Block> BLOCKS = RegistryWrapper.create(Registry.BLOCK, TravelStaffMain.MOD_ID);
    public static final Supplier<Block> TRAVEL_ANCHOR = BLOCKS.register("travel_anchor", BlockTravelAnchor::new);

    public static final RegistryWrapper<Item> ITEMS = RegistryWrapper.create(Registry.ITEM, TravelStaffMain.MOD_ID);
    public static final Supplier<Item> STAFF = ITEMS.register(new ResourceLocation(TravelStaffMain.MOD_ID, "travel_staff").getPath(), () -> new ItemTravelStaff(new Item.Properties()));
    public static final Supplier<Item> ANCHOR_ITEM = ITEMS.register("travel_anchor", () -> new BlockItem(TRAVEL_ANCHOR.get(), new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));

    public static final RegistryWrapper<Enchantment> ENCHANTMENTS = RegistryWrapper.create(Registry.ENCHANTMENT, TravelStaffMain.MOD_ID);
    public static final Supplier<Enchantment> RANGE = ENCHANTMENTS.register("range", () -> RangeEnchantment.INSTANCE);
    public static final Supplier<Enchantment> TELEPORTATION = ENCHANTMENTS.register("teleportation", () -> TeleportationEnchantment.INSTANCE);

    public static final RegistryWrapper<BlockEntityType<?>> TILE_ENTITY_TYPES = RegistryWrapper.create(Registry.BLOCK_ENTITY_TYPE, TravelStaffMain.MOD_ID);
    public static final Supplier<BlockEntityType<TileTravelAnchor>> TRAVEL_ANCHOR_TILE = TILE_ENTITY_TYPES.register("travel_anchor",
            () -> BlockEntityType.Builder.of(TileTravelAnchor::new, TRAVEL_ANCHOR.get()).build(null));
}
