package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.travelstaff.block.RenderTravelAnchor;
import ca.lukegrahamlandry.travelstaff.render.TravelAnchorRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class ForgeModMain {
    
    public ForgeModMain() {
        NetworkInit.registerPackets();
        ForgeRegistry.init(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.register(new ForgeEventListener());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(this::renderAnchors));
    }

    public void renderAnchors(RenderLevelLastEvent event) {
        TravelAnchorRenderer.renderAnchors(event.getPoseStack(), event.getPartialTick());
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvent {
        @SubscribeEvent
        public static void registerRender(EntityRenderersEvent.RegisterRenderers event) {
            System.out.println("qwerty");
            event.registerBlockEntityRenderer(ForgeRegistry.TRAVEL_ANCHOR_TILE.get(), (ctx) -> new RenderTravelAnchor());
        }
    }
}