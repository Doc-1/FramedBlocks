package xfacthd.framedblocks.client.model.geometry.stairs;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.data.PropertyHolder;

public class FramedHalfStairsGeometry extends Geometry
{
    private final Direction dir;
    private final boolean top;
    private final boolean right;

    public FramedHalfStairsGeometry(GeometryFactory.Context ctx)
    {
        this.dir = ctx.state().getValue(FramedProperties.FACING_HOR);
        this.top = ctx.state().getValue(FramedProperties.TOP);
        this.right = ctx.state().getValue(PropertyHolder.RIGHT);
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad)
    {
        Direction face = quad.direction();
        Direction horCut = right ? dir.getCounterClockWise() : dir.getClockWise();
        Direction vertCut = top ? Direction.DOWN : Direction.UP;

        if (face == dir)
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(horCut, .5F))
                    .export(quadMap.get(face));
        }
        else if (face == dir.getOpposite())
        {
            QuadModifier mod = QuadModifier.of(quad)
                    .apply(Modifiers.cut(horCut, .5F));

            mod.derive().apply(Modifiers.cut(vertCut.getOpposite(), .5F))
                    .apply(Modifiers.setPosition(.5F))
                    .export(quadMap.get(null));

            mod.apply(Modifiers.cut(vertCut, .5F))
                    .export(quadMap.get(face));
        }
        else if (!Utils.isY(face) && face.getAxis() != dir.getAxis())
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(dir.getOpposite(), .5F))
                    .applyIf(Modifiers.setPosition(.5F), face == horCut)
                    .export(quadMap.get(face == horCut ? null : face));

            QuadModifier.of(quad)
                    .apply(Modifiers.cut(dir, .5F))
                    .apply(Modifiers.cut(vertCut, .5F))
                    .applyIf(Modifiers.setPosition(.5F), face == horCut)
                    .export(quadMap.get(face == horCut ? null : face));
        }
        else if (Utils.isY(face))
        {
            boolean base = (face == Direction.UP && top) || (face == Direction.DOWN && !top);

            QuadModifier mod = QuadModifier.of(quad)
                    .apply(Modifiers.cut(horCut, .5F));

            if (!base)
            {
                mod.derive().apply(Modifiers.cut(dir, .5F))
                        .apply(Modifiers.setPosition(.5F))
                        .export(quadMap.get(null));
            }

            mod.applyIf(Modifiers.cut(dir.getOpposite(), .5F), !base)
                    .export(quadMap.get(face));
        }
    }
}
