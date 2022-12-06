package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.travelstaff.util.EventHandlers;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = TravelStaffMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientEventListener {
    @SubscribeEvent
    public void onKey(InputEvent.Key event) {
        if (event.getAction() == GLFW.GLFW_PRESS && event.getKey() == Minecraft.getInstance().options.keyJump.getKey().getValue()){
            EventHandlers.onJump(Minecraft.getInstance().player);
        }
        if (event.getAction() == GLFW.GLFW_PRESS && event.getKey() == Minecraft.getInstance().options.keyShift.getKey().getValue()){
            EventHandlers.onSneak(Minecraft.getInstance().player);
        }
    }
}
