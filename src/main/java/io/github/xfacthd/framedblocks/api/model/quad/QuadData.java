package io.github.xfacthd.framedblocks.api.model.quad;

import io.github.xfacthd.framedblocks.api.model.util.ModelUtils;
import net.minecraft.Optionull;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.quad.BakedNormals;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public final class QuadData
{
    private final BakedQuad quad;
    private final Vector3fc pos0;
    private final Vector3fc pos1;
    private final Vector3fc pos2;
    private final Vector3fc pos3;
    @Nullable
    private Vector3f mutPos0;
    @Nullable
    private Vector3f mutPos1;
    @Nullable
    private Vector3f mutPos2;
    @Nullable
    private Vector3f mutPos3;
    private long uv0;
    private long uv1;
    private long uv2;
    private long uv3;
    private BakedNormals normals;
    private int tintIndex;
    private boolean shade;
    private int lightEmission;
    private boolean ao;
    private final boolean uvRotated;

    public QuadData(BakedQuad quad)
    {
        this.quad = quad;
        this.pos0 = quad.position0();
        this.pos1 = quad.position1();
        this.pos2 = quad.position2();
        this.pos3 = quad.position3();
        this.uv0 = quad.packedUV0();
        this.uv1 = quad.packedUV1();
        this.uv2 = quad.packedUV2();
        this.uv3 = quad.packedUV3();
        this.normals = quad.bakedNormals();
        this.tintIndex = quad.tintIndex();
        this.shade = quad.shade();
        this.lightEmission = quad.lightEmission();
        this.ao = quad.hasAmbientOcclusion();
        this.uvRotated = ModelUtils.isQuadRotated(this);
    }

    QuadData(QuadData data)
    {
        this.quad = data.quad;
        this.pos0 = data.pos0;
        this.pos1 = data.pos1;
        this.pos2 = data.pos2;
        this.pos3 = data.pos3;
        this.mutPos0 = Optionull.map(data.mutPos0, Vector3f::new);
        this.mutPos1 = Optionull.map(data.mutPos1, Vector3f::new);
        this.mutPos2 = Optionull.map(data.mutPos2, Vector3f::new);
        this.mutPos3 = Optionull.map(data.mutPos3, Vector3f::new);
        this.uv0 = data.uv0;
        this.uv1 = data.uv1;
        this.uv2 = data.uv2;
        this.uv3 = data.uv3;
        this.normals = data.normals;
        this.tintIndex = data.tintIndex;
        this.shade = data.shade;
        this.lightEmission = data.lightEmission;
        this.ao = data.ao;
        this.uvRotated = data.uvRotated;
    }

    public boolean uvRotated()
    {
        return uvRotated;
    }

    public float pos(int vert, int idx)
    {
        return pos(vert).get(idx);
    }

    public Vector3f pos(int vert, Vector3f out)
    {
        return out.set(pos(vert));
    }

    public void pos(int vert, int idx, float val)
    {
        mutPos(vert).setComponent(idx, val);
    }

    public void pos(int vert, float x, float y, float z)
    {
        mutPos(vert).set(x, y, z);
    }

    public float uv(int vert, int idx)
    {
        int masked = (int) (uv(vert) >>> (32 * (1-idx)) & 0xFFFFFFFFL);
        return Float.intBitsToFloat(masked);
    }

    public void uv(int vert, int idx, float val)
    {
        int shift = 32 * (1-idx);
        long masked = uv(vert) & ~(0xFFFFFFFFL << shift);
        long packed = masked | ((long) Float.floatToIntBits(val) << shift);
        switch (vert)
        {
            case 0 -> uv0 = packed;
            case 1 -> uv1 = packed;
            case 2 -> uv2 = packed;
            case 3 -> uv3 = packed;
            default -> throw new IndexOutOfBoundsException(vert);
        }
    }

    public void uv(int vert, float u, float v)
    {
        long packed = UVPair.pack(u, v);
        switch (vert)
        {
            case 0 -> uv0 = packed;
            case 1 -> uv1 = packed;
            case 2 -> uv2 = packed;
            case 3 -> uv3 = packed;
            default -> throw new IndexOutOfBoundsException(vert);
        }
    }

    public float normal(int vert, int idx)
    {
        int packedNormal = normals.normal(vert);
        return BakedNormals.unpackComponent(packedNormal, idx);
    }

    public Direction direction()
    {
        return quad.direction();
    }

    public int tintIndex()
    {
        return tintIndex;
    }

    public void tintIndex(int tintIndex)
    {
        this.tintIndex = tintIndex;
    }

    public boolean shade()
    {
        return shade;
    }

    public void shade(boolean shade)
    {
        this.shade = shade;
    }

    public int lightEmission()
    {
        return lightEmission;
    }

    public void lightEmission(int lightEmission)
    {
        this.lightEmission = lightEmission;
    }

    public boolean ao()
    {
        return ao;
    }

    public void ao(boolean ao)
    {
        this.ao = ao;
    }

    private Direction recomputeNormals()
    {
        int normal = BakedNormals.computeQuadNormal(pos(0), pos(1), pos(2), pos(3));
        normals = BakedNormals.of(normal);

        float nX = BakedNormals.unpackX(normal);
        float nY = BakedNormals.unpackY(normal);
        float nZ = BakedNormals.unpackZ(normal);
        return Direction.getApproximateNearest(nX, nY, nZ);
    }

    private Vector3fc pos(int vert)
    {
        return switch (vert)
        {
            case 0 -> Objects.requireNonNullElse(mutPos0, pos0);
            case 1 -> Objects.requireNonNullElse(mutPos1, pos1);
            case 2 -> Objects.requireNonNullElse(mutPos2, pos2);
            case 3 -> Objects.requireNonNullElse(mutPos3, pos3);
            default -> throw new IndexOutOfBoundsException(vert);
        };
    }

    private Vector3f mutPos(int vert)
    {
        return switch (vert)
        {
            case 0 -> mutPos0 != null ? mutPos0 : (mutPos0 = new Vector3f(pos0));
            case 1 -> mutPos1 != null ? mutPos1 : (mutPos1 = new Vector3f(pos1));
            case 2 -> mutPos2 != null ? mutPos2 : (mutPos2 = new Vector3f(pos2));
            case 3 -> mutPos3 != null ? mutPos3 : (mutPos3 = new Vector3f(pos3));
            default -> throw new IndexOutOfBoundsException(vert);
        };
    }

    private long uv(int vert)
    {
        return switch (vert)
        {
            case 0 -> uv0;
            case 1 -> uv1;
            case 2 -> uv2;
            case 3 -> uv3;
            default -> throw new IndexOutOfBoundsException(vert);
        };
    }

    BakedQuad toQuad() {
        Direction normalDir = recomputeNormals();
        return new BakedQuad(
                pos(0),
                pos(1),
                pos(2),
                pos(3),
                uv0,
                uv1,
                uv2,
                uv3,
                tintIndex,
                normalDir,
                quad.sprite(),
                shade,
                lightEmission,
                normals,
                quad.bakedColors(),
                ao
        );
    }
}
