package io.github.xfacthd.framedblocks.api.util.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class DeferredLootFunction<T extends LootItemFunction> extends DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<T>>
{
    private DeferredLootFunction(ResourceKey<LootItemFunctionType<?>> key)
    {
        super(key);
    }

    public static <T extends LootItemFunction> DeferredLootFunction<T> createLootFunction(Identifier name)
    {
        return createLootFunction(ResourceKey.create(Registries.LOOT_FUNCTION_TYPE, name));
    }

    public static <T extends LootItemFunction> DeferredLootFunction<T> createLootFunction(ResourceKey<LootItemFunctionType<?>> key)
    {
        return new DeferredLootFunction<>(key);
    }
}
