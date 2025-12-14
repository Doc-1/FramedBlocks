package io.github.xfacthd.framedblocks.api.model;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.model.data.ModelData;
import org.jspecify.annotations.Nullable;

public record ModelPartCollectionFakeLevel(BlockState state, ModelData modelData) implements BlockAndTintGetter
{
    @Override
    public BlockState getBlockState(BlockPos pos)
    {
        if (pos.equals(BlockPos.ZERO))
        {
            return state;
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public ModelData getModelData(BlockPos pos)
    {
        if (pos.equals(BlockPos.ZERO))
        {
            return modelData;
        }
        return ModelData.EMPTY;
    }

    @Override
    public float getShade(Direction direction, boolean shade)
    {
        return 1F;
    }

    @Override
    public float getShade(float normalX, float normalY, float normalZ, boolean shade)
    {
        return 1F;
    }

    @Override
    public LevelLightEngine getLightEngine()
    {
        return LevelLightEngine.EMPTY;
    }

    @Override
    public int getBlockTint(BlockPos blockPos, ColorResolver colorResolver)
    {
        return -1;
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos)
    {
        return null;
    }

    @Override
    public FluidState getFluidState(BlockPos pos)
    {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public int getHeight()
    {
        return 0;
    }

    @Override
    public int getMinY()
    {
        return 0;
    }
}
