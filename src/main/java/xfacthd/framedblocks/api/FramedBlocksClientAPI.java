package xfacthd.framedblocks.api;

import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.util.Utils;

import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings({ "unused", "SameReturnValue" })
public interface FramedBlocksClientAPI
{
    FramedBlocksClientAPI INSTANCE = Utils.loadService(FramedBlocksClientAPI.class);



    /**
     * Add a {@link ModelProperty} for connected textures data to allow FramedBlocks to look up the data for use
     * in the caching of generated quads in the model.
     * <p>
     * The object retrieved via the given {@link ModelProperty} is used as part of the cache key which imposes a few
     * limitations on the CT state:
     * <ul>
     *     <li>The CT state must have valid implementations of {@link Object#hashCode()} and {@link Object#equals(Object)}</li>
     *     <li>The CT state must not contain any data that may leak a {@link Level} or similar data structure such as {@link RenderChunkRegion}s</li>
     *     <li>
     *         The CT state must uniquely identify the visual appearance of a block in a given environment of surrounding blocks
     *         (the set of surrounding blocks as retrieved from the level is not sufficient)
     *     </li>
     * </ul>
     */
    void addConTexProperty(String modId, ModelProperty<?> ctProperty);

    /**
     * Generate overlay quads with the given texture based on all quads on the given side and insert them in the given
     * quad map after the existing quads on the given side of the active render type
     * @param quadMap The {@link QuadMap} containing all transformed quads
     * @param side The side (or {@code null} whose quads shall be operated on
     * @param sprite The texture to be applied to the overlay quads
     */
    void generateOverlayQuads(QuadMap quadMap, @Nullable Direction side, TextureAtlasSprite sprite);

    /**
     * Generate overlay quads with the given texture based on all quads on the given side filtered by the given predicate
     * and insert them in the given quad map after the existing quads on the given side of the active render type
     * @param quadMap The {@link QuadMap} containing all transformed quads
     * @param side The side (or {@code null} whose quads shall be operated on
     * @param sprite The texture to be applied to the overlay quads
     * @param filter The predicate to filter the quads with by their nearest normal direction
     */
    void generateOverlayQuads(QuadMap quadMap, @Nullable Direction side, TextureAtlasSprite sprite, Predicate<Direction> filter);

    /**
     * Generate overlay quads with the given texture based on all quads on the given side filtered by the given predicate
     * and insert them in the given quad map after the existing quads on the given side of the active render type
     * @param quadMap The {@link QuadMap} containing all transformed quads
     * @param side The side (or {@code null} whose quads shall be operated on
     * @param spriteGetter A function returning the texture to be applied to the overlay quad generated from a quad with the given nearest normal direction
     * @param filter The predicate to filter the quads with by their nearest normal direction
     * @apiNote
     */
    void generateOverlayQuads(QuadMap quadMap, @Nullable Direction side, Function<Direction, TextureAtlasSprite> spriteGetter, Predicate<Direction> filter);
}
