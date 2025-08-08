package xfacthd.framedblocks.api.model.standalone;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

public final class StandaloneWrapperKey<T extends CachingModel>
{
    public static final String STANDALONE_DEFINITION_FOLDER = "specialstates";

    private final Holder<Block> block;
    private final ResourceLocation definitionFile;
    private final StandaloneModelKey<T> modelKey;

    public StandaloneWrapperKey(Holder<Block> block, ResourceLocation definitionFile)
    {
        this.block = block;
        this.definitionFile = definitionFile;
        this.modelKey = new StandaloneModelKey<>(definitionFile::toString);
    }

    public Holder<Block> block()
    {
        return block;
    }

    public ResourceLocation definitionFile()
    {
        return definitionFile;
    }

    public StandaloneModelKey<T> modelKey()
    {
        return modelKey;
    }
}
