package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.travelstaff.render.TravelAnchorRenderer;
import net.minecraftforge.client.event.RenderLevelLastEvent;

public class ForgeClientEvents {
    public static void renderAnchors(RenderLevelLastEvent event) {
        TravelAnchorRenderer.renderAnchors(event.getPoseStack(), event.getPartialTick());
    }
}
