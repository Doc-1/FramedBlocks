package xfacthd.framedblocks.api.model.geometry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

@ApiStatus.NonExtendable
public interface OverlayPartGenerator
{
    /**
     * Generate overlay quads with the given texture based on all quads of the given cull faces filtered by the given
     * predicate
     *
     * @param cullFaces    The cull faces whose quads shall be operated on
     * @param sprite       The texture to be applied to the generated overlay quads
     * @param renderType   The {@link RenderType} the generated part should render in
     * @param shaderState  The {@link BlockState} that's visually closest to the generated overlay or {@code null} if no dedicated state should be used, for use by shader mods
     */
    default void generate(@Nullable Direction[] cullFaces, TextureAtlasSprite sprite, RenderType renderType, @Nullable BlockState shaderState)
    {
        generate(cullFaces, dir -> sprite, sprite, dir -> true, renderType, shaderState);
    }

    /**
     * Generate overlay quads with the given texture based on all quads of the given cull faces filtered by the given
     * normal direction predicate
     *
     * @param cullFaces    The cull faces whose quads shall be operated on
     * @param sprite       The texture to be applied to the generated overlay quads
     * @param normalFilter A predicate to filter the quads by their nearest normal direction
     * @param renderType   The {@link RenderType} the generated part should render in
     * @param shaderState  The {@link BlockState} that's visually closest to the generated overlay or {@code null} if no dedicated state should be used, for use by shader mods
     */
    default void generate(@Nullable Direction[] cullFaces, TextureAtlasSprite sprite, Predicate<Direction> normalFilter, RenderType renderType, @Nullable BlockState shaderState)
    {
        generate(cullFaces, dir -> sprite, sprite, normalFilter, renderType, shaderState);
    }

    /**
     * Generate overlay quads with the given texture based on all quads of the given cull faces filtered by the given
     * predicate
     *
     * @param cullFaces     The cull faces whose quads shall be operated on
     * @param spriteGetter  A function returning the texture to be applied to the overlay quad generated from a quad with the given nearest normal direction
     * @param primarySprite The primary sprite, to be used as the part's particle sprite
     * @param renderType    The {@link RenderType} the generated part should render in
     * @param shaderState   The {@link BlockState} that's visually closest to the generated overlay or {@code null} if no dedicated state should be used, for use by shader mods
     */
    default void generate(
            @Nullable Direction[] cullFaces,
            SpriteGetter spriteGetter,
            TextureAtlasSprite primarySprite,
            RenderType renderType,
            @Nullable BlockState shaderState
    )
    {
        generate(cullFaces, spriteGetter, primarySprite, dir -> true, renderType, shaderState);
    }

    /**
     * Generate overlay quads with the given texture based on all quads of the given cull faces filtered by the given
     * predicate
     *
     * @param cullFaces     The cull faces whose quads shall be operated on
     * @param spriteGetter  A function returning the texture to be applied to the overlay quad generated from a quad with the given nearest normal direction
     * @param primarySprite The primary sprite, to be used as the part's particle sprite
     * @param normalFilter  A predicate to filter the quads by their nearest normal direction
     * @param renderType    The {@link RenderType} the generated part should render in
     * @param shaderState   The {@link BlockState} that's visually closest to the generated overlay or {@code null} if no dedicated state should be used, for use by shader mods
     */
    void generate(
            @Nullable Direction[] cullFaces,
            SpriteGetter spriteGetter,
            TextureAtlasSprite primarySprite,
            Predicate<Direction> normalFilter,
            RenderType renderType,
            @Nullable BlockState shaderState
    );



    interface SpriteGetter extends Function<Direction, TextureAtlasSprite>
    {
        @Override
        TextureAtlasSprite apply(Direction side);
    }
}
