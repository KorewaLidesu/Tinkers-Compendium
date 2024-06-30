package lance5057.tDefense.core.materials.traits;

import lance5057.tDefense.core.tools.TDToolEvents;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class TraitCurrentRider extends AbstractTDTrait {

    public TraitCurrentRider() {
        super("currentrider", TextFormatting.AQUA);
    }

    @Override
    public void onArmorTick(ItemStack tool, World world, EntityPlayer player) {
        IAttributeInstance att = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (player.isInWater() && world.getBlockState(player.getPosition()) == Blocks.FLOWING_WATER) {
            if (!att.hasModifier(TDToolEvents.td_currentrider)) {
                att.applyModifier(TDToolEvents.td_currentrider);
            }
        }
    }
}
