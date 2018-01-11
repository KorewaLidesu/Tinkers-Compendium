package lance5057.tDefense.core.tools.armor.cloth;

import lance5057.tDefense.core.materials.ClothMaterialStats;
import lance5057.tDefense.core.materials.HelmMaterialStats;
import lance5057.tDefense.core.parts.TDParts;
import lance5057.tDefense.core.tools.bases.ArmorCore;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;

public class TinkersShawl extends ArmorCore
{
	int	induceDamage	= 0;
	

	public TinkersShawl()
	{
	    super(new PartMaterialType(TDParts.cloth, ClothMaterialStats.TYPE),
	    	new PartMaterialType(TDParts.cloth, ClothMaterialStats.TYPE),
	    	PartMaterialType.extra(TDParts.armorPlate));
		setUnlocalizedName("tinkershawl");
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
	{
		super.onUpdate(stack, world, entity, par4, par5);

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

	
}
