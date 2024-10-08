package lance5057.tDefense.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class CrackedObsidian extends BlockBreakable {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

    public CrackedObsidian() {
        super(Material.ROCK, false);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE).intValue();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(MathHelper.clamp(meta, 0, 3)));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if ((rand.nextInt(3) == 0 || this.countNeighbors(worldIn, pos) < 4) && worldIn.getLightFromNeighbors(pos) > 11
                - state.getValue(AGE).intValue() - state.getLightOpacity()) {
            this.slightlyMelt(worldIn, pos, state, rand, true);
        } else {
            worldIn.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should
     * perform any checks during a neighbor change. Cases may include when redstone
     * power is updated, cactus blocks popping off due to a neighboring solid block,
     * etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (blockIn == this) {
            int i = this.countNeighbors(worldIn, pos);

            if (i < 2) {
                this.turnIntoLava(worldIn, pos);
            }
        }
    }

    private int countNeighbors(World worldIn, BlockPos pos) {
        int i = 0;

        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this) {
                ++i;

                if (i >= 4) {
                    return i;
                }
            }
        }

        return i;
    }

    protected void slightlyMelt(World worldIn, BlockPos pos, IBlockState state, Random rand, boolean meltNeighbors) {
        int i = state.getValue(AGE).intValue();

        if (i < 3) {
            worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i + 1)), 2);
            worldIn.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
        } else {
            this.turnIntoLava(worldIn, pos);

            if (meltNeighbors) {
                for (EnumFacing enumfacing : EnumFacing.values()) {
                    BlockPos blockpos = pos.offset(enumfacing);
                    IBlockState iblockstate = worldIn.getBlockState(blockpos);

                    if (iblockstate.getBlock() == this) {
                        this.slightlyMelt(worldIn, blockpos, iblockstate, rand, false);
                    }
                }
            }
        }
    }

    private void turnIntoLava(World worldIn, BlockPos pos) {
        worldIn.setBlockState(pos, Blocks.LAVA.getDefaultState());
        worldIn.neighborChanged(pos, Blocks.LAVA, pos);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }
}
