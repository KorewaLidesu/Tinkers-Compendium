package lance5057.tDefense.core.tools.basic;

import lance5057.tDefense.core.entities.EntityTinkersFishHook;
import lance5057.tDefense.core.parts.TDParts;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;

import javax.annotation.Nullable;
import java.util.List;

public class FishingRod extends ToolCore {

    public static final float DURABILITY_MODIFIER = 1.0f;

    public FishingRod() {
        super(PartMaterialType.head(TinkerTools.toughToolRod), PartMaterialType.bowstring(TinkerTools.bowString),
                PartMaterialType.handle(TDParts.rivets));

        setTranslationKey("td_fishingRod");
        this.addPropertyOverride(new ResourceLocation("cast"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    boolean flag = entityIn.getHeldItemMainhand() == stack;
                    boolean flag1 = entityIn.getHeldItemOffhand() == stack;

                    if (entityIn.getHeldItemMainhand().getItem() instanceof FishingRod) {
                        flag1 = false;
                    }

                    return (flag || flag1) && entityIn instanceof EntityPlayer
                            && ((EntityPlayer) entityIn).fishEntity != null ? 1.0F : 0.0F;
                }
            }
        });
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            addDefaultSubItems(subItems, null, TinkerMaterials.string, null);
        }
    }

    @Override
    public float damagePotential() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double attackSpeed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public NBTTagCompound buildTag(List<Material> materials) {
        ToolNBT data = buildDefaultTag(materials);
        // 2 base damage, like vanilla swords
        data.attack += 1f;
        data.durability *= DURABILITY_MODIFIER;
        return data.get();
    }

    // TODO: create custom bobber entity
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (playerIn.fishEntity != null) {
            int i = playerIn.fishEntity.handleHookRetraction();
            itemstack.damageItem(i, playerIn);
            playerIn.swingArm(handIn);
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                    SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F,
                    0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        } else {
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                    SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F,
                    0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!worldIn.isRemote) {
                EntityTinkersFishHook entityfishhook = new EntityTinkersFishHook(worldIn, playerIn);
                int j = EnchantmentHelper.getFishingSpeedBonus(itemstack);

                if (j > 0) {
                    entityfishhook.setLureSpeed(j);
                }

                int k = EnchantmentHelper.getFishingLuckBonus(itemstack);

                if (k > 0) {
                    entityfishhook.setLuck(k);
                }

                worldIn.spawnEntity(entityfishhook);
            }

            playerIn.swingArm(handIn);
            playerIn.addStat(StatList.getObjectUseStats(this));
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
