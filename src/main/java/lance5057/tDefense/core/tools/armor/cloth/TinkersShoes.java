package lance5057.tDefense.core.tools.armor.cloth;

import lance5057.tDefense.core.library.ArmorNBT;
import lance5057.tDefense.core.library.ArmorTags;
import lance5057.tDefense.core.library.ArmorTextureBuilder;
import lance5057.tDefense.core.materials.CompendiumMaterials;
import lance5057.tDefense.core.materials.stats.ArmorMaterialStats;
import lance5057.tDefense.core.materials.stats.FabricMaterialStats;
import lance5057.tDefense.core.parts.TDParts;
import lance5057.tDefense.core.tools.armor.renderers.cloth.ModelTinkersShoes;
import lance5057.tDefense.core.tools.bases.ArmorCore;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;

import javax.annotation.Nonnull;
import java.util.List;

public class TinkersShoes extends ArmorCore {
    int induceDamage = 0;

    public TinkersShoes() {
        super(EntityEquipmentSlot.FEET, new PartMaterialType(TDParts.fabric, FabricMaterialStats.TYPE),
                PartMaterialType.extra(TDParts.rivets), new PartMaterialType(TDParts.fabric, FabricMaterialStats.TYPE),
                PartMaterialType.bowstring(TinkerTools.bowString));
        setTranslationKey("tinkershoes");

    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            addDefaultSubItems(subItems, CompendiumMaterials.white.mat, CompendiumMaterials.white.mat, null,
                    TinkerMaterials.string);
        }
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int par4, boolean par5) {
        super.onUpdate(stack, world, entity, par4, par5);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public NBTTagCompound setupTexture(List<Material> materials) {
        NBTTagCompound base = new NBTTagCompound();

        ResourceLocation rc = ArmorTextureBuilder.createArmorTexture("shoes",
                new String[]{"cloth", "metal", "trim", "string"}, materials, 64, 64);

        if (rc != null) {
            base.setString(ArmorTags.TexLoc, rc.toString());
            return base;
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot,
                                    ModelBiped _default) {
        return new ModelTinkersShoes(itemStack);
    }

    @Override
    public NBTTagCompound buildTag(List<Material> materials) {
        ArmorNBT data = buildDefaultTag(materials);
        return data.get();
    }

    @Override
    public EntityEquipmentSlot getArmorSlot(ItemStack stack, EntityEquipmentSlot armorType) {
        return EntityEquipmentSlot.FEET;
    }

    @Override
    public float armorMultiplier() {
        // TODO Auto-generated method stub
        return 0.1f;
    }

    @Override
    public float potencyMultiplier() {
        // TODO Auto-generated method stub
        return 1f;
    }

    @Override
    protected ArmorNBT buildDefaultTag(List<Material> materials) {
        ArmorNBT data = new ArmorNBT();

        if (materials.size() >= 2) {
            ArmorMaterialStats handle = materials.get(0).getStatsOrUnknown(FabricMaterialStats.TYPE);
            ArmorMaterialStats head = materials.get(1).getStatsOrUnknown(FabricMaterialStats.TYPE);
            // start with head
            data.head(this, head, handle);

            // add in accessoires if present
            if (materials.size() >= 3) {
                ExtraMaterialStats binding = materials.get(2).getStatsOrUnknown(MaterialTypes.EXTRA);
                data.extra(binding);
            }

            // calculate handle impact
            // data.head(handle);
        }

        // 3 free modifiers
        data.modifiers = 5;

        return data;
    }

    @Override
    public String getArmorType() {
        return "shoes";
    }
}
