package ca.lukegrahamlandry.travelstaff.block;

import ca.lukegrahamlandry.travelstaff.Constants;
import ca.lukegrahamlandry.travelstaff.platform.Services;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ScreenTravelAnchor extends Screen {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/travel_anchor.png");
    private final String name;
    private final BlockPos pos;
    private EditBox textFieldWidget;
    private int imageWidth = 180;
    private int imageHeight = 50;

    public ScreenTravelAnchor(String name, BlockPos pos) {
        super(new TextComponent(name));
        this.name = name;
        this.pos = pos;
    }

    @Override
    public void init() {
        super.init();
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(true);
        this.textFieldWidget = new EditBox(this.font, this.width / 2 - 50, this.height / 2 - 63, 100, 15, new TranslatableComponent("screen.travel_anchors.search"));
        this.textFieldWidget.setMaxLength(32767);
        this.textFieldWidget.changeFocus(true);
        this.textFieldWidget.setValue(name);
    }

    protected void renderBg(@Nonnull PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        this.blit(poseStack, (int) (this.width - this.imageWidth) / 2, (int) (this.height - this.imageHeight) / 2, 0, 0, this.imageWidth, this.imageHeight);
    }

    /*
    protected void renderLabels(@Nonnull PoseStack poseStack, int mouseX, int mouseY) {
        String title = this.title.getString();
        this.font.draw(poseStack, title, (float) (this.imageWidth / 2 - this.font.width(title) / 2), 6.0F, 0x404040);
        boolean empty = this.textFieldWidget.getValue().trim().isEmpty();
        this.font.draw(poseStack, empty ? I18n.get("screen.travel_anchors.nameless") : this.name, 8, (float) (this.imageHeight - 126), empty ? 0xB31616 : 0x404040);
    }

     */

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        // this.renderTooltip(matrixStack, mouseX, mouseY);
        this.textFieldWidget.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    @Nullable
    public GuiEventListener getFocused() {
        return this.textFieldWidget;
    }

    @Override
    public void removed() {
        super.removed();
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
        if (Minecraft.getInstance().level != null) {
            Services.NETWORK.sendNameChangeToServer(this.textFieldWidget.getValue().trim(), this.pos);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputConstants.Key mapping = InputConstants.getKey(keyCode, scanCode);
        //noinspection ConstantConditions
        /*
        if (keyCode != 256 && (this.minecraft.options.keyInventory.isActiveAndMatches(mapping)
                || this.minecraft.options.keyDrop.isActiveAndMatches(mapping))) {
            return true;
        }

         */
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void tick() {
        this.textFieldWidget.tick();
    }
}
