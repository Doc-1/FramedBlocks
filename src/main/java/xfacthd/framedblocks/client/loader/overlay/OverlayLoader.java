package xfacthd.framedblocks.client.loader.overlay;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;
import org.joml.Vector3f;
import xfacthd.framedblocks.api.util.Utils;

public final class OverlayLoader implements UnbakedModelLoader<UnbakedOverlayModel>
{
    public static final ResourceLocation ID = Utils.rl("overlay");
    private static final Vector3f CENTER = new Vector3f(.5F, .5F, .5F);
    private static final Vector3f DEFAULT_SCALE = new Vector3f(1.001F, 1.001F, 1.001F);

    @Override
    public UnbakedOverlayModel read(JsonObject obj, JsonDeserializationContext ctx) throws JsonParseException
    {
        UnbakedModel model = ctx.deserialize(GsonHelper.getAsJsonObject(obj, "model"), UnbakedModel.class);

        Vector3f offset = UnbakedOverlayModel.VEC_ZERO;
        Vector3f scale = new Vector3f(DEFAULT_SCALE);

        if (obj.has("center"))
        {
            JsonArray arr = GsonHelper.getAsJsonArray(obj, "center");
            if (arr.size() != 3)
            {
                throw new JsonSyntaxException("Invalid center array, expected exactly three elements");
            }

            Vector3f center = new Vector3f(
                    GsonHelper.convertToFloat(arr.get(0), "center[0]") / 16F,
                    GsonHelper.convertToFloat(arr.get(1), "center[1]") / 16F,
                    GsonHelper.convertToFloat(arr.get(2), "center[2]") / 16F
            );

            offset = new Vector3f(CENTER);
            offset.sub(center);

            // Slightly skew the scale to avoid z-fighting on off-center models
            Vector3f scaleAdd = new Vector3f(.01F, .01F, .01F);
            scaleAdd.mul(Mth.abs(offset.x()), Mth.abs(offset.y()), Mth.abs(offset.z()));
            scale.add(scaleAdd);
        }

        return new UnbakedOverlayModel(model, offset, scale);
    }
}
