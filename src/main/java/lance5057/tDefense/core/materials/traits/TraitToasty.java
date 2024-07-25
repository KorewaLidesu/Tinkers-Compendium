package lance5057.tDefense.core.materials.traits;

import lance5057.tDefense.TCConfig;
import lance5057.tDefense.util.ArmorTagUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class TraitToasty extends AbstractTDTrait {

    public TraitToasty() {
        super("toasty", TextFormatting.RED);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onArmorTick(ItemStack tool, World world, EntityPlayer player) {

        List<ItemStack> items = player.inventory.mainInventory;
        for (int i = 0; i < items.size(); i++) {
            if (world.rand.nextInt(100) <= 1) {
                if (items.get(i).getItem() instanceof ItemFood) {
                    ItemStack cooked = FurnaceRecipes.instance().getSmeltingResult(items.get(i)).copy();
                    if (cooked.getItem() != Items.AIR && player.inventory.addItemStackToInventory(cooked)) {
                        player.inventory.mainInventory.get(i).splitStack(1);
                    }
                }

                if (!TCConfig.DontTouchMyBucket) {
                    if (items.get(i).getItem() == Items.WATER_BUCKET) {
                        if (player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET, 1, 0))) {
                            player.inventory.mainInventory.get(i).splitStack(1);
                        }
                    }
                }
            }
        }

        if (ArmorTagUtil.getWater(tool) > 0) {
            ArmorTagUtil.setWater(tool, ArmorTagUtil.getWater(tool) - 1);
        }
    }
}
