package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.travelstaff.network.AnchorListUpdateSerializer;
import ca.lukegrahamlandry.travelstaff.network.SyncAnchorTileSerializer;
import ca.lukegrahamlandry.travelstaff.render.TravelAnchorRenderer;
import ca.lukegrahamlandry.travelstaff.util.NetworkEventHandler;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.OptionalDouble;

public class ForgeClientHelper {
    public static void onSyncTilePacket(SyncAnchorTileSerializer.AnchorTileMessage msg){
        NetworkEventHandler.handleSyncAnchorTile(Minecraft.getInstance().level, msg.nbt, msg.pos);
    }

    public static void onAnchorListUpdatePacket(AnchorListUpdateSerializer.AnchorListUpdateMessage msg) {
        NetworkEventHandler.handleClientUpdateAnchorList(Minecraft.getInstance().level, msg.nbt);
    }

    public static void renderAnchor(PoseStack matrixStack, MultiBufferSource buffer, BlockState mimic, int combinedLight, boolean glow, boolean active, int distanceSq) {
        TravelAnchorRenderer.renderAnchor(matrixStack, buffer, null, mimic, combinedLight, glow, active, distanceSq);
    }

    public static RenderType createLines(String name, int strength) {
        return RenderType.create(TravelStaffMain.MOD_ID + "_" + name,
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

    public static boolean accessSortOnUpload(RenderType parent) {
        return parent.sortOnUpload;
    }
}
