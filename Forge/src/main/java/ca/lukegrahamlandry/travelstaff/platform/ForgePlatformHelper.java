package ca.lukegrahamlandry.travelstaff.platform;

import ca.lukegrahamlandry.travelstaff.Constants;
import ca.lukegrahamlandry.travelstaff.platform.services.IPlatformHelper;
import ca.lukegrahamlandry.travelstaff.render.TravelAnchorRenderer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.OptionalDouble;

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
        TravelAnchorRenderer.renderAnchor(matrixStack, buffer, null, mimic, combinedLight, glow, active, distanceSq);
    }

    @Override
    public RenderType createLines(String name, int strength) {
        return RenderType.create(Constants.MOD_ID + "_" + name,
                DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 256, false, false,
                RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_LINES_SHADER)
                        .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(strength)))
                        .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                        .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                        .setCullState(RenderStateShard.NO_CULL)
                        .createCompositeState(false)
        );
    }

    @Override
    public boolean accessSortOnUpload(RenderType parent) {
        return parent.sortOnUpload;
    }
}
