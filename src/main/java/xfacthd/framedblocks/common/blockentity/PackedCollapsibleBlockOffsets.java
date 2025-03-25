package xfacthd.framedblocks.common.blockentity;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockParts;

public sealed interface PackedCollapsibleBlockOffsets
{
    ModelProperty<PackedCollapsibleBlockOffsets> PROPERTY = new ModelProperty<>();

    int unwrap(BlockState state);

    static int get(ModelData modelData, BlockState partState)
    {
        PackedCollapsibleBlockOffsets offsets = modelData.get(PROPERTY);
        return offsets != null ? offsets.unwrap(partState) : 0;
    }

    record Single(int offsets) implements PackedCollapsibleBlockOffsets
    {
        @Override
        public int unwrap(BlockState state)
        {
            return offsets;
        }
    }

    record Double(DoubleBlockParts parts, int offsetsOne, int offsetsTwo) implements PackedCollapsibleBlockOffsets
    {
        @Override
        public int unwrap(BlockState state)
        {
            return state == parts.stateTwo() ? offsetsTwo : offsetsOne;
        }
    }
}
