package io.github.xfacthd.framedblocks.tests;

import com.google.common.base.Preconditions;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.block.IBlockType;
import io.github.xfacthd.framedblocks.api.block.IFramedBlock;
import io.github.xfacthd.framedblocks.api.util.FramedConstants;
import io.github.xfacthd.framedblocks.common.data.BlockType;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.HorizontalRotation;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

//@GameTestHolder(FramedConstants.MOD_ID)
public final class LightSourceTests
{
    private static final String BATCH_NAME = "lightsource";
    private static final String STRUCTURE_NAME = FramedConstants.MOD_ID + ":lightsourcetests.box";

    /*@GameTestGenerator
    public static Collection<TestFunction> generateLightSourceTests()
    {
        return Arrays.stream(BlockType.values())
                .filter(LightSourceTests::isNotSelfEmitting)
                .map(type -> Utils.rl(type.getName()))
                .map(BuiltInRegistries.BLOCK::getValue)
                .filter(b -> b != Blocks.AIR)
                .map(LightSourceTests::getTestState)
                .map(state -> new TestFunction(
                        BATCH_NAME,
                        getTestName(state),
                        STRUCTURE_NAME,
                        100,
                        0,
                        true,
                        helper -> TestUtils.testBlockLightEmission(helper, state)
                ))
                .toList();
    }*/

    private static boolean isNotSelfEmitting(BlockType type)
    {
        return type != BlockType.FRAMED_TORCH &&
                type != BlockType.FRAMED_WALL_TORCH &&
                type != BlockType.FRAMED_SOUL_TORCH &&
                type != BlockType.FRAMED_SOUL_WALL_TORCH &&
                type != BlockType.FRAMED_REDSTONE_TORCH &&
                type != BlockType.FRAMED_REDSTONE_WALL_TORCH &&
                type != BlockType.FRAMED_LANTERN &&
                type != BlockType.FRAMED_SOUL_LANTERN;
    }

    private static BlockState getTestState(Block block)
    {
        Preconditions.checkArgument(block instanceof IFramedBlock);

        IBlockType type = ((IFramedBlock) block).getBlockType();
        BlockState state = block.defaultBlockState();
        return switch ((BlockType) type)
        {
            case FRAMED_INV_DOUBLE_CORNER_SLOPE_PANEL,
                 FRAMED_INV_DOUBLE_CORNER_SLOPE_PANEL_W,
                 FRAMED_EXT_INNER_DOUBLE_CORNER_SLOPE_PANEL,
                 FRAMED_STACKED_CORNER_SLOPE_PANEL_W,
                 FRAMED_STACKED_INNER_CORNER_SLOPE_PANEL_W
                    -> state.setValue(FramedProperties.FACING_HOR, Direction.SOUTH);
            case FRAMED_EXT_INNER_DOUBLE_CORNER_SLOPE_PANEL_W
                    -> state.setValue(PropertyHolder.ROTATION, HorizontalRotation.RIGHT);
            default -> state;
        };
    }

    private static String getTestName(BlockState state)
    {
        Identifier regName = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return String.format("lightsourcetests.test_%s", regName.getPath());
    }

    private LightSourceTests() { }
}
