package lance5057.tDefense.core.materials.traits;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitVoidCall extends AbstractTDTrait {
    public TraitVoidCall() {
        super("voidcall", TextFormatting.WHITE);
    }

    @Override
    public void onArmorTick(ItemStack tool, World world, EntityPlayer player) {
        if (player.posY <= 0) {
            player.motionY += 1.2;
            ToolHelper.damageTool(tool, 10, player);
        }
    }
}
