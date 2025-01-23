package xfacthd.framedblocks.client.loader.fallback;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class FallbackLoaderBuilder extends CustomLoaderBuilder
{
    private final List<ICondition> conditions = new ArrayList<>();
    @Nullable
    private ResourceLocation fallback;

    public FallbackLoaderBuilder()
    {
        super(FallbackLoader.ID, true);
    }

    public FallbackLoaderBuilder addCondition(ICondition condition)
    {
        Preconditions.checkNotNull(condition, "Condition must not be null");
        conditions.add(condition);
        return this;
    }

    public FallbackLoaderBuilder setFallback(ResourceLocation fallback)
    {
        Preconditions.checkNotNull(fallback, "Fallback must not be null");
        this.fallback = fallback;
        return this;
    }

    @Override
    protected CustomLoaderBuilder copyInternal()
    {
        FallbackLoaderBuilder builder = new FallbackLoaderBuilder();
        builder.conditions.addAll(conditions);
        builder.fallback = fallback;
        return builder;
    }

    @Override
    public JsonObject toJson(JsonObject json)
    {
        Preconditions.checkNotNull(fallback, "No fallback model set");
        Preconditions.checkState(!conditions.isEmpty(), "No conditions specified");

        json = super.toJson(json);
        json.add("conditions", ICondition.LIST_CODEC.encodeStart(JsonOps.INSTANCE, conditions).getOrThrow());
        json.addProperty("fallback", fallback.toString());
        return json;
    }
}
