package xfacthd.framedblocks.client.render.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.blueprint.BlueprintData;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.FBContent;

public final class BlueprintProperty implements ConditionalItemModelProperty
{
    public static final ResourceLocation HAS_DATA = Utils.rl("blueprint_has_data");
    public static final MapCodec<BlueprintProperty> TYPE = MapCodec.unit(new BlueprintProperty());

    private BlueprintProperty() { }

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext ctx)
    {
        return !stack.getOrDefault(FBContent.DC_TYPE_BLUEPRINT_DATA, BlueprintData.EMPTY).isEmpty();
    }

    @Override
    public MapCodec<BlueprintProperty> type()
    {
        return TYPE;
    }
}
