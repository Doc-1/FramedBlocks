package xfacthd.framedblocks.api.model.wrapping;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public interface ModelLookup
{
    BakedModel getBlockStateModel(ModelResourceLocation id);

    BakedModel getStandaloneModel(ResourceLocation id);



    static ModelLookup bind(ModelBakery.BakingResult bakingResult)
    {
        return new ModelLookup()
        {
            @Override
            public BakedModel getBlockStateModel(ModelResourceLocation id)
            {
                return bakingResult.blockStateModels().get(id);
            }

            @Override
            public BakedModel getStandaloneModel(ResourceLocation id)
            {
                return bakingResult.standaloneModels().get(id);
            }
        };
    }
}
