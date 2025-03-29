package xfacthd.framedblocks.api.model.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.MissingBlockModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.EmptyBlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.internal.InternalClientAPI;
import xfacthd.framedblocks.api.model.ExtendedBlockModelPart;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.DefaultAO;
import xfacthd.framedblocks.api.model.quad.QuadData;
import xfacthd.framedblocks.api.util.ConfigView;
import xfacthd.framedblocks.api.util.Utils;

import java.util.List;
import java.util.function.Supplier;

public final class ModelUtils
{
    // Factor 16 is required because the relative UV of a TextureAtlasSprite is not 0-16 anymore since 1.20.2
    public static final float UV_SUBSTEP_COUNT = 16F * 8F;
    @SuppressWarnings("Convert2Lambda")
    public static final ModelBaker.SharedOperationKey<BlockStateModel> MISSING_MODEL_KEY = new ModelBaker.SharedOperationKey<>()
    {
        @Override
        public BlockStateModel compute(ModelBaker baker)
        {
            return new SingleVariant(SimpleModelWrapper.bake(baker, MissingBlockModel.LOCATION, BlockModelRotation.X0_Y0));
        }
    };

    /**
     * Maps a coordinate 'coordTo' between the given coordinates 'coord1' and 'coord2'
     * onto the UV range they occupy as given by the values at 'uv1' and 'uv2' in the 'uv'
     * array, calculates the target UV coordinate corresponding to the value of 'coordTo'
     * and places it at 'uvTo' in the 'uv' array
     * @param sprite The quad's texture
     * @param data The {@link QuadData} being operated on
     * @param coord1 The first coordinate
     * @param coord2 The second coordinate
     * @param coordTo The target coordinate, must lie between coord1 and coord2
     * @param uv1 The first UV texture coordinate
     * @param uv2 The second UV texture coordinate
     * @param uvTo The target UV texture coordinate
     * @param vAxis Whether the modification should happen on the V axis or the U axis
     * @param rotated Whether the UVs are rotated
     */
    public static void remapUV(
            TextureAtlasSprite sprite,
            QuadData data,
            float coord1,
            float coord2,
            float coordTo,
            int uv1,
            int uv2,
            int uvTo,
            boolean vAxis,
            boolean rotated
    )
    {
        float coordMin = Math.min(coord1, coord2);
        float coordMax = Math.max(coord1, coord2);

        int uvIdx = rotated != vAxis ? 1 : 0;

        float uvAbs1 = data.uv(uv1, uvIdx);
        float uvAbs2 = data.uv(uv2, uvIdx);
        float uvAbsMin = Math.min(uvAbs1, uvAbs2);
        float uvAbsMax = Math.max(uvAbs1, uvAbs2);
        boolean invert = ((coord2 > coord1) ^ (uvAbs2 > uvAbs1)) != vAxis;

        if (coordTo == coordMin)
        {
            data.uv(uvTo, uvIdx,  (invert) ? uvAbsMax : uvAbsMin);
        }
        else if (coordTo == coordMax)
        {
            data.uv(uvTo, uvIdx,  (invert) ? uvAbsMin : uvAbsMax);
        }
        else
        {
            if (ConfigView.Client.INSTANCE.useDiscreteUVSteps())
            {
                float uvRelMin = uvIdx == 0 ? sprite.getUOffset(uvAbsMin) : sprite.getVOffset(uvAbsMin);
                float uvRelMax = uvIdx == 0 ? sprite.getUOffset(uvAbsMax) : sprite.getVOffset(uvAbsMax);

                float mult = (coordTo - coordMin) / (coordMax - coordMin);
                if (invert) { mult = 1F - mult; }

                float uvRelTo = Mth.lerp(mult, uvRelMin, uvRelMax);
                uvRelTo = Math.round(uvRelTo * UV_SUBSTEP_COUNT) / UV_SUBSTEP_COUNT;
                data.uv(uvTo, uvIdx, uvIdx == 0 ? sprite.getU(uvRelTo) : sprite.getV(uvRelTo));
            }
            else
            {
                float mult = (coordTo - coordMin) / (coordMax - coordMin);
                if (invert) { mult = 1F - mult; }
                data.uv(uvTo, uvIdx, Mth.lerp(mult, uvAbsMin, uvAbsMax));
            }
        }
    }

    public static boolean isQuadRotated(QuadData data)
    {
        return (Mth.equal(data.uv(0, 1), data.uv(1, 1)) || Mth.equal(data.uv(3, 1), data.uv(2, 1))) &&
               (Mth.equal(data.uv(1, 0), data.uv(2, 0)) || Mth.equal(data.uv(0, 0), data.uv(3, 0)));
    }

    /**
     * Creates a shallow copy of the given BakedQuad in order to invert the tint index for BakedQuads
     * used by the second model of a double block
     * @apiNote The vertex data of the returned BakedQuad is not a copy, it must not be modified later!
     */
    public static BakedQuad invertTintIndex(BakedQuad quad)
    {
        return new BakedQuad(
                quad.vertices(), //Don't need to copy the vertex data, it won't be modified by the caller
                encodeSecondaryTintIndex(quad.tintIndex()),
                quad.direction(),
                quad.sprite(),
                quad.shade(),
                quad.lightEmission(),
                quad.hasAmbientOcclusion()
        );
    }

    public static int encodeSecondaryTintIndex(int tintIndex)
    {
        return (tintIndex + 2) * -1;
    }

    public static int decodeSecondaryTintIndex(int tintIndex)
    {
        return (tintIndex * -1) - 2;
    }

    public static BlockStateModel getModel(BlockState state)
    {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
    }

    public static Supplier<BlockStateModel> getModelDeferred(BlockState state)
    {
        return Lazy.of(() -> getModel(state));
    }

    public static ExtendedBlockModelPart makeModelPart(BlockModelPart srcPart, QuadMap quadMap, BlockState state, DefaultAO defaultAO, @Nullable BlockState shaderState)
    {
        TriState partAO = defaultAO.apply(srcPart.ambientOcclusion());
        RenderType renderType = srcPart.getRenderType(state);
        return makeModelPart(quadMap, partAO, srcPart.particleIcon(), renderType, shaderState);
    }

    public static ExtendedBlockModelPart makeModelPart(QuadMap quadMap, TriState partAO, TextureAtlasSprite particleSprite, RenderType renderType, @Nullable BlockState shaderState)
    {
        return InternalClientAPI.INSTANCE.makeBlockModelPart(quadMap, partAO, particleSprite, renderType, shaderState);
    }

    public static List<BlockModelPart> collectModelParts(BlockStateModel camoModel, BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, boolean supportDynamicGeometry)
    {
        if (!supportDynamicGeometry)
        {
            level = EmptyBlockAndTintGetter.INSTANCE;
            pos = BlockPos.ZERO;
        }
        return camoModel.collectParts(level, pos, state, random);
    }

    /**
     * Guess the cull-face of quads returned by {@link BlockModelPart#getQuads(Direction)}
     * with a {@code null} side (i.e. supposedly uncullable quads) and filter them to return the ones applicable to the given
     * {@link Direction} and touching the block edge. This fixes blocks becoming invisible when mods forget to specify
     * cull-faces in their models
     * <p>
     * Heavily based on <a href="https://github.com/embeddedt/embeddium/blob/72ba934b27fa35856a0a64f3aa6c867592b2e54f/src/main/java/me/jellysquid/mods/sodium/client/model/quad/properties/ModelQuadFlags.java#L41-L115">Embeddium's quad flag calculation</a>,
     * licensed under LGPL v3
     */
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static void getFilteredNullQuads(List<BakedQuad> quadsOut, BlockModelPart modelPart, Direction side)
    {
        List<BakedQuad> nullQuads = modelPart.getQuads(null);
        if (nullQuads.isEmpty()) return;

        for (int i = 0; i < nullQuads.size(); i++)
        {
            BakedQuad quad = nullQuads.get(i);

            // Filter out quads pointing completely the wrong way early
            if (quad.direction() != side) continue;

            float minX = 32F;
            float minY = 32F;
            float minZ = 32F;
            float maxX = -32F;
            float maxY = -32F;
            float maxZ = -32F;

            int[] vertexData = quad.vertices();
            for (int vert = 0; vert < 4; ++vert)
            {
                int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;

                float x = Float.intBitsToFloat(vertexData[offset]);
                float y = Float.intBitsToFloat(vertexData[offset + 1]);
                float z = Float.intBitsToFloat(vertexData[offset + 2]);

                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                minZ = Math.min(minZ, z);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
                maxZ = Math.max(maxZ, z);
            }

            boolean positive = Utils.isPositive(side);
            boolean aligned = switch(side.getAxis())
            {
                case X -> minX == maxX && (positive ? maxX > 0.9999F : minX < 0.0001F);
                case Y -> minY == maxY && (positive ? maxY > 0.9999F : minY < 0.0001F);
                case Z -> minZ == maxZ && (positive ? maxZ > 0.9999F : minZ < 0.0001F);
            };

            if (aligned)
            {
                quadsOut.add(quad);
            }
        }
    }



    private ModelUtils() { }
}
