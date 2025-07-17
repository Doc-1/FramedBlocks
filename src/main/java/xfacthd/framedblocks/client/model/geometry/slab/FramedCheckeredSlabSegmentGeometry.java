package xfacthd.framedblocks.client.model.geometry.slab;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.data.PropertyHolder;

public class FramedCheckeredSlabSegmentGeometry extends Geometry
{
    private final boolean top;
    private final boolean second;

    public FramedCheckeredSlabSegmentGeometry(GeometryFactory.Context ctx)
    {
        this.top = ctx.state().getValue(FramedProperties.TOP);
        this.second = ctx.state().getValue(PropertyHolder.SECOND);
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad)
    {
        Direction quadDir = quad.direction();
        if (Utils.isY(quadDir))
        {
            boolean up = quadDir == Direction.UP;
            Direction xDir = (second ^ up) ? Direction.WEST : Direction.EAST;

            if (up == top)
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.SOUTH, .5F))
                        .apply(Modifiers.cut(xDir, .5F))
                        .export(quadMap.get(quadDir));

                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.NORTH, .5F))
                        .apply(Modifiers.cut(xDir.getOpposite(), .5F))
                        .export(quadMap.get(quadDir));
            }
            else
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.SOUTH, .5F))
                        .apply(Modifiers.cut(xDir.getOpposite(), .5F))
                        .apply(Modifiers.setPosition(.5F))
                        .export(quadMap.get(null));

                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.NORTH, .5F))
                        .apply(Modifiers.cut(xDir, .5F))
                        .apply(Modifiers.setPosition(.5F))
                        .export(quadMap.get(null));
            }
        }
        else
        {
            Direction horDir = Utils.isX(quadDir) ^ second ? quadDir.getCounterClockWise() : quadDir.getClockWise();

            if (!top)
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.UP, .5F))
                        .apply(Modifiers.cut(horDir, .5F))
                        .export(quadMap.get(quadDir));

                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.UP, .5F))
                        .apply(Modifiers.cut(horDir.getOpposite(), .5F))
                        .apply(Modifiers.setPosition(.5F))
                        .export(quadMap.get(null));
            }
            else
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.DOWN, .5F))
                        .apply(Modifiers.cut(horDir.getOpposite(), .5F))
                        .export(quadMap.get(quadDir));

                QuadModifier.of(quad)
                        .apply(Modifiers.cut(Direction.DOWN, .5F))
                        .apply(Modifiers.cut(horDir, .5F))
                        .apply(Modifiers.setPosition(.5F))
                        .export(quadMap.get(null));
            }
        }
    }
}
