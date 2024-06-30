package lance5057.tDefense.core.materials.stats;

import com.google.common.collect.Lists;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.CustomFontColor;
import slimeknights.tconstruct.library.materials.AbstractMaterialStats;

import java.util.List;

public class BaubleMaterialStats extends AbstractMaterialStats {
    public final static String TYPE = "bauble";

    public final static String LOC_Durability = "stat.boots.armor.durability.name";

    public final static String LOC_DurabilityDesc = "stat.boots.armor.durability.desc";

    public final static String COLOR_Durability = CustomFontColor.valueToColorCode(1f);

    public final int durability;    // usually between 1 and 1000

    public BaubleMaterialStats(int durability) {
        super(TYPE);
        this.durability = durability;
    }

    public static String formatDurability(int durability) {
        return formatNumber(LOC_Durability, COLOR_Durability, durability);
    }

    public static String formatDurability(int durability, int ref) {
        return String.format("%s: %s%s%s/%s%s",
                Util.translate(LOC_Durability),
                CustomFontColor.valueToColorCode((float) durability / (float) ref),
                Util.df.format(durability),
                TextFormatting.GRAY,
                COLOR_Durability,
                Util.df.format(ref)) + TextFormatting.RESET;
    }

    @Override
    public List<String> getLocalizedInfo() {
        List<String> info = Lists.newArrayList();

        info.add(formatDurability(durability));

        return info;
    }

    @Override
    public List<String> getLocalizedDesc() {
        List<String> info = Lists.newArrayList();

        info.add(Util.translate(LOC_DurabilityDesc));

        return info;
    }
}
