package lance5057.tDefense.core.materials.traits;

import lance5057.tDefense.core.entities.EntityPhantomWolf;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class TraitDogToy extends AbstractTDTrait {

    public TraitDogToy() {
        super("dogtoy", TextFormatting.WHITE);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onBlock(ItemStack tool, EntityPlayer player, LivingHurtEvent event) {

        if (event.getSource().getTrueSource() instanceof EntityLiving
                || event.getSource().getImmediateSource() instanceof EntityLiving) {
            BlockPos pos = new BlockPos(player.posX + player.world.rand.nextInt(10) - 5, player.posY,
                    player.posZ + player.world.rand.nextInt(10) - 5);
            IBlockState iblockstate = player.world.getBlockState(pos);

            EnumFacing facing = player.getHorizontalFacing();
            pos = pos.offset(facing);
            double d0 = 0.0D;

            if (facing == EnumFacing.UP && iblockstate.getBlock() instanceof BlockFence) {
                d0 = 0.5D;
            }

            EntityPhantomWolf entWolf = new EntityPhantomWolf(player.world);
            entWolf.setLocationAndAngles(pos.getX() + 0.5D, (double) pos.getY() + d0, (double) pos.getZ() + 0.5D,
                    MathHelper.wrapDegrees(player.world.rand.nextFloat() * 360.0F), 0.0F);
            entWolf.rotationYawHead = entWolf.rotationYaw;
            entWolf.renderYawOffset = entWolf.rotationYaw;
            entWolf.onInitialSpawn(player.world.getDifficultyForLocation(new BlockPos(entWolf)),
                    null);

            if (event.getSource().getTrueSource() instanceof EntityLiving)
                entWolf.setAttackTarget((EntityLivingBase) event.getSource().getTrueSource());
            else
                entWolf.setAttackTarget((EntityLivingBase) event.getSource().getImmediateSource());

            entWolf.setAngry(true);
            entWolf.setCollarColor(EnumDyeColor.BLACK);
            entWolf.setTamed(true);
            player.world.spawnEntity(entWolf);
            entWolf.playLivingSound();

            entWolf.setCustomNameTag("Guard Dog");
            entWolf.setDropItemsWhenDead(false);

            // applyItemEntityDataToEntity(player.world, player, null, entity);
        }
    }
}
