package io.github.xfacthd.framedblocks.api.model.cache;

import io.github.xfacthd.framedblocks.api.camo.CamoContent;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface QuadCacheKey
{
    CamoContent<?> camo();

    @Nullable
    Object ctCtx();

    boolean secondPart();

    boolean emissive();
}
