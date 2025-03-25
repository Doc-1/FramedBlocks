package xfacthd.framedblocks.api.model.wrapping;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jetbrains.annotations.Nullable;

public interface ModelLookup
{
    BlockStateModel getBlockStateModel(BlockState state);

    @Nullable
    <T> T getStandaloneModel(StandaloneModelKey<T> id);

    BlockStateModel getMissingBlockModel();



    static ModelLookup bind(ModelBakery.BakingResult bakingResult)
    {
        return new ModelLookup()
        {
            @Override
            public BlockStateModel getBlockStateModel(BlockState state)
            {
                return bakingResult.blockStateModels().get(state);
            }

            @Override
            @Nullable
            public <T> T getStandaloneModel(StandaloneModelKey<T> id)
            {
                return bakingResult.standaloneModels().get(id);
            }

            @Override
            public BlockStateModel getMissingBlockModel()
            {
                return bakingResult.missingModels().block();
            }
        };
    }
}
