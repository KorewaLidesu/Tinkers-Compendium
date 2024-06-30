package lance5057.tDefense.core.library.events;

import lance5057.tDefense.core.tools.bases.ArmorCore;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.library.events.TinkerEvent;

public abstract class TinkerArmorEvent extends TinkerEvent {

    public final ItemStack itemStack;
    public final ArmorCore tool;

    public TinkerArmorEvent(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.tool = (ArmorCore) itemStack.getItem();
    }

    public static class OnRepair extends TinkerArmorEvent {

        public final int amount;

        public OnRepair(ItemStack itemStack, int amount) {
            super(itemStack);
            this.amount = amount;
        }

        public static boolean fireEvent(ItemStack itemStack, int amount) {
            OnRepair event = new OnRepair(itemStack, amount);
            return !MinecraftForge.EVENT_BUS.post(event);
        }
    }
}