package io.github.xfacthd.framedblocks.common.datagen.providers;

import io.github.xfacthd.framedblocks.api.util.FramedConstants;
import io.github.xfacthd.framedblocks.api.util.Utils;
import io.github.xfacthd.framedblocks.client.render.util.AnimationSplitterSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class FramedSpriteSourceProvider extends SpriteSourceProvider
{
    public static final ResourceLocation SPRITE_SAW_STILL = Utils.rl("block/stonecutter_saw_still");

    public FramedSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(output, lookup, FramedConstants.MOD_ID);
    }

    @Override
    protected void gather()
    {
        atlas(AtlasIds.BLOCKS)
                .addSource(new AnimationSplitterSource(
                        Utils.rl("minecraft", "block/stonecutter_saw"),
                        List.of(new AnimationSplitterSource.Frame(0, SPRITE_SAW_STILL))
                ));
    }
}
