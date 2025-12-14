package io.github.xfacthd.framedblocks.api.model.quad;

import net.minecraft.client.renderer.block.model.BakedQuad;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.Nullable;

import java.util.List;

public final class QuadModifier
{
    private static final QuadModifier FAILED = new QuadModifier(null, -1, false, 0, false, false, true);

    private final QuadData data;
    private int tintIndex;
    private boolean shade;
    private int lightEmission;
    private boolean ao;
    private boolean modified;
    private boolean failed;
    private boolean exported;

    /**
     * @return a {@code QuadModifier} for the given {@link BakedQuad}
     */
    public static QuadModifier of(BakedQuad quad)
    {
        return new QuadModifier(new QuadData(quad), quad.tintIndex(), quad.shade(), quad.lightEmission(), quad.hasAmbientOcclusion(), false, false);
    }

    private QuadModifier(@UnknownNullability QuadData data, int tintIndex, boolean shade, int lightEmission, boolean ao, boolean modified, boolean failed)
    {
        this.data = data;
        this.tintIndex = tintIndex;
        this.shade = shade;
        this.lightEmission = lightEmission;
        this.ao = ao;
        this.modified = modified;
        this.failed = failed;
    }

    /**
     * Apply the given {@link Modifier} to the current vertex data if {@code apply} is true. If a previous modifier
     * failed, then the modification will not be applied
     */
    public QuadModifier applyIf(Modifier modifier, boolean apply)
    {
        return apply ? apply(modifier) : this;
    }

    /**
     * Apply the given {@link Modifier} to the current vertex data. If a previous modifier failed,
     * then the modification will not be applied
     */
    public QuadModifier apply(Modifier modifier)
    {
        if (!failed)
        {
            if (exported)
            {
                throw new IllegalStateException(
                        "QuadModifier has been exported, no further modifications allowed without deriving"
                );
            }
            failed = !modifier.accept(data);
            modified = true;
        }
        return this;
    }

    public QuadModifier tintIndex(int tintIndex)
    {
        this.tintIndex = tintIndex;
        modified = true;
        return this;
    }

    public QuadModifier shade(boolean shade)
    {
        if (shade != data.quad.shade())
        {
            this.shade = shade;
            modified = true;
        }
        return this;
    }

    public QuadModifier increaseLightEmission(int lightEmission)
    {
        return lightEmission(Math.max(this.lightEmission, lightEmission));
    }

    public QuadModifier lightEmission(int lightEmission)
    {
        this.lightEmission = lightEmission;
        modified = true;
        return this;
    }

    public QuadModifier ambientOcclusion(boolean ao)
    {
        if (ao != data.quad.hasAmbientOcclusion())
        {
            this.ao = ao;
            modified = true;
        }
        return this;
    }

    /**
     * Re-assemble the quad and add it to the given quad list. If any of modifier failed,
     * the quad will not be exported
     */
    public void export(List<BakedQuad> quadList)
    {
        BakedQuad quad = exportDirect();
        if (quad != null)
        {
            quadList.add(quad);
        }
    }

    @Nullable
    public BakedQuad exportDirect()
    {
        if (failed)
        {
            return null;
        }

        if (!modified)
        {
            return data.quad;
        }

        BakedQuad newQuad = data.toQuad(tintIndex, shade, lightEmission, ao);
        exported = true;
        return newQuad;
    }

    /**
     * Clone this {@code QuadModifier} to continue modifying the source quad in multiple different ways without
     * having to repeat the equivalent modification steps
     * @return a new {@code QuadModifier} with a deep-copy of the current data or an empty,
     * failed modifier if this modifier previously failed
     */
    public QuadModifier derive()
    {
        if (failed)
        {
            return FAILED;
        }
        return new QuadModifier(new QuadData(data), tintIndex, shade, lightEmission, ao, modified, false);
    }

    public boolean hasFailed()
    {
        return failed;
    }

    @FunctionalInterface
    public interface Modifier
    {
        boolean accept(QuadData data);
    }
}
