package xfacthd.framedblocks.api.model.geometry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PartConsumer
{
    /**
     * @param part            The {@link BlockModelPart} to add
     * @param state           The {@link BlockState} the part is from (required for {@link RenderType} lookup)
     * @param includeNull     Whether faces returned for {@code null} face should be included
     * @param reclaimFromNull Whether cullable faces should be recovered from uncullable quads
     * @param cullNonNull     Whether cullable faces should be culled according to the occlusion settings this {@code PartConsumer} was constructed with
     * @param camoPart        Whether the part being passed in is part of the camo ({@code true}) or additional quads ({@code false})
     * @param shaderState     The {@link BlockState} to use as the "shader state" of the resulting model parts
     * @param modifier        An optional modifier to pre-process the quads with after filtering and quad recovery
     */
    void accept(
            BlockModelPart part,
            BlockState state,
            boolean includeNull,
            boolean reclaimFromNull,
            boolean cullNonNull,
            boolean camoPart,
            @Nullable BlockState shaderState,
            @Nullable QuadListModifier modifier
    );

    /**
     * @param model           The model whose {@link BlockModelPart}s to add
     * @param level           The {@link BlockAndTintGetter} to provide to the model for part collection
     * @param pos             The {@link BlockPos} to provide to the model for part collection
     * @param random          The {@link RandomSource} to provide to the model for part collection
     * @param state           The {@link BlockState} the part is from (required for part and {@link RenderType} lookup)
     * @param includeNull     Whether faces returned for {@code null} face should be included
     * @param reclaimFromNull Whether cullable faces should be recovered from uncullable quads
     * @param cullNonNull     Whether cullable faces should be culled according to the occlusion settings this {@code PartConsumer} was constructed with
     * @param camoModel       Whether the parts of the model being passed in is part of the camo ({@code true}) or additional quads ({@code false})
     * @param shaderState     The {@link BlockState} to use as the "shader state" of the resulting model parts
     * @param modifier        An optional modifier to pre-process the quads with after filtering and quad recovery
     */
    default void acceptAll(
            BlockStateModel model,
            BlockAndTintGetter level,
            BlockPos pos,
            RandomSource random,
            BlockState state,
            boolean includeNull,
            boolean reclaimFromNull,
            boolean cullNonNull,
            boolean camoModel,
            @Nullable BlockState shaderState,
            @Nullable QuadListModifier modifier
    )
    {
        for (BlockModelPart part : model.collectParts(level, pos, state, random))
        {
            accept(part, state, includeNull, reclaimFromNull, cullNonNull, camoModel, shaderState, modifier);
        }
    }
}
