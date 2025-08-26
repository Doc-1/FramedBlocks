package io.github.xfacthd.framedblocks.client.model.special;

import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import io.github.xfacthd.framedblocks.api.model.standalone.CachingModel;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.LatchType;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.Map;

public final class FramedChestLidModel implements CachingModel
{
    private static final Direction[] DIRECTIONS = Direction.Plane.HORIZONTAL.stream().toArray(Direction[]::new);
    private static final ChestType[] TYPES = ChestType.values();
    private static final LatchType[] LATCHES = LatchType.values();
    private static final int DIRECTION_COUNT = DIRECTIONS.length;
    private static final int TYPE_COUNT = TYPES.length;
    private static final int MODEL_COUNT = DIRECTION_COUNT * TYPE_COUNT * LATCHES.length;

    private final BlockStateModel[] models = new BlockStateModel[MODEL_COUNT];

    public FramedChestLidModel(Map<BlockState, BlockStateModel> models)
    {
        for (Direction dir : Direction.Plane.HORIZONTAL)
        {
            for (ChestType type : TYPES)
            {
                for (LatchType latch : LATCHES)
                {
                    BlockState state = FBContent.BLOCK_FRAMED_CHEST.value().defaultBlockState()
                            .setValue(FramedProperties.FACING_HOR, dir)
                            .setValue(BlockStateProperties.CHEST_TYPE, type)
                            .setValue(PropertyHolder.LATCH_TYPE, latch);
                    this.models[makeModelIndex(dir, type, latch)] = models.get(state);
                }
            }
        }
    }

    public BlockStateModel getModel(Direction dir, ChestType type, LatchType latch)
    {
        return models[makeModelIndex(dir, type, latch)];
    }

    @Override
    public void clearCache()
    {
        for (BlockStateModel model : models)
        {
            if (model instanceof AbstractFramedBlockModel fbModel)
            {
                fbModel.clearCache();
            }
        }
    }

    private static int makeModelIndex(Direction dir, ChestType type, LatchType latch)
    {
        return dir.get2DDataValue() + (type.ordinal() * DIRECTION_COUNT) + (latch.ordinal() * DIRECTION_COUNT * TYPE_COUNT);
    }
}
