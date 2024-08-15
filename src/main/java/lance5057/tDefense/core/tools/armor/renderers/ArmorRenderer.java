package lance5057.tDefense.core.tools.armor.renderers;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

import java.util.Map;

public class ArmorRenderer extends ModelBiped {
    public Map<String, Boolean> partsList;

    public ItemStack stack;
    public String defaultFolder;
    public EntityEquipmentSlot slot;
    public NBTTagCompound defaultTags = new NBTTagCompound();
    protected boolean visor = false;
    protected float visorTime = 0f;
    DynamicTexture texture;

    public ArmorRenderer(float a, float b, int c, int d, ItemStack stack) {
        super(a, b, c, d);

        this.stack = stack;
    }

    public ArmorRenderer(float a, float b, int c, int d, ItemStack stack, EntityEquipmentSlot s) {
        super(a, b, c, d);

        this.stack = stack;
        slot = s;
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    // Mojang plz
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                                  float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn instanceof EntityArmorStand) {
            EntityArmorStand entityarmorstand = (EntityArmorStand) entityIn;
            this.bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
            this.bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
            this.bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
            this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
            this.bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            this.bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            this.bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            this.bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
            this.bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
            this.bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
            this.bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
            this.bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
            this.bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
            this.bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
            this.bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
            this.bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
            this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
            this.bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
            this.bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
            this.bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
            this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
            copyModelAngles(this.bipedHead, this.bipedHeadwear);
        } else {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
            if (entityIn instanceof EntityZombie) {
                boolean flag = ((EntityZombie)entityIn).isArmsRaised();
                float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
                float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
                this.bipedRightArm.rotateAngleZ = 0.0F;
                this.bipedLeftArm.rotateAngleZ = 0.0F;
                this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
                this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
                float f2 = -(float)Math.PI / (flag ? 1.5F : 2.25F);
                this.bipedRightArm.rotateAngleX = f2;
                this.bipedLeftArm.rotateAngleX = f2;
                this.bipedRightArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
                this.bipedLeftArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
                this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
                this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
                this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
                this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            }
        }

    }

    protected ModelRenderer lerpModel(ModelRenderer from, ModelRenderer to, float time) {
        if (from.offsetX != to.offsetX)
            from.offsetX = lerp(from.offsetX, to.offsetX, time);
        if (from.offsetY != to.offsetY)
            from.offsetY = lerp(from.offsetY, to.offsetY, time);
        if (from.offsetZ != to.offsetZ)
            from.offsetZ = lerp(from.offsetZ, to.offsetZ, time);

        if (from.rotateAngleX != to.rotateAngleX)
            from.rotateAngleX = lerp(from.rotateAngleX, to.rotateAngleX, time);
        if (from.rotateAngleY != to.rotateAngleY)
            from.rotateAngleY = lerp(from.rotateAngleY, to.rotateAngleY, time);
        if (from.rotateAngleZ != to.rotateAngleZ)
            from.rotateAngleZ = lerp(from.rotateAngleZ, to.rotateAngleZ, time);

        return from;
    }

    private float lerp(float from, float to, float time) {
        return (1 - time) * from + time * to;
    }
}
