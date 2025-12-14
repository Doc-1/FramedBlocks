package io.github.xfacthd.framedblocks.tests;

import com.google.common.base.Preconditions;
import io.github.xfacthd.framedblocks.api.block.IBlockType;
import io.github.xfacthd.framedblocks.api.block.IFramedBlock;
import io.github.xfacthd.framedblocks.api.util.FramedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

//@GameTestHolder(FramedConstants.MOD_ID)
public final class IntangibilityTests
{
    private static final String BATCH_NAME = "intangibility";
    private static final String STRUCTURE_NAME = FramedConstants.MOD_ID + ":floor_slab_1x1";

    /*@GameTestGenerator
    public static Collection<TestFunction> generateIntangibilityTests()
    {
        return Arrays.stream(BlockType.values())
                .filter(BlockType::allowMakingIntangible)
                .map(type -> Utils.rl(type.getName()))
                .map(BuiltInRegistries.BLOCK::getValue)
                .filter(b -> b != Blocks.AIR)
                .map(IntangibilityTests::getTestState)
                .map(state -> new TestFunction(
                        BATCH_NAME,
                        getTestName(state),
                        STRUCTURE_NAME,
                        100,
                        0,
                        true,
                        helper -> TestUtils.testBlockIntangibility(helper, state)
                ))
                .toList();
    }

    @BeforeBatch(batch = BATCH_NAME)
    public static void beforeBatch(ServerLevel level)
    {
        ServerConfig.VIEW.setOverrideIntangibilityConfig(true);
    }

    @AfterBatch(batch = BATCH_NAME)
    public static void afterBatch(ServerLevel level)
    {
        ServerConfig.VIEW.setOverrideIntangibilityConfig(false);
    }*/

    private static BlockState getTestState(Block block)
    {
        Preconditions.checkArgument(block instanceof IFramedBlock, "Invalid test block: " + block);

        IBlockType type = ((IFramedBlock) block).getBlockType();
        return block.defaultBlockState();
    }

    private static String getTestName(BlockState state)
    {
        Identifier regName = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return String.format("intangibilitytests.test_%s", regName.getPath());
    }



    private IntangibilityTests() { }
}
