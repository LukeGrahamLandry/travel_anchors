package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.travelstaff.platform.Services;
import ca.lukegrahamlandry.travelstaff.util.EventHandlers;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class ForgeEventListener {
    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer().level.isClientSide()) return;
        Services.NETWORK.sendAnchorListToClient((ServerPlayer) event.getPlayer());
    }

    @SubscribeEvent
    public void playerChangeDim(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getPlayer().level.isClientSide()) return;
        Services.NETWORK.sendAnchorListToClient((ServerPlayer) event.getPlayer());
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        InteractionResult result = EventHandlers.onRightClick(event.getPlayer(), event.getHand(), event.getItemStack());
        if (result != InteractionResult.PASS){
            event.setResult(Event.Result.DENY);
            event.setCancellationResult(result);
        }
    }

    @SubscribeEvent
    public void onEmptyClick(PlayerInteractEvent.RightClickEmpty event) {
        InteractionResult result = EventHandlers.onEmptyClick(event.getPlayer(), event.getHand(), event.getItemStack());
        if (result != InteractionResult.PASS){
            event.setResult(Event.Result.DENY);
            event.setCancellationResult(result);
        }
    }

    @SubscribeEvent
    public void onBlockEmptyClick(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getItemStack().isEmpty()) return;
        InteractionResult result = EventHandlers.onEmptyBlockClick(event.getPlayer(), event.getHand(), event.getItemStack());
        if (result != InteractionResult.PASS){
            event.setResult(Event.Result.DENY);
            event.setCancellationResult(result);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onKey(InputEvent.KeyInputEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS && event.getKey() == Minecraft.getInstance().options.keyJump.getKey().getValue()){
            EventHandlers.onJump(Minecraft.getInstance().player);
        }
        if (event.getAction() == GLFW.GLFW_PRESS && event.getKey() == Minecraft.getInstance().options.keyShift.getKey().getValue()){
            EventHandlers.onSneak();
        }
    }
}
