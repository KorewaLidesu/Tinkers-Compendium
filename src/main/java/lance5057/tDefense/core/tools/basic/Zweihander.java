package lance5057.tDefense.core.tools.basic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

public class Zweihander extends SwordCore {

    public static final float DURABILITY_MODIFIER = 1.5f;

    public Zweihander() {
        super(PartMaterialType.head(TinkerTools.largeSwordBlade), PartMaterialType.head(TinkerTools.swordBlade),
                PartMaterialType.handle(TinkerTools.toughToolRod), PartMaterialType.extra(TinkerTools.toughBinding));

        setTranslationKey("td_zweihander");
        addCategory(Category.WEAPON);
    }

    @Override
    public float damagePotential() {
        return 1.0f;
    }

    @Override
    public double attackSpeed() {
        return 1.0d;
    }

    // sword sweep attack
    @Override
    public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
        // deal damage first
        boolean hit = super.dealDamage(stack, player, entity, damage);
        // and then sweep
        if (hit && !ToolHelper.isBroken(stack)) {
            // sweep code from EntityPlayer#attackTargetEntityWithCurrentItem()
            // basically: no crit, no sprinting and has to stand on the ground
            // for sweep. Also has to move regularly slowly
            double d0 = player.distanceWalkedModified - player.prevDistanceWalkedModified;
            boolean flag = true;
            if (player instanceof EntityPlayer) {
                flag = ((EntityPlayer) player).getCooledAttackStrength(0.5F) > 0.9f;
            }
            boolean flag2 = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder()
                    && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding();
            if (flag && !player.isSprinting() && !flag2 && player.onGround && d0 < (double) player.getAIMoveSpeed()) {
                for (EntityLivingBase entitylivingbase : player.getEntityWorld().getEntitiesWithinAABB(
                        EntityLivingBase.class, entity.getEntityBoundingBox().expand(2.0D, 0.25D, 2.0D))) {
                    if (entitylivingbase != player && entitylivingbase != entity
                            && !player.isOnSameTeam(entitylivingbase)
                            && player.getDistanceSq(entitylivingbase) < 9.0D) {
                        entitylivingbase.knockBack(player, 0.4F,
                                MathHelper.sin(player.rotationYaw * 0.017453292F),
                                -MathHelper.cos(player.rotationYaw * 0.017453292F));
                        super.dealDamage(stack, player, entitylivingbase, damage / 2);
                    }
                }

                player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ,
                        SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                if (player instanceof EntityPlayer) {
                    ((EntityPlayer) player).spawnSweepParticles();
                }
            }
        }

        return hit;
    }

    @Override
    public float getRepairModifierForPart(int index) {
        return DURABILITY_MODIFIER;
    }

    @Override
    public ToolNBT buildTagData(List<Material> materials) {
        HeadMaterialStats head = materials.get(0).getStatsOrUnknown(MaterialTypes.HEAD);
        HeadMaterialStats spike = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
        HandleMaterialStats handle = materials.get(2).getStatsOrUnknown(MaterialTypes.HANDLE);
        ExtraMaterialStats binding = materials.get(3).getStatsOrUnknown(MaterialTypes.EXTRA);

        ToolNBT data = new ToolNBT();
        data.head(head, spike);
        data.handle(handle);
        data.extra(binding);

        data.attack *= 1.3f;
        data.attack += 2f;

        // triple durability!
        data.durability *= DURABILITY_MODIFIER;

        return data;
    }
}
