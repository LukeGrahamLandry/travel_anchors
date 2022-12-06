package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.lib.registry.RegistryWrapper;
import ca.lukegrahamlandry.travelstaff.block.BlockTravelAnchor;
import ca.lukegrahamlandry.travelstaff.block.TileTravelAnchor;
import ca.lukegrahamlandry.travelstaff.enchantments.RangeEnchantment;
import ca.lukegrahamlandry.travelstaff.enchantments.TeleportationEnchantment;
import ca.lukegrahamlandry.travelstaff.item.ItemTravelStaff;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeRegistry {
    public static void init(IEventBus modEventBus){
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ENCHANTMENTS.register(modEventBus);
        TILE_ENTITY_TYPES.register(modEventBus);
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final RegistryObject<Block> TRAVEL_ANCHOR = BLOCKS.register(Constants.TRAVEL_ANCHOR_KEY.getPath(), BlockTravelAnchor::new);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public static final RegistryObject<Item> STAFF = ITEMS.register(Constants.TELEPORT_STAFF_KEY.getPath(), () -> new ItemTravelStaff(new Item.Properties()));
    public static final RegistryObject<Item> ANCHOR_ITEM = ITEMS.register(Constants.TRAVEL_ANCHOR_KEY.getPath(), () -> new BlockItem(TRAVEL_ANCHOR.get(), new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Constants.MOD_ID);
    public static final RegistryObject<Enchantment> RANGE = ENCHANTMENTS.register("range", () -> RangeEnchantment.INSTANCE);
    public static final RegistryObject<Enchantment> TELEPORTATION = ENCHANTMENTS.register("teleportation", () -> TeleportationEnchantment.INSTANCE);

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);
    public static final RegistryObject<BlockEntityType<TileTravelAnchor>> TRAVEL_ANCHOR_TILE = TILE_ENTITY_TYPES.register(Constants.TRAVEL_ANCHOR_KEY.getPath(),
            () -> BlockEntityType.Builder.of(TileTravelAnchor::new, TRAVEL_ANCHOR.get()).build(null));
}
