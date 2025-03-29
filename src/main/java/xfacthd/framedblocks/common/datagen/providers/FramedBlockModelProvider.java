package xfacthd.framedblocks.common.datagen.providers;

import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.MissingBlockModel;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import net.neoforged.neoforge.common.conditions.NeoForgeConditions;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.datagen.models.AbstractFramedBlockModelProvider;
import xfacthd.framedblocks.api.model.item.tint.FramedBlockItemTintProvider;
import xfacthd.framedblocks.api.util.ClientUtils;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.client.itemmodel.FramedBlockItemModel;
import xfacthd.framedblocks.client.itemmodel.TankItemModel;
import xfacthd.framedblocks.client.itemmodel.tintprovider.FramedTargetItemTintProvider;
import xfacthd.framedblocks.client.loader.fallback.FallbackLoaderBuilder;
import xfacthd.framedblocks.client.model.geometry.cube.FramedCollapsibleBlockGeometry;
import xfacthd.framedblocks.client.model.geometry.cube.FramedCollapsibleCopycatBlockGeometry;
import xfacthd.framedblocks.client.model.geometry.cube.FramedMarkedCubeGeometry;
import xfacthd.framedblocks.client.model.geometry.cube.FramedTargetGeometry;
import xfacthd.framedblocks.client.model.geometry.interactive.FramedFlowerPotGeometry;
import xfacthd.framedblocks.client.model.geometry.rail.FramedFancyRailGeometry;
import xfacthd.framedblocks.client.render.item.TankItemRenderer;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.compat.amendments.AmendmentsCompat;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.ChainType;

import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings({ "MethodMayBeStatic", "SameParameterValue" })
public final class FramedBlockModelProvider extends AbstractFramedBlockModelProvider
{
    private static final ResourceLocation TEXTURE = Utils.rl("block/framed_block");
    private static final ResourceLocation TEXTURE_ALT = Utils.rl("block/framed_block_alt");
    private static final ResourceLocation TEXTURE_UNDERLAY = ResourceLocation.withDefaultNamespace("block/stripped_dark_oak_log");
    private static final ModelTemplate TEMPLATE_CUTOUT_CUBE = ModelTemplates.CUBE_ALL.extend().renderType("cutout").build();
    private static final ResourceLocation TRAPDOOR_TEMPLATE_LOC = ResourceLocation.withDefaultNamespace("block/template_orientable_trapdoor_bottom");
    private static final ResourceLocation THIN_BLOCK_LOC = ResourceLocation.withDefaultNamespace("block/thin_block");

    public FramedBlockModelProvider(PackOutput output)
    {
        super(output, FramedConstants.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        ResourceLocation cube = TEMPLATE_CUTOUT_CUBE.create(FBContent.BLOCK_FRAMED_CUBE.value(), TextureMapping.cube(TEXTURE), blockModels.modelOutput);
        ResourceLocation stoneCube = makeUnderlayedCube(blockModels, Utils.rl("block/framed_stone_cube"), TEXTURE, mcLocation("block/stone"), $ -> {});
        ResourceLocation obsidianCube = makeUnderlayedCube(blockModels, Utils.rl("block/framed_obsidian_cube"), TEXTURE, mcLocation("block/obsidian"), $ -> {});
        ResourceLocation ironCube = makeUnderlayedCube(blockModels, Utils.rl("block/framed_iron_cube"), TEXTURE, mcLocation("block/iron_block"), $ -> {});
        ResourceLocation goldCube = makeUnderlayedCube(blockModels, Utils.rl("block/framed_gold_cube"), TEXTURE, mcLocation("block/gold_block"), $ -> {});

        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_HALF_SLOPE, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_HALF_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DIVIDED_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_HALF_SLOPE, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_DOUBLE_HALF_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CORNER_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INNER_CORNER_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PRISM_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INNER_PRISM_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_PRISM_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_THREEWAY_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INNER_THREEWAY_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_THREEWAY_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPE_EDGE, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_SLOPE_EDGE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_DOUBLE_SLOPE_EDGE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_SLOPE_EDGE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CORNER_SLOPE_EDGE, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INNER_CORNER_SLOPE_EDGE, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_CORNER_SLOPE_EDGE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_INNER_CORNER_SLOPE_EDGE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_DOUBLE_CORNER_SLOPE_EDGE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_DOUBLE_INNER_CORNER_SLOPE_EDGE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_CORNER_SLOPE_EDGE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_INNER_CORNER_SLOPE_EDGE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_THREEWAY_CORNER_SLOPE_EDGE, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INNER_THREEWAY_CORNER_SLOPE_EDGE, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ADJ_DOUBLE_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ADJ_DOUBLE_COPYCAT_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DIVIDED_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLAB_EDGE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLAB_CORNER, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ADJ_DOUBLE_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ADJ_DOUBLE_COPYCAT_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DIVIDED_PANEL_HOR, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DIVIDED_PANEL_VERT, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CORNER_PILLAR, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_HALF_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DIVIDED_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_HALF_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPED_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLICED_STAIRS_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLICED_STAIRS_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_DOUBLE_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_HALF_STAIRS, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_DIVIDED_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_DOUBLE_HALF_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_SLICED_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_SLOPED_STAIRS, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_THREEWAY_CORNER_PILLAR, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_THREEWAY_CORNER_PILLAR, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FENCE_GATE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_TRAP_DOOR, cube, builder -> builder.itemBaseModel(TRAPDOOR_TEMPLATE_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_IRON_TRAP_DOOR, ironCube, builder -> builder.itemBaseModel(TRAPDOOR_TEMPLATE_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LADDER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_BUTTON, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STONE_BUTTON, stoneCube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LARGE_BUTTON, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LARGE_STONE_BUTTON, stoneCube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_WALL_SIGN, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_BOARD, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LATTICE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_THICK_LATTICE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_HORIZONTAL_PANE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_RAIL_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_POWERED_RAIL_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DETECTOR_RAIL_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ACTIVATOR_RAIL_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FANCY_RAIL_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FANCY_POWERED_RAIL_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL_SLOPE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PILLAR, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_HALF_PILLAR, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_POST, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PRISM, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_INNER_PRISM, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_INNER_DOUBLE_PRISM, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPED_PRISM, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_INNER_SLOPED_PRISM, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_INNER_DOUBLE_SLOPED_PRISM, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPE_SLAB, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_SLOPE_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_COMPOUND_SLOPE_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_SLOPE_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_SLOPE_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_DOUBLE_SLOPE_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_SLOPE_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_SLOPE_SLAB_CORNER, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_INNER_SLOPE_SLAB_CORNER, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_ELEVATED_SLOPE_SLAB_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_ELEVATED_INNER_SLOPE_SLAB_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_DOUBLE_SLOPE_SLAB_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_INVERSE_DOUBLE_SLOPE_SLAB_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_ELEVATED_DOUBLE_SLOPE_SLAB_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_ELEVATED_INNER_DOUBLE_SLOPE_SLAB_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_STACKED_SLOPE_SLAB_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_STACKED_INNER_SLOPE_SLAB_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPE_PANEL, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_SLOPE_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_COMPOUND_SLOPE_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_SLOPE_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_SLOPE_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_DOUBLE_SLOPE_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_SLOPE_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_SLOPE_PANEL_CORNER, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_INNER_SLOPE_PANEL_CORNER, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_EXTENDED_SLOPE_PANEL_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_EXTENDED_INNER_SLOPE_PANEL_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_DOUBLE_SLOPE_PANEL_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_INVERSE_DOUBLE_SLOPE_PANEL_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_EXTENDED_DOUBLE_SLOPE_PANEL_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_EXTENDED_INNER_DOUBLE_SLOPE_PANEL_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_STACKED_SLOPE_PANEL_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_STACKED_INNER_SLOPE_PANEL_CORNER, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SMALL_CORNER_SLOPE_PANEL, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_SMALL_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LARGE_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_LARGE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SMALL_INNER_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_SMALL_INNER_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LARGE_INNER_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_LARGE_INNER_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SMALL_DOUBLE_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_SMALL_DOUBLE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LARGE_DOUBLE_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_LARGE_DOUBLE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_INNER_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_INNER_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_DOUBLE_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_DOUBLE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_INNER_DOUBLE_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_INNER_DOUBLE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_STACKED_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_INNER_CORNER_SLOPE_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_STACKED_INNER_CORNER_SLOPE_PANEL_WALL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PYRAMID, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PYRAMID_SLAB, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_GATE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_IRON_GATE, ironCube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_MINI_CUBE, cube, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CENTERED_SLAB, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CENTERED_PANEL, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_MASONRY_CORNER_SEGMENT, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_MASONRY_CORNER, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_CUBE_SEGMENT, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_CUBE, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_SLAB_SEGMENT, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_SLAB, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_PANEL_SEGMENT, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_PANEL, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_TUBE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CORNER_TUBE, cube);
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_HOPPER, cube);

        registerFramedCube(blockModels, cube);
        registerFramedFence(blockModels, cube);
        registerFramedDoor(blockModels, cube);
        registerFramedIronDoor(blockModels, ironCube);
        registerFramedPressurePlate(blockModels, cube);
        registerFramedStonePressurePlate(blockModels, stoneCube);
        registerFramedObsidianPressurePlate(blockModels, obsidianCube);
        registerFramedGoldPressurePlate(blockModels, goldCube);
        registerFramedIronPressurePlate(blockModels, ironCube);
        registerFramedLever(blockModels);
        registerFramedSign(blockModels, cube);
        registerFramedHangingSign(blockModels);
        registerFramedWallHangingSign(blockModels);
        registerFramedTorch(blockModels);
        registerFramedWallTorch(blockModels);
        registerFramedSoulTorch(blockModels);
        registerFramedSoulWallTorch(blockModels);
        registerFramedRedstoneTorch(blockModels);
        registerFramedRedstoneWallTorch(blockModels);
        registerFramedCornerStrip(blockModels, cube);
        registerFramedChest(blockModels);
        registerFramedSecretStorage(blockModels);
        registerFramedTank(blockModels);
        registerFramedBarsBlock(blockModels, cube);
        registerFramedPaneBlock(blockModels, cube);
        registerFramedFlowerPotBlock(blockModels, cube);
        registerFramedCollapsibleBlock(blockModels);
        registerFramedCollapsibleCopycatBlock(blockModels);
        registerFramedBouncyBlock(blockModels);
        registerFramedRedstoneBlock(blockModels);
        registerFramedTarget(blockModels, cube);
        registerFramedItemFrame(blockModels);
        registerFramedFancyRail(blockModels);
        registerFramedFancyPoweredRail(blockModels);
        registerFramedFancyDetectorRail(blockModels);
        registerFramedFancyActivatorRail(blockModels);
        registerFramedOneWayWindow(blockModels);
        registerFramedBookshelf(blockModels);
        registerFramedChiseledBookshelf(blockModels);
        registerFramedChain(blockModels, cube);
        registerFramedLantern(blockModels);
        registerFramedSoulLantern(blockModels);

        registerFramingSaw(blockModels);
        registerPoweredFramingSaw(blockModels);
    }

    private void registerFramedCube(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        ResourceLocation solidUnderlay = TEMPLATE_CUTOUT_CUBE.create(
                Utils.rl("block/framed_underlay"),
                new TextureMapping()
                        .put(TextureSlot.ALL, TEXTURE_UNDERLAY)
                        .putForced(TextureSlot.PARTICLE, TEXTURE),
                blockModels.modelOutput
        );
        ResourceLocation altCube = TEMPLATE_CUTOUT_CUBE.create(
                Utils.rl("block/framed_cube_alt"),
                TextureMapping.cube(TEXTURE_ALT),
                blockModels.modelOutput
        );
        ResourceLocation reinforcement = TEMPLATE_CUTOUT_CUBE.create(
                Utils.rl("block/framed_reinforcement"),
                TextureMapping.cube(Utils.rl("block/framed_reinforcement")),
                blockModels.modelOutput
        );

        framedMultiPart(blockModels, FBContent.BLOCK_FRAMED_CUBE, gen -> gen
                .with(
                        BlockModelGenerators.condition().term(PropertyHolder.SOLID_BG, true),
                        BlockModelGenerators.plainVariant(solidUnderlay)
                )
                .with(
                        BlockModelGenerators.condition().term(PropertyHolder.ALT, false),
                        BlockModelGenerators.plainVariant(cube)
                )
                .with(
                        BlockModelGenerators.condition().term(PropertyHolder.ALT, true),
                        BlockModelGenerators.plainVariant(altCube)
                )
                .with(
                        BlockModelGenerators.condition().term(PropertyHolder.REINFORCED, true),
                        BlockModelGenerators.plainVariant(reinforcement)
                )
        );

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_CUBE);
    }

    private void registerFramedFence(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_FENCE, cube);

        blockItemFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_FENCE,
                ModelTemplates.createItem("framedblocks:framed_fence_inventory", TextureSlot.TEXTURE, SLOT_UNDERLAY),
                new TextureMapping()
                        .put(TextureSlot.TEXTURE, TEXTURE)
                        .put(SLOT_UNDERLAY, TEXTURE_UNDERLAY)
        );
    }

    private void registerFramedDoor(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_DOOR, cube);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_DOOR.value().asItem());
    }

    private void registerFramedIronDoor(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_IRON_DOOR, cube);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_IRON_DOOR.value().asItem());
    }

    private void registerFramedPressurePlate(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_PRESSURE_PLATE, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_WATERLOGGABLE_PRESSURE_PLATE, cube);

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_PRESSURE_PLATE, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
    }

    private void registerFramedStonePressurePlate(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_STONE_PRESSURE_PLATE, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_WATERLOGGABLE_STONE_PRESSURE_PLATE, cube);

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_STONE_PRESSURE_PLATE, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
    }

    private void registerFramedObsidianPressurePlate(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_OBSIDIAN_PRESSURE_PLATE, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_WATERLOGGABLE_OBSIDIAN_PRESSURE_PLATE, cube);

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_OBSIDIAN_PRESSURE_PLATE, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
    }

    private void registerFramedGoldPressurePlate(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_GOLD_PRESSURE_PLATE, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_WATERLOGGABLE_GOLD_PRESSURE_PLATE, cube);

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_GOLD_PRESSURE_PLATE, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
    }

    private void registerFramedIronPressurePlate(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_IRON_PRESSURE_PLATE, cube);
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_WATERLOGGABLE_IRON_PRESSURE_PLATE, cube);

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_IRON_PRESSURE_PLATE, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
    }

    private void registerFramedLever(BlockModelGenerators blockModels)
    {
        TextureSlot slotBase = TextureSlot.create("base");

        ResourceLocation lever = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_LEVER,
                ModelTemplates.create("lever", slotBase, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(slotBase, ClientUtils.DUMMY_TEXTURE)
                        .put(TextureSlot.PARTICLE, TEXTURE)
        );
        ResourceLocation leverOn = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_LEVER,
                ModelTemplates.create("lever_on", "_on", slotBase, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(slotBase, ClientUtils.DUMMY_TEXTURE)
                        .put(TextureSlot.PARTICLE, TEXTURE)
        );

        framedVariant(blockModels, FBContent.BLOCK_FRAMED_LEVER, gen ->
                gen.with(BlockModelGenerators.createBooleanModelDispatch(
                        LeverBlock.POWERED,
                        BlockModelGenerators.plainVariant(lever),
                        BlockModelGenerators.plainVariant(leverOn)
                ))
                .with(PropertyDispatch.modify(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
                        .generate((face, dir)->
                        {
                            int yRot = (dir.get2DDataValue() + (face != AttachFace.CEILING ? 2 : 0)) % 4;
                            return VariantMutator.X_ROT.withValue(rotByIdx(face.ordinal()))
                                    .then(VariantMutator.Y_ROT.withValue(rotByIdx(yRot)));
                        })
                )
        );

        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_LEVER.value().asItem());
    }

    private void registerFramedSign(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_SIGN, cube);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_SIGN.value().asItem());
    }

    private void registerFramedHangingSign(BlockModelGenerators blockModels)
    {
        ResourceLocation model = Utils.rl("block/framed_hanging_sign");
        ResourceLocation modelAttached = Utils.rl("block/framed_hanging_sign_attached");

        framedVariant(blockModels, FBContent.BLOCK_FRAMED_HANGING_SIGN, gen ->
                gen.with(BlockModelGenerators.createBooleanModelDispatch(
                        BlockStateProperties.ATTACHED,
                        BlockModelGenerators.plainVariant(modelAttached),
                        BlockModelGenerators.plainVariant(model)
                ))
                .with(PropertyDispatch.modify(BlockStateProperties.ROTATION_16).generate(FramedBlockModelProvider::rotationToVariant))
        );

        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_HANGING_SIGN.value().asItem());
    }

    private void registerFramedWallHangingSign(BlockModelGenerators blockModels)
    {
        framedVariant(
                blockModels,
                FBContent.BLOCK_FRAMED_WALL_HANGING_SIGN,
                BlockModelGenerators.plainVariant(Utils.rl("block/framed_wall_hanging_sign")),
                gen -> gen.with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT)
        );
    }

    private void registerFramedTorch(BlockModelGenerators blockModels)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_TORCH);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_TORCH.value());
    }

    private void registerFramedWallTorch(BlockModelGenerators blockModels)
    {
        framedVariant(
                blockModels,
                FBContent.BLOCK_FRAMED_WALL_TORCH,
                BlockModelGenerators.plainVariant(Utils.rl("block/framed_wall_torch")),
                gen -> gen.with(BlockModelGenerators.ROTATION_TORCH)
        );
    }

    private void registerFramedSoulTorch(BlockModelGenerators blockModels)
    {
        framedBlockFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_SOUL_TORCH,
                ModelTemplates.create("framedblocks:framed_torch", TextureSlot.TOP, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(TextureSlot.TOP, mcLocation("block/soul_torch"))
                        .put(TextureSlot.PARTICLE, Utils.rl("block/framed_soul_torch"))
        );
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_SOUL_TORCH.value());
    }

    private void registerFramedSoulWallTorch(BlockModelGenerators blockModels)
    {
        ResourceLocation wallTorch = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_SOUL_WALL_TORCH,
                ModelTemplates.create("framedblocks:framed_wall_torch", TextureSlot.TOP, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(TextureSlot.TOP, mcLocation("block/soul_torch"))
                        .put(TextureSlot.PARTICLE, Utils.rl("block/framed_soul_torch"))
        );
        framedVariant(
                blockModels,
                FBContent.BLOCK_FRAMED_SOUL_WALL_TORCH,
                BlockModelGenerators.plainVariant(wallTorch),
                gen -> gen.with(BlockModelGenerators.ROTATION_TORCH)
        );
    }

    private void registerFramedRedstoneTorch(BlockModelGenerators blockModels)
    {
        ResourceLocation torch = Utils.rl("block/framed_redstone_torch");
        ResourceLocation torchOff = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_REDSTONE_TORCH,
                ModelTemplates.create("framedblocks:framed_torch", "_off", TextureSlot.TOP, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(TextureSlot.TOP, mcLocation("block/redstone_torch_off"))
                        .put(TextureSlot.PARTICLE, Utils.rl("block/framed_redstone_torch_off"))
        );
        framedVariant(blockModels, FBContent.BLOCK_FRAMED_REDSTONE_TORCH, gen ->
                gen.with(BlockModelGenerators.createBooleanModelDispatch(
                        BlockStateProperties.LIT,
                        BlockModelGenerators.plainVariant(torch),
                        BlockModelGenerators.plainVariant(torchOff)
                ))
        );

        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_REDSTONE_TORCH.value());
    }

    private void registerFramedRedstoneWallTorch(BlockModelGenerators blockModels)
    {
        ResourceLocation wallTorch = Utils.rl("block/framed_redstone_wall_torch");
        ResourceLocation wallTorchOff = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_REDSTONE_WALL_TORCH,
                ModelTemplates.create("framedblocks:framed_wall_torch", "_off", TextureSlot.TOP, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(TextureSlot.TOP, mcLocation("block/redstone_torch_off"))
                        .put(TextureSlot.PARTICLE, Utils.rl("block/framed_redstone_torch_off"))
        );
        framedVariant(blockModels, FBContent.BLOCK_FRAMED_REDSTONE_WALL_TORCH, gen ->
                gen.with(BlockModelGenerators.createBooleanModelDispatch(
                        BlockStateProperties.LIT,
                        BlockModelGenerators.plainVariant(wallTorch),
                        BlockModelGenerators.plainVariant(wallTorchOff)
                ))
                .with(BlockModelGenerators.ROTATION_TORCH)
        );
    }

    private void registerFramedCornerStrip(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_CORNER_STRIP, cube);

        ResourceLocation cornerStripItem = ExtendedModelTemplateBuilder.builder()
                .parent(mcLocation("block/block"))
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, builder ->
                        builder.rotation(0F, 115F, 0F)
                                .translation(-1F, 4.2F, 0F)
                                .scale(.4F, .4F, .4F)
                )
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, builder ->
                        builder.rotation(0F, 135F, 0F)
                                .translation(0F, 4.2F, 1F)
                                .scale(.4F, .4F, .4F)
                )
                .build()
                .create(ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_CORNER_STRIP.value().asItem()), new TextureMapping(), blockModels.modelOutput);
        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_CORNER_STRIP, builder -> builder.itemBaseModel(cornerStripItem));
    }

    private void registerFramedChest(BlockModelGenerators blockModels)
    {
        ResourceLocation chest = Utils.rl("block/framed_chest");
        ResourceLocation chestLeft = Utils.rl("block/framed_chest_left");
        ResourceLocation chestRight = Utils.rl("block/framed_chest_right");

        framedVariant(blockModels, FBContent.BLOCK_FRAMED_CHEST, gen ->
                gen.with(PropertyDispatch.initial(BlockStateProperties.CHEST_TYPE)
                        .select(ChestType.SINGLE, BlockModelGenerators.plainVariant(chest))
                        .select(ChestType.LEFT, BlockModelGenerators.plainVariant(chestLeft))
                        .select(ChestType.RIGHT, BlockModelGenerators.plainVariant(chestRight))
                )
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
        );

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_CHEST);
    }

    private void registerFramedSecretStorage(BlockModelGenerators blockModels)
    {
        TextureSlot slotBarrelSide = TextureSlot.create("barrel");
        TextureSlot slotBarrelTop = TextureSlot.create("barrel_top");
        TextureSlot slotBarrelBottom = TextureSlot.create("barrel_bottom");

        ModelTemplate template = ExtendedModelTemplateBuilder.builder()
                .parent(mcLocation("block/block"))
                .requiredTextureSlot(slotBarrelSide)
                .requiredTextureSlot(slotBarrelTop)
                .requiredTextureSlot(slotBarrelBottom)
                .requiredTextureSlot(SLOT_FRAME)
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .renderType("cutout")
                .element(elem -> elem
                        .cube(slotBarrelSide)
                        .face(Direction.UP, face -> face.texture(slotBarrelTop))
                        .face(Direction.DOWN, face -> face.texture(slotBarrelBottom))
                )
                .element(elem -> elem.cube(SLOT_FRAME))
                .build();

        ResourceLocation block = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_SECRET_STORAGE,
                template,
                new TextureMapping()
                        .put(slotBarrelSide, mcLocation("block/barrel_side"))
                        .put(slotBarrelTop, mcLocation("block/barrel_top"))
                        .put(slotBarrelBottom, mcLocation("block/barrel_bottom"))
                        .put(SLOT_FRAME, TEXTURE)
                        .put(TextureSlot.PARTICLE, TEXTURE)
        );

        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SECRET_STORAGE, block);
    }

    private void registerFramedTank(BlockModelGenerators blockModels)
    {
        TextureSlot slotGlass = TextureSlot.create("glass");

        ModelTemplate template = ExtendedModelTemplateBuilder.builder()
                .parent(mcLocation("block/block"))
                .requiredTextureSlot(slotGlass)
                .requiredTextureSlot(SLOT_FRAME)
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .renderType("cutout")
                .element(elem -> elem.cube(SLOT_FRAME))
                .element(elem -> elem.cube(slotGlass))
                .build();

        ResourceLocation block = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_TANK,
                template,
                new TextureMapping()
                        .put(slotGlass, mcLocation("block/glass"))
                        .put(SLOT_FRAME, TEXTURE)
                        .put(TextureSlot.PARTICLE, TEXTURE)
        );

        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_TANK, block);
        ItemModel.Unbaked itemModel = nestedFramedBlockItemModel(FBContent.BLOCK_FRAMED_TANK)
                .tintProvider(FramedBlockItemTintProvider.INSTANCE_SINGLE)
                .build();
        blockModels.itemModelOutput.accept(
                FBContent.BLOCK_FRAMED_TANK.value().asItem(),
                new TankItemModel.Unbaked((FramedBlockItemModel.Unbaked) itemModel, TankItemRenderer.Unbaked.INSTANCE)
        );
    }

    private void registerFramedBarsBlock(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_BARS, cube);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_BARS.value().asItem());
    }

    private void registerFramedPaneBlock(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_PANE, cube);
        blockModels.registerSimpleItemModel(
                FBContent.BLOCK_FRAMED_PANE.value().asItem(),
                ModelTemplates.FLAT_ITEM.create(
                        FBContent.BLOCK_FRAMED_PANE.value().asItem(),
                        TextureMapping.layer0(TEXTURE),
                        blockModels.modelOutput
                )
        );
    }

    private void registerFramedFlowerPotBlock(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_FLOWER_POT, cube)
                .addAuxModel(FramedFlowerPotGeometry.HANGING_MODEL_KEY, singleVariant(FramedFlowerPotGeometry.HANGING_MODEL_LOCATION));
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_FLOWER_POT.value().asItem());

        ExtendedModelTemplateBuilder.builder()
                .parent(AmendmentsCompat.HANGING_MODEL_LOCATION)
                .customLoader(FallbackLoaderBuilder::new, builder ->
                        builder.addCondition(NeoForgeConditions.modLoaded(AmendmentsCompat.MOD_ID))
                                .setFallback(MissingBlockModel.LOCATION)
                )
                .build()
                .create(FramedFlowerPotGeometry.HANGING_MODEL_LOCATION, new TextureMapping(), blockModels.modelOutput);
    }

    private void registerFramedCollapsibleBlock(BlockModelGenerators blockModels)
    {
        ResourceLocation block = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_BLOCK, TEXTURE, mcLocation("block/oak_planks"), $ -> {});
        ResourceLocation altCube = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_BLOCK, TEXTURE_ALT, mcLocation("block/spruce_planks"), builder -> builder.suffix("_alt"));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_BLOCK, block)
                .addAuxModel(FramedCollapsibleBlockGeometry.ALT_BASE_MODEL_KEY, singleVariant(altCube));
    }

    private void registerFramedCollapsibleCopycatBlock(BlockModelGenerators blockModels)
    {
        ResourceLocation block = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_COPYCAT_BLOCK, TEXTURE, mcLocation("block/copper_block"), $ -> {});
        ResourceLocation altCube = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_COPYCAT_BLOCK, TEXTURE_ALT, mcLocation("block/copper_block"), builder -> builder.suffix("_alt"));
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_COPYCAT_BLOCK, block)
                .addAuxModel(FramedCollapsibleCopycatBlockGeometry.ALT_BASE_MODEL_KEY, singleVariant(altCube));
    }

    private void registerFramedBouncyBlock(BlockModelGenerators blockModels)
    {
        ResourceLocation block = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_BOUNCY_CUBE, TEXTURE, mcLocation("block/slime_block"), $ -> {});
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_BOUNCY_CUBE, block)
                .addAuxModel(FramedMarkedCubeGeometry.FRAME_KEY, singleVariant(FramedMarkedCubeGeometry.SLIME_FRAME_LOCATION));

        makeOverlayCube(blockModels, FramedMarkedCubeGeometry.SLIME_FRAME_LOCATION, FramedMarkedCubeGeometry.SLIME_FRAME_LOCATION);
    }

    private void registerFramedRedstoneBlock(BlockModelGenerators blockModels)
    {
        ResourceLocation block = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_REDSTONE_BLOCK, TEXTURE, mcLocation("block/redstone_block"), $ -> {});
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_REDSTONE_BLOCK, block)
                .addAuxModel(FramedMarkedCubeGeometry.FRAME_KEY, singleVariant(FramedMarkedCubeGeometry.REDSTONE_FRAME_LOCATION));

        makeOverlayCube(blockModels, FramedMarkedCubeGeometry.REDSTONE_FRAME_LOCATION, FramedMarkedCubeGeometry.REDSTONE_FRAME_LOCATION);
    }

    private void registerFramedTarget(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_TARGET, cube, builder -> builder.tintProvider(FramedTargetItemTintProvider.INSTANCE))
                .addAuxModel(FramedTargetGeometry.OVERLAY_KEY, singleVariant(FramedTargetGeometry.OVERLAY_LOCATION));

        makeOverlayCube(blockModels, FramedTargetGeometry.OVERLAY_LOCATION, FramedTargetGeometry.OVERLAY_LOCATION, builder ->
                builder.element(elem -> elem
                        .cube(TextureSlot.ALL)
                        .faces((dir, face) -> face.tintindex(FramedTargetGeometry.OVERLAY_TINT_IDX))
                )
        );
    }

    private void registerFramedItemFrame(BlockModelGenerators blockModels)
    {
        TextureSlot slotWood = TextureSlot.create("wood");
        ModelTemplate templateItemFrame = ModelTemplates.create(
                "framedblocks:template_framed_item_frame",
                TextureSlot.FRONT, TextureSlot.BACK, slotWood, TextureSlot.PARTICLE
        );
        ModelTemplate templateMapItemFram = ModelTemplates.create(
                "framedblocks:template_framed_item_frame_map",
                "_map",
                TextureSlot.FRONT, TextureSlot.BACK, slotWood, TextureSlot.PARTICLE
        );

        ResourceLocation normalFrame = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_ITEM_FRAME,
                templateItemFrame,
                new TextureMapping()
                        .put(TextureSlot.FRONT, mcLocation("block/item_frame"))
                        .put(TextureSlot.BACK, ClientUtils.DUMMY_TEXTURE)
                        .put(slotWood, ClientUtils.DUMMY_TEXTURE)
                        .put(TextureSlot.PARTICLE, TEXTURE)
        );
        ResourceLocation normalMapFrame = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_ITEM_FRAME,
                templateMapItemFram,
                new TextureMapping()
                        .put(TextureSlot.FRONT, mcLocation("block/item_frame"))
                        .put(TextureSlot.BACK, ClientUtils.DUMMY_TEXTURE)
                        .put(slotWood, ClientUtils.DUMMY_TEXTURE)
                        .put(TextureSlot.PARTICLE, TEXTURE)
        );
        ResourceLocation glowFrame = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_GLOWING_ITEM_FRAME,
                templateItemFrame,
                new TextureMapping()
                        .put(TextureSlot.FRONT, mcLocation("block/glow_item_frame"))
                        .put(TextureSlot.BACK, ClientUtils.DUMMY_TEXTURE)
                        .put(slotWood, ClientUtils.DUMMY_TEXTURE)
                        .put(TextureSlot.PARTICLE, TEXTURE)
        );
        ResourceLocation glowMapFrame = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_GLOWING_ITEM_FRAME,
                templateMapItemFram,
                new TextureMapping()
                        .put(TextureSlot.FRONT, mcLocation("block/glow_item_frame"))
                        .put(TextureSlot.BACK, ClientUtils.DUMMY_TEXTURE)
                        .put(slotWood, ClientUtils.DUMMY_TEXTURE)
                        .put(TextureSlot.PARTICLE, TEXTURE)
        );

        framedVariant(blockModels, FBContent.BLOCK_FRAMED_ITEM_FRAME, gen ->
                gen.with(BlockModelGenerators.createBooleanModelDispatch(
                        PropertyHolder.MAP_FRAME,
                        BlockModelGenerators.plainVariant(normalMapFrame),
                        BlockModelGenerators.plainVariant(normalFrame)
                ))
                .with(ROTATION_FACING_ALT)
        );
        framedVariant(blockModels, FBContent.BLOCK_FRAMED_GLOWING_ITEM_FRAME, gen ->
                gen.with(BlockModelGenerators.createBooleanModelDispatch(
                        PropertyHolder.MAP_FRAME,
                        BlockModelGenerators.plainVariant(glowMapFrame),
                        BlockModelGenerators.plainVariant(glowFrame)
                ))
                .with(ROTATION_FACING_ALT)
        );

        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_ITEM_FRAME.value().asItem());
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_GLOWING_ITEM_FRAME.value().asItem());
    }

    private static MultiVariant railVariant(
            RailShape shape,
            ResourceLocation normalRail,
            ResourceLocation ascendingRail,
            @Nullable ResourceLocation curvedRail
    )
    {
        Direction dir = FramedFancyRailGeometry.getDirectionFromRailShape(shape);
        ResourceLocation model;
        if (shape.isSlope())
        {
            model = ascendingRail;
            dir = dir.getOpposite();
        }
        else if (shape == RailShape.NORTH_SOUTH || shape == RailShape.EAST_WEST)
        {
            model = normalRail;
        }
        else
        {
            model = Objects.requireNonNull(curvedRail, "Encountered unsupported curve shape on " + normalRail);
            if (shape == RailShape.NORTH_WEST || shape == RailShape.SOUTH_EAST)
            {
                dir = dir.getClockWise();
            }
            else
            {
                dir = dir.getOpposite();
            }
        }

        return BlockModelGenerators.plainVariant(model).with(horDirToVariant(dir));
    }

    private void registerFramedFancyRail(BlockModelGenerators blockModels)
    {
        ResourceLocation normalRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_RAIL.value());
        ResourceLocation ascendingRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_RAIL.value(), "_ascending");
        ResourceLocation curvedRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_RAIL.value(), "_curved");

        framedVariant(blockModels, FBContent.BLOCK_FRAMED_FANCY_RAIL, gen ->
                gen.with(PropertyDispatch.initial(BlockStateProperties.RAIL_SHAPE)
                        .generate(shape -> railVariant(shape, normalRail, ascendingRail, curvedRail)))
        );

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_FANCY_RAIL, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
    }

    private void registerFramedFancyPoweredRail(BlockModelGenerators blockModels)
    {
        ResourceLocation normalRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_POWERED_RAIL.value());
        ResourceLocation normalRailOn = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_FANCY_POWERED_RAIL,
                ModelTemplates.create("framedblocks:framed_fancy_powered_rail", "_on", TextureSlot.TEXTURE, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(TextureSlot.TEXTURE, mcLocation("block/powered_rail_on"))
                        .put(TextureSlot.PARTICLE, mcLocation("block/powered_rail_on"))
        );
        ResourceLocation ascendingRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_POWERED_RAIL.value(), "_ascending");
        ResourceLocation ascendingRailOn = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_FANCY_POWERED_RAIL,
                ModelTemplates.create("framedblocks:framed_fancy_powered_rail_ascending", "_ascending_on", TextureSlot.TEXTURE, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(TextureSlot.TEXTURE, mcLocation("block/powered_rail_on"))
                        .put(TextureSlot.PARTICLE, mcLocation("block/powered_rail_on"))
        );

        framedVariant(blockModels, FBContent.BLOCK_FRAMED_FANCY_POWERED_RAIL, gen ->
                gen.with(PropertyDispatch.initial(BlockStateProperties.RAIL_SHAPE_STRAIGHT, BlockStateProperties.POWERED)
                        .generate((shape, powered) ->
                                railVariant(shape, powered ? normalRailOn : normalRail, powered ? ascendingRailOn : ascendingRail, null)
                        )
                )
        );

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_FANCY_POWERED_RAIL, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
    }

    private void registerFramedFancyDetectorRail(BlockModelGenerators blockModels)
    {
        ResourceLocation normalRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL.value());
        ResourceLocation normalRailOn = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL,
                ModelTemplates.create("framedblocks:framed_fancy_detector_rail", "_on", TextureSlot.TEXTURE, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(TextureSlot.TEXTURE, mcLocation("block/detector_rail_on"))
                        .put(TextureSlot.PARTICLE, mcLocation("block/detector_rail_on"))
        );
        ResourceLocation ascendingRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL.value(), "_ascending");
        ResourceLocation ascendingRailOn = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL,
                ModelTemplates.create("framedblocks:framed_fancy_detector_rail_ascending", "_ascending_on", TextureSlot.TEXTURE, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(TextureSlot.TEXTURE, mcLocation("block/detector_rail_on"))
                        .put(TextureSlot.PARTICLE, mcLocation("block/detector_rail_on"))
        );

        framedVariant(blockModels, FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL, gen ->
                gen.with(PropertyDispatch.initial(BlockStateProperties.RAIL_SHAPE_STRAIGHT, BlockStateProperties.POWERED)
                        .generate((shape, powered) ->
                                railVariant(shape, powered ? normalRailOn : normalRail, powered ? ascendingRailOn : ascendingRail, null)
                        )
                )
        );

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
    }

    private void registerFramedFancyActivatorRail(BlockModelGenerators blockModels)
    {
        ResourceLocation normalRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL.value());
        ResourceLocation normalRailOn = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL,
                ModelTemplates.create("framedblocks:framed_fancy_activator_rail", "_on", TextureSlot.TEXTURE, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(TextureSlot.TEXTURE, mcLocation("block/activator_rail_on"))
                        .put(TextureSlot.PARTICLE, mcLocation("block/activator_rail_on"))
        );
        ResourceLocation ascendingRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL.value(), "_ascending");
        ResourceLocation ascendingRailOn = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL,
                ModelTemplates.create("framedblocks:framed_fancy_activator_rail_ascending", "_ascending_on", TextureSlot.TEXTURE, TextureSlot.PARTICLE),
                new TextureMapping()
                        .put(TextureSlot.TEXTURE, mcLocation("block/activator_rail_on"))
                        .put(TextureSlot.PARTICLE, mcLocation("block/activator_rail_on"))
        );

        framedVariant(blockModels, FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL, gen ->
                gen.with(PropertyDispatch.initial(BlockStateProperties.RAIL_SHAPE_STRAIGHT, BlockStateProperties.POWERED)
                        .generate((shape, powered) ->
                                railVariant(shape, powered ? normalRailOn : normalRail, powered ? ascendingRailOn : ascendingRail, null)
                        )
                )
        );

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL, builder -> builder.itemBaseModel(THIN_BLOCK_LOC));
    }

    private void registerFramedOneWayWindow(BlockModelGenerators blockModels)
    {
        ResourceLocation model = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_ONE_WAY_WINDOW, TEXTURE, mcLocation("block/moss_block"), $ -> {});
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ONE_WAY_WINDOW, model);
    }

    private void registerFramedBookshelf(BlockModelGenerators blockModels)
    {
        simpleFramedBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_BOOKSHELF, Utils.rl("block/framed_bookshelf"));
    }

    private void registerFramedChiseledBookshelf(BlockModelGenerators blockModels)
    {
        String[] bookSlots = new String[] { "top_left", "top_mid", "top_right", "bottom_left", "bottom_mid", "bottom_right" };
        ModelTemplate[] bookSlotTemplates = Arrays.stream(bookSlots)
                .map(slot -> ModelTemplates.create("framedblocks:template_framed_chiseled_bookshelf_slot_" + slot, TextureSlot.TEXTURE))
                .toArray(ModelTemplate[]::new);

        String baseName = "block/framed_chiseled_bookshelf";
        ResourceLocation[] modelsEmpty = new ResourceLocation[6];
        ResourceLocation[] modelsFilled = new ResourceLocation[6];
        for (int i = 0; i < ChiseledBookShelfBlockEntity.MAX_BOOKS_IN_STORAGE; i++)
        {
            String slot = bookSlots[i];

            modelsEmpty[i] = bookSlotTemplates[i].create(
                    Utils.rl(baseName + "_empty_slot_" + slot),
                    TextureMapping.defaultTexture(mcLocation("block/chiseled_bookshelf_empty")),
                    blockModels.modelOutput
            );
            modelsFilled[i] = bookSlotTemplates[i].create(
                    Utils.rl(baseName + "_occupied_slot_" + slot),
                    TextureMapping.defaultTexture(mcLocation("block/chiseled_bookshelf_occupied")),
                    blockModels.modelOutput
            );
        }
        ResourceLocation baseModel = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_FRAMED_CHISELED_BOOKSHELF,
                ModelTemplates.create("block", TextureSlot.PARTICLE),
                TextureMapping.particle(TEXTURE)
        );

        framedMultiPart(blockModels, FBContent.BLOCK_FRAMED_CHISELED_BOOKSHELF, gen ->
        {
            gen.with(BlockModelGenerators.plainVariant(baseModel));
            for (Direction dir : Direction.Plane.HORIZONTAL)
            {
                for (int i = 0; i < ChiseledBookShelfBlockEntity.MAX_BOOKS_IN_STORAGE; i++)
                {
                    BooleanProperty prop = ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(i);
                    ConditionBuilder facingCondition = BlockModelGenerators.condition().term(BlockStateProperties.HORIZONTAL_FACING, dir);

                    gen.with(
                            and(facingCondition, BlockModelGenerators.condition().term(prop, false)),
                            BlockModelGenerators.plainVariant(modelsEmpty[i]).with(horDirToVariant(dir.getOpposite()))
                    );
                    gen.with(
                            and(facingCondition, BlockModelGenerators.condition().term(prop, true)),
                            BlockModelGenerators.plainVariant(modelsFilled[i]).with(horDirToVariant(dir.getOpposite()))
                    );
                }
            }
            return gen;
        });

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_CHISELED_BOOKSHELF);
    }

    private void registerFramedChain(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleFramedBlock(blockModels, FBContent.BLOCK_FRAMED_CHAIN, cube);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_CHAIN.value().asItem());
    }

    private void registerFramedLantern(BlockModelGenerators blockModels)
    {
        ResourceLocation standing = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_LANTERN.value());
        ResourceLocation hanging = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_LANTERN.value(), "_hanging");
        ResourceLocation chainStanding = modLocation("block/framed_lantern_chain_standing");
        ResourceLocation chainHanging = modLocation("block/framed_lantern_chain_hanging");

        ConditionBuilder standingCondition = BlockModelGenerators.condition().term(BlockStateProperties.HANGING, false);
        ConditionBuilder hangingCondition = BlockModelGenerators.condition().term(BlockStateProperties.HANGING, true);
        ConditionBuilder chainCondition = BlockModelGenerators.condition().term(PropertyHolder.CHAIN_TYPE, ChainType.METAL);
        framedMultiPart(blockModels, FBContent.BLOCK_FRAMED_LANTERN, gen -> gen
                .with(standingCondition, BlockModelGenerators.plainVariant(standing))
                .with(hangingCondition, BlockModelGenerators.plainVariant(hanging))
                .with(and(chainCondition, standingCondition), BlockModelGenerators.plainVariant(chainStanding))
                .with(and(chainCondition, hangingCondition), BlockModelGenerators.plainVariant(chainHanging))
        );

        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_LANTERN.value().asItem());
    }

    private void registerFramedSoulLantern(BlockModelGenerators blockModels)
    {
        ResourceLocation standing = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_SOUL_LANTERN.value());
        ResourceLocation hanging = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_SOUL_LANTERN.value(), "_hanging");
        ResourceLocation chainStanding = modLocation("block/framed_lantern_chain_standing");
        ResourceLocation chainHanging = modLocation("block/framed_lantern_chain_hanging");

        ConditionBuilder standingCondition = BlockModelGenerators.condition().term(BlockStateProperties.HANGING, false);
        ConditionBuilder hangingCondition = BlockModelGenerators.condition().term(BlockStateProperties.HANGING, true);
        ConditionBuilder chainCondition = BlockModelGenerators.condition().term(PropertyHolder.CHAIN_TYPE, ChainType.METAL);
        framedMultiPart(blockModels, FBContent.BLOCK_FRAMED_SOUL_LANTERN, gen -> gen
                .with(standingCondition, BlockModelGenerators.plainVariant(standing))
                .with(hangingCondition, BlockModelGenerators.plainVariant(hanging))
                .with(and(chainCondition, standingCondition), BlockModelGenerators.plainVariant(chainStanding))
                .with(and(chainCondition, hangingCondition), BlockModelGenerators.plainVariant(chainHanging))
        );

        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_SOUL_LANTERN.value().asItem());
    }



    private void registerFramingSaw(BlockModelGenerators blockModels)
    {
        ResourceLocation model = Utils.rl("block/framing_saw");
        ResourceLocation modelEncoder = Utils.rl("block/framing_saw_encoder");
        variant(blockModels, FBContent.BLOCK_FRAMING_SAW, gen ->
                gen.with(BlockModelGenerators.createBooleanModelDispatch(
                        PropertyHolder.SAW_ENCODER,
                        BlockModelGenerators.plainVariant(modelEncoder),
                        BlockModelGenerators.plainVariant(model)
                ))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT)
        );
        blockModels.registerSimpleItemModel(FBContent.BLOCK_FRAMING_SAW.value(), model);
    }

    private void registerPoweredFramingSaw(BlockModelGenerators blockModels)
    {
        TextureSlot slotSaw = TextureSlot.create("saw");
        ModelTemplate templatePoweredSaw = ModelTemplates.create("framedblocks:powered_framing_saw", slotSaw);

        ResourceLocation modelInactive = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_POWERED_FRAMING_SAW,
                templatePoweredSaw.extend().suffix("_inactive").build(),
                TextureMapping.singleSlot(slotSaw, FramedSpriteSourceProvider.SPRITE_SAW_STILL)
        );
        ResourceLocation modelActive = blockModelFromTemplate(
                blockModels,
                FBContent.BLOCK_POWERED_FRAMING_SAW,
                templatePoweredSaw.extend().suffix("_active").build(),
                TextureMapping.singleSlot(slotSaw, mcLocation("block/stonecutter_saw"))
        );

        variant(blockModels, FBContent.BLOCK_POWERED_FRAMING_SAW, gen ->
                gen.with(BlockModelGenerators.createBooleanModelDispatch(
                        PropertyHolder.ACTIVE,
                        BlockModelGenerators.plainVariant(modelActive),
                        BlockModelGenerators.plainVariant(modelInactive)
                ))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT)
        );
        blockModels.registerSimpleItemModel(FBContent.BLOCK_POWERED_FRAMING_SAW.value(), modelActive);
    }
}
