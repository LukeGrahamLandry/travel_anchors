package ca.lukegrahamlandry.travelstaff;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class FabricModMain implements ModInitializer {
    
    @Override
    public void onInitialize() {
        FabricRegistry.init();
    }
}
