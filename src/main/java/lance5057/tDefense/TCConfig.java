package lance5057.tDefense;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID)
public class TCConfig {
    @Config.Comment("Don't turn these on, you'll get a lot of weird files all over your pc")
    public static boolean developerFeatures = false;

    @Config.Comment("Enable Debug Features")
    public static boolean debug = false;

    @Config.Comment("Enable Hardmode Features")
    public static boolean hardmode = false;

    @Config.Comment("Turn off scary things")
    public static boolean IAmEasilyStartled = false;

    @Config.Comment("Reduce nausea effects")
    public static boolean KiddyCoastersMakeMeRetch = false;

    @Config.Comment("Stop filling my buckets!")
    public static boolean DontTouchMyBucket = false;

    @Config.Comment("Make all Compendium ores drop clumps instead of blocks")
    public static boolean OreClumps = false;

    @Config.Name("Shields")
    public static Shields shields = new Shields();
    @Config.Name("Armor")
    public static Armor armor = new Armor();
    @Config.Name("Tools")
    public static Tools tools = new Tools();
    @Config.Name("Baubles")
    public static Bauble baubles = new Bauble();
    @Config.Name("Materials")
    public static Materials materials = new Materials();
    @Config.Name("Components")
    public static Components components = new Components();
    @Config.Name("Addons")
    public static Addons addons = new Addons();
    @Config.Name("Traits")
    public static Traits traits = new Traits();
    @Config.Name("Textiles")
    public static Textiles textiles = new Textiles();
    @Config.Name("Finishing Anvil")
    public static FinishingAnvil anvil = new FinishingAnvil();

    public static class Shields {
        @Config.RequiresMcRestart()
        @Config.Comment("Enable Shields")
        public boolean enableShields = true;

        @Config.RequiresMcRestart()
        public boolean enableHeaterShield = true;

        @Config.RequiresMcRestart()
        public boolean enableBuckler = true;

        @Config.RequiresMcRestart()
        public boolean enableTowerShield = true;

        @Config.Comment("How much damage should a shield block percentage wise if the material has no shield data built in?")
        public int defaultShieldBlockPercentage = 33;
    }

    public static class Tools {
        @Config.RequiresMcRestart()
        public boolean enableTools = true;

        @Config.RequiresMcRestart()
        public boolean enableZweihander = true;

        @Config.RequiresMcRestart()
        public boolean enableShears = true;

        @Config.RequiresMcRestart()
        public boolean enableFishingRod = true;

        @Config.RequiresMcRestart()
        public boolean enableSaw = true;

        @Config.RequiresMcRestart()
        public boolean enableMallet = true;

        @Config.RequiresMcRestart()
        public boolean enableFireDrill = true;

        @Config.RequiresMcRestart()
        public boolean enableBackpack = true;
    }

    public static class Materials {

        public boolean generateOres = true;

        public boolean aeonsteel = true;
        public boolean queensgold = true;
        public boolean dogbearium = true;
        public boolean sinisterium = true;
        public boolean nihilite = true;
        public boolean orichalcum = true;
        public boolean pandorium = true;
        public boolean rosegold = true;
        public boolean platinum = true;
        public boolean valyriansteel = true;
        public boolean froststeel = true;
        public boolean gold = true;
        public boolean silver = true;

        public boolean mithril = true;

        @Config.Comment("Enable/Disable all wool cloth materials?")
        public boolean cloth = true;
        public boolean black = true;
        public boolean red = true;
        public boolean green = true;
        public boolean brown = true;
        public boolean blue = true;
        public boolean purple = true;
        public boolean cyan = true;
        public boolean lightgray = true;
        public boolean gray = true;
        public boolean pink = true;
        public boolean lime = true;
        public boolean yellow = true;
        public boolean lightblue = true;
        public boolean magenta = true;
        public boolean orange = true;
//        public boolean white = true;

        public boolean silky = true;
        public boolean roughspun = true;

        @Config.Comment("Enable/Disable all gem materials?")
        public boolean gems;

        public boolean sapphire;
        public boolean ruby;
        public boolean emerald;
        public boolean diamond;
        public boolean starsapphire;
        public boolean starruby;
        public boolean citrine;
        public boolean quartz;
        public boolean amethyst;
        public boolean lapis;
        public boolean topaz;
        public boolean garnet;
        public boolean opal;
        public boolean tanzinite;
        public boolean amber;

        public boolean ice;

        @Config.Comment("Materials that will spawn in easy difficulty on the overworld")
        public String[] easyoverworld = {"wood", "stone", "flint", "cactus", "bone", "paper"};

        @Config.Comment("Materials that will spawn in normal difficulty on the overworld, includes lower difficulties")
        public String[] normaloverworld = {};
    }

    public static class Addons {
        @Config.RequiresMcRestart()
        public boolean Thaumcraft = true;

        @Config.RequiresMcRestart()
        public boolean BloodMagic = true;

        @Config.RequiresMcRestart()
        public boolean Botania = true;

        @Config.RequiresMcRestart()
        public boolean Evilcraft = true;

        @Config.RequiresMcRestart()
        public boolean ImmersiveEngineering = true;

        @Config.RequiresMcRestart()
        public boolean TwilightForest = true;

        @Config.RequiresMcRestart()
        public boolean Abyssalcraft = true;

        @Config.RequiresMcRestart()
        public boolean EnderIO = true;

        @Config.RequiresMcRestart()
        public boolean ToolLeveling = true;

        @Config.RequiresMcRestart()
        public boolean EBWizardry = true;

        @Config.RequiresMcRestart()
        public boolean ActuallyAdditions = true;
    }

    public static class Armor {
        @Config.Comment("Toggle Helm Overlay")
        public boolean HelmOverlay = false;

        @Config.RequiresMcRestart()
        public boolean enableClothArmor = true;

        @Config.RequiresMcRestart()
        public boolean enableChainArmor = true;

        @Config.RequiresMcRestart()
        public boolean enableHeavyArmor = true;

        @Config.RequiresMcRestart()
        public boolean mobsSpawnWithGear = true;
    }

    public static class Bauble {

        public boolean enableBaubles = false;

        public boolean enableTabard = true;

        public boolean enableRing = true;
    }

    public static class Traits {
        @Config.Comment("Materials that should be considered flammable")
        public String[] incinerateMaterials = {"wood", "cactus", "sponge", "paper"};
        @Config.Comment("Materials that should be considered magnetic")
        public String[] magneticMaterials = {"iron", "pigiron", "steel"};
        @Config.Comment("Max number of additional modifer slots per level.\nDefault: 2")

        public int extraModifiers = 2;

        public boolean isFlammable(String s) {
            for (String str : incinerateMaterials) {
                if (str.equals(s))
                    return true;
            }
            return false;
        }

        public boolean isMetal(String s) {
            for (String str : magneticMaterials) {
                if (str.equals(s))
                    return true;
            }
            return false;
        }
    }

    public static class Components {
        @Config.RequiresMcRestart()
        public boolean enableComponents = true;

        @Config.RequiresMcRestart()
        public boolean enableDust = true;
        @Config.RequiresMcRestart()
        public boolean enableGrain = true;
        @Config.RequiresMcRestart()
        public boolean enablePlate = true;
        @Config.RequiresMcRestart()
        public boolean enableCoin = true;
        @Config.RequiresMcRestart()
        public boolean enableGear = true;
        @Config.RequiresMcRestart()
        public boolean enableRod = true;

        @Config.RequiresMcRestart()
        public boolean enableDoors = true;
        @Config.RequiresMcRestart()
        public boolean enableTrapDoors = true;
        @Config.RequiresMcRestart()
        public boolean enableBars = true;
        @Config.RequiresMcRestart()
        public boolean enableStake = true;
        @Config.RequiresMcRestart()
        public boolean enableCoil = true;
        @Config.RequiresMcRestart()
        public boolean enableSpring = true;
        @Config.RequiresMcRestart()
        public boolean enableWire = true;
        @Config.RequiresMcRestart()
        public boolean enableCasing = true;
    }

    public static class Textiles {
        @Config.RequiresMcRestart()
        public boolean enableTextiles = true;

        public int grassFiberDropPercent = 50;
        public int cottonSeedDropPercent = 10;
    }

    public static class FinishingAnvil {
        public boolean enableFinishingAnvilTools = true;

        public String[] overrides =
                {
                        "tconstruct:broadsword 8"
                };
    }
    // public int SoulBoundID;
    // public int DazeID;
    // public int RainbowID;
    // public int XPBoostID;
    // public int ShearFortuneID;
    //
    // public int[] CrestFeathersID;
    // public int[] CrestMirrorsID;
    // public int[] CrestLegendsID;
    // public int[] CrestBladesID;
    // public int[] CrestGluttonyID;
    // public int[] CrestPitchID;
    // public int[] CrestThornsID;
    // public int[] CrestSanguisugaID;
    // public int[] CrestWindsID;
    // public int[] CrestRetributionID;
    // public int[] CrestLightID;
    //
    // //public int JokeInsultID;
    // //public int JokePyrotechID;
    // //public int JokeCompensateID;
    //
    // public int SoulSteveID;
    //
    // public int ArmorProtectionID;
    // public int ArmorFireProtectionID;
    // public int ArmorBlastProtectionID;
    // public int ArmorProjectileProtectionID;
    // public int ArmorFeatherfallID;
    // public int ArmorGlowstepID;
    // public int ArmorFrostwalkerID;
    // public int ArmorFirewalkerID;
    // public int ArmorDepthstriderID;
    // public int ArmorRebreatherID;
    // public int ArmorNightvisionID;
    // public int ArmorAntiBlindnessID;
    // public int ArmorPumpkinID;
    // public int ArmorDodgeID;
    // public int ArmorThornsID;
    // public int ArmorAbsorptionID;
    // public int ArmorJumpboostID;
    // public int ArmorSpeedID;
    // public int ArmorHighstepID;
    // public int ArmorKnockbackResistID;
    //
    // public boolean BotaniaAddon;
    // public int CorpseIvyModID;
    // public int ManaRepairModID;
    // public int TerraCoreModID;
    // public int ArmorPixieCoreModID;
    // public int ArmorManaDiscountModID;

    // public boolean ThaumcraftAddon;
    // public int RevealingModID;
    // public int VisDiscountModID;
    // public int CapsModID;
    // public int SpellbindModID;

    // public boolean BloodMagicAddon;
    // public int SuppingModID;
    // public int BloodOathModID;
    // public int ScabbingModID;
    // public int DivinationModID;

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has been
         * changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Reference.MOD_ID)) {
                ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
