package xfacthd.framedblocks.common.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.BlockUtils;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.blockentity.special.PoweredFramingSawBlockEntity;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.menu.PoweredFramingSawMenu;

public class PoweredFramingSawBlock extends FramingSawBlock implements EntityBlock
{
    public static final Component POWERED_SAW_MENU_TITLE = Utils.translate("title", "powered_framing_saw");

    public PoweredFramingSawBlock(Properties props)
    {
        super(props);
        registerDefaultState(defaultBlockState().setValue(PropertyHolder.ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FramedProperties.FACING_HOR, PropertyHolder.ACTIVE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state)
    {
        return FramingSawBlock.SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new PoweredFramingSawBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (!level.isClientSide())
        {
            return BlockUtils.createBlockEntityTicker(
                    type, FBContent.BE_TYPE_POWERED_FRAMING_SAW.value(), PoweredFramingSawBlockEntity::tick
            );
        }
        return null;
    }

    @Override
    protected Component getSawMenuTitle()
    {
        return POWERED_SAW_MENU_TITLE;
    }

    @Override
    protected AbstractContainerMenu createSawMenu(int containerId, Inventory inventory, Level level, BlockPos pos)
    {
        return new PoweredFramingSawMenu(containerId, inventory, level, pos);
    }
}
