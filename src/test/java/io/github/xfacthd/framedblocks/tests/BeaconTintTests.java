package io.github.xfacthd.framedblocks.tests;

import com.google.common.base.Preconditions;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.block.IBlockType;
import io.github.xfacthd.framedblocks.api.block.IFramedBlock;
import io.github.xfacthd.framedblocks.api.util.FramedConstants;
import io.github.xfacthd.framedblocks.common.data.BlockType;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.CompoundDirection;
import io.github.xfacthd.framedblocks.common.data.property.DirectionAxis;
import io.github.xfacthd.framedblocks.common.data.property.HorizontalRotation;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Set;

//@GameTestHolder(FramedConstants.MOD_ID)
public final class BeaconTintTests
{
    private static final String BATCH_NAME = "beacon_tint";
    private static final String STRUCTURE_NAME = FramedConstants.MOD_ID + ":floor_slab_1x1";

    // All blocks that are completely unable to apply a tint to the beacon beam
    private static final Set<BlockType> NON_TINTING = Set.of(
            BlockType.FRAMED_SLOPE_EDGE,
            BlockType.FRAMED_CORNER_SLOPE_EDGE,
            BlockType.FRAMED_THREEWAY_CORNER_SLOPE_EDGE,
            BlockType.FRAMED_SLAB_EDGE,
            BlockType.FRAMED_SLAB_CORNER,
            BlockType.FRAMED_PANEL,
            BlockType.FRAMED_CORNER_PILLAR,
            BlockType.FRAMED_DIVIDED_PANEL_HORIZONTAL,
            BlockType.FRAMED_DIVIDED_PANEL_VERTICAL,
            BlockType.FRAMED_FENCE,
            BlockType.FRAMED_FENCE_GATE,
            BlockType.FRAMED_DOOR,
            BlockType.FRAMED_IRON_DOOR,
            BlockType.FRAMED_LADDER,
            BlockType.FRAMED_BUTTON,
            BlockType.FRAMED_STONE_BUTTON,
            BlockType.FRAMED_LEVER,
            BlockType.FRAMED_SIGN,
            BlockType.FRAMED_WALL_SIGN,
            BlockType.FRAMED_HANGING_SIGN,
            BlockType.FRAMED_WALL_HANGING_SIGN,
            BlockType.FRAMED_TORCH,
            BlockType.FRAMED_WALL_TORCH,
            BlockType.FRAMED_SOUL_TORCH,
            BlockType.FRAMED_SOUL_WALL_TORCH,
            BlockType.FRAMED_REDSTONE_TORCH,
            BlockType.FRAMED_REDSTONE_WALL_TORCH,
            BlockType.FRAMED_LATTICE_BLOCK,
            BlockType.FRAMED_VERTICAL_STAIRS,
            BlockType.FRAMED_VERTICAL_SLICED_STAIRS,
            BlockType.FRAMED_BARS,
            BlockType.FRAMED_PANE,
            BlockType.FRAMED_FLOWER_POT,
            BlockType.FRAMED_POST,
            BlockType.FRAMED_HALF_STAIRS,
            BlockType.FRAMED_DOUBLE_HALF_STAIRS,
            BlockType.FRAMED_SLOPE_PANEL,
            BlockType.FRAMED_DOUBLE_SLOPE_PANEL,
            BlockType.FRAMED_FLAT_SLOPE_PANEL_CORNER,
            BlockType.FRAMED_FLAT_INNER_SLOPE_PANEL_CORNER,
            BlockType.FRAMED_FLAT_DOUBLE_SLOPE_PANEL_CORNER,
            BlockType.FRAMED_FLAT_INV_DOUBLE_SLOPE_PANEL_CORNER,
            BlockType.FRAMED_SMALL_CORNER_SLOPE_PANEL,
            BlockType.FRAMED_SMALL_CORNER_SLOPE_PANEL_W,
            BlockType.FRAMED_SMALL_INNER_CORNER_SLOPE_PANEL,
            BlockType.FRAMED_SMALL_INNER_CORNER_SLOPE_PANEL_W,
            BlockType.FRAMED_SMALL_DOUBLE_CORNER_SLOPE_PANEL,
            BlockType.FRAMED_SMALL_DOUBLE_CORNER_SLOPE_PANEL_W,
            BlockType.FRAMED_BOARD,
            BlockType.FRAMED_CORNER_STRIP,
            BlockType.FRAMED_GATE,
            BlockType.FRAMED_IRON_GATE,
            BlockType.FRAMED_ITEM_FRAME,
            BlockType.FRAMED_GLOWING_ITEM_FRAME,
            BlockType.FRAMED_FANCY_RAIL,
            BlockType.FRAMED_FANCY_POWERED_RAIL,
            BlockType.FRAMED_FANCY_DETECTOR_RAIL,
            BlockType.FRAMED_FANCY_ACTIVATOR_RAIL,
            BlockType.FRAMED_HALF_SLOPE,
            BlockType.FRAMED_DOUBLE_HALF_SLOPE,
            BlockType.FRAMED_CHECKERED_PANEL_SEGMENT,
            BlockType.FRAMED_CHECKERED_PANEL,
            BlockType.FRAMED_CHAIN,
            BlockType.FRAMED_LANTERN,
            BlockType.FRAMED_SOUL_LANTERN
    );

    /*@GameTestGenerator
    public static Collection<TestFunction> generateBeaconTintTests()
    {
        return Arrays.stream(BlockType.values())
                .filter(type -> !NON_TINTING.contains(type))
                .map(type -> Utils.rl(type.getName()))
                .map(BuiltInRegistries.BLOCK::getValue)
                .filter(b -> b != Blocks.AIR)
                .map(BeaconTintTests::getTestState)
                .map(state -> new TestFunction(
                        BATCH_NAME,
                        getTestName(state),
                        STRUCTURE_NAME,
                        100,
                        0,
                        true,
                        helper -> TestUtils.testBeaconBeamTinting(helper, state)
                ))
                .toList();
    }*/

    private static BlockState getTestState(Block block)
    {
        Preconditions.checkArgument(block instanceof IFramedBlock);

        IBlockType type = ((IFramedBlock) block).getBlockType();
        if (type instanceof BlockType blockType)
        {
            BlockState state = block.defaultBlockState();
            return switch (blockType)
            {
                case FRAMED_PILLAR -> state.setValue(BlockStateProperties.AXIS, Direction.Axis.Y);
                case FRAMED_HALF_PILLAR -> state.setValue(BlockStateProperties.FACING, Direction.DOWN);
                case FRAMED_PRISM -> state.setValue(PropertyHolder.FACING_AXIS, DirectionAxis.UP_X);
                case FRAMED_SLOPED_PRISM -> state.setValue(PropertyHolder.FACING_DIR, CompoundDirection.UP_NORTH);
                case FRAMED_EXT_DOUBLE_CORNER_SLOPE_PANEL,
                     FRAMED_EXT_DOUBLE_CORNER_SLOPE_PANEL_W,
                     FRAMED_EXT_INNER_DOUBLE_CORNER_SLOPE_PANEL,
                     FRAMED_INV_DOUBLE_CORNER_SLOPE_PANEL,
                     FRAMED_INV_DOUBLE_CORNER_SLOPE_PANEL_W,
                     FRAMED_STACKED_CORNER_SLOPE_PANEL,
                     FRAMED_STACKED_CORNER_SLOPE_PANEL_W,
                     FRAMED_STACKED_INNER_CORNER_SLOPE_PANEL,
                     FRAMED_STACKED_INNER_CORNER_SLOPE_PANEL_W -> state.setValue(FramedProperties.FACING_HOR, Direction.SOUTH);
                case FRAMED_LARGE_BUTTON, FRAMED_LARGE_STONE_BUTTON -> state.setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR);
                case FRAMED_EXT_INNER_DOUBLE_CORNER_SLOPE_PANEL_W -> state.setValue(PropertyHolder.ROTATION, HorizontalRotation.RIGHT);
                default -> state;
            };
        }
        return block.defaultBlockState();
    }

    private static String getTestName(BlockState state)
    {
        Identifier regName = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return String.format("beacontinttests.test_%s", regName.getPath());
    }



    private BeaconTintTests() { }
}
