package lance5057.tDefense.core.materials.traits;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class TraitAcknowledged extends AbstractTDTrait {

    public TraitAcknowledged() {
        super("acknowledged", 0xffffff);

        //MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onXpDrop(LivingExperienceDropEvent event) {
        EntityPlayer player = event.getAttackingPlayer();
        if (player != null) {
            for (ItemStack armor : player.getArmorInventoryList()) {
                if (TinkerUtil.hasTrait(TagUtil.getTagSafe(armor), identifier)) {
                    event.setDroppedExperience(getUpdateXP(event.getDroppedExperience()));
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null) {
            for (ItemStack armor : player.getArmorInventoryList()) {
                if (TinkerUtil.hasTrait(TagUtil.getTagSafe(armor), identifier)) {
                    float r = random.nextFloat();
                    int expToDrop = event.getExpToDrop();
                    // 30% chance for 1 bonus xp
                    if (r < 0.33f || (expToDrop == 0 && r < 0.03f)) {
                        event.setExpToDrop(expToDrop + 1);
                    }
                }
            }
        }
    }

    private int getUpdateXP(int xp) {
        if (xp == 0) {
            // 3% chance to give 1 xp still
            if (random.nextFloat() < 0.03f) {
                return 1;
            }
            return 0;
        }
        float exp = (float) xp * 1.25f + random.nextFloat() * 0.25f;
        return 1 + Math.round(exp);
    }
}