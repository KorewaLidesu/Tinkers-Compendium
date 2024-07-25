package lance5057.tDefense.proxy;

import com.google.common.collect.ImmutableList;
import lance5057.tDefense.Reference;
import lance5057.tDefense.TCCommands;
import lance5057.tDefense.TCConfig;
import lance5057.tDefense.TinkersCompendium;
import lance5057.tDefense.core.blocks.ColoredBlockMapper;
import lance5057.tDefense.core.library.*;
import lance5057.tDefense.core.library.book.CompendiumBook;
import lance5057.tDefense.core.tools.TDTools;
import lance5057.tDefense.core.tools.bases.ArmorCore;
import lance5057.tDefense.renderers.deserializers.AlphaColorTextureDeserializer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.client.CustomFontRenderer;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.client.material.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.client.model.ToolModelLoader;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolPart;

import javax.annotation.Nonnull;

//@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ClientProxy extends CommonProxy {
    // static final ToolModelLoader toolmodel = new ToolModelLoader();
    // public static final ModelSheath sheath = new ModelSheath();
    // ModifierSoulHandler SoulHandler;

    // public static ModelTinkersTabard sheath;

    private static final TDModelLoader loader = new TDModelLoader();
    public static TextureMap armorMap;
    ToolBuildGuiInfo roundshieldGUI;
    ToolBuildGuiInfo heatershieldGUI;
    ToolBuildGuiInfo towershieldGUI;
    ToolBuildGuiInfo zweihanderGUI;
    ToolBuildGuiInfo shearsGUI;
    ToolBuildGuiInfo fishingRodGUI;
    ToolBuildGuiInfo malletGUI;
    ToolBuildGuiInfo sawGUI;
    ToolBuildGuiInfo fireDrillGUI;
    ArmorBuildGuiInfo hoodGUI;
    ArmorBuildGuiInfo shawlGUI;
    ArmorBuildGuiInfo robeGUI;
    ArmorBuildGuiInfo shoesGUI;
    ArmorBuildGuiInfo coifGUI;
    ArmorBuildGuiInfo hauberkGUI;
    ArmorBuildGuiInfo chaussesGUI;
    ArmorBuildGuiInfo bootsGUI;
    ArmorBuildGuiInfo helmGUI;
    ArmorBuildGuiInfo breastplateGUI;
    ArmorBuildGuiInfo grievesGUI;
    ArmorBuildGuiInfo sabatonsGUI;
    // ToolBuildGuiInfo sheatheGUI;
    ToolBuildGuiInfo ringGUI;
    // ToolBuildGuiInfo amuletGUI;
    ToolBuildGuiInfo tabardGUI;

    // public static SheatheModel sheathe;
    ToolBuildGuiInfo backpackGUI;

    @Override
    public void preInit() {

        armorMap = new TextureMap("armortextures");

        ClientCommandHandler.instance.registerCommand(new TCCommands());
        ModelLoaderRegistry.registerLoader(loader);
        MaterialRenderInfoLoader.addRenderInfo("alpha_color", AlphaColorTextureDeserializer.class);

        MinecraftForge.EVENT_BUS.register(CustomArmorTextureCreator.INSTANCE);

        if (TCConfig.armor.enableClothArmor) {
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/hood/_hood_cloth"));
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/hood/_hood_trim"));
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/hood/_hood_metal"));

            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/shawl/_shawl_cloth"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/shawl/_shawl_trim"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/shawl/_shawl_metal"));

            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/robe/_robe_cloth"));
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/robe/_robe_trim"));
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/robe/_robe_metal"));

            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/shoes/_shoes_cloth"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/shoes/_shoes_trim"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/shoes/_shoes_metal"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/shoes/_shoes_string"));
        }

        if (TCConfig.armor.enableChainArmor) {
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/coif/_coif_chain"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/coif/_coif_circlet"));
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/coif/_coif_cloth"));

            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/chausses/_chausses_chain"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/chausses/_chausses_clasp"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/chausses/_chausses_cloth"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/chausses/_chausses_plate"));

            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/hauberk/_hauberk_chain"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/hauberk/_hauberk_plate"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/hauberk/_hauberk_clasp"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/hauberk/_hauberk_cloth"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/hauberk/_hauberk_rivet"));

            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/boots/_boots_chain"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/boots/_boots_plate"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/boots/_boots_cloth"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/boots/_boots_string"));
        }

        if (TCConfig.armor.enableHeavyArmor) {
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/helm/_helm_chain"));
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/helm/_helm_plate"));
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/helm/_helm_top"));
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/helm/_helm_trim"));
            CustomArmorTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/helm/_helm_cloth"));

            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/breastplate/_breastplate_chain"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/breastplate/_breastplate_plate"));
            CustomArmorTextureCreator.registerTexture(
                    new ResourceLocation(Reference.MOD_ID, "armor/breastplate/_breastplate_smallplate"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/breastplate/_breastplate_trim"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/breastplate/_breastplate_cloth"));

            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/grieves/_grieves_chain"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/grieves/_grieves_plate"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/grieves/_grieves_cloth"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/grieves/_grieves_trim"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/grieves/_grieves_clasp"));

            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/sabatons/_sabatons_caps"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/sabatons/_sabatons_plates"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/sabatons/_sabatons_soles"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/sabatons/_sabatons_trim"));
            CustomArmorTextureCreator
                    .registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/sabatons/_sabatons_rivets"));
        }
    }

    @Override
    public void init() {

        createToolGuis();
        setToolGuis();
        registerToolGuis();
        createToolModels();

        Minecraft mc = Minecraft.getMinecraft();

        // Font Renderer for the tinker books
        FontRenderer bookRenderer = new CustomFontRenderer(mc.gameSettings,
                new ResourceLocation("textures/font/ascii.png"), mc.renderEngine);
        bookRenderer.setUnicodeFlag(true);
        CompendiumBook.INSTANCE.fontRenderer = bookRenderer;

        for (ToolCore tc : TinkerRegistry.getTools()) {
            for (String s : TCConfig.anvil.overrides) {
                String[] info = s.split(" ");
                if (info[0].equals(tc.getRegistryName().toString())) {
                    TDClientRegistry.addVarient(tc, Integer.valueOf(info[1]));
                }
            }
        }

        // ModelRegisterUtil.registerPartModel(TDMaterials.plate);
    }

    @Override
    public void postInit() {
        // registerItemColorHandler(new ColorHandler(0xa470e0),
        // TDMaterials.aeonsteelIngot);
        // registerItemColorHandler(new ColorHandler(0xa470e0),
        // TDMaterials.aeonsteelNugget);
        // registerItemColorHandler(new ColorHandler(0xa470e0),
        // TDMaterials.aeonsteelDust);
        // registerItemColorHandler(new ColorHandler(0xa470e0),
        // TDMaterials.aeonsteelGrain);
        // registerItemColorHandler(new ColorHandler(0xa470e0),
        // TDMaterials.aeonsteelCoin);
        // registerItemColorHandler(new ColorHandler(0xa470e0),
        // TDMaterials.aeonsteelGear);
        // registerItemColorHandler(new ColorHandler(0xa470e0),
        // TDMaterials.aeonsteelPlate);
        // registerItemColorHandler(new ColorHandler(0xa470e0),
        // TDMaterials.aeonsteelRod);

        // TDMaterials.queensgold.setupClient();

        if (TCConfig.shields.enableShields && TCConfig.shields.enableHeaterShield) {
            TinkersCompendium.tab.setDisplayIcon(TDTools.heatershield.buildItemForRendering(
                    ImmutableList.of(TinkerRegistry.getMaterial("iron"), TinkerRegistry.getMaterial("cobalt"),
                            TinkerRegistry.getMaterial("cobalt"), TinkerRegistry.getMaterial("iron"))));
        }
    }

//	@SideOnly(Side.CLIENT)
//	@SubscribeEvent
//	public static void registerModels(ModelRegistryEvent event) {
//		if()
//	}

    @Override
    public void registerFluidModels(Fluid fluid) {
        if (fluid == null)
            return;
        Block block = fluid.getBlock();
        if (block != null) {
            Item item = Item.getItemFromBlock(block);
            FluidStateMapper mapper = new FluidStateMapper(fluid);
            if (item != null) {
                ModelBakery.registerItemVariants(item);
                ModelLoader.setCustomMeshDefinition(item, mapper);
            }
            ModelLoader.setCustomStateMapper(block, mapper);
        }
    }

    @Override
    public void registerToolModel(ToolCore tool) {
        ModelRegisterUtil.registerToolModel(tool);
    }

    @Override
    public ResourceLocation registerAnvilToolModel(ToolCore tool) {
        if (tool == null || tool.getRegistryName() == null) {
            return null;
        }
        ResourceLocation itemLocation = tool.getRegistryName();
        String path = "tools/" + itemLocation.getPath() + ToolModelLoader.EXTENSION;

        ResourceLocation location = new ResourceLocation(Reference.MOD_ID, path);
        ToolModelLoader.addPartMapping(location, tool);

        return ModelRegisterUtil.registerToolModel(tool, location);
    }

    @Override
    public void registerArmorModel(ArmorCore tool) {
        TDModelRegistar.registerToolModel(tool);
    }

    @Override
    public void registerModifierModel(Modifier mod) {
        ModelRegisterUtil.registerModifierModel(mod,
                new ResourceLocation(Reference.MOD_ID, "models/item/modifiers/" + mod.getIdentifier()));
    }

    @Override
    public void registerPartModel(ToolPart part) {
        ModelRegisterUtil.registerPartModel(part);
    }

    @Override
    public void registerArmorPartModel(ArmorPart part) {
        ModelRegisterUtil.registerPartModel(part);
    }

    @Override
    public void registerMatColor(Material mat, int color) {
        mat.setRenderInfo(color);
    }

    public void createToolGuis() {
        if (TCConfig.shields.enableShields) {
            if (TCConfig.shields.enableBuckler)
                roundshieldGUI = new ToolBuildGuiInfo(TDTools.roundshield);
            if (TCConfig.shields.enableHeaterShield)
                heatershieldGUI = new ToolBuildGuiInfo(TDTools.heatershield);
            if (TCConfig.shields.enableTowerShield)
                towershieldGUI = new ToolBuildGuiInfo(TDTools.towershield);
        }

        if (TCConfig.tools.enableTools) {
            if (TCConfig.tools.enableZweihander)
                zweihanderGUI = new ToolBuildGuiInfo(TDTools.zweihander);
            if (TCConfig.tools.enableShears)
                shearsGUI = new ToolBuildGuiInfo(TDTools.shears);
            if (TCConfig.tools.enableFishingRod)
                fishingRodGUI = new ToolBuildGuiInfo(TDTools.fishingRod);
            if (TCConfig.tools.enableMallet)
                malletGUI = new ToolBuildGuiInfo(TDTools.mallet);
            if (TCConfig.tools.enableSaw)
                sawGUI = new ToolBuildGuiInfo(TDTools.saw);
            if (TCConfig.tools.enableFireDrill)
                fireDrillGUI = new ToolBuildGuiInfo(TDTools.fireDrill);
            if (TCConfig.tools.enableBackpack)
                backpackGUI = new ToolBuildGuiInfo(TDTools.backpack);
        }

        if (TCConfig.armor.enableClothArmor) {
            hoodGUI = new ArmorBuildGuiInfo(TDTools.hood);
            shawlGUI = new ArmorBuildGuiInfo(TDTools.shawl);
            robeGUI = new ArmorBuildGuiInfo(TDTools.robe);
            shoesGUI = new ArmorBuildGuiInfo(TDTools.shoes);
        }

        if (TCConfig.armor.enableChainArmor) {
            coifGUI = new ArmorBuildGuiInfo(TDTools.coif);
            hauberkGUI = new ArmorBuildGuiInfo(TDTools.hauberk);
            chaussesGUI = new ArmorBuildGuiInfo(TDTools.chausses);
            bootsGUI = new ArmorBuildGuiInfo(TDTools.boots);
        }

        if (TCConfig.armor.enableHeavyArmor) {
            helmGUI = new ArmorBuildGuiInfo(TDTools.helm);
            breastplateGUI = new ArmorBuildGuiInfo(TDTools.breastplate);
            grievesGUI = new ArmorBuildGuiInfo(TDTools.grieves);
            sabatonsGUI = new ArmorBuildGuiInfo(TDTools.sabatons);
        }

        if (TCConfig.baubles.enableBaubles) {
            if (TCConfig.baubles.enableTabard) {
                tabardGUI = new ToolBuildGuiInfo(TDTools.tabard);
            }
            if (TCConfig.baubles.enableRing) {
                ringGUI = new ToolBuildGuiInfo(TDTools.ring);
            }
        }

        // sheatheGUI = new ToolBuildGuiInfo(TDTools.sheathe);
        // amuletGUI = new ToolBuildGuiInfo(TDTools.amulet);

    }

    public void setupToolGuis() {

    }

    public void registerToolGuis() {
        if (TCConfig.shields.enableShields) {
            if (TCConfig.shields.enableBuckler)
                TinkerRegistryClient.addToolBuilding(roundshieldGUI);
            if (TCConfig.shields.enableHeaterShield)
                TinkerRegistryClient.addToolBuilding(heatershieldGUI);
            if (TCConfig.shields.enableTowerShield)
                TinkerRegistryClient.addToolBuilding(towershieldGUI);
        }

        if (TCConfig.tools.enableTools) {
            if (TCConfig.tools.enableZweihander)
                TinkerRegistryClient.addToolBuilding(zweihanderGUI);
            if (TCConfig.tools.enableShears)
                TinkerRegistryClient.addToolBuilding(shearsGUI);
            if (TCConfig.tools.enableFishingRod)
                TinkerRegistryClient.addToolBuilding(fishingRodGUI);
            if (TCConfig.tools.enableMallet)
                TinkerRegistryClient.addToolBuilding(malletGUI);
            if (TCConfig.tools.enableSaw)
                TinkerRegistryClient.addToolBuilding(sawGUI);
            if (TCConfig.tools.enableFireDrill)
                TinkerRegistryClient.addToolBuilding(fireDrillGUI);
            if (TCConfig.tools.enableBackpack)
                TinkerRegistryClient.addToolBuilding(backpackGUI);
        }

        if (TCConfig.armor.enableClothArmor) {
            TDClientRegistry.addArmorBuilding(hoodGUI);
            TDClientRegistry.addArmorBuilding(shawlGUI);
            TDClientRegistry.addArmorBuilding(robeGUI);
            TDClientRegistry.addArmorBuilding(shoesGUI);
        }

        if (TCConfig.armor.enableChainArmor) {
            TDClientRegistry.addArmorBuilding(coifGUI);
            TDClientRegistry.addArmorBuilding(hauberkGUI);
            TDClientRegistry.addArmorBuilding(chaussesGUI);
            TDClientRegistry.addArmorBuilding(bootsGUI);
        }
        // TinkerRegistryClient.addToolBuilding(bootsGUI);

        // TinkerRegistryClient.addToolBuilding(sheatheGUI);
        // TinkerRegistryClient.addToolBuilding(ringGUI);
        // TinkerRegistryClient.addToolBuilding(amuletGUI);
        //
        if (TCConfig.armor.enableHeavyArmor) {
            TDClientRegistry.addArmorBuilding(helmGUI);
            TDClientRegistry.addArmorBuilding(breastplateGUI);
            TDClientRegistry.addArmorBuilding(grievesGUI);
            TDClientRegistry.addArmorBuilding(sabatonsGUI);
        }

        if (TCConfig.baubles.enableBaubles) {
            if (TCConfig.baubles.enableTabard) {
                TinkerRegistryClient.addToolBuilding(tabardGUI);
            }
            if (TCConfig.baubles.enableRing) {
                TinkerRegistryClient.addToolBuilding(ringGUI);
            }
        }
    }

    @Override
    public void reloadRenderers() {
        setToolGuis();
    }

    public void setToolGuis() {
        if (TCConfig.shields.enableShields) {
            if (TCConfig.shields.enableBuckler) {
                roundshieldGUI.positions.clear();
                roundshieldGUI.addSlotPosition(34, 15);
                roundshieldGUI.addSlotPosition(34, 33);
                roundshieldGUI.addSlotPosition(34, 51);
            }

            if (TCConfig.shields.enableHeaterShield) {
                heatershieldGUI.positions.clear();
                heatershieldGUI.addSlotPosition(34, 15);
                heatershieldGUI.addSlotPosition(25, 33);
                heatershieldGUI.addSlotPosition(43, 33);
                heatershieldGUI.addSlotPosition(34, 51);
            }

            if (TCConfig.shields.enableTowerShield) {
                towershieldGUI.positions.clear();
                towershieldGUI.addSlotPosition(34, 15);
                towershieldGUI.addSlotPosition(25, 33);
                towershieldGUI.addSlotPosition(43, 33);
                towershieldGUI.addSlotPosition(34, 51);
            }
        }

        if (TCConfig.tools.enableTools) {
            if (TCConfig.tools.enableZweihander) {
                zweihanderGUI.positions.clear();
                zweihanderGUI.addSlotPosition(34, 15);
                zweihanderGUI.addSlotPosition(25, 33);
                zweihanderGUI.addSlotPosition(43, 33);
                zweihanderGUI.addSlotPosition(34, 51);
            }

            if (TCConfig.tools.enableShears) {
                shearsGUI.positions.clear();
                shearsGUI.addSlotPosition(34, 15 + 8);
                shearsGUI.addSlotPosition(43, 33 + 8);
                shearsGUI.addSlotPosition(34, 51 + 8);
            }

            if (TCConfig.tools.enableFishingRod) {
                fishingRodGUI.positions.clear();
                fishingRodGUI.addSlotPosition(34, 15 + 8);
                fishingRodGUI.addSlotPosition(43, 33 + 8);
                fishingRodGUI.addSlotPosition(34, 51 + 8);
            }
            if (TCConfig.tools.enableMallet) {
                malletGUI.positions.clear();
                malletGUI.addSlotPosition(34, 15 + 8);
                malletGUI.addSlotPosition(43, 33 + 8);
                malletGUI.addSlotPosition(34, 51 + 8);
            }
            if (TCConfig.tools.enableSaw) {
                sawGUI.positions.clear();
                sawGUI.addSlotPosition(34, 15 + 8);
                sawGUI.addSlotPosition(43, 33 + 8);
                sawGUI.addSlotPosition(34, 51 + 8);
            }
            if (TCConfig.tools.enableFireDrill) {
                fireDrillGUI.positions.clear();
                fireDrillGUI.addSlotPosition(34, 15 + 8);
                fireDrillGUI.addSlotPosition(43, 33 + 8);
                fireDrillGUI.addSlotPosition(34, 51 + 8);
            }
            if (TCConfig.tools.enableBackpack) {
                backpackGUI.positions.clear();
                backpackGUI.addSlotPosition(34, 15);
                backpackGUI.addSlotPosition(25, 33);
                backpackGUI.addSlotPosition(43, 33);
                backpackGUI.addSlotPosition(34, 51);
            }
        }

        if (TCConfig.armor.enableClothArmor) {
            hoodGUI.positions.clear();
            hoodGUI.addSlotPosition(25 - 6, 33 + 5);
            hoodGUI.addSlotPosition(43 - 6, 33 + 5);
            hoodGUI.addSlotPosition(34 - 6, 51 + 5);

            shawlGUI.positions.clear();
            shawlGUI.addSlotPosition(25 - 6, 33 + 5);
            shawlGUI.addSlotPosition(43 - 6, 33 + 5);
            shawlGUI.addSlotPosition(34 - 6, 51 + 5);

            robeGUI.positions.clear();
            robeGUI.addSlotPosition(25 - 6, 33 + 5);
            robeGUI.addSlotPosition(43 - 6, 33 + 5);
            robeGUI.addSlotPosition(34 - 6, 51 + 5);

            shoesGUI.positions.clear();
            shoesGUI.addSlotPosition(25 - 6, 33 + 5);
            shoesGUI.addSlotPosition(34 - 6, 51 + 5);
            shoesGUI.addSlotPosition(43 - 6, 33 + 5);
            shoesGUI.addSlotPosition(34 - 6, 51 + 5 - 36);
        }

        if (TCConfig.armor.enableChainArmor) {
            coifGUI.positions.clear();
            coifGUI.addSlotPosition(25 - 6, 33 + 5);
            coifGUI.addSlotPosition(43 - 6, 33 + 5);
            coifGUI.addSlotPosition(34 - 6, 51 + 5);

            hauberkGUI.positions.clear();
            hauberkGUI.addSlotPosition(28, 33 + 9);
            hauberkGUI.addSlotPosition(46, 33);
            hauberkGUI.addSlotPosition(37, 51 + 9);
            hauberkGUI.addSlotPosition(10, 33);
            hauberkGUI.addSlotPosition(19, 51 + 9);

            chaussesGUI.positions.clear();
            chaussesGUI.addSlotPosition(25 - 6, 33 + 5);
            chaussesGUI.addSlotPosition(43 - 6, 33 + 5);
            chaussesGUI.addSlotPosition(34 - 6, 51 + 5);
            chaussesGUI.addSlotPosition(34 - 6, 51 + 5 - 36);

            bootsGUI.positions.clear();
            bootsGUI.addSlotPosition(25 - 6, 33 + 5);
            bootsGUI.addSlotPosition(34 - 6, 51 + 5);
            bootsGUI.addSlotPosition(43 - 6, 33 + 5);
            bootsGUI.addSlotPosition(34 - 6, 51 + 5 - 36);
        }

        if (TCConfig.armor.enableHeavyArmor) {
            breastplateGUI.positions.clear();
            breastplateGUI.addSlotPosition(28, 33 + 9);
            breastplateGUI.addSlotPosition(46, 33);
            breastplateGUI.addSlotPosition(37, 51 + 9);
            breastplateGUI.addSlotPosition(10, 33);
            breastplateGUI.addSlotPosition(19, 51 + 9);

            helmGUI.positions.clear();
            helmGUI.addSlotPosition(28, 23);
            helmGUI.addSlotPosition(46, 33 + 8);
            helmGUI.addSlotPosition(37, 51 + 8);
            helmGUI.addSlotPosition(10, 33 + 8);
            helmGUI.addSlotPosition(19, 51 + 8);

            sabatonsGUI.positions.clear();
            sabatonsGUI.addSlotPosition(28 + 18, 16 + 18);
            sabatonsGUI.addSlotPosition(28 - 18, 16 + 18);
            sabatonsGUI.addSlotPosition(28 + 18, 16 + 18 + 18);
            sabatonsGUI.addSlotPosition(28 - 18, 16 + 18 + 18);
            sabatonsGUI.addSlotPosition(28, 16 + 18 + 9);

            grievesGUI.positions.clear();
            grievesGUI.addSlotPosition(28 - 14, 23 + 18);
            grievesGUI.addSlotPosition(28 + 14, 23 + 18);
            grievesGUI.addSlotPosition(28 + 14, 23 + 18 + 18);
            grievesGUI.addSlotPosition(28, 23);
            grievesGUI.addSlotPosition(28 - 14, 23 + 18 + 18);
        }

        // sheatheGUI.positions.clear();
        // sheatheGUI.addSlotPosition(34, 15);
        // sheatheGUI.addSlotPosition(34, 33);
        // sheatheGUI.addSlotPosition(34, 51);
        //

        if (TCConfig.baubles.enableBaubles) {
            if (TCConfig.baubles.enableTabard) {
                tabardGUI.positions.clear();
                tabardGUI.addSlotPosition(34, 15);
                tabardGUI.addSlotPosition(34, 33);
            }
            if (TCConfig.baubles.enableRing) {
                ringGUI.positions.clear();
                ringGUI.addSlotPosition(34, 15);
                ringGUI.addSlotPosition(34, 33);
                ringGUI.addSlotPosition(34, 51);
            }
        }

        //
        // amuletGUI.positions.clear();
        // amuletGUI.addSlotPosition(34, 15);
        // amuletGUI.addSlotPosition(34, 33);
        // amuletGUI.addSlotPosition(34, 51);
    }

    void createToolModels() {
        // sheathe = new SheatheModel();
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta,
                new ModelResourceLocation(Reference.MOD_ID + ":" + id, "inventory"));
    }

    @Override
    public void registerItemBlockRenderer(Block block, int meta, String file) {
        Item b = Item.getItemFromBlock(block);
        ModelLoader.setCustomModelResourceLocation(b, meta,
                new ModelResourceLocation(Reference.MOD_ID + ":" + file, "inventory"));
    }

    @Override
    public void registerBlockRenderer(Block block, String file) {
        ModelLoader.setCustomStateMapper(block, new ColoredBlockMapper(file));
    }

    @Override
    public void registerItemColorHandler(int c, Item i) {
        ItemColors itemcolors = Minecraft.getMinecraft().getItemColors();
        itemcolors.registerItemColorHandler(new ItemColorHandler(c), i);
    }

    @Override
    public void registerItem16ColorHandler(Item i) {
        ItemColors itemcolors = Minecraft.getMinecraft().getItemColors();
        itemcolors.registerItemColorHandler(new Item16ColorHandler(), i);
    }

    @Override
    public void registerBlockColorHandler(int c, Block i) {
        BlockColors blockcolors = Minecraft.getMinecraft().getBlockColors();
        blockcolors.registerBlockColorHandler(new BlockColorHandler(c), i);
    }

    public static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

        public final Fluid fluid;
        public final ModelResourceLocation location;

        public FluidStateMapper(Fluid fluid) {
            this.fluid = fluid;

            // have each block hold its fluid per nbt? hm
            this.location = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "fluid_block"),
                    fluid.getName());
        }

        @Nonnull
        @Override
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
            return location;
        }

        @Nonnull
        @Override
        public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
            return location;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ItemColorHandler implements IItemColor {
        int color;

        public ItemColorHandler(int color) {
            this.color = color;
        }

        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            if (tintIndex != 2)
                return color;
            return 0xFFFFFF;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Item16ColorHandler implements IItemColor {
        public static final int[] DYE_COLORS = new int[]{1973019, 11743532, 3887386, 5320730, 2437522, 8073150,
                2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

        public Item16ColorHandler() {
        }

        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            if (tintIndex != 2)
                return DYE_COLORS[stack.getMetadata()];
            return 0xFFFFFF;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class BlockColorHandler implements IBlockColor {
        int color;

        public BlockColorHandler(int color) {
            this.color = color;
        }

        @Override
        public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
            if (tintIndex == 1)
                return color;
            return 0xffffff;

        }
    }

//	@SideOnly(Side.CLIENT)
//	public static class Block16ColorHandler implements IBlockColor {
//		public Block16ColorHandler() {
//		}
//
//		public static final int[] DYE_COLORS = new int[] { 15790320, 15435844, 12801229, 6719955, 14602026, 4312372,
//				14188952, 4408131, 11250603, 2651799, 8073150, 2437522, 5320730, 3887386, 11743532, 1973019 };
//
//		@Override
//		public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
//			if (tintIndex == 1)
//				if (state.getBlock() instanceof BlockColored)
//					return DYE_COLORS[((BlockColored) state.getBlock()).getMetaFromState(state)];
//			return 0xffffff;
//
//		}
//	}
}
