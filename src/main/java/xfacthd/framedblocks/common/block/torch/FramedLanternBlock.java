package xfacthd.framedblocks.common.block.torch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.BlockUtils;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.block.IBlockType;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.block.IFramedBlockInternal;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;

import java.util.List;

public class FramedLanternBlock extends LanternBlock implements IFramedBlockInternal
{
    private final BlockType type;

    public FramedLanternBlock(BlockType type, Properties props)
    {
        super(IFramedBlock.applyDefaultProperties(props, type)
                .mapColor(MapColor.METAL)
                .forceSolidOn()
                .strength(3.5F)
                .sound(SoundType.LANTERN)
                .lightLevel(type == BlockType.FRAMED_SOUL_LANTERN ? (state -> 10) : (state -> 15))
                .pushReaction(PushReaction.DESTROY)
        );
        this.type = type;
        BlockUtils.configureStandardProperties(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        BlockUtils.addRequiredProperties(builder);
        builder.add(PropertyHolder.CHAIN_TYPE);
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
    )
    {
        return handleUse(state, level, pos, player, hand, hit);
    }

    @Override
    public boolean handleBlockLeftClick(BlockState state, Level level, BlockPos pos, Player player)
    {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(Utils.FRAMED_HAMMER.value()))
        {
            if (!level.isClientSide())
            {
                state = state.setValue(PropertyHolder.CHAIN_TYPE, state.getValue(PropertyHolder.CHAIN_TYPE).next());
                level.setBlock(pos, state, Block.UPDATE_ALL);
            }
            return true;
        }
        return false;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        tryApplyCamoImmediately(level, pos, placer, stack);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
    {
        return Math.max(state.getLightEmission(), super.getLightEmission(state, level, pos));
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos)
    {
        return getCamoShadeBrightness(state, level, pos, super.getShadeBrightness(state, level, pos));
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state)
    {
        return state.getValue(FramedProperties.PROPAGATES_SKYLIGHT);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)
    {
        return getCamoDrops(super.getDrops(state, builder), builder);
    }

    @Override
    public IBlockType getBlockType()
    {
        return type;
    }

    @Override
    @Nullable
    public  BlockState getItemModelSource()
    {
        return null;
    }

    @Override
    public BlockState getJadeRenderState(BlockState state)
    {
        return defaultBlockState();
    }
}
