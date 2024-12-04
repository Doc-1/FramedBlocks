package xfacthd.framedblocks.client.loader.overlay;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.DelegateUnbakedModel;
import net.neoforged.neoforge.client.model.SimpleModelState;
import org.joml.Vector3f;

final class UnbakedOverlayModel extends DelegateUnbakedModel
{
    public static final Vector3f VEC_ZERO = new Vector3f();

    private final Vector3f offset;
    private final Vector3f scale;

    UnbakedOverlayModel(UnbakedModel wrapped, Vector3f offset, Vector3f scale)
    {
        super(wrapped);
        this.offset = offset;
        this.scale = scale;
    }

    @Override
    public BakedModel bake(TextureSlots textures, ModelBaker baker, ModelState transform, boolean ao, boolean blockLight, ItemTransforms itemTransforms, ContextMap additionalProperties)
    {
        Transformation transformation = transform.getRotation().compose(new Transformation(offset, null, scale, null));
        transform = new SimpleModelState(transformation, transform.isUvLocked());

        BakedModel model = wrapped.bake(textures, baker, transform, ao, blockLight, itemTransforms, additionalProperties);
        return offset.equals(VEC_ZERO) ? model : new BakedOverlayModel(model, offset, scale);
    }
}
