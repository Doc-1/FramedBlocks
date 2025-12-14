package io.github.xfacthd.framedblocks.common.compat.jade;

import io.github.xfacthd.framedblocks.api.block.IFramedBlock;
import io.github.xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import io.github.xfacthd.framedblocks.api.block.blockentity.FramedDoubleBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.Element;

final class FramedBlockComponentProvider implements IBlockComponentProvider
{
    static final FramedBlockComponentProvider INSTANCE = new FramedBlockComponentProvider();

    private FramedBlockComponentProvider() { }

    @Override
    @Nullable
    public Element getIcon(BlockAccessor accessor, IPluginConfig config, @Nullable Element currentIcon)
    {
        if (!(accessor.getBlockState().getBlock() instanceof IFramedBlock block)) return null;
        if (!block.shouldRenderAsBlockInJadeTooltip()) return null;
        if (!(accessor.getBlockEntity() instanceof FramedBlockEntity blockEntity)) return null;

        return new FramedBlockElement(accessor.getBlockState(), blockEntity);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
    {
        if (accessor.getBlockEntity() instanceof FramedBlockEntity fbe)
        {
            if (fbe.getBlockType().isDoubleBlock() && fbe instanceof FramedDoubleBlockEntity fdbe)
            {
                tooltip.add(Component.translatable(JadeCompat.LABEL_CAMO_ONE, fbe.getCamo().getContent().getCamoName()));
                tooltip.add(Component.translatable(JadeCompat.LABEL_CAMO_TWO, fdbe.getCamoTwo().getContent().getCamoName()));
            }
            else
            {
                tooltip.add(Component.translatable(JadeCompat.LABEL_CAMO, fbe.getCamo().getContent().getCamoName()));
            }
        }
    }

    @Override
    public Identifier getUid()
    {
        return JadeCompat.ID_FRAMED_BLOCK;
    }
}
