package xfacthd.framedblocks.api.model.wrapping;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.SpriteGetter;
import net.minecraft.resources.ResourceLocation;

public interface TextureLookup
{
    TextureAtlasSprite get(ResourceLocation id);



    @SuppressWarnings("deprecation")
    static TextureLookup bindSpriteGetter(SpriteGetter getter, ModelDebugName debugName)
    {
        return id -> getter.get(new Material(TextureAtlas.LOCATION_BLOCKS, id), debugName);
    }

    /**
     * {@return a lookup that is only usable at the end of or outside of a resource reload}
     */
    @SuppressWarnings("deprecation")
    static TextureLookup runtime()
    {
        return id -> Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(id);
    }
}
