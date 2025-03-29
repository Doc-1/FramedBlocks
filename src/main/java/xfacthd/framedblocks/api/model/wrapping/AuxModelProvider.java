package xfacthd.framedblocks.api.model.wrapping;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBakery;

public interface AuxModelProvider
{
    BlockStateModel getModel(String key);

    static AuxModelProvider empty(ModelBakery.BakingResult bakingResult)
    {
        return key -> bakingResult.missingModels().block();
    }
}
