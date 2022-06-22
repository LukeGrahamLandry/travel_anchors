package ca.lukegrahamlandry.travelstaff.util;

import ca.lukegrahamlandry.travelstaff.block.ScreenTravelAnchor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class GuiHelper {
    public static void openAnchorGui(String name, BlockPos pos) {
        Minecraft.getInstance().setScreen(new ScreenTravelAnchor(name, pos));
    }
}
