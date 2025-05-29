package xfacthd.framedblocks.common.data.datamaps;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import xfacthd.framedblocks.common.data.FramedDataMaps;

import java.util.function.Function;

public record SoundEventGroup(String group)
{
    public static final Codec<SoundEventGroup> CODEC = Codec.STRING.xmap(SoundEventGroup::new, SoundEventGroup::group);

    public static boolean isSameSound(SoundType typeOne, SoundType typeTwo, Function<SoundType, SoundEvent> eventResolver)
    {
        if (typeOne == typeTwo) return true;

        SoundEvent soundOne = eventResolver.apply(typeOne);
        SoundEvent soundTwo = eventResolver.apply(typeTwo);
        if (soundOne == soundTwo) return true;

        SoundEventGroup groupOne = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundOne).getData(FramedDataMaps.SOUND_EVENT_GROUPS);
        if (groupOne == null) return false;

        SoundEventGroup groupTwo = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundTwo).getData(FramedDataMaps.SOUND_EVENT_GROUPS);
        return groupOne.equals(groupTwo);
    }
}
