package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.travelstaff.render.TravelAnchorRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ExampleMod {
    
    public ExampleMod() {
        CommonClass.init();
        NetworkInit.registerPackets();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(ForgeClientEvents::renderAnchors));

    }
}