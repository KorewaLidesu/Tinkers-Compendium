package lance5057.tDefense.core.materials.traits;

import lance5057.tDefense.util.ArmorTagUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class TraitElectrostaticDischarge extends AbstractTDTrait {

    public TraitElectrostaticDischarge() {
        super("electrostaticdischarge", TextFormatting.YELLOW);
    }

    @Override
    public void onDamageTaken(ItemStack tool, LivingHurtEvent e) {
        if (ArmorTagUtil.getCharge(tool) > 10) {
            EntityLivingBase player = e.getEntityLiving();

            for (EntityLivingBase entitylivingbase : player.getEntityWorld().getEntitiesWithinAABB(
                    EntityLivingBase.class, player.getEntityBoundingBox().expand(2.0D, 0.25D, 2.0D))) {
                if (entitylivingbase != player && entitylivingbase != player && !player.isOnSameTeam(entitylivingbase)
                        && player.getDistanceSq(entitylivingbase) < 9.0D) {
                    entitylivingbase.knockBack(player, 0.4F, MathHelper.sin(player.rotationYaw * 0.017453292F),
                            -MathHelper.cos(player.rotationYaw * 0.017453292F));
                }
            }
            ArmorTagUtil.setCharge(tool, 0);
        } else {
            ArmorTagUtil.setCharge(tool, ArmorTagUtil.getCharge(tool) + 1);
        }

    }
}
