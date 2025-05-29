package xfacthd.framedblocks.common.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import xfacthd.framedblocks.common.data.camo.block.rotator.BlockCamoRotatorPrototype;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.data.camo.block.rotator.BlockCamoRotators;
import xfacthd.framedblocks.common.data.datamaps.SoundEventGroup;

public final class FramedDataMaps
{
    public static final DataMapType<Block, BlockCamoRotatorPrototype> BLOCK_CAMO_ROTATORS = DataMapType.builder(
            Utils.rl("block_camo_rotators"),
            Registries.BLOCK,
            BlockCamoRotatorPrototype.CODEC
    ).synced(BlockCamoRotatorPrototype.CODEC, true).build();
    public static final DataMapType<SoundEvent, SoundEventGroup> SOUND_EVENT_GROUPS = DataMapType.builder(
            Utils.rl("sound_event_groups"),
            Registries.SOUND_EVENT,
            SoundEventGroup.CODEC
    ).synced(SoundEventGroup.CODEC, true).build();

    public static void onRegisterDataMapTypes(final RegisterDataMapTypesEvent event)
    {
        event.register(BLOCK_CAMO_ROTATORS);
        event.register(SOUND_EVENT_GROUPS);
    }

    public static void onDataMapsUpdated(final DataMapsUpdatedEvent event)
    {
        if (event.getRegistryKey() == Registries.BLOCK)
        {
            BlockCamoRotators.reload();
        }
    }

    private FramedDataMaps() { }
}
