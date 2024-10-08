package lance5057.tDefense.core.library.modifiers;

import com.google.common.collect.ImmutableList;
import lance5057.tDefense.core.materials.traits.AbstractTDTrait;
import lance5057.tDefense.core.tools.bases.ArmorCore;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifierDisplay;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TinkerUtil;

import java.util.List;

/**
 * Represents a modifier that has trait-logic Modifier can have multiple levels.
 * Since this is intended for modifiers it uses a modifier
 */
public abstract class ModifierTDTrait extends AbstractTDTrait implements IModifierDisplay {

    protected final int maxLevel;

    public ModifierTDTrait(String identifier, int color) {
        this(identifier, color, 0, 0);
    }

    public ModifierTDTrait(String identifier, int color, boolean ignoreMe) {
        this.maxLevel = 0;
        this.aspects.clear();

        addAspects(new ModifierAspect.DataAspect(this, color));
        addAspects(getAspects());
    }

    public ModifierTDTrait(String identifier, int color, int maxLevel, int countPerLevel) {
        super(identifier, color);

        // register the modifier trait
        TinkerRegistry.addTrait(this);

        this.maxLevel = maxLevel;
        this.aspects.clear();

        addAspects(new ModifierAspect.DataAspect(this, color));
        addAspects(getAspects());
    }

    protected abstract ModifierAspect[] getAspects();

    @Override
    public boolean canApplyCustom(ItemStack stack) {
        if (!(stack.getItem() instanceof ArmorCore)) {
            return false;
        }
        // not present yet, ok
        if (super.canApplyCustom(stack)) {
            return true;
        }
        // no max level
        else if (maxLevel == 0) {
            return false;
        }

        // already present, limit by level
        NBTTagCompound tag = TinkerUtil.getModifierTag(stack, identifier);

        return ModifierNBT.readTag(tag).level <= maxLevel;
    }

    @Override
    public String getTooltip(NBTTagCompound modifierTag, boolean detailed) {
        if (maxLevel > 0) {
            return getLeveledTooltip(modifierTag, detailed);
        }
        return super.getTooltip(modifierTag, detailed);
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public List<List<ItemStack>> getItems() {
        ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();

        for (RecipeMatch rm : items) {
            List<ItemStack> in = rm.getInputs();
            if (!in.isEmpty()) {
                builder.add(in);
            }
        }

        return builder.build();
    }

    public ModifierNBT.IntegerNBT getData(ItemStack tool) {
        NBTTagCompound tag = TinkerUtil.getModifierTag(tool, getModifierIdentifier());
        return ModifierNBT.readInteger(tag);
    }

}