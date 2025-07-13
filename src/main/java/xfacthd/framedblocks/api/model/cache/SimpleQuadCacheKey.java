package xfacthd.framedblocks.api.model.cache;

import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.camo.CamoContent;

/**
 * @param camo       The {@link CamoContent} of the camo applied to the block
 * @param ctCtx      The connected textures context data used by the camo model, may be null
 * @param secondPart Whether the generated quads are part of the second part of a double block model
 * @param emissive   Whether the generated quads should be emissive
 */
public record SimpleQuadCacheKey(CamoContent<?> camo, @Nullable Object ctCtx, boolean secondPart, boolean emissive) implements QuadCacheKey { }
