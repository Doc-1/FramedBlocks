package xfacthd.framedblocks.mixin.client;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(BlockModelShaper.class)
public interface AccessorBlockModelShaper
{
    @Accessor("modelByStateCache")
    Map<BlockState, BlockStateModel> framedblocks$getModelByStateCache();
}
