package xfacthd.framedblocks.api.model;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.EmptyBlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.model.item.ItemModelInfo;

import java.util.List;

public abstract class AbstractFramedBlockModel extends DelegateBlockStateModel
{
    private final BlockState state;
    @Nullable
    private final ItemModelInfo itemModelInfo;

    protected AbstractFramedBlockModel(BlockStateModel baseModel, BlockState state, ItemModelInfo itemModelInfo)
    {
        super(baseModel);
        this.state = state;
        boolean isItemModel = state.getBlock() instanceof IFramedBlock block && block.getItemModelSource() == state;
        this.itemModelInfo = isItemModel ? itemModelInfo : null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void collectParts(RandomSource random, List<BlockModelPart> parts)
    {
        collectParts(EmptyBlockAndTintGetter.INSTANCE, BlockPos.ZERO, state, random, parts);
    }

    @Override
    @SuppressWarnings("deprecation")
    public TextureAtlasSprite particleIcon()
    {
        return particleIcon(EmptyBlockAndTintGetter.INSTANCE, BlockPos.ZERO, state);
    }

    public void clearCache() { }

    @Nullable
    public ItemModelInfo getItemModelInfo()
    {
        return itemModelInfo;
    }
}
