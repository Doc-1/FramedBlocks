package xfacthd.framedblocks.cmdtests.tests;

import com.google.common.base.Stopwatch;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.block.IFramedDoubleBlock;
import xfacthd.framedblocks.api.camo.CamoContainer;
import xfacthd.framedblocks.api.camo.block.SimpleBlockCamoContainer;
import xfacthd.framedblocks.api.camo.empty.EmptyCamoContainer;
import xfacthd.framedblocks.api.model.ModelPartCollectionFakeLevel;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedDoubleBlockData;
import xfacthd.framedblocks.client.model.baked.FramedBlockModel;
import xfacthd.framedblocks.cmdtests.SpecialTestCommand;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.util.MarkdownTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class ModelPerformanceTest
{
    public static final String NAME = "ModelPerformance";
    private static final String PREFIX = "[" + NAME + "] ";
    private static final int RUNS = 10;
    private static final int SAMPLE_COUNT = 10_000;
    private static final Direction[] DIRECTIONS = Stream.concat(
            Arrays.stream(Direction.values()), Stream.of((Direction) null)
    ).toArray(Direction[]::new);
    private static final RandomSource RANDOM = new SingleThreadedRandomSource(RandomSupport.generateUniqueSeed());
    private static final CamoContainer<?, ?> TEST_CAMO_CONTAINER = new SimpleBlockCamoContainer(Blocks.STONE.defaultBlockState(), FBContent.FACTORY_BLOCK.get());

    public static void testModelPerformance(
            @SuppressWarnings("unused") CommandContext<CommandSourceStack> ctx, Consumer<Component> msgQueueAppender
    )
    {
        Map<String, BlockState> testStates = new LinkedHashMap<>();

        String stoneName = BuiltInRegistries.BLOCK.getKey(Blocks.STONE).toString();
        testStates.put(stoneName, Blocks.STONE.defaultBlockState());
        for (BlockType type : BlockType.values())
        {
            BlockState state = FBContent.byType(type).defaultBlockState();
            String blockName = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
            testStates.put(blockName, state);
        }

        // Warmup runs
        msgQueueAppender.accept(Component.literal(PREFIX + "Warmup..."));
        for (BlockState state : testStates.values())
        {
            testModel(state, makeModelData(state, EmptyCamoContainer.EMPTY));
            testModel(state, makeModelData(state, TEST_CAMO_CONTAINER));
        }

        msgQueueAppender.accept(Component.literal(PREFIX + "Measure..."));
        Map<String, List<Result>> results = new LinkedHashMap<>();
        for (int i = 0; i < RUNS; i++)
        {
            msgQueueAppender.accept(Component.literal(PREFIX + "  Run " + (i + 1)));

            for (Map.Entry<String, BlockState> entry : testStates.entrySet())
            {
                BlockState state = entry.getValue();
                boolean stone = state.getBlock() == Blocks.STONE;
                long timeEmpty = testModel(state, makeModelData(state, EmptyCamoContainer.EMPTY));
                long timeCamo = stone ? 0 : testModel(state, makeModelData(state, TEST_CAMO_CONTAINER));
                results.computeIfAbsent(entry.getKey(), $ -> new ArrayList<>()).add(new Result(timeEmpty, timeCamo));
            }
        }

        msgQueueAppender.accept(Component.literal(PREFIX + "Done, exporting results..."));

        MarkdownTable table = new MarkdownTable();
        table.header("Block");
        for (int i = 0; i < RUNS; i++)
        {
            table.header("Run %d".formatted(i + 1), true);
        }
        table.header("Average", true).header("Relative", true);

        int[] count = new int[1];
        long[] stoneAvg = new long[1];
        long[] allEmptyAvg = new long[results.size() - 1];
        float[] allEmptyRel = new float[results.size() - 1];
        long[] allCamoAvg = new long[results.size() - 1];
        float[] allCamoRel = new float[results.size() - 1];
        results.forEach((name, values) ->
        {
            boolean stone = name.equals(stoneName);

            table.cell(name + " (empty)");
            long total = 0;
            for (Result entry : values)
            {
                table.cell("%6d us".formatted(entry.timeEmpty));
                total += entry.timeEmpty;
            }
            long emptyAvg = total / RUNS;
            float emptyRel = stone ? 1 : ((float) emptyAvg / (float) stoneAvg[0]);
            table.cell("%6d us".formatted(emptyAvg)).cell("%6.02f".formatted(emptyRel)).newRow();

            if (stone)
            {
                stoneAvg[0] = emptyAvg;
                count[0]++;
                return;
            }

            table.cell(name + " (camo)");
            total = 0;
            for (Result entry : values)
            {
                table.cell("%6d us".formatted(entry.timeCamo));
                total += entry.timeCamo;
            }
            long camoAvg = total / RUNS;
            float camoRel = ((float) camoAvg / (float) stoneAvg[0]);
            table.cell("%6d us".formatted(camoAvg)).cell("%6.02f".formatted(camoRel)).newRow();

            allEmptyAvg[count[0] - 1] = emptyAvg;
            allEmptyRel[count[0] - 1] = emptyRel;
            allCamoAvg[count[0] - 1] = camoAvg;
            allCamoRel[count[0] - 1] = camoRel;
            count[0]++;
        });

        StringBuilder data = new StringBuilder();

        int minBlank = 0;
        int maxBlank = 0;
        int minCamo = 0;
        int maxCamo = 0;
        for (int i = 0; i < results.size() - 1; i++)
        {
            minBlank = compare(allEmptyAvg, i, minBlank, true);
            maxBlank = compare(allEmptyAvg, i, maxBlank, false);
            minCamo = compare(allCamoAvg, i, minCamo, true);
            maxCamo = compare(allCamoAvg, i, maxCamo, false);
        }

        data.append("Relative speed:\n")
                .append("- Min (blank): ").append("%6.2f (%6d us)\n".formatted(allEmptyRel[minBlank], allEmptyAvg[minBlank]))
                .append("- Max (blank): ").append("%6.2f (%6d us)\n".formatted(allEmptyRel[maxBlank], allEmptyAvg[maxBlank]))
                .append("- Min (camo):  ").append("%6.2f (%6d us)\n".formatted(allCamoRel[minCamo], allCamoAvg[minCamo]))
                .append("- Max (camo):  ").append("%6.2f (%6d us)\n".formatted(allCamoRel[maxCamo], allCamoAvg[maxCamo]))
                .append("\n\n").append(table.print());

        Component msg = SpecialTestCommand.writeResultToFile("modelperf", "md", data.toString());
        msgQueueAppender.accept(Component.literal(PREFIX).append(msg));
    }

    private static int compare(long[] data, int idx, int prevIdx, boolean min)
    {
        if (min ? data[idx] < data[prevIdx] : data[idx] > data[prevIdx])
        {
            return idx;
        }
        return prevIdx;
    }

    private static long testModel(BlockState state, ModelData data)
    {
        BlockStateModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        if (model instanceof FramedBlockModel framedModel)
        {
            framedModel.clearCache();
        }

        BlockAndTintGetter level = new ModelPartCollectionFakeLevel(state, data);

        Stopwatch watch = Stopwatch.createStarted();

        for (int i = 0; i < SAMPLE_COUNT; i++)
        {
            for (BlockModelPart part : model.collectParts(level, BlockPos.ZERO, state, RANDOM))
            {
                for (Direction side : DIRECTIONS)
                {
                    part.getQuads(side);
                }
            }
        }

        watch.stop();
        return watch.elapsed(TimeUnit.MICROSECONDS);
    }

    private static ModelData makeModelData(BlockState state, CamoContainer<?, ?> camo)
    {
        AbstractFramedBlockData fbData;
        if (state.getBlock() instanceof IFramedDoubleBlock doubleBlock)
        {
            FramedBlockData dataOne = new FramedBlockData(camo, false);
            FramedBlockData dataTwo = new FramedBlockData(camo, true);
            fbData = new FramedDoubleBlockData(doubleBlock.getCache(state).getParts(), dataOne, dataTwo);
        }
        else
        {
            fbData = new FramedBlockData(camo, false);
        }
        return ModelData.of(AbstractFramedBlockData.PROPERTY, fbData);
    }



    private record Result(long timeEmpty, long timeCamo) { }



    private ModelPerformanceTest() { }
}
