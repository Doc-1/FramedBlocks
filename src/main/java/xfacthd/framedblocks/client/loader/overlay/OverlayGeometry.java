package xfacthd.framedblocks.client.loader.overlay;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Function;

record OverlayGeometry(BlockModel wrapped, Vector3f offset, Vector3f scale) implements IUnbakedGeometry<OverlayGeometry>
{
    public static final Vector3f VEC_ZERO = new Vector3f();

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState transform, List<ItemOverride> overrides)
    {
        Transformation transformation = transform.getRotation().compose(new Transformation(offset, null, scale, null));
        transform = new SimpleModelState(transformation, transform.isUvLocked());

        BakedModel model = wrapped.bake(spriteGetter, transform, true);
        return offset.equals(VEC_ZERO) ? model : new OverlayModel(model, offset, scale);
    }

    @Override
    public void resolveDependencies(UnbakedModel.Resolver modelGetter, IGeometryBakingContext context)
    {
        wrapped.resolveDependencies(modelGetter);
    }
}
