package xfacthd.framedblocks.client.model.item.modelprovider;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.item.block.BlockItemModelProvider;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.util.Utils;

import java.util.function.Supplier;

public final class FenceBlockItemModelProvider implements BlockItemModelProvider
{
    public static final FenceBlockItemModelProvider INSTANCE = new FenceBlockItemModelProvider();

    private FenceBlockItemModelProvider() { }

    @Override
    public Supplier<BlockStateModel> create(BlockState state, ModelBaker baker)
    {
        return BlockItemModelProvider.forGeometry(state, state, FenceItemGeometry::new);
    }

    private static final class FenceItemGeometry extends Geometry
    {
        private FenceItemGeometry(GeometryFactory.Context ctx) { }

        @Override
        public void transformQuad(QuadMap quadMap, BakedQuad quad)
        {
            Direction quadDir = quad.direction();
            if (Utils.isY(quadDir))
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cutTopBottom(6F/16F, 0F, 10F/16F, 4F/16F))
                        .export(quadMap.get(quadDir));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutTopBottom(6F/16F, 12F/16F, 10F/16F, 1F))
                        .export(quadMap.get(quadDir));

                boolean up = quadDir == Direction.UP;
                float posOne = up ? 15F/16F : 4F/16F;
                float posTwo = up ? 9F/16F : 10F/16F;

                QuadModifier.of(quad)
                        .apply(Modifiers.cutTopBottom(7F/16F, 4F/16F, 9F/16F, 12F/16F))
                        .apply(Modifiers.setPosition(posOne))
                        .export(quadMap.get(null));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutTopBottom(7F/16F, 4F/16F, 9F/16F, 12F/16F))
                        .apply(Modifiers.setPosition(posTwo))
                        .export(quadMap.get(null));

                QuadModifier.of(quad)
                        .apply(Modifiers.cutTopBottom(7F/16F, 0F, 9F/16F, 2F/16F))
                        .apply(Modifiers.setPosition(posOne))
                        .apply(Modifiers.offset(Direction.SOUTH, 1F))
                        .export(quadMap.get(null));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutTopBottom(7F/16F, 0F, 9F/16F, 2F/16F))
                        .apply(Modifiers.setPosition(posTwo))
                        .apply(Modifiers.offset(Direction.SOUTH, 1F))
                        .export(quadMap.get(null));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutTopBottom(7F/16F, 14F/16F, 9F/16F, 1F))
                        .apply(Modifiers.setPosition(posOne))
                        .apply(Modifiers.offset(Direction.NORTH, 1F))
                        .export(quadMap.get(null));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutTopBottom(7F/16F, 14F/16F, 9F/16F, 1F))
                        .apply(Modifiers.setPosition(posTwo))
                        .apply(Modifiers.offset(Direction.NORTH, 1F))
                        .export(quadMap.get(null));
            }
            else if (Utils.isX(quadDir))
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideLeftRight(Direction.SOUTH, 4F/16F))
                        .apply(Modifiers.setPosition(10F/16F))
                        .export(quadMap.get(null));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideLeftRight(Direction.NORTH, 4F/16F))
                        .apply(Modifiers.setPosition(10F/16F))
                        .export(quadMap.get(null));

                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideLeftRight(12F/16F))
                        .apply(Modifiers.cutSideUpDown(false, 15F/16F))
                        .apply(Modifiers.cutSideUpDown(true, 4F/16F))
                        .apply(Modifiers.setPosition(9F/16F))
                        .export(quadMap.get(null));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideLeftRight(12F/16F))
                        .apply(Modifiers.cutSideUpDown(false, 9F/16F))
                        .apply(Modifiers.cutSideUpDown(true, 10F/16F))
                        .apply(Modifiers.setPosition(9F/16F))
                        .export(quadMap.get(null));

                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideLeftRight(Direction.SOUTH, 2F/16F))
                        .apply(Modifiers.cutSideUpDown(false, 15F/16F))
                        .apply(Modifiers.cutSideUpDown(true, 4F/16F))
                        .apply(Modifiers.setPosition(9F/16F))
                        .apply(Modifiers.offset(Direction.SOUTH, 1F))
                        .export(quadMap.get(null));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideLeftRight(Direction.SOUTH, 2F/16F))
                        .apply(Modifiers.cutSideUpDown(false, 9F/16F))
                        .apply(Modifiers.cutSideUpDown(true, 10F/16F))
                        .apply(Modifiers.setPosition(9F/16F))
                        .apply(Modifiers.offset(Direction.SOUTH, 1F))
                        .export(quadMap.get(null));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideLeftRight(Direction.NORTH, 2F/16F))
                        .apply(Modifiers.cutSideUpDown(false, 15F/16F))
                        .apply(Modifiers.cutSideUpDown(true, 4F/16F))
                        .apply(Modifiers.setPosition(9F/16F))
                        .apply(Modifiers.offset(Direction.NORTH, 1F))
                        .export(quadMap.get(null));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideLeftRight(Direction.NORTH, 2F/16F))
                        .apply(Modifiers.cutSideUpDown(false, 9F/16F))
                        .apply(Modifiers.cutSideUpDown(true, 10F/16F))
                        .apply(Modifiers.setPosition(9F/16F))
                        .apply(Modifiers.offset(Direction.NORTH, 1F))
                        .export(quadMap.get(null));
            }
            else
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideLeftRight(10F/16F))
                        .export(quadMap.get(quadDir));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideLeftRight(10F/16F))
                        .apply(Modifiers.setPosition(4F/16F))
                        .export(quadMap.get(null));

                QuadModifier.of(quad)
                        .apply(Modifiers.cutSide(7F/16F, 12F/16F, 9F/16F, 15F/16F))
                        .apply(Modifiers.setPosition(18F/16F))
                        .export(quadMap.get(null));
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSide(7F/16F, 6F/16F, 9F/16F, 9F/16F))
                        .apply(Modifiers.setPosition(18F/16F))
                        .export(quadMap.get(null));
            }
        }

        @Override
        public boolean useSolidNoCamoModel()
        {
            return true;
        }
    }
}
