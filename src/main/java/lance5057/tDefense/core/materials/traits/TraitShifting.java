package lance5057.tDefense.core.materials.traits;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TraitShifting extends AbstractTDTrait {
    List<AbstractTDTrait> traitPool;

    public TraitShifting(AbstractTDTrait... traits) {
        super("shifting", TextFormatting.LIGHT_PURPLE);

        traitPool = new ArrayList();
        traitPool.addAll(Arrays.asList(traits));
    }

    @Override
    public void onDamageTaken(ItemStack tool, LivingHurtEvent e) {
        traitPool.get(e.getEntityLiving().world.rand.nextInt(traitPool.size())).onDamageTaken(tool, e);
    }
}
