package xfacthd.framedblocks.client.model.interactive;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.model.cache.QuadCacheKey;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.geometry.OverlayPartGenerator;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.config.ClientConfig;

public class FramedLargeStoneButtonGeometry extends FramedLargeButtonGeometry
{
    private static final ResourceLocation FRAME_LOCATION_FRONT = Utils.rl("block/large_stone_button_frame_front");
    private static final ResourceLocation FRAME_LOCATION_SIDE = Utils.rl("block/large_stone_button_frame_side");

    private final BlockState state;
    private final TextureAtlasSprite frameSpriteFront;
    private final TextureAtlasSprite frameSpriteSide;
    private final Direction[] overlayCullFaces;
    private final OverlayPartGenerator.SpriteGetter overlaySpriteGetter;

    private FramedLargeStoneButtonGeometry(GeometryFactory.Context ctx)
    {
        super(ctx);
        this.state = ctx.state();
        this.frameSpriteFront = ctx.textureLookup().get(FRAME_LOCATION_FRONT);
        this.frameSpriteSide = ctx.textureLookup().get(FRAME_LOCATION_SIDE);
        this.overlayCullFaces = new Direction[] { facing.getOpposite(), null };
        this.overlaySpriteGetter = dir -> dir.getAxis() == facing.getAxis() ? frameSpriteFront : frameSpriteSide;
    }

    @Override
    public void generateOverlayParts(OverlayPartGenerator generator, RandomSource rand, ModelData data, QuadCacheKey cacheKey)
    {
        AbstractFramedBlockData fbData = data.get(AbstractFramedBlockData.PROPERTY);
        if (fbData != null && !fbData.unwrap(state).getCamoContent().isEmpty())
        {
            generator.generate(overlayCullFaces, overlaySpriteGetter, frameSpriteFront, RenderType.cutout(), Blocks.STONE.defaultBlockState());
        }
    }



    public static FramedLargeButtonGeometry create(GeometryFactory.Context ctx)
    {
        if (ClientConfig.VIEW.showButtonPlateOverlay())
        {
            return new FramedLargeStoneButtonGeometry(ctx);
        }
        return new FramedLargeButtonGeometry(ctx);
    }
}
