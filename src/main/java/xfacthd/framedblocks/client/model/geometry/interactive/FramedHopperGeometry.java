package xfacthd.framedblocks.client.model.geometry.interactive;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HopperBlock;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.util.Utils;

public class FramedHopperGeometry extends Geometry
{
    private final Direction facing;

    public FramedHopperGeometry(GeometryFactory.Context ctx)
    {
        this.facing = ctx.state().getValue(HopperBlock.FACING);
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad)
    {
        Direction quadDir = quad.direction();

        if (Utils.isY(quadDir) && facing != Direction.DOWN)
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(facing.getOpposite(), 4F/16F))
                    .apply(Modifiers.cut(facing.getClockWise().getAxis(), 10F/16F))
                    .apply(Modifiers.setPosition(quadDir == Direction.UP ? 8F/16F : 12F/16F))
                    .export(quadMap.get(null));
        }
        if (quadDir == Direction.UP)
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(Direction.NORTH, 2F/16F))
                    .export(quadMap.get(quadDir));
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(Direction.SOUTH, 2F/16F))
                    .export(quadMap.get(quadDir));
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(Direction.Axis.Z, 14F/16F))
                    .apply(Modifiers.cut(Direction.EAST, 2F/16F))
                    .export(quadMap.get(quadDir));
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(Direction.Axis.Z, 14F/16F))
                    .apply(Modifiers.cut(Direction.WEST, 2F/16F))
                    .export(quadMap.get(quadDir));

            QuadModifier.of(quad)
                    .apply(Modifiers.cutTopBottom(2F/16F, 2F/16F, 14F/16F, 14F/16F))
                    .apply(Modifiers.setPosition(11F/16F))
                    .export(quadMap.get(null));
        }
        else if (quadDir == Direction.DOWN)
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(Direction.NORTH, 4F/16F))
                    .apply(Modifiers.setPosition(6F/16F))
                    .export(quadMap.get(null));
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(Direction.SOUTH, 4F/16F))
                    .apply(Modifiers.setPosition(6F/16F))
                    .export(quadMap.get(null));
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(Direction.Axis.Z, 12F/16F))
                    .apply(Modifiers.cut(Direction.EAST, 4F/16F))
                    .apply(Modifiers.setPosition(6F/16F))
                    .export(quadMap.get(null));
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(Direction.Axis.Z, 12F/16F))
                    .apply(Modifiers.cut(Direction.WEST, 4F/16F))
                    .apply(Modifiers.setPosition(6F/16F))
                    .export(quadMap.get(null));

            QuadModifier.of(quad)
                    .apply(Modifiers.cutTopBottom(4F/16F, 4F/16F, 12F/16F, 12F/16F))
                    .apply(Modifiers.setPosition(12F/16F))
                    .export(quadMap.get(null));

            if (facing == Direction.DOWN)
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cutTopBottom(6F/16F, 6F/16F, 10F/16F, 10F/16F))
                        .export(quadMap.get(null));
            }
        }
        else
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(Direction.DOWN, 6F/16F))
                    .export(quadMap.get(quadDir));
            QuadModifier.of(quad)
                    .apply(Modifiers.cutSide(4F/16F, 4F/16F, 12F/16F, 10F/16F))
                    .apply(Modifiers.setPosition(12F/16F))
                    .export(quadMap.get(null));
            QuadModifier.of(quad)
                    .apply(Modifiers.cut(Direction.DOWN, 5F/16F))
                    .apply(Modifiers.cut(quadDir.getClockWise().getAxis(), 14F/16F))
                    .apply(Modifiers.setPosition(2F/16F))
                    .export(quadMap.get(null));

            if (facing == Direction.DOWN)
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSide(6F/16F, 0F, 10F/16F, 4F/16F))
                        .apply(Modifiers.setPosition(10F/16F))
                        .export(quadMap.get(null));
            }
            else if (quadDir == facing)
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSide(6F/16F, 4F/16F, 10F/16F, 8F/16F))
                        .export(quadMap.get(quadDir));
            }
            else if (quadDir.getAxis() == facing.getClockWise().getAxis())
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cut(facing.getOpposite(), 4F/16F))
                        .apply(Modifiers.cut(Direction.DOWN, 12F/16F))
                        .apply(Modifiers.cut(Direction.UP, 8F/16F))
                        .apply(Modifiers.setPosition(10F/16F))
                        .export(quadMap.get(null));
            }
        }
    }

    @Override
    public boolean useSolidNoCamoModel()
    {
        return true;
    }
}
