package xfacthd.framedblocks.api.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.EmptyModel;
import org.jetbrains.annotations.ApiStatus;
import xfacthd.framedblocks.api.util.Utils;

import java.util.Map;
import java.util.Objects;

public final class ErrorModel
{
    public static final ResourceLocation LOCATION = Utils.rl("item/error");
    private static BakedModel errorModel = null;

    public static BakedModel get()
    {
        return Objects.requireNonNullElse(errorModel, EmptyModel.BAKED);
    }

    @ApiStatus.Internal
    public static void reload(Map<ResourceLocation, BakedModel> models)
    {
        errorModel = models.get(LOCATION);
    }

    private ErrorModel() { }
}
