package xfacthd.framedblocks.client.model.overlaygen;

import com.google.common.base.Preconditions;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.TriState;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.model.ExtendedBlockModelPart;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.OverlayPartGenerator;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.api.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class OverlayModelPartGenerator implements OverlayPartGenerator
{
    private final ArrayList<ExtendedBlockModelPart> staticParts;
    private final TriState ambientOcclusion;
    private final List<ExtendedBlockModelPart> generatedParts = new ArrayList<>();
    private boolean flushed = false;

    public OverlayModelPartGenerator(ArrayList<ExtendedBlockModelPart> staticParts, TriState ambientOcclusion)
    {
        this.staticParts = staticParts;
        this.ambientOcclusion = ambientOcclusion;
    }

    @Override
    public void generate(
            @Nullable Direction[] cullfaces,
            SpriteGetter spriteGetter,
            TextureAtlasSprite primarySprite,
            Predicate<Direction> normalFilter,
            ChunkSectionLayer chunkLayer,
            @Nullable BlockState shaderState
    )
    {
        Preconditions.checkState(!flushed, "OverlayPartGenerator was already flushed");

        QuadMap quadMap = new QuadMap();
        for (BlockModelPart part : staticParts)
        {
            for (Direction side : cullfaces)
            {
                List<BakedQuad> srcQuads = part.getQuads(side);
                List<BakedQuad> newQuads = OverlayQuadGenerator.generate(srcQuads, spriteGetter, normalFilter);
                Utils.copyAll(newQuads, quadMap.get(side));
            }
        }
        generatedParts.add(ModelUtils.makeModelPart(quadMap, ambientOcclusion, primarySprite, chunkLayer, shaderState));
    }

    public void flush()
    {
        flushed = true;
        Utils.copyAll(generatedParts, staticParts);
    }
}
