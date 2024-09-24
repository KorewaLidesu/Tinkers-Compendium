package lance5057.tDefense.core.tools.basic;

import lance5057.tDefense.core.addons.toolleveling.AddonToolLeveling;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

public class FireDrill extends ToolCore {

    public static final float DURABILITY_MODIFIER = 0.75f;

    public FireDrill() {
        super(PartMaterialType.head(TinkerTools.bowLimb), PartMaterialType.bowstring(TinkerTools.bowString),
                PartMaterialType.handle(TinkerTools.toolRod));

        setTranslationKey("td_fireDrill");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            addDefaultSubItems(subItems, null, TinkerMaterials.string, null);
        }
    }


    @Override
    public float damagePotential() {
        return 0.5f;
    }

    @Override
    public double attackSpeed() {
        return 1;
    }

    @Override
    public NBTTagCompound buildTag(List<Material> materials) {
        ToolNBT data = buildDefaultTag(materials);
        data.durability *= DURABILITY_MODIFIER;
        return data.get();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        pos = pos.offset(facing);
        ItemStack itemstack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos, facing, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            if (checkForStick(player)) {
                int curDur = ToolHelper.getMaxDurability(itemstack);

                if (worldIn.rand.nextInt(curDur) < ToolHelper.getCurrentDurability(itemstack) * 0.75f) {
                    if (worldIn.isAirBlock(pos)) {

                        worldIn.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F,
                                itemRand.nextFloat() * 0.4F + 0.8F);
                        worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
                    }

                    if (player instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, itemstack);
                    }

                    if (removeStick(player)) {
                        ToolHelper.damageTool(itemstack, 1, null);
                        AddonToolLeveling.xpAdder.addXp(itemstack, 10, player);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return EnumActionResult.FAIL;
    }

    private boolean checkForStick(EntityPlayer player) {
        for (ItemStack s : player.inventory.mainInventory) {
            if (s.getItem().equals(Items.STICK))
                return true;
        }
        return false;
    }

    private boolean removeStick(EntityPlayer player) {
        for (ItemStack s : player.inventory.mainInventory) {
            if (s.getItem().equals(Items.STICK)) {
                s.setCount(s.getCount() - 1);
                return true;
            }
        }
        return false;
    }
}
