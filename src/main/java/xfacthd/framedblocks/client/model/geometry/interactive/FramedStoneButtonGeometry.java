package xfacthd.framedblocks.client.model.geometry.interactive;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.model.cache.QuadCacheKey;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.geometry.OverlayPartGenerator;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.config.ClientConfig;

public class FramedStoneButtonGeometry extends FramedButtonGeometry
{
    private static final ResourceLocation FRAME_LOCATION_FRONT = Utils.rl("block/stone_button_frame_front");
    private static final ResourceLocation FRAME_LOCATION_NARROW = Utils.rl("block/stone_button_frame_narrow");
    private static final ResourceLocation FRAME_LOCATION_WIDE = Utils.rl("block/stone_button_frame_wide");

    private final BlockState state;
    private final TextureAtlasSprite frameSpriteFront;
    private final TextureAtlasSprite frameSpriteNarrow;
    private final TextureAtlasSprite frameSpriteWide;
    private final Direction[] overlayCullFaces;
    private final OverlayPartGenerator.SpriteGetter overlaySpriteGetter;

    private FramedStoneButtonGeometry(GeometryFactory.Context ctx)
    {
        super(ctx);
        this.state = ctx.state();
        this.frameSpriteFront = ctx.textureLookup().get(FRAME_LOCATION_FRONT);
        this.frameSpriteNarrow = ctx.textureLookup().get(FRAME_LOCATION_NARROW);
        this.frameSpriteWide = ctx.textureLookup().get(FRAME_LOCATION_WIDE);
        this.overlayCullFaces = new Direction[] { facing.getOpposite(), null };
        Direction.Axis wideAxis = face == AttachFace.WALL ? Direction.Axis.Y : dir.getAxis();
        this.overlaySpriteGetter = dir ->
        {
            if (dir.getAxis() == facing.getAxis()) return frameSpriteFront;
            if (dir.getAxis() == wideAxis) return frameSpriteWide;
            return frameSpriteNarrow;
        };
    }

    @Override
    public void generateOverlayParts(OverlayPartGenerator generator, RandomSource rand, ModelData data, QuadCacheKey cacheKey)
    {
        AbstractFramedBlockData fbData = data.get(AbstractFramedBlockData.PROPERTY);
        if (fbData != null && !fbData.unwrap(state).getCamoContent().isEmpty())
        {
            generator.generate(overlayCullFaces, overlaySpriteGetter, frameSpriteFront, ChunkSectionLayer.CUTOUT, Blocks.STONE.defaultBlockState());
        }
    }

    @Override
    public boolean useBaseModel()
    {
        return true;
    }



    public static FramedButtonGeometry create(GeometryFactory.Context ctx)
    {
        if (ClientConfig.VIEW.showButtonPlateOverlay())
        {
            return new FramedStoneButtonGeometry(ctx);
        }
        return new FramedButtonGeometry(ctx);
    }
}
