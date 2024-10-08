package lance5057.tDefense.core.tools.bases;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import electroblob.wizardry.constants.Constants;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.ItemWizardArmour;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import lance5057.tDefense.Reference;
import lance5057.tDefense.core.addons.toolleveling.AddonToolLeveling;
import lance5057.tDefense.core.library.ArmorNBT;
import lance5057.tDefense.core.library.ArmorTags;
import lance5057.tDefense.core.library.ArmorToolTipBuilder;
import lance5057.tDefense.core.library.TCRegistry;
import lance5057.tDefense.core.tools.armor.renderers.ArmorRenderer;
import lance5057.tDefense.util.ArmorTagUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.common.ClientProxy;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.IToolStationDisplay;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.DualToolHarvestUtils;
import slimeknights.tconstruct.library.tools.IAoeTool;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.library.utils.TooltipBuilder;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.traits.InfiTool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Intermediate abstraction layer for all tools/melee weapons. This class has
 * all the callbacks for blocks and enemies so tools and weapons can share
 * behaviour.
 */
public abstract class ArmorCore extends ArmorBase implements IToolStationDisplay {

    public final static int DEFAULT_MODIFIERS = 3;
    public static final String TAG_SWITCHED_HAND_HAX = "SwitchedHand";

    // why is this private!?
    protected static final UUID[] ARMOR_MODIFIERS = new UUID[]{
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    public ArmorCore(EntityEquipmentSlot slot, PartMaterialType... requiredComponents) {
        super(slot, requiredComponents);

        this.setCreativeTab(TinkerRegistry.tabTools);
        this.setNoRepair(); // >_>

        TCRegistry.registerTool(this);
        addCategory(Category.TOOL);
    }

    public static boolean isEqualTinkersItem(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null || item1.getItem() != item2.getItem()) {
            return false;
        }
        if (!(item1.getItem() instanceof ArmorCore)) {
            return false;
        }

        NBTTagCompound tag1 = TagUtil.getTagSafe(item1);
        NBTTagCompound tag2 = TagUtil.getTagSafe(item2);

        NBTTagList mods1 = TagUtil.getModifiersTagList(tag1);
        NBTTagList mods2 = TagUtil.getModifiersTagList(tag2);

        if (mods1.tagCount() != mods1.tagCount()) {
            return false;
        }

        // check modifiers
        for (int i = 0; i < mods1.tagCount(); i++) {
            NBTTagCompound tag = mods1.getCompoundTagAt(i);
            ModifierNBT data = ModifierNBT.readTag(tag);
            IModifier modifier = TinkerRegistry.getModifier(data.identifier);
            if (modifier != null && !modifier.equalModifier(tag, mods2.getCompoundTagAt(i))) {
                return false;
            }
        }

        return TagUtil.getBaseMaterialsTagList(tag1).equals(TagUtil.getBaseMaterialsTagList(tag2)) && // materials
                // used
                TagUtil.getBaseModifiersUsed(tag1) == TagUtil.getBaseModifiersUsed(tag2) && // number
                // of
                // free
                // modifiers
                // used
                TagUtil.getOriginalToolStats(tag1).equals(TagUtil.getOriginalToolStats(tag2)); // unmodified
        // base
        // stats
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onSpellCastPreEvent(SpellCastEvent.Pre event) {
        // Armour cost reduction
        if (event.getCaster() == null) return;
        int armourPieces = getMatchingArmourCount(event.getCaster(), event.getSpell().getElement());
        float multiplier = 1f - armourPieces * Constants.COST_REDUCTION_PER_ARMOUR;
        if (armourPieces == WizardryUtilities.ARMOUR_SLOTS.length) multiplier -= Constants.FULL_ARMOUR_SET_BONUS;
        event.getModifiers().set(SpellModifiers.COST, event.getModifiers().get(SpellModifiers.COST) * multiplier, false);
    }

    /**
     * Counts the number of armour pieces the given entity is wearing that match the given element.
     */
    public static int getMatchingArmourCount(EntityLivingBase entity, Element element) {
        return (int) Arrays.stream(WizardryUtilities.ARMOUR_SLOTS)
                .map(s -> entity.getItemStackFromSlot(s).getItem())
                .filter(i -> i instanceof ItemWizardArmour && ((ItemWizardArmour) i).element == element)
                .count();
    }

    @Override
    public int getMaxDamage(@Nonnull ItemStack stack) {
        return ToolHelper.getDurabilityStat(stack);
    }

    /* Tool and Weapon specific properties */

    @Override
    public void setDamage(@Nonnull ItemStack stack, int damage) {
        int max = getMaxDamage(stack);
        super.setDamage(stack, Math.min(max, damage));

        if (getDamage(stack) == max) {
            ToolHelper.breakTool(stack, null);
        }
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return super.showDurabilityBar(stack) && !ToolHelper.isBroken(stack);
    }

    /**
     * Multiplier applied to the actual mining speed of the tool Internally a hammer
     * and pick have the same speed, but a hammer is 2/3 slower
     */
    public float miningSpeedModifier() {
        return 1f;
    }

    /**
     * Multiplier for damage from materials. Should be fixed per tool.
     */
    public float damagePotential() {
        return 0;
    }

    /**
     * A fixed damage value where the calculations start to apply dimishing returns.
     * Basically if you'd hit more than that damage with this tool, the damage is
     * gradually reduced depending on how much the cutoff is exceeded.
     */
    public float damageCutoff() {
        return 15.0f; // in general this should be sufficient and only needs
        // increasing if it's a stronger weapon
        // fun fact: diamond sword with sharpness V has 15 damage
    }

    /**
     * Allows you set the base attack speed, can be changed by modifiers. Equivalent
     * to the vanilla attack speed. 4 is equal to any standard item. Value has to be
     * greater than zero.
     */
    public double attackSpeed() {
        return 0;
    }

    /**
     * Knockback modifier. Basically this takes the vanilla knockback on hit and
     * modifies it by this factor.
     */
    public float knockback() {
        return 1.0f;
    }

    public abstract float armorMultiplier();

    public abstract float potencyMultiplier();

    // @Override
    // public float getStrVsBlock(ItemStack stack, IBlockState state) {
    // if(isEffective(state) || ToolHelper.isToolEffective(stack, state)) {
    // return ToolHelper.calcDigSpeed(stack, state);
    // }
    // return super.getStrVsBlock(stack, state);
    // }

    /**
     * Actually deal damage to the entity we hit. Can be overridden for special
     * behaviour
     *
     * @return True if the entity was hit. Usually the return value of
     * {@link Entity#attackEntityFrom(DamageSource, float)}
     */
    public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
        if (player instanceof EntityPlayer) {
            return entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player), damage);
        }
        return entity.attackEntityFrom(DamageSource.causeMobDamage(player), damage);
    }

    protected boolean readyForSpecialAttack(EntityLivingBase player) {
        return player instanceof EntityPlayer && ((EntityPlayer) player).getCooledAttackStrength(0.5f) > 0.9f;
    }

    /**
     * Called when an entity is getting damaged with the tool. Reduce the tools
     * durability accordingly player can be null!
     */
    public void reduceDurabilityOnHit(ItemStack stack, EntityPlayer player, float damage) {
        damage = Math.max(1f, damage / 10f);
        if (!hasCategory(Category.WEAPON)) {
            damage *= 2;
        }
        ToolHelper.damageTool(stack, (int) damage, player);
    }

    public boolean isEffective(IBlockState state) {
        return false;
    }

    @Override
    public boolean canHarvestBlock(@Nonnull IBlockState state, @Nonnull ItemStack stack) {
        return isEffective(state);
    }

    // @Override
    // public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player,
    // Entity entity) {
    // return ToolHelper.attackEntity(stack, this, player, entity);
    // }

    @Override
    public boolean onBlockStartBreak(@Nonnull ItemStack itemstack, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        if (!ToolHelper.isBroken(itemstack) && this instanceof IAoeTool && ((IAoeTool) this).isAoeHarvestTool()) {
            for (BlockPos extraPos : ((IAoeTool) this).getAOEBlocks(itemstack, player.getEntityWorld(), player, pos)) {
                breakExtraBlock(itemstack, player.getEntityWorld(), player, extraPos, pos);
            }
        }

        // this is a really dumb hack.
        // Basically when something with silktouch harvests a block from the
        // offhand
        // the game can't detect that. so we have to switch around the items in
        // the hands for the break call
        // it's switched back in onBlockDestroyed
        if (DualToolHarvestUtils.shouldUseOffhand(player, pos, player.getHeldItemMainhand())) {
            ItemStack off = player.getHeldItemOffhand();
            switchItemsInHands(player);
            // remember, off is in the mainhand now
            NBTTagCompound tag = TagUtil.getTagSafe(off);
            tag.setLong(TAG_SWITCHED_HAND_HAX, player.getEntityWorld().getTotalWorldTime());
            off.setTagCompound(tag);
        }

        return breakBlock(itemstack, pos, player);
    }

    /**
     * Called to break the base block, return false to perform no breaking
     *
     * @param itemstack Tool ItemStack
     * @param pos       Current position
     * @param player    Player instance
     * @return true if the normal block break code should be skipped
     */
    protected boolean breakBlock(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    /**
     * Called when an AOE block is broken by the tool. Use to oveerride the block
     * breaking logic
     *
     * @param tool   Tool ItemStack
     * @param world  World instance
     * @param player Player instance
     * @param pos    Current position
     * @param refPos Base position
     */
    protected void breakExtraBlock(ItemStack tool, World world, EntityPlayer player, BlockPos pos, BlockPos refPos) {
        ToolHelper.breakExtraBlock(tool, world, player, pos, refPos);
    }

    @Override
    public boolean onEntitySwing(@Nonnull EntityLivingBase entityLiving, @Nonnull ItemStack stack) {
        /*
         * if(attackSpeed() > 0) { int speed = Math.min(5, attackSpeed());
         * ToolHelper.swingItem(speed, entityLiving); return true; }
         */
        return super.onEntitySwing(entityLiving, stack);
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
        float speed = ToolHelper.getActualAttackSpeed(stack);
        int time = Math.round(20f / speed);
        if (time < target.hurtResistantTime / 2) {
            target.hurtResistantTime = (target.hurtResistantTime + time) / 2;
            target.hurtTime = (target.hurtTime + time) / 2;
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(@Nonnull EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
//        ArmorNBT nbt = ArmorTagUtil.getToolStats(stack);
//
//        if (equipmentSlot == this.armorType)
//        {
//        	multimap.remove(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double)this.damageReduceAmount, 0));
//            multimap.remove(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", (double)this.toughness, 0));
//
//            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double)nbt.armorRating, 0));
//            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", (double)nbt.armorToughness, 0));
//        }

        return multimap;
    }

    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot,
                                                                     @Nonnull ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        ArmorNBT nbt = ArmorTagUtil.getToolStats(stack);

        if (slot == this.armorType) {
            multimap.remove(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(
                    ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", this.damageReduceAmount, 0));
            multimap.remove(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(
                    ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", this.toughness, 0));

            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(
                    ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", nbt.armorRating, 0));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(
                    ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", nbt.armorToughness, 0));
        }

        NBTTagList traitsTagList = TagUtil.getTraitsTagList(stack);
        for (int i = 0; i < traitsTagList.tagCount(); i++) {
            ITrait trait = TinkerRegistry.getTrait(traitsTagList.getStringTagAt(i));
            if (trait != null) {
                trait.getAttributeModifiers(slot, stack, multimap);
            }
        }

        return multimap;
    }

    @Override
    public List<String> getInformation(ItemStack stack) {
        return getInformation(stack, true);
    }

    @Override
    public void getTooltip(ItemStack stack, List<String> tooltips) {
        if (ToolHelper.isBroken(stack)) {
            tooltips.add("" + TextFormatting.DARK_RED + TextFormatting.BOLD + getBrokenTooltip(stack));
        }
        super.getTooltip(stack, tooltips);
    }

    protected String getBrokenTooltip(ItemStack itemStack) {
        return Util.translate(TooltipBuilder.LOC_Broken);
    }

    @Override
    public void getTooltipDetailed(ItemStack stack, List<String> tooltips) {
        tooltips.addAll(getInformation(stack, false));
    }

    public List<String> getInformation(ItemStack stack, boolean detailed) {
        ArmorToolTipBuilder info = new ArmorToolTipBuilder(stack);

        info.addDurability(!detailed);
//		if (hasCategory(Category.HARVEST)) {
//			info.addHarvestLevel();
//			info.addMiningSpeed();
//		}
//		if (hasCategory(Category.LAUNCHER)) {
//			info.addDrawSpeed();
//			info.addRange();
//			info.addProjectileBonusDamage();
//		}
//		info.addAttack();

        info.addArmorRating();
        info.addArmorToughness();
        info.addArmorPotency();

        if (ToolHelper.getFreeModifiers(stack) > 0) {
            info.addFreeModifiers();
        }

        if (detailed) {
            info.addModifierInfo();
        }

        return info.getTooltip();
    }

    @Override
    public void getTooltipComponents(ItemStack stack, List<String> tooltips) {
        List<Material> materials = TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(stack));
        List<PartMaterialType> component = getRequiredComponents();

        if (materials.size() < component.size()) {
            return;
        }

        for (int i = 0; i < component.size(); i++) {
            PartMaterialType pmt = component.get(i);
            Material material = materials.get(i);

            // get (one possible) toolpart used to craft the thing
            Iterator<IToolPart> partIter = pmt.getPossibleParts().iterator();
            if (!partIter.hasNext()) {
                continue;
            }

            IToolPart part = partIter.next();
            ItemStack partStack = part.getItemstackWithMaterial(material);
            if (partStack != null) {
                // we have the part, add it
                tooltips.add(material.getTextColor() + TextFormatting.UNDERLINE + partStack.getDisplayName());

                Set<ITrait> usedTraits = Sets.newHashSet();
                // find out which stats and traits it contributes and add it to
                // the tooltip
                for (IMaterialStats stats : material.getAllStats()) {
                    if (pmt.usesStat(stats.getIdentifier())) {
                        tooltips.addAll(stats.getLocalizedInfo());
                        for (ITrait trait : pmt.getApplicableTraitsForMaterial(material)) {
                            if (!usedTraits.contains(trait)) {
                                tooltips.add(material.getTextColor() + trait.getLocalizedName());
                                usedTraits.add(trait);
                            }
                        }
                    }
                }
                tooltips.add("");
            }
        }
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public FontRenderer getFontRenderer(@Nonnull ItemStack stack) {
        return ClientProxy.fontRenderer;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return TagUtil.hasEnchantEffect(stack);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        // if the tool is not named we use the repair tools for a prefix like
        // thing
        List<Material> materials = TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(stack));
        // we save all the ones for the name in a set so we don't have the same
        // material in it twice
        Set<Material> nameMaterials = Sets.newLinkedHashSet();

        for (int index : getRepairParts()) {
            if (index < materials.size()) {
                nameMaterials.add(materials.get(index));
            }
        }

        return Material.getCombinedItemName(super.getItemStackDisplayName(stack), nameMaterials);
    }

    // Creative tab items
    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            addDefaultSubItems(subItems);
        }
    }

    protected void addDefaultSubItems(List<ItemStack> subItems, Material... fixedMaterials) {
        for (Material head : TinkerRegistry.getAllMaterials()) {
            List<Material> mats = new ArrayList<>(requiredComponents.length);

            for (int i = 0; i < requiredComponents.length; i++) {
                if (fixedMaterials.length > i && fixedMaterials[i] != null
                        && requiredComponents[i].isValidMaterial(fixedMaterials[i])) {
                    mats.add(fixedMaterials[i]);
                } else {
                    // todo: check for applicability with stats
                    mats.add(head);
                }
            }

            ItemStack tool = buildItem(mats);
            // only valid ones
            if (hasValidMaterials(tool)) {
                subItems.add(tool);
                if (!Config.listAllToolMaterials) {
                    break;
                }
            }
        }
    }

    protected void addInfiTool(List<ItemStack> subItems, String name) {
        ItemStack tool = getInfiTool(name);
        if (hasValidMaterials(tool)) {
            subItems.add(tool);
        }
    }

    protected ItemStack getInfiTool(String name) {
        // The InfiHarvester!
        List<Material> materials = ImmutableList.of(TinkerMaterials.slime, TinkerMaterials.cobalt,
                TinkerMaterials.ardite, TinkerMaterials.ardite);
        materials = materials.subList(0, requiredComponents.length);
        ItemStack tool = buildItem(materials);
        tool.setStackDisplayName(name);
        InfiTool.INSTANCE.apply(tool);

        return tool;
    }

    @Override
    public int getHarvestLevel(@Nonnull ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player,
                               @Nullable IBlockState blockState) {
        if (ToolHelper.isBroken(stack)) {
            return -1;
        }

        if (this.getToolClasses(stack).contains(toolClass)) {
            // will return 0 if the tag has no info anyway
            return ToolHelper.getHarvestLevelStat(stack);
        }

        return super.getHarvestLevel(stack, toolClass, player, blockState);
    }

    @Override
    @Nonnull
    public Set<String> getToolClasses(@Nonnull ItemStack stack) {
        // no classes if broken
        if (ToolHelper.isBroken(stack)) {
            return Collections.emptySet();
        }
        return super.getToolClasses(stack);
    }

    /**
     * A simple string identifier for the tool, used for identification in texture
     * generation etc.
     */
    public String getIdentifier() {
        return getRegistryName().getPath();
    }

    /**
     * The tools name completely without material information
     */
    @Override
    public String getLocalizedToolName() {
        return Util.translate(getTranslationKey() + ".name");
    }

    /* Additional Trait callbacks */

    /**
     * The tools name with the given material. e.g. "Wooden Pickaxe"
     */
    public String getLocalizedToolName(Material material) {
        return material.getLocalizedItemName(getLocalizedToolName());
    }

    /**
     * Returns info about the Tool. Displayed in the tool stations etc.
     */
    public String getLocalizedDescription() {
        return Util.translate(getTranslationKey() + ".desc");
    }

    @Override
    protected int repairCustom(Material material, NonNullList<ItemStack> repairItems) {
        Optional<RecipeMatch.Match> matchOptional = RecipeMatch.of(TinkerTools.sharpeningKit).matches(repairItems);
        if (!matchOptional.isPresent()) {
            return 0;
        }

        RecipeMatch.Match match = matchOptional.get();
        for (ItemStack stacks : match.stacks) {
            // invalid material?
            if (TinkerTools.sharpeningKit.getMaterial(stacks) != material) {
                return 0;
            }
        }

        RecipeMatch.removeMatch(repairItems, match);
        HeadMaterialStats stats = material.getStats(MaterialTypes.HEAD);
        float durability = stats.durability * match.amount * TinkerTools.sharpeningKit.getCost();
        durability /= Material.VALUE_Ingot;
        return (int) (durability);
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        onUpdateTraits(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    protected void onUpdateTraits(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!isSelected && entityIn instanceof EntityPlayer
                && ((EntityPlayer) entityIn).getHeldItemOffhand() == stack) {
            isSelected = true;
        }
        NBTTagList list = TagUtil.getTraitsTagList(stack);
        for (int i = 0; i < list.tagCount(); i++) {
            ITrait trait = TinkerRegistry.getTrait(list.getStringTagAt(i));
            if (trait != null) {
                trait.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
            }
        }
    }

    @Override
    public boolean onBlockDestroyed(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull IBlockState state, @Nonnull BlockPos pos,
                                    @Nonnull EntityLivingBase entityLiving) {
        // move item back into offhand. See onBlockBreakStart
        if (stack != null && entityLiving != null && stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            assert tag != null;
            if (tag.getLong(TAG_SWITCHED_HAND_HAX) == entityLiving.getEntityWorld().getTotalWorldTime()) {
                tag.removeTag(TAG_SWITCHED_HAND_HAX);
                stack.setTagCompound(tag);

                switchItemsInHands(entityLiving);
            }
        }
        if (ToolHelper.isBroken(stack)) {
            return false;
        }

        boolean effective = isEffective(state) || ToolHelper.isToolEffective(stack, worldIn.getBlockState(pos));
        int damage = effective ? 1 : 2;

        afterBlockBreak(stack, worldIn, state, pos, entityLiving, damage, effective);

        return hasCategory(Category.TOOL);
    }

    protected void switchItemsInHands(EntityLivingBase entityLiving) {
        ItemStack main = entityLiving.getHeldItemMainhand();
        ItemStack off = entityLiving.getHeldItemOffhand();
        entityLiving.setHeldItem(EnumHand.OFF_HAND, main);
        entityLiving.setHeldItem(EnumHand.MAIN_HAND, off);
    }

    public void afterBlockBreak(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase player,
                                int damage, boolean wasEffective) {
        NBTTagList list = TagUtil.getTraitsTagList(stack);
        for (int i = 0; i < list.tagCount(); i++) {
            ITrait trait = TinkerRegistry.getTrait(list.getStringTagAt(i));
            if (trait != null) {
                trait.afterBlockBreak(stack, world, state, pos, player, wasEffective);
            }
        }

        ToolHelper.damageTool(stack, damage, player);
    }

    // elevate to public
    @Nonnull
    @Override
    public RayTraceResult rayTrace(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, boolean useLiquids) {
        return super.rayTrace(worldIn, playerIn, useLiquids);
    }

    protected void preventSlowDown(Entity entityIn, float originalSpeed) {
        TinkerTools.proxy.preventPlayerSlowdown(entityIn, originalSpeed, this);
    }

    @Override
    public boolean shouldCauseBlockBreakReset(@Nonnull ItemStack oldStack, @Nonnull ItemStack newStack) {
        return shouldCauseReequipAnimation(oldStack, newStack, false);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldCauseReequipAnimation(@Nonnull ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
        if (TagUtil.getResetFlag(newStack)) {
            TagUtil.setResetFlag(newStack, false);
            return true;
        }
        if (oldStack == newStack) {
            return false;
        }
        if (slotChanged) {
            return true;
        }

        if (oldStack.hasEffect() != newStack.hasEffect()) {
            return true;
        }

        Multimap<String, AttributeModifier> attributesNew = newStack
                .getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
        Multimap<String, AttributeModifier> attributesOld = oldStack
                .getAttributeModifiers(EntityEquipmentSlot.MAINHAND);

        if (attributesNew.size() != attributesOld.size()) {
            return true;
        }
        for (String key : attributesOld.keySet()) {
            if (!attributesNew.containsKey(key)) {
                return true;
            }
            Iterator<AttributeModifier> iter1 = attributesNew.get(key).iterator();
            Iterator<AttributeModifier> iter2 = attributesOld.get(key).iterator();
            while (iter1.hasNext() && iter2.hasNext()) {
                if (!iter1.next().equals(iter2.next())) {
                    return true;
                }
            }
        }

        if (oldStack.getItem() == newStack.getItem() && newStack.getItem() instanceof ArmorCore) {
            return !isEqualTinkersItem(oldStack, newStack);
        }
        return !ItemStack.areItemStacksEqual(oldStack, newStack);
    }

    /**
     * Builds a default tool from: 1. Handle 2. Head 3. Accessoire (if present)
     */
    protected ArmorNBT buildDefaultTag(List<Material> materials) {
        return null;
    }

    public EntityEquipmentSlot getArmorSlot(ItemStack stack, EntityEquipmentSlot armorType) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public ArmorRenderer getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack) {
        return null;
    }

    @Override
    public String getArmorTexture(@Nonnull ItemStack stack, @Nonnull Entity entity, @Nonnull EntityEquipmentSlot slot, @Nonnull String type) {
        String s = TagUtil.getTagSafe(TagUtil.getTagSafe(stack), ArmorTags.DynTex).getString(ArmorTags.TexLoc);
        if ((s == "" || s == null) || !checkForTexture(s)) {
            NBTTagCompound texTag = setupTexture(
                    TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(stack)));
            if (texTag != null)
                TagUtil.getTagSafe(stack).setTag(ArmorTags.DynTex, texTag);
            NBTTagCompound n = TagUtil.getTagSafe(TagUtil.getTagSafe(stack), ArmorTags.DynTex);
            s = n.getString(ArmorTags.TexLoc);
        }
        return s;
    }

    private String buildRC(ItemStack stack) {
        String texName = Reference.MOD_ID + "_" + getArmorType();
        for (Material m : TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(stack))) {
            texName += "_" + m.identifier;
        }

        return texName;
    }

    public abstract String getArmorType();

    private boolean checkForTexture(String s) {
        return Minecraft.getMinecraft().getTextureManager().getTexture(new ResourceLocation(s)) != null;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage,
                                         int slot) {
        ArmorNBT tag = ArmorTagUtil.getOriginalToolStats(armor);
        ArmorProperties a = new ArmorProperties(0, 0, Integer.MAX_VALUE);

        a.Armor = tag.armorRating;
        a.Toughness = tag.armorToughness;

        int count = 0;
        for (ItemStack oarmor : player.getArmorInventoryList()) {
            if (TinkerUtil.hasTrait(TagUtil.getTagSafe(armor), "tempered")) {
                count++;
            }
        }

        if (count > 1)
            a.Toughness += count - 1;

        if (player instanceof EntityPlayer)
            AddonToolLeveling.xpAdder.addXp(armor, (int) (damage / 4), (EntityPlayer) player);

        return a;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        ArmorNBT tag = ArmorTagUtil.getOriginalToolStats(armor);
        return tag.armorRating;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        // TODO Auto-generated method stub

    }
}