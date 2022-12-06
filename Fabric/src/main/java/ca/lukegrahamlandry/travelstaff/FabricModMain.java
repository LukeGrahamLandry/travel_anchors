package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.lib.event.fabric.WrapperLibModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class FabricModMain implements ModInitializer {
    @Override
    public void onInitialize() {
        new WrapperLibModInitializer().onInitialize();
        TravelStaffMain.init();
        FabricEventListener.init();
        FabricNetworkHandler.init();
    }
}
