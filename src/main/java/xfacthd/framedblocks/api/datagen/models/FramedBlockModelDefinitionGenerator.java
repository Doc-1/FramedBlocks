package xfacthd.framedblocks.api.datagen.models;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.world.level.block.Block;
import xfacthd.framedblocks.api.internal.InternalClientAPI;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
public final class FramedBlockModelDefinitionGenerator implements BlockModelDefinitionGenerator
{
    private final Block block;
    private final Either<BlockModelDefinition, SingleVariant.Unbaked> baseModel;
    private final Map<String, SingleVariant.Unbaked> auxModels = new HashMap<>();

    public FramedBlockModelDefinitionGenerator(Block block, BlockModelDefinition definition)
    {
        this.block = block;
        this.baseModel = Either.left(definition);
    }

    public FramedBlockModelDefinitionGenerator(Block block, SingleVariant.Unbaked variant)
    {
        this.block = block;
        this.baseModel = Either.right(variant);
    }

    public FramedBlockModelDefinitionGenerator addAuxModel(String key, SingleVariant.Unbaked model)
    {
        auxModels.put(key, model);
        return this;
    }

    @Override
    public Block block()
    {
        return block;
    }

    @Override
    public BlockModelDefinition create()
    {
        return InternalClientAPI.INSTANCE.createFramedBlockDefinition(baseModel, auxModels);
    }
}
