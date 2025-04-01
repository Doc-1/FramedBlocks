package xfacthd.framedblocks.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.block.cache.StateCache;
import xfacthd.framedblocks.api.block.render.ParticleHelper;
import xfacthd.framedblocks.api.blueprint.BlueprintData;
import xfacthd.framedblocks.api.camo.CamoContainer;
import xfacthd.framedblocks.api.predicate.cull.SideSkipPredicate;
import xfacthd.framedblocks.api.camo.CamoList;
import xfacthd.framedblocks.common.blockentity.doubled.FramedDoubleBlockEntity;
import xfacthd.framedblocks.common.data.doubleblock.CamoGetter;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockParts;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockStateCache;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockTopInteractionMode;
import xfacthd.framedblocks.common.data.doubleblock.SolidityCheck;

import java.util.Optional;

public interface IFramedDoubleBlock extends xfacthd.framedblocks.api.block.IFramedDoubleBlock
{
    @Override
    @SuppressWarnings("deprecation")
    default SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity)
    {
        if (level.getBlockEntity(pos) instanceof FramedDoubleBlockEntity be)
        {
            return be.getSoundType();
        }
        return state.getSoundType();
    }

    @Override
    BlockEntity newBlockEntity(BlockPos pos, BlockState state);

    @ApiStatus.OverrideOnly
    DoubleBlockTopInteractionMode calculateTopInteractionMode(BlockState state);

    @ApiStatus.OverrideOnly
    DoubleBlockParts calculateParts(BlockState state);

    @ApiStatus.OverrideOnly
    SolidityCheck calculateSolidityCheck(BlockState state, Direction side);

    @ApiStatus.OverrideOnly
    CamoGetter calculateCamoGetter(BlockState state, Direction side, @Nullable Direction edge);

    @Override
    default StateCache initCache(BlockState state)
    {
        return new DoubleBlockStateCache(state, getBlockType());
    }

    @Override
    default DoubleBlockStateCache getCache(BlockState state)
    {
        return (DoubleBlockStateCache) state.framedblocks$getCache();
    }

    @Override
    @Nullable
    default BlockState runOcclusionTestAndGetLookupState(
            SideSkipPredicate pred, BlockGetter level, BlockPos pos, BlockState state, BlockState adjState, Direction side
    )
    {
        DoubleBlockParts partStates = getCache(adjState).getParts();
        if (pred.test(level, pos, state, partStates.stateOne(), side))
        {
            return partStates.stateOne();
        }
        if (pred.test(level, pos, state, partStates.stateTwo(), side))
        {
            return partStates.stateTwo();
        }
        return null;
    }

    @Override
    @Nullable
    default BlockState getComponentAtEdge(
            BlockGetter level, BlockPos pos, BlockState state, Direction side, @Nullable Direction edge
    )
    {
        DoubleBlockStateCache cache = getCache(state);
        return cache.getCamoGetter(side, edge).getComponent(cache.getParts());
    }

    @Override
    @Nullable
    default BlockState getComponentBySkipPredicate(
            BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction side
    )
    {
        DoubleBlockParts parts = getCache(state).getParts();
        BlockState compA = parts.stateOne();
        if (testComponent(level, pos, compA, neighborState, side))
        {
            return compA;
        }
        BlockState compB = parts.stateTwo();
        if (testComponent(level, pos, compB, neighborState, side))
        {
            return compB;
        }
        return null;
    }

    static boolean testComponent(BlockGetter ctLevel, BlockPos pos, BlockState component, BlockState neighborState, Direction side)
    {
        IFramedBlock block = (IFramedBlock) component.getBlock();
        return block.getBlockType().getSideSkipPredicate().test(ctLevel, pos, component, neighborState, side);
    }

    @Override
    default boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity)
    {
        if (level.getBlockEntity(pos) instanceof FramedDoubleBlockEntity be)
        {
            DoubleBlockStateCache cache = getCache(state);
            DoubleBlockParts partStates = cache.getParts();
            switch (cache.getTopInteractionMode())
            {
                case FIRST -> ParticleHelper.spawnRunningParticles(be.getCamo(partStates.stateOne()), level, pos, entity);
                case SECOND -> ParticleHelper.spawnRunningParticles(be.getCamo(partStates.stateTwo()), level, pos, entity);
                case EITHER ->
                {
                    ParticleHelper.spawnRunningParticles(be.getCamo(partStates.stateOne()), level, pos, entity);
                    ParticleHelper.spawnRunningParticles(be.getCamo(partStates.stateTwo()), level, pos, entity);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    default boolean addLandingEffects(
            BlockState state, ServerLevel level, BlockPos pos, BlockState sameState, LivingEntity entity, int count
    )
    {
        if (level.getBlockEntity(pos) instanceof FramedDoubleBlockEntity be)
        {
            DoubleBlockStateCache cache = getCache(state);
            DoubleBlockParts partStates = cache.getParts();
            switch (cache.getTopInteractionMode())
            {
                case FIRST -> ParticleHelper.spawnLandingParticles(be.getCamo(partStates.stateOne()), level, pos, entity, count);
                case SECOND -> ParticleHelper.spawnLandingParticles(be.getCamo(partStates.stateTwo()), level, pos, entity, count);
                case EITHER ->
                {
                    ParticleHelper.spawnLandingParticles(be.getCamo(partStates.stateOne()), level, pos, entity, count);
                    ParticleHelper.spawnLandingParticles(be.getCamo(partStates.stateTwo()), level, pos, entity, count);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    default Optional<MutableComponent> printCamoData(CamoList camos, boolean blueprint)
    {
        return printCamoData(camos.getCamo(0), camos.getCamo(1), blueprint);
    }

    static Optional<MutableComponent> printCamoData(CamoContainer<?, ?> camoContainer, CamoContainer<?, ?> camoContainerTwo, boolean force)
    {
        if (force || !camoContainer.isEmpty() || !camoContainerTwo.isEmpty())
        {
            MutableComponent component = getCamoComponent(camoContainer);
            component.append(Component.literal(" | ").withStyle(ChatFormatting.GOLD));
            component.append(getCamoComponent(camoContainerTwo));

            return Optional.of(component);
        }
        return Optional.empty();
    }

    static MutableComponent getCamoComponent(CamoContainer<?, ?> camoContainer)
    {
        if (!camoContainer.isEmpty())
        {
            return camoContainer.getContent().getCamoName().withStyle(ChatFormatting.WHITE);
        }
        return BlueprintData.BLOCK_NONE.copy();
    }
}
