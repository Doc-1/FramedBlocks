package xfacthd.framedblocks.client.model.baked;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import xfacthd.framedblocks.api.model.item.ItemModelInfo;
import xfacthd.framedblocks.api.model.util.ModelUtils;

import java.util.List;
import java.util.function.Supplier;

public final class CopyingFramedBlockModel extends AbstractFramedBlockModel
{
    private final Supplier<BlockStateModel> srcModel;

    public CopyingFramedBlockModel(BlockStateModel baseModel, BlockState srcState)
    {
        super(baseModel, srcState, ItemModelInfo.DEFAULT);
        this.srcModel = ModelUtils.getModelDeferred(srcState);
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts)
    {
        srcModel.get().collectParts(level, pos, state, random, parts);
    }

    @Override
    public TextureAtlasSprite particleIcon(BlockAndTintGetter level, BlockPos pos, BlockState state)
    {
        return srcModel.get().particleIcon(level, pos, state);
    }
}
