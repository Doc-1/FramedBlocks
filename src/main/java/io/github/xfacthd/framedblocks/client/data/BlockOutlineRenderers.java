package io.github.xfacthd.framedblocks.client.data;

import io.github.xfacthd.framedblocks.api.render.outline.ModelBasedOutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.outline.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.outline.RegisterOutlineRenderersEvent;
import io.github.xfacthd.framedblocks.client.data.outline.*;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.data.BlockType;

public final class BlockOutlineRenderers
{
    public static void onRegisterOutlineRenderers(RegisterOutlineRenderersEvent event)
    {
        for (BlockType type : BlockType.values())
        {
            if (type.useModelBasedOutline())
            {
                event.register(type, new ModelBasedOutlineRenderer(FBContent.byType(type)));
            }
        }

        event.register(BlockType.FRAMED_STACKED_SLOPE_EDGE, ElevatedSlopeEdgeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_INNER_CORNER_SLOPE_EDGE, new InnerCornerSlopeEdgeOutlineRenderer());
        event.register(BlockType.FRAMED_ELEVATED_INNER_CORNER_SLOPE_EDGE, ElevatedInnerCornerSlopeEdgeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_STACKED_CORNER_SLOPE_EDGE, ElevatedCornerSlopeEdgeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_STACKED_INNER_CORNER_SLOPE_EDGE, ElevatedInnerCornerSlopeEdgeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_INNER_THREEWAY_CORNER_SLOPE_EDGE, new InnerThreewayCornerSlopeEdgeOutlineRenderer());
        event.register(BlockType.FRAMED_RAIL_SLOPE, SlopeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_POWERED_RAIL_SLOPE, SlopeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_DETECTOR_RAIL_SLOPE, SlopeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_ACTIVATOR_RAIL_SLOPE, SlopeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_FANCY_RAIL_SLOPE, SlopeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_FANCY_POWERED_RAIL_SLOPE, SlopeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_FANCY_DETECTOR_RAIL_SLOPE, SlopeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_FANCY_ACTIVATOR_RAIL_SLOPE, SlopeOutlineRenderer.INSTANCE);
        event.register(BlockType.FRAMED_COLLAPSIBLE_BLOCK, new CollapsibleBlockOutlineRenderer());
        event.register(BlockType.FRAMED_LARGE_CORNER_SLOPE_PANEL, new LargeCornerSlopePanelOutlineRenderer());
        event.register(BlockType.FRAMED_LARGE_CORNER_SLOPE_PANEL_W, new LargeCornerSlopePanelWallOutlineRenderer());
        event.register(BlockType.FRAMED_LARGE_INNER_CORNER_SLOPE_PANEL, new LargeInnerCornerSlopePanelOutlineRenderer());
        event.register(BlockType.FRAMED_LARGE_INNER_CORNER_SLOPE_PANEL_W, new LargeInnerCornerSlopePanelWallOutlineRenderer());
        event.register(BlockType.FRAMED_EXT_INNER_CORNER_SLOPE_PANEL, new ExtendedInnerCornerSlopePanelOutlineRenderer());
        event.register(BlockType.FRAMED_EXT_INNER_CORNER_SLOPE_PANEL_W, new ExtendedInnerCornerSlopePanelWallOutlineRenderer());
        event.register(BlockType.FRAMED_INV_DOUBLE_CORNER_SLOPE_PANEL, new InverseDoubleCornerSlopePanelOutlineRenderer());
        event.register(BlockType.FRAMED_INV_DOUBLE_CORNER_SLOPE_PANEL_W, new InverseDoubleCornerSlopePanelWallOutlineRenderer());
        event.register(BlockType.FRAMED_STACKED_CORNER_SLOPE_PANEL, new StackedCornerSlopePanelOutlineRenderer());
        event.register(BlockType.FRAMED_STACKED_CORNER_SLOPE_PANEL_W, new StackedCornerSlopePanelWallOutlineRenderer());
        event.register(BlockType.FRAMED_STACKED_INNER_CORNER_SLOPE_PANEL, new ExtendedInnerCornerSlopePanelOutlineRenderer());
        event.register(BlockType.FRAMED_STACKED_INNER_CORNER_SLOPE_PANEL_W, new ExtendedInnerCornerSlopePanelWallOutlineRenderer());
        event.register(BlockType.FRAMED_ITEM_FRAME, OutlineRenderer.NO_OP);
        event.register(BlockType.FRAMED_GLOWING_ITEM_FRAME, OutlineRenderer.NO_OP);
    }

    private BlockOutlineRenderers() { }
}
