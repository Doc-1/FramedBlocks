package io.github.xfacthd.framedblocks.common.data.shapes;

import com.google.common.collect.ImmutableList;
import io.github.xfacthd.framedblocks.api.shapes.ShapeGenerator;
import io.github.xfacthd.framedblocks.api.shapes.ShapeProvider;
import net.minecraft.world.level.block.state.BlockState;

public interface SplitShapeGenerator extends ShapeGenerator
{
    ShapeProvider generateOcclusionShapes(ImmutableList<BlockState> states);
}
