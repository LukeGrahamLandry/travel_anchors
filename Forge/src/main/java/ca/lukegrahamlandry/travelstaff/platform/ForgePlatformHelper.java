package ca.lukegrahamlandry.travelstaff.platform;

import ca.lukegrahamlandry.travelstaff.ForgeClientHelper;
import ca.lukegrahamlandry.travelstaff.platform.services.IPlatformHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public void renderAnchor(PoseStack matrixStack, MultiBufferSource buffer, BlockState mimic, int combinedLight, boolean glow, boolean active, int distanceSq) {
        ForgeClientHelper.renderAnchor(matrixStack, buffer, mimic, combinedLight, glow, active, distanceSq);
    }

    @Override
    public RenderType createLines(String name, int strength) {
        return ForgeClientHelper.createLines(name, strength);
    }

    @Override
    public boolean accessSortOnUpload(RenderType parent) {
        return ForgeClientHelper.accessSortOnUpload(parent);
    }
}
