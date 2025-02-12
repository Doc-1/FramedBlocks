package xfacthd.framedblocks.api.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import xfacthd.framedblocks.api.type.IBlockType;
import xfacthd.framedblocks.mixin.AccessorStateDefinitionBuilder;
import xfacthd.framedblocks.mixin.InvokerBlock;

import java.util.Set;

public final class BlockUtils
{
    public static final Set<Property<?>> REQUIRED_STATE_PROPERTIES = Set.of(
            FramedProperties.GLOWING,
            FramedProperties.PROPAGATES_SKYLIGHT
    );

    /**
     * Adds the {@link Property}s which are required to be present on all blocks implementing {@link IFramedBlock}
     * to the given {@link StateDefinition.Builder}
     */
    public static void addRequiredProperties(StateDefinition.Builder<Block, BlockState> builder)
    {
        REQUIRED_STATE_PROPERTIES.forEach(builder::add);
    }

    /**
     * Adds the {@link Property}s which are required to be present on all blocks implementing {@link IFramedBlock}
     * and properties that depend on the {@link IBlockType}'s configuration to the given {@link StateDefinition.Builder}
     *
     * @apiNote This method must only be used by blocks which return a constant value from {@link IFramedBlock#getBlockType()}
     */
    public static <T extends Block & IFramedBlock> void addStandardProperties(T block, StateDefinition.Builder<Block, BlockState> builder)
    {
        addRequiredProperties(builder);

        if (block.getBlockType().canOccludeWithSolidCamo())
        {
            builder.add(FramedProperties.SOLID);
        }

        boolean hasWaterlogging = hasProperty(builder, BlockStateProperties.WATERLOGGED);
        boolean needsWaterlogging = block.getBlockType().supportsWaterLogging();
        if (needsWaterlogging && !hasWaterlogging)
        {
            builder.add(BlockStateProperties.WATERLOGGED);
        }
        else if (!needsWaterlogging && hasWaterlogging)
        {
            removeProperty(builder, BlockStateProperties.WATERLOGGED);
        }
    }

    /**
     * Configures the default {@link BlockState} of the given {@link IFramedBlock}
     *
     * @param block The block to configure the default state for
     */
    public static <T extends Block & IFramedBlock> void configureStandardProperties(T block)
    {
        BlockState state = block.defaultBlockState()
                .setValue(FramedProperties.GLOWING, false)
                .setValue(FramedProperties.PROPAGATES_SKYLIGHT, false);
        if (block.getBlockType().canOccludeWithSolidCamo())
        {
            state = state.setValue(FramedProperties.SOLID, false);
        }
        if (block.getBlockType().supportsWaterLogging())
        {
            state = state.setValue(BlockStateProperties.WATERLOGGED, false);
        }
        ((InvokerBlock) block).framedblocks$callRegisterDefaultState(state);
    }

    /**
     * Copies the {@link Property}s which are required to be present on all blocks implementing {@link IFramedBlock}
     * between two {@link BlockState}s of the same {@link Block}
     */
    public static BlockState copyRequiredProperties(BlockState from, BlockState to)
    {
        Preconditions.checkArgument(from.getBlock() == to.getBlock(), "Both states must be from the same block");

        for (Property<?> property : REQUIRED_STATE_PROPERTIES)
        {
            to = Block.copyProperty(from, to, property);
        }
        return to;
    }

    /**
     * Copies all standard {@link Property}s between two {@link BlockState}s of the same {@link IFramedBlock}
     *
     * @param block The block owning the two states
     * @param from The state to copy the properties from
     * @param to The state to apply the properties to
     * @param copyWaterlogging Whether the {@link BlockStateProperties#WATERLOGGED} property should be copied
     */
    public static <T extends Block & IFramedBlock> BlockState copyStandardProperties(T block, BlockState from, BlockState to, boolean copyWaterlogging)
    {
        Preconditions.checkArgument(from.getBlock() == block, "The provided states must be owned by the provided block");

        to = copyRequiredProperties(from, to);
        if (block.getBlockType().canOccludeWithSolidCamo())
        {
            to = Block.copyProperty(from, to, FramedProperties.SOLID);
        }
        if (copyWaterlogging && block.getBlockType().supportsWaterLogging())
        {
            to = Block.copyProperty(from, to, BlockStateProperties.WATERLOGGED);
        }
        return to;
    }

    /**
     * Check whether the given {@link StateDefinition.Builder} contains the given {@link Property}
     *
     * @param builder The builder to check
     * @param property The property to check for
     * @return whether the builder contains the property
     */
    public static boolean hasProperty(StateDefinition.Builder<Block, BlockState> builder, Property<?> property)
    {
        return ((AccessorStateDefinitionBuilder) builder).framedblocks$getProperties().containsKey(property.getName());
    }

    /**
     * Removes the given {@link Property} from the given {@link StateDefinition.Builder}
     *
     * @param builder The builder to remove the property from
     * @param property The property to remove, if present
     */
    public static void removeProperty(StateDefinition.Builder<Block, BlockState> builder, Property<?> property)
    {
        ((AccessorStateDefinitionBuilder) builder).framedblocks$getProperties().remove(property.getName());
    }



    private BlockUtils() { }
}
