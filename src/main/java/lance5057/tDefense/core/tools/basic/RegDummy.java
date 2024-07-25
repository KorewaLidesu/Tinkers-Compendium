package lance5057.tDefense.core.tools.basic;

import lance5057.tDefense.core.materials.stats.*;
import lance5057.tDefense.core.parts.TDParts;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

//Till I figure out why Armor Parts wont register normally
public class RegDummy extends ToolCore {

    public static final float DURABILITY_MODIFIER = 1.0f;

    public RegDummy() {
        super(new PartMaterialType(TinkerTools.largePlate, ChestMaterialStats.TYPE),
                new PartMaterialType(TDParts.armorPlate, ChestMaterialStats.TYPE),
                PartMaterialType.handle(TDParts.filigree), PartMaterialType.extra(TDParts.chainmail),
                new PartMaterialType(TDParts.fabric, FabricMaterialStats.TYPE),
                new PartMaterialType(TDParts.armorPlate, LegsMaterialStats.TYPE),
                new PartMaterialType(TDParts.chainmail, LegsMaterialStats.TYPE), PartMaterialType.extra(TDParts.clasp),
                new PartMaterialType(TDParts.fabric, FabricMaterialStats.TYPE),
                new PartMaterialType(TinkerTools.panHead, HelmMaterialStats.TYPE),
                new PartMaterialType(TDParts.chainmail, HelmMaterialStats.TYPE),
                PartMaterialType.extra(TDParts.armorPlate),
                new PartMaterialType(TDParts.fabric, FabricMaterialStats.TYPE),
                new PartMaterialType(TDParts.armorPlate, FeetMaterialStats.TYPE),
                new PartMaterialType(TDParts.armorPlate, FeetMaterialStats.TYPE),
                PartMaterialType.extra(TDParts.rivets), new PartMaterialType(TDParts.fabric, FabricMaterialStats.TYPE));

//		super(PartMaterialType.head(TinkerTools.swordBlade), 
//				PartMaterialType.handle(TinkerTools.toolRod), 
//				PartMaterialType.extra(TinkerTools.wideGuard),
//				PartMaterialType.extra(TDParts.rivets),
//				PartMaterialType.extra(TDParts.fabric),
//				PartMaterialType.extra(TDParts.filigree),
//				PartMaterialType.extra(TinkerTools.arrowHead));

        setTranslationKey("regdummy");
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
}
