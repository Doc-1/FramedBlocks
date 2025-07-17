package xfacthd.framedblocks.client.model.geometry.stairs;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.util.Utils;

public class FramedSlopedStairsGeometry extends Geometry
{
    private final Direction dir;
    private final boolean top;

    public FramedSlopedStairsGeometry(GeometryFactory.Context ctx)
    {
        this.dir = ctx.state().getValue(FramedProperties.FACING_HOR);
        this.top = ctx.state().getValue(FramedProperties.TOP);
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad, ModelData modelData)
    {
        Direction quadDir = quad.direction();

        if (Utils.isY(quadDir))
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(dir.getOpposite(), 1, 0))
                    .export(quadMap.get(quadDir));

            QuadModifier.of(quad)
                    .apply(Modifiers.cut(dir, 1, 0))
                    .apply(Modifiers.setPosition(.5F))
                    .export(quadMap.get(null));
        }
        else
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(top ? Direction.DOWN : Direction.UP, .5F))
                    .export(quadMap.get(quadDir));

            if (quadDir == dir.getOpposite())
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.makeHorizontalSlope(false, 45))
                        .apply(Modifiers.cut(top ? Direction.UP : Direction.DOWN, .5F))
                        .export(quadMap.get(null));
            }
        }
    }
}
