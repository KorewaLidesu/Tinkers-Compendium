package lance5057.tDefense.core.tools.armor.renderers.chain;

import lance5057.tDefense.core.tools.armor.renderers.ArmorRenderer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.item.ItemStack;

/**
 * TinkersHauberk - Either Mojang or a mod author Created using Tabula 7.0.0
 */
public class ModelTinkersHauberk extends ArmorRenderer {
    public ModelRenderer ChestBelt;
    public ModelRenderer ChestBeltR;
    public ModelRenderer ChestBelt2;
    public ModelRenderer ChestBeltL;
    public ModelRenderer Chest;
    public ModelRenderer LeftArm;
    public ModelRenderer RightArm;
    public ModelRenderer Pauldron1;
    public ModelRenderer Pauldron2;
    public ModelRenderer PauldronStudR;
    public ModelRenderer Pauldron3R;
    public ModelRenderer Pauldron1L;
    public ModelRenderer Pauldron2L;
    public ModelRenderer PauldronStudL;
    public ModelRenderer Pauldron3L;

    public ModelTinkersHauberk(ItemStack stack) {
        super(0.25f, 0, 64, 64, stack);

        this.textureWidth = 48;
        this.textureHeight = 48;

        // Chest

        this.Chest = new ModelRenderer(this, 0, 7);
        this.Chest.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Chest.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F);
        this.bipedBody = this.Chest;

        this.ChestBeltL = new ModelRenderer(this, 0, 23);
        this.ChestBeltL.mirror = true;
        this.ChestBeltL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ChestBeltL.addBox(-8.0F, 2.3F, -2.5F, 11, 2, 5, -0.2F);
        this.setRotateAngle(ChestBeltL, 0.0F, 0.0F, -0.6981317007977318F);
        this.Chest.addChild(ChestBeltL);

        this.ChestBelt = new ModelRenderer(this, 0, 0);
        this.ChestBelt.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ChestBelt.addBox(-4.5F, 3.0F, -2.5F, 9, 2, 5, -0.19F);
        this.Chest.addChild(ChestBelt);

        this.ChestBelt2 = new ModelRenderer(this, 0, 0);
        this.ChestBelt2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ChestBelt2.addBox(-4.5F, 5.0F, -2.5F, 9, 2, 5, -0.19F);
        this.Chest.addChild(ChestBelt2);

        this.ChestBeltR = new ModelRenderer(this, 0, 23);
        this.ChestBeltR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ChestBeltR.addBox(-3.0F, 2.3F, -2.5F, 6, 2, 5, -0.21F);
        this.setRotateAngle(ChestBeltR, 0.0F, 0.0F, 0.6981317007977318F);
        this.Chest.addChild(ChestBeltR);

        // Left Arm

        this.LeftArm = new ModelRenderer(this, 0, 30);
        this.LeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.LeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
        this.bipedLeftArm = this.LeftArm;

        this.Pauldron1L = new ModelRenderer(this, 24, 7);
        this.Pauldron1L.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Pauldron1L.addBox(-0.5F, -1.9F, -2.5F, 4, 4, 5, 0.0F);
        this.setRotateAngle(Pauldron1L, 0.0F, 0.0F, -0.2617993877991494F);
        this.LeftArm.addChild(Pauldron1L);

        this.Pauldron2L = new ModelRenderer(this, 28, 0);
        this.Pauldron2L.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Pauldron2L.addBox(-1.0F, -3.0F, -2.0F, 3, 3, 4, 0.1F);
        this.setRotateAngle(Pauldron2L, 0.0F, 0.0F, 0.2617993877991494F);
        this.LeftArm.addChild(Pauldron2L);

        this.Pauldron3L = new ModelRenderer(this, 27, 16);
        this.Pauldron3L.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Pauldron3L.addBox(0.2F, -0.5F, -2.0F, 3, 4, 4, 0.1F);
        this.setRotateAngle(Pauldron3L, 0.0F, 0.0F, -0.08726646259971647F);
        this.LeftArm.addChild(Pauldron3L);

        this.PauldronStudL = new ModelRenderer(this, 16, 30);
        this.PauldronStudL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.PauldronStudL.addBox(-1.0F, -1.5F, -2.5F, 2, 2, 5, 0.1F);
        this.setRotateAngle(PauldronStudL, 0.0F, 0.0F, 0.2617993877991494F);
        this.LeftArm.addChild(PauldronStudL);

        // Right Arm

        this.RightArm = new ModelRenderer(this, 0, 30);
        this.RightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.RightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
        this.bipedRightArm = this.RightArm;

        this.Pauldron1 = new ModelRenderer(this, 24, 7);
        this.Pauldron1.mirror = true;
        this.Pauldron1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Pauldron1.addBox(-3.7F, -2.1F, -2.5F, 4, 4, 5, 0.0F);
        this.setRotateAngle(Pauldron1, 0.0F, 0.0F, 0.2617993877991494F);
        this.RightArm.addChild(Pauldron1);

        this.Pauldron2 = new ModelRenderer(this, 28, 0);
        this.Pauldron2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Pauldron2.addBox(-2.2F, -3.2F, -2.0F, 3, 3, 4, 0.1F);
        this.setRotateAngle(Pauldron2, 0.0F, 0.0F, -0.2617993877991494F);
        this.RightArm.addChild(Pauldron2);

        this.Pauldron3R = new ModelRenderer(this, 27, 16);
        this.Pauldron3R.mirror = true;
        this.Pauldron3R.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Pauldron3R.addBox(-3.4F, -0.7F, -2.0F, 3, 4, 4, 0.1F);
        this.setRotateAngle(Pauldron3R, 0.0F, 0.0F, 0.08726646259971647F);
        this.RightArm.addChild(Pauldron3R);

        this.PauldronStudR = new ModelRenderer(this, 16, 30);
        this.PauldronStudR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.PauldronStudR.addBox(-1.2000000000000002F, -1.7000000000000002F, -2.5F, 2, 2, 5, 0.1F);
        this.setRotateAngle(PauldronStudR, 0.0F, 0.0F, -0.2617993877991494F);
        this.RightArm.addChild(PauldronStudR);

    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
