package xfacthd.framedblocks.mixin;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xfacthd.framedblocks.api.internal.duck.StateDefinitionBuilderAccessor;

import java.util.Map;

@Mixin(StateDefinition.Builder.class)
public class MixinStateDefinitionBuilder implements StateDefinitionBuilderAccessor
{
    @Shadow
    @Final
    private Map<String, Property<?>> properties;

    @Override
    public boolean framedblocks$hasProperty(Property<?> property)
    {
        return properties.containsKey(property.getName());
    }

    @Override
    public void framedblocks$removeProperty(Property<?> property)
    {
        properties.remove(property.getName());
    }
}
