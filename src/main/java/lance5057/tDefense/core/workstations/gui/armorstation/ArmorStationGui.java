package lance5057.tDefense.core.workstations.gui.armorstation;

import com.google.common.collect.Lists;
import lance5057.tDefense.Reference;
import lance5057.tDefense.core.library.ArmorBuildGuiInfo;
import lance5057.tDefense.core.library.TCRegistry;
import lance5057.tDefense.core.library.TDClientRegistry;
import lance5057.tDefense.core.network.ArmorStationSelectionPacket;
import lance5057.tDefense.core.tools.bases.ArmorBase;
import lance5057.tDefense.core.tools.bases.ArmorCore;
import lance5057.tDefense.core.workstations.tileentities.ArmorStationTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;
import slimeknights.mantle.client.gui.GuiElement;
import slimeknights.mantle.client.gui.GuiElementScalable;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.Icons;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.tinkering.IModifyable;
import slimeknights.tconstruct.library.tinkering.IToolStationDisplay;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.tools.common.client.GuiTinkerStation;
import slimeknights.tconstruct.tools.common.client.module.GuiInfoPanel;
import slimeknights.tconstruct.tools.common.inventory.SlotToolStationIn;
import slimeknights.tconstruct.tools.common.network.ToolStationTextPacket;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class ArmorStationGui extends GuiTinkerStation {

    public static final int Column_Count = 5;
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/toolstation.png");
    private static final GuiElement TextFieldActive = new GuiElement(0, 210, 102, 12, 256, 256);
    private static final GuiElement ItemCover = new GuiElement(176, 18, 80, 64);
    private static final GuiElement SlotBackground = new GuiElement(176, 0, 18, 18);
    private static final GuiElement SlotBorder = new GuiElement(194, 0, 18, 18);
    private static final GuiElement SlotSpaceTop = new GuiElement(0, 174 + 2, 18, 2);
    private static final GuiElement SlotSpaceBottom = new GuiElement(0, 174, 18, 2);
    private static final GuiElement PanelSpaceL = new GuiElement(0, 174, 5, 4);
    private static final GuiElement PanelSpaceR = new GuiElement(9, 174, 9, 4);
    private static final GuiElement BeamLeft = new GuiElement(0, 180, 2, 7);
    private static final GuiElement BeamRight = new GuiElement(131, 180, 2, 7);
    private static final GuiElementScalable BeamCenter = new GuiElementScalable(2, 180, 129, 7);
    private static final GuiElement PlayerDisplay = new GuiElement(188, 138, 68, 86);
    private static final GuiElement ArmorSlots = new GuiElement(152, 224, 104, 32);
    private static final int Table_slot_count = 6;
    public GuiTextField textField;
    public ArmorBuildGuiInfo currentInfo = ArmorStationGuiButtonRepair.info;
    protected GuiElement buttonDecorationTop = SlotSpaceTop;
    protected GuiElement buttonDecorationBot = SlotSpaceBottom;
    protected GuiElement panelDecorationL = PanelSpaceL;
    protected GuiElement panelDecorationR = PanelSpaceR;
    protected GuiElement beamL = new GuiElement(0, 0, 0, 0);
    protected GuiElement beamR = new GuiElement(0, 0, 0, 0);
    protected GuiElementScalable beamC = new GuiElementScalable(0, 0, 0, 0);
    protected ArmorStationGuiButtons buttons;
    protected int activeSlots; // how many of the available slots are active
    protected GuiInfoPanel toolInfo;
    protected GuiInfoPanel traitInfo;

    public ArmorStationGui(InventoryPlayer playerInv, World world, BlockPos pos, ArmorStationTile armorStationTile) {
        super(world, pos, (ArmorStationContainer) armorStationTile.createContainer(playerInv, world, pos));

        buttons = new ArmorStationGuiButtons(this, inventorySlots);
        this.addModule(buttons);
        toolInfo = new GuiInfoPanel(this, inventorySlots);
        this.addModule(toolInfo);
        traitInfo = new GuiInfoPanel(this, inventorySlots);
        this.addModule(traitInfo);

        toolInfo.yOffset = 5;
        traitInfo.yOffset = toolInfo.getYSize() + 9;

        this.ySize = 174;

        wood();
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
        ent.rotationYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
        ent.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);

        // workaround to line up the tabs on switching even though the GUI is a tad higher
        this.guiTop += 4;
        this.cornerY += 4;

        textField = new GuiTextField(0, fontRenderer, cornerX + 70, cornerY + 7, 92, 12);
        //textField.setFocused(true);
        //textField.setCanLoseFocus(false);
        textField.setEnableBackgroundDrawing(false);
        textField.setMaxStringLength(40);

        buttons.xOffset = -2;
        buttons.yOffset = beamC.h + buttonDecorationTop.h;
        toolInfo.xOffset = 2;
        toolInfo.yOffset = beamC.h + panelDecorationL.h;
        traitInfo.xOffset = toolInfo.xOffset;
        traitInfo.yOffset = toolInfo.yOffset + toolInfo.getYSize() + 4;

//    for(GuiModule module : modules) {
//      module.guiTop += 4;
//    }

        updateGUI();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    public Set<ArmorCore> getBuildableItems() {
        return TCRegistry.getArmorStationCrafting();
    }

    public void onToolSelection(ArmorBuildGuiInfo data) {
        activeSlots = Math.min(data.positions.size(), Table_slot_count);
        currentInfo = data;

        ArmorCore tool = null;

        if (data.armor.getItem() instanceof ArmorCore) {
            tool = (ArmorCore) data.armor.getItem();
        }

        ((ArmorStationContainer) inventorySlots).setToolSelection(tool, activeSlots);
        // update the server (and others)
        TinkerNetwork.sendToServer(new ArmorStationSelectionPacket(tool, activeSlots));
        updateGUI();
    }

    public void onToolSelectionPacket(ArmorStationSelectionPacket armorStationSelectionPacket) {
        ArmorBuildGuiInfo info = TDClientRegistry.getArmorBuildInfoForArmor(armorStationSelectionPacket.armor);
        if (info == null) {
            info = ArmorStationGuiButtonRepair.info;
        }
        activeSlots = armorStationSelectionPacket.activeSlots;
        currentInfo = info;

        buttons.setSelectedButtonByTool(currentInfo.armor);

        updateGUI();
    }

    public void updateGUI() {
        int i;
        for (i = 0; i < activeSlots; i++) {
            Point point = currentInfo.positions.get(i);

            Slot slot = inventorySlots.getSlot(i);
            slot.xPos = point.getX();
            slot.yPos = point.getY();
        }

        // remaining slots
        int stillFilled = 0;
        for (; i < Table_slot_count; i++) {
            Slot slot = inventorySlots.getSlot(i);

            if (slot.getHasStack()) {
                slot.xPos = 87 + 20 * stillFilled;
                slot.yPos = 62;
                stillFilled++;
            } else {
                // todo: slot.disable
                slot.xPos = 0;
                slot.yPos = 0;
            }
        }

        updateDisplay();
    }

    @Override
    public void updateDisplay() {
        // tool info of existing or tool to build
        ArmorStationContainer container = (ArmorStationContainer) inventorySlots;
        ItemStack toolStack = container.getResult();
        if (toolStack.isEmpty()) {
            toolStack = inventorySlots.getSlot(0).getStack();
        }

        // current tool to build or repair/modify
        if (toolStack.getItem() instanceof IModifyable) {
            if (toolStack.getItem() instanceof IToolStationDisplay) {
                IToolStationDisplay tool = (IToolStationDisplay) toolStack.getItem();
                toolInfo.setCaption(tool.getLocalizedToolName());
                toolInfo.setText(tool.getInformation(toolStack));
            } else {
                toolInfo.setCaption(toolStack.getDisplayName());
                toolInfo.setText();
            }

            traitInfo.setCaption(I18n.translateToLocal("gui.toolstation.traits"));

            List<String> mods = Lists.newLinkedList();
            List<String> tips = Lists.newLinkedList();
            NBTTagList tagList = TagUtil.getModifiersTagList(toolStack);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound tag = tagList.getCompoundTagAt(i);
                ModifierNBT data = ModifierNBT.readTag(tag);

                // get matching modifier
                IModifier modifier = TinkerRegistry.getModifier(data.identifier);
                if (modifier == null || modifier.isHidden()) {
                    continue;
                }

                mods.add(data.getColorString() + modifier.getTooltip(tag, true));
                tips.add(data.getColorString() + modifier.getLocalizedDesc());
            }

            if (mods.isEmpty()) {
                mods.add(I18n.translateToLocal("gui.toolstation.noTraits"));
            }

            traitInfo.setText(mods, tips);
        }
        // repair info
        else if (currentInfo.armor.isEmpty()) {
            toolInfo.setCaption(I18n.translateToLocal("gui.toolstation.repair"));
            toolInfo.setText();

            traitInfo.setCaption(null);
            String c = TextFormatting.DARK_GRAY.toString();
            String[] art = new String[]{
                    c,
                    c,
                    c + "       .",
                    c + "     /( _________",
                    c + "     |  >:=========`",
                    c + "     )(  ",
                    c + "     \"\""
            };
            traitInfo.setText(art);
        }
        // tool build info
        else {
            ArmorCore tool = (ArmorCore) currentInfo.armor.getItem();
            toolInfo.setCaption(tool.getLocalizedToolName());
            toolInfo.setText(tool.getLocalizedDescription());

            // Components
            List<String> text = Lists.newLinkedList();
            List<PartMaterialType> pms = tool.getRequiredComponents();
            for (int i = 0; i < pms.size(); i++) {
                PartMaterialType pmt = pms.get(i);
                StringBuilder sb = new StringBuilder();

                ItemStack slotStack = container.getSlot(i).getStack();
                if (!pmt.isValid(slotStack)) {
                    sb.append(TextFormatting.RED);

                    // is an item in the slot?
                    if (slotStack.getItem() instanceof IToolPart) {
                        if (pmt.isValidItem((IToolPart) slotStack.getItem())) {
                            // the item has an invalid material
                            warning(Util.translate("gui.error.wrong_material_part"));
                        }
                    }
                }

                sb.append(" * ");
                for (IToolPart part : pmt.getPossibleParts()) {
                    if (part instanceof Item) {
                        sb.append(((Item) part).getItemStackDisplayName(new ItemStack((Item) part)));
                        sb.append("/");
                    }
                }
                sb.deleteCharAt(sb.length() - 1); // removes last '/'
                text.add(sb.toString());
            }
            traitInfo.setCaption(I18n.translateToLocal("gui.toolstation.components"));
            traitInfo.setText(text.toArray(new String[text.size()]));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!textField.isFocused()) {
            super.keyTyped(typedChar, keyCode);
        } else {
            if (keyCode == 1) {
                this.mc.player.closeScreen();
            }

            textField.textboxKeyTyped(typedChar, keyCode);
            TinkerNetwork.sendToServer(new ToolStationTextPacket(textField.getText()));
            ((ArmorStationContainer) container).setToolName(textField.getText());
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        textField.updateCursorCounter();
    }

    @Override
    public void drawSlot(Slot slotIn) {
        // don't draw dormant slots with no item
        if (slotIn instanceof SlotToolStationIn && ((SlotToolStationIn) slotIn).isDormant() && !slotIn.getHasStack()) {
            return;
        }

        super.drawSlot(slotIn);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawBackground(BACKGROUND);

        if (textField.isFocused()) {
            TextFieldActive.draw(cornerX + 68, cornerY + 6);
        }

        // draw textfield
        textField.drawTextBox();

        //int xOff = 3;
        //int yOff = 6;

        int x = 0;
        int y = 0;

        // draw the item background
        final float scale = 3.7f;
        final float xOff = 10f;
        final float yOff = 22f;
        GlStateManager.translate(xOff, yOff, 0);
        GlStateManager.scale(scale, scale, 1.0f);
        {
            int logoX = (int) (this.cornerX / scale);
            int logoY = (int) (this.cornerY / scale);

            if (currentInfo != null) {
                if (!currentInfo.armor.isEmpty()) {
                    itemRender.renderItemIntoGUI(currentInfo.armor, logoX, logoY);
                } else if (currentInfo == ArmorStationGuiButtonRepair.info) {
                    this.mc.getTextureManager().bindTexture(Icons.ICON);
                    Icons.ICON_Anvil.draw(logoX, logoY);
                }
            }
        }
        GlStateManager.scale(1f / scale, 1f / scale, 1.0f);
        GlStateManager.translate(-xOff, -yOff, 0);

        // rebind gui texture since itemstack drawing sets it to something else
        this.mc.getTextureManager().bindTexture(BACKGROUND);

        // reset state after item drawing
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();

        // draw the halftransparent "cover" over the item
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.82f);
        ItemCover.draw(this.cornerX + 7, this.cornerY + 18);

        // the slot backgrounds
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.28f);
        for (int i = 0; i < activeSlots; i++) {
            Slot slot = inventorySlots.getSlot(i);
            SlotBackground.draw(x + this.cornerX + slot.xPos - 1, y + this.cornerY + slot.yPos - 1);
        }

        // full opaque. Draw the borders of the slots
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        for (int i = 0; i < Table_slot_count; i++) {
            Slot slot = inventorySlots.getSlot(i);
            if (slot instanceof SlotToolStationIn && (!((SlotToolStationIn) slot).isDormant() || slot.getHasStack())) {
                SlotBorder.draw(
                        x + this.cornerX + slot.xPos - 1, y + this.cornerY + slot.yPos - 1);
            }
        }

        this.mc.getTextureManager().bindTexture(Icons.ICON);

        // slot logos
        if (currentInfo == ArmorStationGuiButtonRepair.info) {
            drawRepairSlotIcons();
        } else if (currentInfo.armor.getItem() instanceof ArmorBase) {
            for (int i = 0; i < activeSlots; i++) {
                Slot slot = inventorySlots.getSlot(i);
                if (!(slot instanceof SlotToolStationIn)) {
                    continue;
                }

                ItemStack stack = ((SlotToolStationIn) slot).icon;
                if (stack == null) {
                    continue;
                }

                itemRender.renderItemIntoGUI(stack,
                        x + this.cornerX + slot.xPos,
                        y + this.cornerY + slot.yPos);
            }
        }

        this.mc.getTextureManager().bindTexture(BACKGROUND);
        x = buttons.getGuiLeft() - beamL.w;
        y = cornerY;
        // draw the beams at the top
        x += beamL.draw(x, y);
        x += beamC.drawScaledX(x, y, buttons.getXSize());
        beamR.draw(x, y);

        x = toolInfo.getGuiLeft() - beamL.w;
        x += beamL.draw(x, y);
        x += beamC.drawScaledX(x, y, toolInfo.getXSize());
        beamR.draw(x, y);

        // draw the decoration for the buttons
//    for(Object o : buttons.buttonList) {
//      GuiButton button = (GuiButton) o;
//
//      buttonDecorationTop.draw(button.x, button.y - buttonDecorationTop.h);
//      // don't draw the bottom for the buttons in the last row
//      if(button.id < buttons.buttonList.size() - Column_Count) {
//        buttonDecorationBot.draw(button.x, button.y + button.height);
//      }
//    }

        // draw the decorations for the panels
        panelDecorationL.draw(toolInfo.getGuiLeft() + 5, toolInfo.getGuiTop() - panelDecorationL.h);
        panelDecorationR.draw(toolInfo.guiRight() - 5 - panelDecorationR.w, toolInfo.getGuiTop() - panelDecorationR.h);
        panelDecorationL.draw(traitInfo.getGuiLeft() + 5, traitInfo.getGuiTop() - panelDecorationL.h);
        panelDecorationR.draw(traitInfo.guiRight() - 5 - panelDecorationR.w, traitInfo.getGuiTop() - panelDecorationR.h);


        GlStateManager.enableDepth();

        PlayerDisplay.draw(x - 371, y + 118);
        ArmorSlots.draw(x - 304, y + 173);
        drawEntityOnScreen(x - 338, y + 190, 32, this.guiLeft - mouseX, this.guiTop - mouseY, Minecraft.getMinecraft().player);

        // continue as usual and hope that the drawing state is not completely wrecked
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    protected void drawRepairSlotIcons() {
        for (int i = 0; i < activeSlots; i++) {
            drawRepairSlotIcon(i);
        }
    }

    protected void drawRepairSlotIcon(int i) {
        GuiElement icon = null;
        Slot slot = inventorySlots.getSlot(i);
        // only empty solts get the logo since something else than the displayed thing might be in there.
        // which would look weird.
        if (slot.getHasStack()) {
            return;
        }

        if (i == 0) {
            icon = Icons.ICON_Pickaxe;
        } else if (i == 1) {
            icon = Icons.ICON_Dust;
        } else if (i == 2) {
            icon = Icons.ICON_Lapis;
        } else if (i == 3) {
            icon = Icons.ICON_Ingot;
        } else if (i == 4) {
            icon = Icons.ICON_Gem;
        } else if (i == 5) {
            icon = Icons.ICON_Quartz;
        }

        if (icon != null) {
            drawIconEmpty(slot, icon);
        }
    }

    protected void wood() {
        toolInfo.wood();
        traitInfo.wood();

        buttonDecorationTop = SlotSpaceTop.shift(SlotSpaceTop.w, 0);
        buttonDecorationBot = SlotSpaceBottom.shift(SlotSpaceBottom.w, 0);
        panelDecorationL = PanelSpaceL.shift(18, 0);
        panelDecorationR = PanelSpaceR.shift(18, 0);

        buttons.wood();

        beamL = BeamLeft;
        beamR = BeamRight;
        beamC = BeamCenter;
    }

    protected void metal() {
        toolInfo.metal();
        traitInfo.metal();

        buttonDecorationTop = SlotSpaceTop.shift(SlotSpaceTop.w * 2, 0);
        buttonDecorationBot = SlotSpaceBottom.shift(SlotSpaceBottom.w * 2, 0);
        panelDecorationL = PanelSpaceL.shift(18 * 2, 0);
        panelDecorationR = PanelSpaceR.shift(18 * 2, 0);

        buttons.metal();

        beamL = BeamLeft.shift(0, BeamLeft.h);
        beamR = BeamRight.shift(0, BeamRight.h);
        beamC = BeamCenter.shift(0, BeamCenter.h);
    }

    @Override
    public void error(String message) {
        toolInfo.setCaption(I18n.translateToLocal("gui.error"));
        toolInfo.setText(message);
        traitInfo.setCaption(null);
        traitInfo.setText();
    }

    @Override
    public void warning(String message) {
        toolInfo.setCaption(I18n.translateToLocal("gui.warning"));
        toolInfo.setText(message);
        traitInfo.setCaption(null);
        traitInfo.setText();
    }
}