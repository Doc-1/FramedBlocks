package xfacthd.framedblocks.tests;

import com.google.common.base.Preconditions;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.type.IBlockType;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.test.TestUtils;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.HorizontalRotation;

import java.util.Arrays;
import java.util.Collection;

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
        ResourceLocation regName = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return String.format("lightsourcetests.test_%s", regName.getPath());
    }

    private LightSourceTests() { }
}
