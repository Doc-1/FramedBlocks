package xfacthd.framedblocks.client.model.geometry.cube;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.data.PropertyHolder;

public class FramedTubeGeometry extends Geometry
{
    private final Direction.Axis axis;
    private final float thickness;

    public FramedTubeGeometry(GeometryFactory.Context ctx)
    {
        this.axis = ctx.state().getValue(BlockStateProperties.AXIS);
        this.thickness = (ctx.state().getValue(PropertyHolder.THICK) ? 3F : 2F) / 16F;
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad, ModelData modelData)
    {
        Direction quadDir = quad.direction();
        if (axis == Direction.Axis.Y)
        {
            if (quadDir.getAxis() == axis)
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.NORTH, thickness))
                        .export(quadMap.get(quadDir));
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.SOUTH, thickness))
                        .export(quadMap.get(quadDir));
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.EAST, thickness))
                        .apply(Modifiers.cut(Direction.Axis.Z, 1F - thickness))
                        .export(quadMap.get(quadDir));
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.WEST, thickness))
                        .apply(Modifiers.cut(Direction.Axis.Z, 1F - thickness))
                        .export(quadMap.get(quadDir));
            }
            else
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(quadDir.getClockWise().getAxis(), 1F - thickness))
                        .apply(Modifiers.setPosition(thickness))
                        .export(quadMap.get(null));
            }
        }
        else
        {
            if (quadDir.getAxis() == axis)
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.UP, thickness))
                        .export(quadMap.get(quadDir));
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.DOWN, thickness))
                        .export(quadMap.get(quadDir));
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(quadDir.getClockWise(), thickness))
                        .apply(Modifiers.cut(Direction.Axis.Y, 1F - thickness))
                        .export(quadMap.get(quadDir));
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(quadDir.getCounterClockWise(), thickness))
                        .apply(Modifiers.cut(Direction.Axis.Y, 1F - thickness))
                        .export(quadMap.get(quadDir));
            }
            else if (Utils.isY(quadDir))
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Utils.nextAxisNotEqualTo(axis, Direction.Axis.Y), 1F - thickness))
                        .apply(Modifiers.setPosition(thickness))
                        .export(quadMap.get(null));
            }
            else
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.Axis.Y, 1F - thickness))
                        .apply(Modifiers.setPosition(thickness))
                        .export(quadMap.get(null));
            }
        }
    }

    @Override
    public boolean transformAllQuads()
    {
        return true;
    }
}
