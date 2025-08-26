package io.github.xfacthd.framedblocks.common.datagen.providers;

import io.github.xfacthd.framedblocks.common.data.FramedDataMaps;
import io.github.xfacthd.framedblocks.common.data.datamaps.SoundEventGroup;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public final class FramedDataMapProvider extends DataMapProvider
{
    public FramedDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider)
    {
        SoundEventGroup stoneLike = new SoundEventGroup("stone_like");
        builder(FramedDataMaps.SOUND_EVENT_GROUPS)
                .add(key(SoundEvents.STONE_BREAK), stoneLike, false)
                .add(key(SoundEvents.STONE_STEP), stoneLike, false)
                .add(key(SoundEvents.STONE_PLACE), stoneLike, false)
                .add(key(SoundEvents.STONE_HIT), stoneLike, false)
                .add(key(SoundEvents.STONE_FALL), stoneLike, false)
                .add(key(SoundEvents.GLASS_BREAK), stoneLike, false)
                .add(key(SoundEvents.GLASS_STEP), stoneLike, false)
                .add(key(SoundEvents.GLASS_PLACE), stoneLike, false)
                .add(key(SoundEvents.GLASS_HIT), stoneLike, false)
                .add(key(SoundEvents.GLASS_FALL), stoneLike, false);
    }

    private static ResourceKey<SoundEvent> key(SoundEvent event)
    {
        return BuiltInRegistries.SOUND_EVENT.getResourceKey(event).orElseThrow();
    }
}
