package xfacthd.framedblocks.client.loader.fallback;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.List;
import java.util.function.Function;

final class FallbackGeometry implements IUnbakedGeometry<FallbackGeometry>
{
    private final BlockModel model;

    public FallbackGeometry(BlockModel model)
    {
        this.model = model;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext ctx, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, List<ItemOverride> overrides)
    {
        return model.bake(spriteGetter, modelState, true);
    }

    @Override
    public void resolveDependencies(UnbakedModel.Resolver modelGetter, IGeometryBakingContext context)
    {
        model.resolveDependencies(modelGetter);
    }
}
