package xfacthd.framedblocks.common.item.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.BlockUtils;
import xfacthd.framedblocks.api.block.item.FramedBlockItem;
import xfacthd.framedblocks.api.camo.CamoContainer;
import xfacthd.framedblocks.api.camo.empty.EmptyCamoContainer;
import xfacthd.framedblocks.api.camo.CamoList;
import xfacthd.framedblocks.api.util.SoundUtils;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.blockentity.doubled.FramedDoubleBlockEntity;

public abstract class FramedSpecialBlockItem extends FramedBlockItem
{
    private final boolean doubleBlock;

    public FramedSpecialBlockItem(Block block, boolean doubleBlock, Properties props)
    {
        super(block, props);
        this.doubleBlock = doubleBlock;
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx)
    {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState originalState = level.getBlockState(pos);
        if (ctx.canPlace() && originalState.is(getBlock()) && ctx.getPlayer() != null)
        {
            BlockState newState = getReplacementState(ctx, originalState);
            if (newState != null)
            {
                if (!level.isClientSide())
                {
                    boolean writeToCamoTwo = shouldWriteToCamoTwo(ctx, originalState);
                    CamoList camos = ctx.getItemInHand().getOrDefault(FBContent.DC_TYPE_CAMO_LIST, CamoList.EMPTY);
                    BlockUtils.wrapInStateCopy(
                            level,
                            pos,
                            ctx.getPlayer(),
                            ctx.getItemInHand(),
                            writeToCamoTwo,
                            true,
                            () -> level.setBlockAndUpdate(pos, newState)
                    );

                    CamoContainer<?, ?> camo = EmptyCamoContainer.EMPTY;
                    if (doubleBlock && !camos.isEmpty() && level.getBlockEntity(pos) instanceof FramedDoubleBlockEntity be)
                    {
                        camo = camos.getCamo(0);
                        be.setCamo(camo, !writeToCamoTwo);
                    }
                    SoundUtils.playPlaceSound(ctx, camo.getContent().getSoundType(), true);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.place(ctx);
    }

    @Nullable
    protected abstract BlockState getReplacementState(BlockPlaceContext ctx, BlockState originalState);

    protected abstract boolean shouldWriteToCamoTwo(BlockPlaceContext ctx, BlockState originalState);



    public static abstract class Single extends FramedSpecialBlockItem
    {
        public Single(Block block, Properties props)
        {
            super(block, false, props);
        }

        @Override
        protected boolean shouldWriteToCamoTwo(BlockPlaceContext ctx, BlockState originalState)
        {
            return false;
        }
    }
}
