package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.travelstaff.block.RenderTravelAnchor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import  net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class FabricModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(FabricRegistry.ANCHOR_TILE, (ctx) -> new RenderTravelAnchor());
        FabricNetworkHandler.initClient();
    }
}