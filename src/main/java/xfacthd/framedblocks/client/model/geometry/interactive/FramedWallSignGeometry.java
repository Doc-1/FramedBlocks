package xfacthd.framedblocks.client.model.geometry.interactive;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.util.Utils;

public class FramedWallSignGeometry extends Geometry
{
    private static final float DEPTH = 2F/16F;
    private final Direction dir;

    public FramedWallSignGeometry(GeometryFactory.Context ctx)
    {
        this.dir = ctx.state().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad, ModelData modelData)
    {
        Direction quadDir = quad.direction();
        if (quadDir.getAxis() == dir.getAxis())
        {
            boolean inset = quadDir == dir;
            QuadModifier.of(quad)
                    .apply(Modifiers.cutSide(0F, 4.5F/16F, 1F, 12.5F/16F))
                    .applyIf(Modifiers.setPosition(DEPTH), inset)
                    .export(quadMap.get(inset ? null : quadDir));
        }
        else if (Utils.isY(quadDir))
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(dir, DEPTH))
                    .apply(Modifiers.setPosition(quadDir == Direction.UP ? 12.5F/16F : 11.5F/16F))
                    .export(quadMap.get(null));
        }
        else
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(dir, DEPTH))
                    .apply(Modifiers.cut(Direction.UP, 12.5F/16F))
                    .apply(Modifiers.cut(Direction.DOWN, 11.5F/16F))
                    .export(quadMap.get(quadDir));
        }
    }
}