package xfacthd.framedblocks.client.loader.fallback;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.MissingBlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;
import net.neoforged.neoforge.client.model.UnbakedModelParser;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import xfacthd.framedblocks.api.util.Utils;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public final class FallbackLoader implements UnbakedModelLoader<UnbakedModel>
{
    public static final ResourceLocation ID = Utils.rl("fallback");
    private static final FileToIdConverter MODEL_LISTER = FileToIdConverter.json("models");

    @Override
    public UnbakedModel read(JsonObject json, JsonDeserializationContext ctx) throws JsonParseException
    {
        JsonArray conditionArray = GsonHelper.getAsJsonArray(json, ConditionalOps.DEFAULT_CONDITIONS_KEY);
        List<ICondition> conditions = ICondition.LIST_CODEC.decode(JsonOps.INSTANCE, conditionArray).getOrThrow(
                err -> new JsonParseException("Failed to parse conditions: " + err)
        ).getFirst();

        if (conditions.stream().allMatch(cond -> cond.test(ICondition.IContext.EMPTY)))
        {
            json.remove("loader");
            return ctx.deserialize(json, BlockModel.class);
        }

        ResourceLocation fallback = ResourceLocation.parse(GsonHelper.getAsString(json, "fallback"));
        // Missing model must be special-cased as it's a "synthetic" model and cannot be loaded from a file
        if (fallback.equals(MissingBlockModel.LOCATION))
        {
            return MissingBlockModel.missingModel();
        }
        fallback = MODEL_LISTER.idToFile(fallback);
        try
        {
            Resource resource = Minecraft.getInstance().getResourceManager().getResourceOrThrow(fallback);
            try (Reader reader = resource.openAsReader())
            {
                return UnbakedModelParser.parse(reader);
            }
        }
        catch (IOException e)
        {
            throw new JsonParseException("Failed to parse fallback model", e);
        }
    }
}
