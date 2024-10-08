package lance5057.tDefense.core.library;

import lance5057.tDefense.core.library.events.TinkerArmorEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.common.network.ToolBreakAnimationPacket;
import slimeknights.tconstruct.tools.modifiers.ModReinforced;

public final class ArmorHelper {
    /* Tool Durability */

    public static int getCurrentDurability(ItemStack stack) {
        return stack.getMaxDamage() - stack.getItemDamage();
    }

    public static int getMaxDurability(ItemStack stack) {
        return stack.getMaxDamage();
    }

    /**
     * Damages the tool. Entity is only needed in case the tool breaks for rendering
     * the break effect.
     */
    public static void damageTool(ItemStack stack, int amount, EntityLivingBase entity) {
        if (amount == 0 || isBroken(stack)) {
            return;
        }

        int actualAmount = amount;

        for (ITrait trait : TinkerUtil.getTraitsOrdered(stack)) {
            if (amount > 0) {
                actualAmount = trait.onToolDamage(stack, amount, actualAmount, entity);
            } else {
                actualAmount = trait.onToolHeal(stack, amount, actualAmount, entity);
            }
        }

        // extra compatibility for unbreaking.. because things just love to mess it up..
        // like 3rd party stuff
        if (actualAmount > 0 && TagUtil.getTagSafe(stack).getBoolean(ModReinforced.TAG_UNBREAKABLE)) {
            actualAmount = 0;
        }

        // ensure we never deal more damage than durability
        actualAmount = Math.min(actualAmount, getCurrentDurability(stack));
        stack.setItemDamage(stack.getItemDamage() + actualAmount);

        if (getCurrentDurability(stack) == 0) {
            breakTool(stack, entity);
        }
    }

    public static void healTool(ItemStack stack, int amount, EntityLivingBase entity) {
        damageTool(stack, -amount, entity);
    }

    public static boolean isBroken(ItemStack stack) {
        return TagUtil.getToolTag(stack).getBoolean(Tags.BROKEN);
    }

    public static void breakTool(ItemStack stack, EntityLivingBase entity) {
        NBTTagCompound tag = TagUtil.getToolTag(stack);
        tag.setBoolean(Tags.BROKEN, true);
        TagUtil.setToolTag(stack, tag);

        if (entity instanceof EntityPlayerMP) {
            TinkerNetwork.sendTo(new ToolBreakAnimationPacket(stack), (EntityPlayerMP) entity);
        }
    }

    public static void unbreakTool(ItemStack stack) {
        if (isBroken(stack)) {
            // ensure correct damage value
            stack.setItemDamage(stack.getMaxDamage());

            // setItemDamage might break the tool again, so we do this afterwards
            NBTTagCompound tag = TagUtil.getToolTag(stack);
            tag.setBoolean(Tags.BROKEN, false);
            TagUtil.setToolTag(stack, tag);
        }
    }

    public static void repairTool(ItemStack stack, int amount) {
        // entity is optional, only needed for rendering break effect, never needed when
        // repairing
        repairTool(stack, amount, null);
    }

    public static void repairTool(ItemStack stack, int amount, EntityLivingBase entity) {
        unbreakTool(stack);

        TinkerArmorEvent.OnRepair.fireEvent(stack, amount);

        healTool(stack, amount, entity);
    }
}