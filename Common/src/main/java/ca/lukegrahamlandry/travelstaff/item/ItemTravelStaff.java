package ca.lukegrahamlandry.travelstaff.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTravelStaff extends Item {
    public ItemTravelStaff(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, List<Component> tooltip, @Nonnull TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("tooltip.travelstaff.travel_staff").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public int getEnchantmentValue() {
        return 20;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }
}
