package xfacthd.framedblocks.client.model.geometry.cube;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.geometry.PartConsumer;
import xfacthd.framedblocks.api.model.util.StandaloneModels;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.config.ClientConfig;

public class FramedMarkedCubeGeometry extends FramedCubeGeometry
{
    public static final ResourceLocation SLIME_FRAME_LOCATION = Utils.rl("block/slime_frame");
    public static final ResourceLocation REDSTONE_FRAME_LOCATION = Utils.rl("block/redstone_frame");
    public static final StandaloneModelKey<BlockModelPart> SLIME_FRAME_KEY = new StandaloneModelKey<>(SLIME_FRAME_LOCATION);
    public static final StandaloneModelKey<BlockModelPart> REDSTONE_FRAME_KEY = new StandaloneModelKey<>(REDSTONE_FRAME_LOCATION);

    private final BlockState state;
    private final BlockModelPart frameModel;
    private final BlockState frameShaderState;

    private FramedMarkedCubeGeometry(GeometryFactory.Context ctx, StandaloneModelKey<BlockModelPart> frameKey, BlockState frameShaderState)
    {
        super(ctx);
        this.state = ctx.state();
        this.frameModel = StandaloneModels.getBlockModelPart(ctx.modelLookup(), ctx.textureLookup(), frameKey);
        this.frameShaderState = frameShaderState;
    }

    @Override
    public void collectAdditionalPartsUncached(PartConsumer consumer, BlockAndTintGetter level, BlockPos pos, RandomSource random, ModelData data)
    {
        AbstractFramedBlockData fbData = data.get(AbstractFramedBlockData.PROPERTY);
        if (fbData != null && !fbData.unwrap(false).getCamoContent().isEmpty())
        {
            consumer.accept(frameModel, state, false, false, true, false, frameShaderState, null);
        }
    }



    public static FramedCubeGeometry slime(GeometryFactory.Context ctx)
    {
        if (ClientConfig.VIEW.showSpecialCubeOverlay())
        {
            return new FramedMarkedCubeGeometry(ctx, SLIME_FRAME_KEY, Blocks.SLIME_BLOCK.defaultBlockState());
        }
        return new FramedCubeGeometry(ctx);
    }

    public static FramedCubeGeometry redstone(GeometryFactory.Context ctx)
    {
        if (ClientConfig.VIEW.showSpecialCubeOverlay())
        {
            return new FramedMarkedCubeGeometry(ctx, REDSTONE_FRAME_KEY, Blocks.REDSTONE_BLOCK.defaultBlockState());
        }
        return new FramedCubeGeometry(ctx);
    }
}
