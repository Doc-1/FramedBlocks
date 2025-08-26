package io.github.xfacthd.framedblocks.common.data.shapes;

import io.github.xfacthd.framedblocks.api.shapes.ShapeGenerator;
import io.github.xfacthd.framedblocks.api.shapes.ShapeProvider;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface SplitShapeGenerator extends ShapeGenerator
{
    ShapeProvider generateOcclusionShapes(List<BlockState> states);
}
