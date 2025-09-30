package io.github.xfacthd.framedblocks.client.util.duck;

import io.github.xfacthd.framedblocks.mixin.client.AccessorSpriteContents;
import net.minecraft.server.packs.metadata.MetadataSectionType;

import java.util.List;

@SuppressWarnings("unused") // Used via interface injection
public interface DefaultedAccessorSpriteContents extends AccessorSpriteContents
{
    @Override
    default List<MetadataSectionType.WithValue<?>> framedblocks$getAdditionalMetadata()
    {
        throw new AssertionError();
    }
}
