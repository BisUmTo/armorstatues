package fuzs.armorstatues.api.client.gui.screens.armorstand;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.armorstatues.api.ArmorStatuesApi;
import fuzs.armorstatues.api.network.client.data.DataSyncHandler;
import fuzs.armorstatues.api.world.inventory.ArmorStandHolder;
import fuzs.armorstatues.api.world.inventory.ArmorStandMenu;
import fuzs.armorstatues.api.world.inventory.data.ArmorStandScreenType;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ArmorStandEquipmentScreen extends AbstractContainerScreen<ArmorStandMenu> implements ArmorStandScreen {
    private static final ResourceLocation ARMOR_STAND_EQUIPMENT_LOCATION = new ResourceLocation(ArmorStatuesApi.MOD_ID, "textures/gui/container/armor_stand/equipment.png");

    private final Inventory inventory;
    private final DataSyncHandler dataSyncHandler;
    private int mouseX;
    private int mouseY;

    public ArmorStandEquipmentScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super((ArmorStandMenu) holder, inventory, component);
        this.inventory = inventory;
        this.dataSyncHandler = dataSyncHandler;
        this.imageWidth = 210;
        this.imageHeight = 188;
    }

    @Override
    public ArmorStandHolder getHolder() {
        return this.menu;
    }

    @Override
    public <T extends Screen & MenuAccess<ArmorStandMenu> & ArmorStandScreen> T createScreenType(ArmorStandScreenType screenType) {
        T screen = ArmorStandScreenFactory.createScreenType(screenType, this.menu, this.inventory, this.title, this.dataSyncHandler);
        screen.setMouseX(this.mouseX);
        screen.setMouseY(this.mouseY);
        return screen;
    }

    @Override
    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    @Override
    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(AbstractArmorStandScreen.makeCloseButton(this, this.leftPos, this.imageWidth, this.topPos));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (AbstractArmorStandScreen.handleTabClicked((int) mouseX, (int) mouseY, this.leftPos, this.topPos, this.imageHeight, this, this.dataSyncHandler.tabs())) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // hook this in before super, as the default key tab is normally used for cycling focused widgets (which we don't really need...)
        if (AbstractArmorStandScreen.tryCycleTabs(this, keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float pPartialTick) {
        this.renderBackground(poseStack);
        this.renderBg(poseStack, pPartialTick, mouseX, mouseY);
        super.render(poseStack, mouseX, mouseY, pPartialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
        if (this.menu.getCarried().isEmpty()) {
            AbstractArmorStandScreen.findHoveredTab(this.leftPos, this.topPos, this.imageHeight, mouseX, mouseY, this.dataSyncHandler.tabs()).ifPresent(hoveredTab -> {
                this.renderTooltip(poseStack, hoveredTab.getComponent(), mouseX, mouseY);
            });
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float pPartialTick, int pX, int pY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ARMOR_STAND_EQUIPMENT_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        AbstractArmorStandScreen.drawTabs(poseStack, this.leftPos, this.topPos, this.imageHeight, this, this.dataSyncHandler.tabs());
        InventoryScreen.renderEntityInInventory(i + 104, j + 84, 30, (float) (i + 104 - 10) - this.mouseX, (float) (j + 84 - 44) - this.mouseY, this.menu.getArmorStand());
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

    }

    @Override
    public ArmorStandScreenType getScreenType() {
        return ArmorStandScreenType.EQUIPMENT;
    }
}
