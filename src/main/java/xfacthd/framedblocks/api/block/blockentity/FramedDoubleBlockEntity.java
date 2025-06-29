package xfacthd.framedblocks.api.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.ARGB;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedDoubleBlock;
import xfacthd.framedblocks.api.block.cache.DoubleBlockStateCache;
import xfacthd.framedblocks.api.block.doubleblock.DoubleBlockParts;
import xfacthd.framedblocks.api.blueprint.BlueprintData;
import xfacthd.framedblocks.api.camo.CamoContainer;
import xfacthd.framedblocks.api.camo.CamoContainerHelper;
import xfacthd.framedblocks.api.camo.CamoList;
import xfacthd.framedblocks.api.camo.empty.EmptyCamoContainer;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedDoubleBlockData;
import xfacthd.framedblocks.api.util.ColorUtils;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.api.util.ValueMerger;
import xfacthd.framedblocks.api.util.registration.DeferredBlockEntity;

import java.util.List;

public class FramedDoubleBlockEntity extends FramedBlockEntity implements IFramedDoubleBlockEntity
{
    private static final DeferredBlockEntity<FramedDoubleBlockEntity> DEFAULT_TYPE = DeferredBlockEntity.createBlockEntity(
            Utils.rl("framed_double_tile")
    );
    private static final ValueMerger<MapColor> MAP_COLOR_MERGER = new ValueMerger<>(ColorUtils::average);
    private static final ValueMerger<Integer> BEACON_MULT_MERGER = new ValueMerger<>(ARGB::average);
    private static final ValueMerger<Integer> FLAMMABILITY_MERGER = new ValueMerger<>(i -> i == -1, Math::min);

    private final boolean[] culledFaces = new boolean[6];
    private CamoContainer<?, ?> camoContainer = EmptyCamoContainer.EMPTY;

    @ApiStatus.Internal
    public FramedDoubleBlockEntity(BlockPos pos, BlockState state)
    {
        this(DEFAULT_TYPE.value(), pos, state);
    }

    protected FramedDoubleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    @Override
    public void setCamoInternal(CamoContainer<?, ?> camo, boolean secondary)
    {
        if (secondary)
        {
            this.camoContainer = camo;
        }
        else
        {
            super.setCamoInternal(camo, false);
        }
    }

    @Override
    public CamoContainer<?, ?> getCamo(BlockState state)
    {
        DoubleBlockParts parts = getStateCache().getParts();
        if (state == parts.stateOne())
        {
            return getCamo();
        }
        if (state == parts.stateTwo())
        {
            return getCamoTwo();
        }
        return EmptyCamoContainer.EMPTY;
    }

    @Override
    protected CamoContainer<?, ?> getCamo(boolean secondary)
    {
        return secondary ? camoContainer : getCamo();
    }

    @Override
    public final CamoContainer<?, ?> getCamoTwo()
    {
        return camoContainer;
    }

    @Override
    protected int getLightValue()
    {
        return Math.max(camoContainer.getContent().getLightEmission(), super.getLightValue());
    }

    @Override
    protected boolean isValidRemovalToolForAnyCamo(ItemStack stack)
    {
        return super.isValidRemovalToolForAnyCamo(stack) || CamoContainerHelper.isValidRemovalTool(camoContainer, stack);
    }

    @Override
    public IFramedDoubleBlock getBlock()
    {
        return (IFramedDoubleBlock) super.getBlock();
    }

    @Override
    public DoubleBlockStateCache getStateCache()
    {
        return (DoubleBlockStateCache) super.getStateCache();
    }

    @Override
    public boolean canAutoApplyCamoOnPlacement()
    {
        return false;
    }

    @Override
    public boolean canTriviallyDropAllCamos()
    {
        return super.canTriviallyDropAllCamos() && camoContainer.canTriviallyConvertToItemStack();
    }

    @Override
    protected void addCamoDrops(List<ItemStack> drops)
    {
        super.addCamoDrops(drops);
        dropCamo(drops, camoContainer);
    }

    @Override
    @Nullable
    public MapColor getMapColor()
    {
        return switch (getStateCache().getTopInteractionMode())
        {
            case FIRST -> super.getMapColor();
            case SECOND -> camoContainer.getMapColor(level(), worldPosition);
            case EITHER -> MAP_COLOR_MERGER.apply(super.getMapColor(), camoContainer.getMapColor(level(), worldPosition));
        };
    }

    @Override
    @Nullable
    public Integer getCamoBeaconColorMultiplier(LevelReader level, BlockPos pos, BlockPos beaconPos)
    {
        Integer superMult = super.getCamoBeaconColorMultiplier(level, pos, beaconPos);
        Integer localMult = camoContainer.getBeaconColorMultiplier(level, pos, beaconPos);
        return BEACON_MULT_MERGER.apply(superMult, localMult);
    }

    @Override
    public boolean shouldCamoDisplayFluidOverlay(BlockAndTintGetter level, BlockPos pos, FluidState fluid)
    {
        if (camoContainer.getContent().shouldDisplayFluidOverlay(level, pos, fluid))
        {
            return true;
        }
        return super.shouldCamoDisplayFluidOverlay(level, pos, fluid);
    }

    @Override
    public float getCamoFriction(BlockState state, @Nullable Entity entity, float frameFriction)
    {
        return switch (getStateCache().getTopInteractionMode())
        {
            case FIRST -> getCamo().getContent().getFriction(level(), worldPosition, entity, frameFriction);
            case SECOND -> getCamoTwo().getContent().getFriction(level(), worldPosition, entity, frameFriction);
            case EITHER -> Math.max(
                    getCamo().getContent().getFriction(level(), worldPosition, entity, frameFriction),
                    getCamoTwo().getContent().getFriction(level(), worldPosition, entity, frameFriction)
            );
        };
    }

    @Override
    public TriState canCamoSustainPlant(BlockGetter level, Direction side, BlockState plant)
    {
        return getStateCache().getSolidityCheck(side).canSustainPlant(this, level, side, plant);
    }

    @Override
    public boolean canEntityDestroyCamo(Entity entity)
    {
        return super.canEntityDestroyCamo(entity) && camoContainer.getContent().canEntityDestroy(level(), worldPosition, entity);
    }

    @Override
    protected boolean isCamoSolid()
    {
        return super.isCamoSolid() && camoContainer.getContent().isSolid();
    }

    @Override
    protected boolean doesCamoPropagateSkylightDown()
    {
        return camoContainer.getContent().propagatesSkylightDown() && super.doesCamoPropagateSkylightDown();
    }

    @Override
    public float getCamoExplosionResistance(Explosion explosion)
    {
        return Math.max(
                super.getCamoExplosionResistance(explosion),
                camoContainer.getContent().getExplosionResistance(level(), worldPosition, explosion)
        );
    }

    @Override
    public boolean isCamoFlammable(Direction face)
    {
        CamoContainer<?, ?> camo = getCamo(face);
        if (camo.isEmpty() && (!getCamo().isEmpty() || !camoContainer.isEmpty()))
        {
            return (getCamo().isEmpty() || getCamo().getContent().isFlammable(level(), worldPosition, face)) &&
                   (camoContainer.isEmpty() || camoContainer.getContent().isFlammable(level(), worldPosition, face));
        }
        else if (!camo.isEmpty())
        {
            return camo.getContent().isFlammable(level(), worldPosition, face);
        }
        return true;
    }

    @Override
    public int getCamoFlammability(Direction face)
    {
        int flammabilityOne = super.getCamoFlammability(face);
        int flammabilityTwo = camoContainer.getContent().getFlammability(level(), worldPosition, face);
        return FLAMMABILITY_MERGER.apply(flammabilityOne, flammabilityTwo);
    }

    @Override
    public int getCamoFireSpreadSpeed(Direction face)
    {
        int spreadSpeedOne = super.getCamoFireSpreadSpeed(face);
        int spreadSpeedTwo = camoContainer.getContent().getFireSpreadSpeed(level(), worldPosition, face);
        return FLAMMABILITY_MERGER.apply(spreadSpeedOne, spreadSpeedTwo);
    }

    @Override
    protected boolean hitSecondary(BlockHitResult hit, Vec3 lookVec, Vec3 eyePos)
    {
        lookVec = lookVec.normalize().multiply(1D/16D, 1D/16D, 1D/16D);
        Vec3 vecStart = hit.getLocation().subtract(lookVec);
        Vec3 vecEnd = hit.getLocation().add(lookVec);
        DoubleBlockParts parts = getParts();

        VoxelShape shapeSec = parts.stateTwo().getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
        BlockHitResult clipSec = shapeSec.clip(vecStart, vecEnd, worldPosition);
        if (clipSec == null)
        {
            return false;
        }

        VoxelShape shapePri = parts.stateOne().getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
        BlockHitResult clipPri = shapePri.clip(vecStart, vecEnd, worldPosition);
        if (clipPri == null)
        {
            return true;
        }

        return eyePos.distanceToSqr(clipSec.getLocation()) < eyePos.distanceToSqr(clipPri.getLocation());
    }

    @Override
    public final CamoContainer<?, ?> getCamo(Direction side)
    {
        return getCamo(side, null);
    }

    @Override
    public final CamoContainer<?, ?> getCamo(Direction side, @Nullable Direction edge)
    {
        return getStateCache().getCamoGetter(side, edge).getCamo(this);
    }

    @Override
    protected boolean updateCulling(Direction side, BlockState state, boolean rerender)
    {
        DoubleBlockParts parts = getStateCache().getParts();
        boolean changed = super.updateCulling(side, parts.stateOne(), rerender);
        changed |= updateCulling(culledFaces, parts.stateTwo(), side, rerender);
        return changed;
    }

    @Override
    public void setBlockState(BlockState state)
    {
        DoubleBlockStateCache prevCache = getStateCache();
        super.setBlockState(state);
        if (level != null && level.isClientSide() && !getStateCache().getParts().equals(prevCache.getParts()))
        {
            requestModelDataUpdate();
        }
    }

    /*
     * Debug rendering
     */

    public final DoubleBlockParts getParts()
    {
        return getStateCache().getParts();
    }

    public final boolean debugHitSecondary(BlockHitResult hit, Player player)
    {
        return hitSecondary(hit, player);
    }

    /*
     * Sync
     */

    @Override
    protected void writeToDataPacket(ValueOutput valueOutput)
    {
        super.writeToDataPacket(valueOutput);
        CamoContainerHelper.writeToNetwork(valueOutput.child(CAMO_TWO_NBT_KEY), camoContainer);
    }

    @Override
    protected boolean readFromDataPacket(ValueInput valueInput)
    {
        boolean needUpdate = false;
        CamoContainer<?, ?> newCamo = CamoContainerHelper.readFromNetwork(valueInput.child(CAMO_TWO_NBT_KEY));
        if (!newCamo.equals(camoContainer))
        {
            int oldLight = getLightValue();
            camoContainer = newCamo;
            if (oldLight != getLightValue())
            {
                doLightUpdate();
            }

            needUpdate = true;
            updateCulling(true, false);
        }

        return super.readFromDataPacket(valueInput) || needUpdate;
    }

    @Override
    protected void writeUpdateTag(ValueOutput valueOutput)
    {
        super.writeUpdateTag(valueOutput);
        CamoContainerHelper.writeToNetwork(valueOutput.child(CAMO_TWO_NBT_KEY), camoContainer);
    }

    @Override
    protected boolean readCamoFromUpdateTag(ValueInput valueInput)
    {
        boolean changed = super.readCamoFromUpdateTag(valueInput);
        CamoContainer<?, ?> newCamo = CamoContainerHelper.readFromNetwork(valueInput.child(CAMO_TWO_NBT_KEY));
        if (!newCamo.equals(camoContainer))
        {
            camoContainer = newCamo;
            changed = true;
        }
        return changed;
    }

    /*
     * Model data
     */

    @Override
    protected final AbstractFramedBlockData computeBlockData(boolean includeCullInfo)
    {
        FramedBlockData modelDataOne = (FramedBlockData) super.computeBlockData(includeCullInfo);
        boolean[] cullData = includeCullInfo ? culledFaces : FramedBlockData.NO_CULLED_FACES;
        FramedBlockData modelDataTwo = new FramedBlockData(camoContainer, cullData, true, isReinforced(), isEmissive(), modelDataOne.isViewBlocking());
        return new FramedDoubleBlockData(getParts(), modelDataOne, modelDataTwo);
    }

    /*
     * Blueprint handling
     */

    @Override
    protected CamoList collectCamosForBlueprint()
    {
        return CamoList.of(getCamo(), camoContainer);
    }

    @Override
    protected void applyCamosFromBlueprint(BlueprintData blueprintData)
    {
        super.applyCamosFromBlueprint(blueprintData);
        setCamo(blueprintData.camos().getCamo(1), true);
    }

    /*
     * DataComponent handling
     */

    @Override
    public void removeComponentsFromTag(ValueOutput valueOutput)
    {
        super.removeComponentsFromTag(valueOutput);
        valueOutput.discard(CAMO_TWO_NBT_KEY);
    }

    @Override
    protected void collectCamoComponents(DataComponentMap.Builder builder)
    {
        builder.set(Utils.DC_TYPE_CAMO_LIST, CamoList.of(getCamo(), camoContainer));
    }

    @Override
    protected void applyCamoComponents(DataComponentGetter input)
    {
        super.applyCamoComponents(input);
        setCamo(input.getOrDefault(Utils.DC_TYPE_CAMO_LIST, CamoList.EMPTY).getCamo(1), true);
    }

    /*
     * NBT stuff
     */

    @Override
    public void saveAdditional(ValueOutput valueOutput)
    {
        valueOutput.store(CAMO_TWO_NBT_KEY, CamoContainerHelper.CODEC, camoContainer);

        super.saveAdditional(valueOutput);
    }

    @Override
    public void loadAdditional(ValueInput valueInput)
    {
        super.loadAdditional(valueInput);
        camoContainer = loadAndValidateCamo(valueInput, CAMO_TWO_NBT_KEY);
    }
}
