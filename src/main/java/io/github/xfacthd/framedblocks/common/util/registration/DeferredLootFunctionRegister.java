package io.github.xfacthd.framedblocks.common.util.registration;

import com.mojang.serialization.MapCodec;
import io.github.xfacthd.framedblocks.api.util.registration.DeferredLootFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class DeferredLootFunctionRegister extends DeferredRegister<LootItemFunctionType<?>>
{
    private DeferredLootFunctionRegister(String namespace)
    {
        super(Registries.LOOT_FUNCTION_TYPE, namespace);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <I extends LootItemFunctionType<?>> DeferredHolder<LootItemFunctionType<?>, I> createHolder(
            ResourceKey<? extends Registry<LootItemFunctionType<?>>> registryKey, Identifier key
    )
    {
        return (DeferredHolder<LootItemFunctionType<?>, I>) DeferredLootFunction.createLootFunction(ResourceKey.create(registryKey, key));
    }

    public <T extends LootItemFunction> DeferredLootFunction<T> registerLootFunction(String name, MapCodec<T> codec)
    {
        return (DeferredLootFunction<T>) register(name, () -> new LootItemFunctionType<>(codec));
    }

    public static DeferredLootFunctionRegister create(String namespace)
    {
        return new DeferredLootFunctionRegister(namespace);
    }
}
