package io.github.xfacthd.framedblocks.api.render.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.model.util.ModelUtils;
import io.github.xfacthd.framedblocks.api.model.wrapping.statemerger.StateMerger;
import io.github.xfacthd.framedblocks.api.util.Utils;
import io.github.xfacthd.framedblocks.client.model.wrapping.ModelWrappingManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.EmptyBlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.quad.BakedNormals;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An {@link OutlineRenderer} which derives the lines from the block's geometry instead of requiring them
 * to be manually specified for the individual block.
 */
public final class ModelBasedOutlineRenderer implements SimpleOutlineRenderer
{
    @ApiStatus.Internal
    public static final Identifier LISTENER_ID = Utils.id("model_based_outline_renderer");
    private static final List<ModelBasedOutlineRenderer> RENDERERS = new ArrayList<>();
    private static final @Nullable Direction[] DIRECTIONS = Arrays.copyOf(Direction.values(), 7);
    private static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();
    private static final List<BlockModelPart> SCRATCH_LIST = new ObjectArrayList<>();

    private final Map<BlockState, float[]> cachedVertices = new IdentityHashMap<>();
    private final StateMerger stateMerger;

    public ModelBasedOutlineRenderer(Block block)
    {
        this.stateMerger = ModelWrappingManager.getHandler(block).getStateMerger();
        RENDERERS.add(this);
    }

    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        state = stateMerger.apply(state);
        float[] vertices = cachedVertices.computeIfAbsent(state, ModelBasedOutlineRenderer::computeVertices);
        drawer.drawLines(vertices);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state) { }

    @ApiStatus.Internal
    public static void clearCaches()
    {
        RENDERERS.forEach(renderer -> renderer.cachedVertices.clear());
    }

    private static float[] computeVertices(BlockState state)
    {
        BlockStateModel model = ModelUtils.getModel(state);
        model.collectParts(EmptyBlockAndTintGetter.INSTANCE, BlockPos.ZERO, state, RANDOM, SCRATCH_LIST);
        Set<Quad> uniqueQuads = new HashSet<>();
        for (BlockModelPart part : SCRATCH_LIST)
        {
            for (Direction dir : DIRECTIONS)
            {
                for (BakedQuad quad : part.getQuads(dir))
                {
                    Vector3fc v0 = quad.position0();
                    Vector3fc v1 = quad.position1();
                    Vector3fc v2 = quad.position2();
                    Vector3fc v3 = quad.position3();
                    int normal = quad.bakedNormals().normal(0);
                    uniqueQuads.add(new Quad(v0, v1, v2, v3, normal));
                }
            }
        }
        SCRATCH_LIST.clear();

        Int2ObjectMap<Object2IntMap<Line>> linesByNormal = new Int2ObjectOpenHashMap<>();
        for (Quad quad : uniqueQuads)
        {
            Object2IntMap<Line> lines = linesByNormal.computeIfAbsent(quad.normal, $ -> new Object2IntOpenHashMap<>());
            appendEdge(quad.v0, quad.v1, lines);
            appendEdge(quad.v2, quad.v3, lines);
            appendEdge(quad.v0, quad.v3, lines);
            appendEdge(quad.v1, quad.v2, lines);
        }

        Set<Line> uniqueLines = new HashSet<>();
        for (Int2ObjectMap.Entry<Object2IntMap<Line>> entry : linesByNormal.int2ObjectEntrySet())
        {
            int normal = entry.getIntKey();
            int oppNormal = BakedNormals.pack(
                    -BakedNormals.unpackX(normal),
                    -BakedNormals.unpackY(normal),
                    -BakedNormals.unpackZ(normal)
            );
            // TODO: try to "XOR" partially overlapping lines originating from a specific normal to support blocks with "stair side" faces built of two quads
            Object2IntMap<Line> oppEntry = linesByNormal.getOrDefault(oppNormal, Object2IntMaps.emptyMap());
            for (Object2IntMap.Entry<Line> line : entry.getValue().object2IntEntrySet())
            {
                if (line.getIntValue() == 1 && !oppEntry.containsKey(line.getKey()))
                {
                    uniqueLines.add(line.getKey());
                }
            }
        }

        // TODO: merge parallel lines with a shared point and eliminate partially overlapping lines

        float[] packedLines = new float[uniqueLines.size() * 6];
        int idx = 0;
        for (Line line : uniqueLines)
        {
            packedLines[idx    ] = line.x1;
            packedLines[idx + 1] = line.y1;
            packedLines[idx + 2] = line.z1;
            packedLines[idx + 3] = line.x2;
            packedLines[idx + 4] = line.y2;
            packedLines[idx + 5] = line.z2;
            idx += 6;
        }
        return packedLines;
    }

    private static void appendEdge(Vector3fc v1, Vector3fc v2, Object2IntMap<Line> lines)
    {
        Line line = new Line(v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z());
        if (line.isNonZero())
        {
            lines.computeInt(line, ($, count) -> count == null ? 1 : count + 1);
        }
    }

    private record Quad(Vector3fc v0, Vector3fc v1, Vector3fc v2, Vector3fc v3, int normal)
    {
        Quad
        {
            if (BakedNormals.isUnspecified(normal))
            {
                normal = BakedNormals.computeQuadNormal(v0, v1, v2, v3);
            }
        }
    }

    private record Line(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        Line
        {
            x1 = quantize(x1);
            y1 = quantize(y1);
            z1 = quantize(z1);
            x2 = quantize(x2);
            y2 = quantize(y2);
            z2 = quantize(z2);

            if (compare(x1, y1, z1, x2, y2, z2) > 0)
            {
                float tx = x1;
                float ty = y1;
                float tz = z1;
                x1 = x2;
                y1 = y2;
                z1 = z2;
                x2 = tx;
                y2 = ty;
                z2 = tz;
            }
        }

        private static float quantize(float value)
        {
            return Math.round(value * 64F) / 64F;
        }

        private static int compare(float x1, float y1, float z1, float x2, float y2, float z2)
        {
            int compX = Float.compare(x1, x2);
            if (compX != 0) return compX;
            int compY = Float.compare(y1, y2);
            if (compY != 0) return compY;
            return Float.compare(z1, z2);
        }

        public boolean isNonZero()
        {
            return x1 != x2 || y1 != y2 || z1 != z2;
        }
    }
}
