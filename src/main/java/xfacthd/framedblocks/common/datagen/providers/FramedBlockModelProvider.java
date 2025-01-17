package xfacthd.framedblocks.common.datagen.providers;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.Condition;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.datagen.models.AbstractFramedBlockModelProvider;
import xfacthd.framedblocks.api.model.item.FramedBlockItemTintProvider;
import xfacthd.framedblocks.api.util.ClientUtils;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.client.itemmodel.FramedBlockItemModel;
import xfacthd.framedblocks.client.itemmodel.TankItemModel;
import xfacthd.framedblocks.client.itemmodel.tintprovider.FramedTargetItemTintProvider;
import xfacthd.framedblocks.client.model.cube.FramedMarkedCubeGeometry;
import xfacthd.framedblocks.client.model.cube.FramedTargetGeometry;
import xfacthd.framedblocks.client.model.rail.FramedFancyRailGeometry;
import xfacthd.framedblocks.client.render.item.TankItemRenderer;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.PropertyHolder;

import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings({ "MethodMayBeStatic", "SameParameterValue" })
public final class FramedBlockModelProvider extends AbstractFramedBlockModelProvider
{
    private static final ResourceLocation TEXTURE = Utils.rl("block/framed_block");
    private static final ResourceLocation TEXTURE_ALT = Utils.rl("block/framed_block_alt");
    private static final ResourceLocation TEXTURE_UNDERLAY = ResourceLocation.withDefaultNamespace("block/stripped_dark_oak_log");
    private static final ModelTemplate TEMPLATE_CUTOUT_CUBE = ModelTemplates.CUBE_ALL.extend().renderType("cutout").build();

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

        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_HALF_SLOPE, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_HALF_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DIVIDED_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_HALF_SLOPE, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_DOUBLE_HALF_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CORNER_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INNER_CORNER_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PRISM_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INNER_PRISM_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_PRISM_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_THREEWAY_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INNER_THREEWAY_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_THREEWAY_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_DOUBLE_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CORNER_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INNER_CORNER_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_CORNER_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_INNER_CORNER_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_DOUBLE_CORNER_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_DOUBLE_INNER_CORNER_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_CORNER_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_INNER_CORNER_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_THREEWAY_CORNER_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INNER_THREEWAY_CORNER_SLOPE_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ADJ_DOUBLE_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ADJ_DOUBLE_COPYCAT_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DIVIDED_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLAB_EDGE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLAB_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ADJ_DOUBLE_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ADJ_DOUBLE_COPYCAT_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DIVIDED_PANEL_HOR, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DIVIDED_PANEL_VERT, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CORNER_PILLAR, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_HALF_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DIVIDED_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_HALF_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPED_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLICED_STAIRS_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLICED_STAIRS_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_DOUBLE_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_HALF_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_DIVIDED_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_DOUBLE_HALF_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_SLICED_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_VERTICAL_SLOPED_STAIRS, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_THREEWAY_CORNER_PILLAR, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_THREEWAY_CORNER_PILLAR, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FENCE_GATE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_TRAP_DOOR, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_IRON_TRAP_DOOR, ironCube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LADDER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STONE_BUTTON, stoneCube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LARGE_BUTTON, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LARGE_STONE_BUTTON, stoneCube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_WALL_SIGN, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_WALL_BOARD, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CORNER_STRIP, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LATTICE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_THICK_LATTICE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_HORIZONTAL_PANE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_RAIL_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_POWERED_RAIL_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DETECTOR_RAIL_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ACTIVATOR_RAIL_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FANCY_RAIL_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FANCY_POWERED_RAIL_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL_SLOPE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PILLAR, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_HALF_PILLAR, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_POST, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PRISM, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_INNER_PRISM, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_INNER_DOUBLE_PRISM, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPED_PRISM, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_INNER_SLOPED_PRISM, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_INNER_DOUBLE_SLOPED_PRISM, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPE_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_SLOPE_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_COMPOUND_SLOPE_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_SLOPE_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_SLOPE_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ELEVATED_DOUBLE_SLOPE_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_SLOPE_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_SLOPE_SLAB_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_INNER_SLOPE_SLAB_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_ELEVATED_SLOPE_SLAB_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_ELEVATED_INNER_SLOPE_SLAB_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_DOUBLE_SLOPE_SLAB_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_INVERSE_DOUBLE_SLOPE_SLAB_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_ELEVATED_DOUBLE_SLOPE_SLAB_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_ELEVATED_INNER_DOUBLE_SLOPE_SLAB_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_STACKED_SLOPE_SLAB_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_STACKED_INNER_SLOPE_SLAB_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SLOPE_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_SLOPE_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_COMPOUND_SLOPE_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_DOUBLE_SLOPE_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_SLOPE_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_DOUBLE_SLOPE_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_SLOPE_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_SLOPE_PANEL_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_INNER_SLOPE_PANEL_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_EXTENDED_SLOPE_PANEL_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_EXTENDED_INNER_SLOPE_PANEL_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_DOUBLE_SLOPE_PANEL_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_INVERSE_DOUBLE_SLOPE_PANEL_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_EXTENDED_DOUBLE_SLOPE_PANEL_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_EXTENDED_INNER_DOUBLE_SLOPE_PANEL_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_STACKED_SLOPE_PANEL_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_FLAT_STACKED_INNER_SLOPE_PANEL_CORNER, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SMALL_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_SMALL_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LARGE_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_LARGE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SMALL_INNER_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_SMALL_INNER_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LARGE_INNER_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_LARGE_INNER_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SMALL_DOUBLE_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_SMALL_DOUBLE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_LARGE_DOUBLE_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_LARGE_DOUBLE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_INNER_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_INNER_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_DOUBLE_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_DOUBLE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_INNER_DOUBLE_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_EXTENDED_INNER_DOUBLE_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_STACKED_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_STACKED_INNER_CORNER_SLOPE_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_STACKED_INNER_CORNER_SLOPE_PANEL_WALL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PYRAMID, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_PYRAMID_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_GATE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_IRON_GATE, ironCube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_MINI_CUBE, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CENTERED_SLAB, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CENTERED_PANEL, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_MASONRY_CORNER_SEGMENT, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_MASONRY_CORNER, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_CUBE_SEGMENT, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_CUBE, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_SLAB_SEGMENT, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_SLAB, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_PANEL_SEGMENT, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_CHECKERED_PANEL, cube);
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_TUBE, cube);

        registerFramedCube(blockModels, cube);
        //registerFramedSlab(blockModels, cube);
        //registerFramedStairs(blockModels, cube);
        //registerFramedWall(blockModels, cube);
        registerFramedFence(blockModels, cube);
        registerFramedDoor(blockModels, cube);
        registerFramedIronDoor(blockModels, ironCube);
        //registerFramedTrapDoor(blockModels, cube);
        registerFramedPressurePlate(blockModels, cube);
        registerFramedStonePressurePlate(blockModels, stoneCube);
        registerFramedObsidianPressurePlate(blockModels, obsidianCube);
        registerFramedGoldPressurePlate(blockModels, goldCube);
        registerFramedIronPressurePlate(blockModels, ironCube);
        registerFramedButton(blockModels, cube);
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
        registerFramedFloorBoard(blockModels, cube);
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
        registerFramedGlowingCube(blockModels);
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

        multiPart(blockModels, FBContent.BLOCK_FRAMED_CUBE)
                .with(
                        Condition.condition().term(PropertyHolder.SOLID_BG, true),
                        Variant.variant().with(VariantProperties.MODEL, solidUnderlay)
                )
                .with(
                        Condition.condition().term(PropertyHolder.ALT, false),
                        Variant.variant().with(VariantProperties.MODEL, cube)
                )
                .with(
                        Condition.condition().term(PropertyHolder.ALT, true),
                        Variant.variant().with(VariantProperties.MODEL, altCube)
                )
                .with(
                        Condition.condition().term(PropertyHolder.REINFORCED, true),
                        Variant.variant().with(VariantProperties.MODEL, reinforcement)
                );

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_CUBE);
    }

    private void registerFramedFence(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        multiPart(blockModels, FBContent.BLOCK_FRAMED_FENCE).with(Variant.variant().with(VariantProperties.MODEL, cube));

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
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_DOOR, cube);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_DOOR.value().asItem());
    }

    private void registerFramedIronDoor(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_IRON_DOOR, cube);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_IRON_DOOR.value().asItem());
    }

    private void registerFramedPressurePlate(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_PRESSURE_PLATE, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_WATERLOGGABLE_PRESSURE_PLATE, cube);

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_PRESSURE_PLATE);
    }

    private void registerFramedStonePressurePlate(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_STONE_PRESSURE_PLATE, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_WATERLOGGABLE_STONE_PRESSURE_PLATE, cube);

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_STONE_PRESSURE_PLATE);
    }

    private void registerFramedObsidianPressurePlate(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_OBSIDIAN_PRESSURE_PLATE, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_WATERLOGGABLE_OBSIDIAN_PRESSURE_PLATE, cube);

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_OBSIDIAN_PRESSURE_PLATE);
    }

    private void registerFramedGoldPressurePlate(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_GOLD_PRESSURE_PLATE, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_WATERLOGGABLE_GOLD_PRESSURE_PLATE, cube);

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_GOLD_PRESSURE_PLATE);
    }

    private void registerFramedIronPressurePlate(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_IRON_PRESSURE_PLATE, cube);
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_WATERLOGGABLE_IRON_PRESSURE_PLATE, cube);

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_IRON_PRESSURE_PLATE);
    }

    private void registerFramedButton(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_BUTTON, cube);

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_BUTTON);
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

        variant(blockModels, FBContent.BLOCK_FRAMED_LEVER)
                .with(BlockModelGenerators.createBooleanModelDispatch(LeverBlock.POWERED, lever, leverOn))
                .with(PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
                        .generate((face, dir)->
                        {
                            int yRot = (dir.get2DDataValue() + (face != AttachFace.CEILING ? 2 : 0)) % 4;
                            return Variant.variant()
                                    .with(VariantProperties.X_ROT, rotByIdx(face.ordinal()))
                                    .with(VariantProperties.Y_ROT, rotByIdx(yRot));
                        })
                );

        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_LEVER.value().asItem());
    }

    private void registerFramedSign(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_SIGN, cube);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_SIGN.value().asItem());
    }

    private void registerFramedHangingSign(BlockModelGenerators blockModels)
    {
        ResourceLocation model = Utils.rl("block/framed_hanging_sign");
        ResourceLocation modelAttached = Utils.rl("block/framed_hanging_sign_attached");

        variant(blockModels, FBContent.BLOCK_FRAMED_HANGING_SIGN)
                .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.ATTACHED, modelAttached, model))
                .with(PropertyDispatch.property(BlockStateProperties.ROTATION_16).generate(FramedBlockModelProvider::rotationToVariant));

        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_HANGING_SIGN.value().asItem());
    }

    private void registerFramedWallHangingSign(BlockModelGenerators blockModels)
    {
        variant(blockModels, FBContent.BLOCK_FRAMED_WALL_HANGING_SIGN, Variant.variant().with(VariantProperties.MODEL, Utils.rl("block/framed_wall_hanging_sign")))
                .with(BlockModelGenerators.createHorizontalFacingDispatchAlt());
    }

    private void registerFramedTorch(BlockModelGenerators blockModels)
    {
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_TORCH);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_TORCH.value());
    }

    private void registerFramedWallTorch(BlockModelGenerators blockModels)
    {
        variant(blockModels, FBContent.BLOCK_FRAMED_WALL_TORCH, Variant.variant().with(VariantProperties.MODEL, Utils.rl("block/framed_wall_torch")))
                .with(BlockModelGenerators.createTorchHorizontalDispatch());
    }

    private void registerFramedSoulTorch(BlockModelGenerators blockModels)
    {
        blockFromTemplate(
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
        variant(blockModels, FBContent.BLOCK_FRAMED_SOUL_WALL_TORCH, Variant.variant().with(VariantProperties.MODEL, wallTorch))
                .with(BlockModelGenerators.createTorchHorizontalDispatch());
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
        variant(blockModels, FBContent.BLOCK_FRAMED_REDSTONE_TORCH).with(BlockModelGenerators.createBooleanModelDispatch(
                BlockStateProperties.LIT, torch, torchOff
        ));

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
        variant(blockModels, FBContent.BLOCK_FRAMED_REDSTONE_WALL_TORCH)
                .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, wallTorch, wallTorchOff))
                .with(BlockModelGenerators.createTorchHorizontalDispatch());
    }

    private void registerFramedFloorBoard(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_FLOOR, cube);
        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_FLOOR);
    }

    private void registerFramedChest(BlockModelGenerators blockModels)
    {
        ResourceLocation chest = Utils.rl("block/framed_chest");
        ResourceLocation chestLeft = Utils.rl("block/framed_chest_left");
        ResourceLocation chestRight = Utils.rl("block/framed_chest_right");

        variant(blockModels, FBContent.BLOCK_FRAMED_CHEST)
                .with(BlockModelGenerators.createHorizontalFacingDispatch())
                .with(PropertyDispatch.property(BlockStateProperties.CHEST_TYPE)
                        .select(ChestType.SINGLE, Variant.variant().with(VariantProperties.MODEL, chest))
                        .select(ChestType.LEFT, Variant.variant().with(VariantProperties.MODEL, chestLeft))
                        .select(ChestType.RIGHT, Variant.variant().with(VariantProperties.MODEL, chestRight))
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

        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_SECRET_STORAGE, block);
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

        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_TANK, block);
        blockModels.itemModelOutput.accept(
                FBContent.BLOCK_FRAMED_TANK.value().asItem(),
                new TankItemModel.Unbaked(
                        new FramedBlockItemModel.Unbaked(
                                FBContent.BLOCK_FRAMED_TANK.value(),
                                FramedBlockItemTintProvider.INSTANCE_SINGLE
                        ),
                        TankItemRenderer.Unbaked.INSTANCE
                )
        );
    }

    private void registerFramedBarsBlock(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        multiPart(blockModels, FBContent.BLOCK_FRAMED_BARS).with(Variant.variant().with(VariantProperties.MODEL, cube));
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_BARS.value().asItem());
    }

    private void registerFramedPaneBlock(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        multiPart(blockModels, FBContent.BLOCK_FRAMED_PANE).with(Variant.variant().with(VariantProperties.MODEL, cube));
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
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_FLOWER_POT, cube);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_FLOWER_POT.value().asItem());
    }

    private void registerFramedCollapsibleBlock(BlockModelGenerators blockModels)
    {
        ResourceLocation block = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_BLOCK, TEXTURE, mcLocation("block/oak_planks"), $ -> {});
        makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_BLOCK, TEXTURE_ALT, mcLocation("block/spruce_planks"), builder -> builder.suffix("_alt"));
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_BLOCK, block);
    }

    private void registerFramedCollapsibleCopycatBlock(BlockModelGenerators blockModels)
    {
        ResourceLocation block = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_COPYCAT_BLOCK, TEXTURE, mcLocation("block/copper_block"), $ -> {});
        makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_COPYCAT_BLOCK, TEXTURE_ALT, mcLocation("block/copper_block"), builder -> builder.suffix("_alt"));
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_COLLAPSIBLE_COPYCAT_BLOCK, block);
    }

    private void registerFramedBouncyBlock(BlockModelGenerators blockModels)
    {
        ResourceLocation block = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_BOUNCY_CUBE, TEXTURE, mcLocation("block/slime_block"), $ -> {});
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_BOUNCY_CUBE, block);

        makeOverlayCube(blockModels, FramedMarkedCubeGeometry.SLIME_FRAME_LOCATION, Utils.rl("block/slime_frame"));
    }

    private void registerFramedRedstoneBlock(BlockModelGenerators blockModels)
    {
        ResourceLocation block = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_REDSTONE_BLOCK, TEXTURE, mcLocation("block/redstone_block"), $ -> {});
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_REDSTONE_BLOCK, block);

        makeOverlayCube(blockModels, FramedMarkedCubeGeometry.REDSTONE_FRAME_LOCATION, Utils.rl("block/redstone_frame"));
    }

    private void registerFramedGlowingCube(BlockModelGenerators blockModels)
    {
        ResourceLocation block = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_GLOWING_CUBE, TEXTURE, ClientUtils.DUMMY_TEXTURE, builder ->
        {
            builder.ambientOcclusion(false);
            for (int i = 0; i < builder.getElementCount(); i++)
            {
                builder.element(i, elem -> elem.emissivity(15, 15).shade(false));
            }
        });

        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_GLOWING_CUBE, block);
    }

    private void registerFramedTarget(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_TARGET, cube, FramedTargetItemTintProvider.INSTANCE);

        makeOverlayCube(blockModels, Utils.rl("block/target_overlay"), Utils.rl("block/target_overlay"), builder ->
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

        variant(blockModels, FBContent.BLOCK_FRAMED_ITEM_FRAME)
                .with(BlockModelGenerators.createBooleanModelDispatch(PropertyHolder.MAP_FRAME, normalMapFrame, normalFrame))
                .with(createFacingDispatchAlt());
        variant(blockModels, FBContent.BLOCK_FRAMED_GLOWING_ITEM_FRAME)
                .with(BlockModelGenerators.createBooleanModelDispatch(PropertyHolder.MAP_FRAME, glowMapFrame, glowFrame))
                .with(createFacingDispatchAlt());

        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_ITEM_FRAME.value().asItem());
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_GLOWING_ITEM_FRAME.value().asItem());
    }

    private static Variant railVariant(
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

        return horDirToVariant(dir).with(VariantProperties.MODEL, model);
    }

    private void registerFramedFancyRail(BlockModelGenerators blockModels)
    {
        ResourceLocation normalRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_RAIL.value());
        ResourceLocation ascendingRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_RAIL.value(), "_ascending");
        ResourceLocation curvedRail = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_FANCY_RAIL.value(), "_curved");

        variant(blockModels, FBContent.BLOCK_FRAMED_FANCY_RAIL).with(PropertyDispatch.property(BlockStateProperties.RAIL_SHAPE).generate(shape ->
                railVariant(shape, normalRail, ascendingRail, curvedRail)
        ));

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_FANCY_RAIL);
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

        variant(blockModels, FBContent.BLOCK_FRAMED_FANCY_POWERED_RAIL)
                .with(PropertyDispatch.properties(BlockStateProperties.RAIL_SHAPE_STRAIGHT, BlockStateProperties.POWERED)
                        .generate((shape, powered) ->
                                railVariant(shape, powered ? normalRailOn : normalRail, powered ? ascendingRailOn : ascendingRail, null)
                        )
                );

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_FANCY_POWERED_RAIL);
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

        variant(blockModels, FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL)
                .with(PropertyDispatch.properties(BlockStateProperties.RAIL_SHAPE_STRAIGHT, BlockStateProperties.POWERED)
                        .generate((shape, powered) ->
                                railVariant(shape, powered ? normalRailOn : normalRail, powered ? ascendingRailOn : ascendingRail, null)
                        )
                );

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL);
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

        variant(blockModels, FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL)
                .with(PropertyDispatch.properties(BlockStateProperties.RAIL_SHAPE_STRAIGHT, BlockStateProperties.POWERED)
                        .generate((shape, powered) ->
                                railVariant(shape, powered ? normalRailOn : normalRail, powered ? ascendingRailOn : ascendingRail, null)
                        )
                );

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL);
    }

    private void registerFramedOneWayWindow(BlockModelGenerators blockModels)
    {
        ResourceLocation model = makeUnderlayedCube(blockModels, FBContent.BLOCK_FRAMED_ONE_WAY_WINDOW, TEXTURE, mcLocation("block/moss_block"), $ -> {});
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_ONE_WAY_WINDOW, model);
    }

    private void registerFramedBookshelf(BlockModelGenerators blockModels)
    {
        simpleBlockWithItem(blockModels, FBContent.BLOCK_FRAMED_BOOKSHELF, Utils.rl("block/framed_bookshelf"));
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

        MultiPartGenerator builder = multiPart(blockModels, FBContent.BLOCK_FRAMED_CHISELED_BOOKSHELF)
                .with(Variant.variant().with(VariantProperties.MODEL, baseModel));
        for (Direction dir : Direction.Plane.HORIZONTAL)
        {
            for (int i = 0; i < ChiseledBookShelfBlockEntity.MAX_BOOKS_IN_STORAGE; i++)
            {
                BooleanProperty prop = ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(i);

                builder.with(
                        Condition.and(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, dir).term(prop, false)),
                        horDirToVariant(dir.getOpposite()).with(VariantProperties.MODEL, modelsEmpty[i])
                );
                builder.with(
                        Condition.and(Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, dir).term(prop, true)),
                        horDirToVariant(dir.getOpposite()).with(VariantProperties.MODEL, modelsFilled[i])
                );
            }
        }

        framedBlockItemModel(blockModels, FBContent.BLOCK_FRAMED_CHISELED_BOOKSHELF);
    }

    private void registerFramedChain(BlockModelGenerators blockModels, ResourceLocation cube)
    {
        simpleBlock(blockModels, FBContent.BLOCK_FRAMED_CHAIN, cube);
        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_CHAIN.value().asItem());
    }

    private void registerFramedLantern(BlockModelGenerators blockModels)
    {
        ResourceLocation standing = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_LANTERN.value());
        ResourceLocation hanging = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_LANTERN.value(), "_hanging");
        variant(blockModels, FBContent.BLOCK_FRAMED_LANTERN)
                .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.HANGING, hanging, standing));

        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_LANTERN.value().asItem());
    }

    private void registerFramedSoulLantern(BlockModelGenerators blockModels)
    {
        ResourceLocation standing = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_SOUL_LANTERN.value());
        ResourceLocation hanging = ModelLocationUtils.getModelLocation(FBContent.BLOCK_FRAMED_SOUL_LANTERN.value(), "_hanging");
        variant(blockModels, FBContent.BLOCK_FRAMED_SOUL_LANTERN)
                .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.HANGING, hanging, standing));

        blockModels.registerSimpleFlatItemModel(FBContent.BLOCK_FRAMED_SOUL_LANTERN.value().asItem());
    }



    private void registerFramingSaw(BlockModelGenerators blockModels)
    {
        ResourceLocation model = Utils.rl("block/framing_saw");
        ResourceLocation modelEncoder = Utils.rl("block/framing_saw_encoder");
        variant(blockModels, FBContent.BLOCK_FRAMING_SAW)
                .with(BlockModelGenerators.createBooleanModelDispatch(PropertyHolder.SAW_ENCODER, modelEncoder, model))
                .with(BlockModelGenerators.createHorizontalFacingDispatchAlt());
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

        variant(blockModels, FBContent.BLOCK_POWERED_FRAMING_SAW)
                .with(BlockModelGenerators.createBooleanModelDispatch(PropertyHolder.ACTIVE, modelActive, modelInactive))
                .with(BlockModelGenerators.createHorizontalFacingDispatchAlt());
        blockModels.registerSimpleItemModel(FBContent.BLOCK_POWERED_FRAMING_SAW.value(), modelActive);
    }
}
