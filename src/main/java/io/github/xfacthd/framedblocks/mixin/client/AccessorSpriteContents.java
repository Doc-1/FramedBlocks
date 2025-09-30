package io.github.xfacthd.framedblocks.mixin.client;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SpriteContents.class)
public interface AccessorSpriteContents
{
    @Accessor("additionalMetadata")
    List<MetadataSectionType.WithValue<?>> framedblocks$getAdditionalMetadata();
}
