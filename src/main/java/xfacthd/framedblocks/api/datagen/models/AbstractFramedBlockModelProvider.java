package xfacthd.framedblocks.api.datagen.models;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import net.neoforged.neoforge.common.util.TransformationHelper;

import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings("SameParameterValue")
public abstract class AbstractFramedBlockModelProvider extends ModelProvider
{
    protected static final TextureSlot SLOT_FRAME = TextureSlot.create("frame");
    protected static final TextureSlot SLOT_UNDERLAY = TextureSlot.create("underlay");

    protected AbstractFramedBlockModelProvider(PackOutput output, String modId)
    {
        super(output, modId);
    }

    protected static MultiVariantGenerator variant(BlockModelGenerators blockModels, Holder<Block> block)
    {
        return variant(blockModels, block, Variant.variant());
    }

    protected static MultiVariantGenerator variant(BlockModelGenerators blockModels, Holder<Block> block, Variant... baseVariants)
    {
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block.value(), baseVariants);
        blockModels.blockStateOutput.accept(generator);
        return generator;
    }

    protected static MultiPartGenerator multiPart(BlockModelGenerators blockModels, Holder<Block> block)
    {
        MultiPartGenerator generator = MultiPartGenerator.multiPart(block.value());
        blockModels.blockStateOutput.accept(generator);
        return generator;
    }

    protected static void simpleBlock(BlockModelGenerators blockModels, Holder<Block> block)
    {
        simpleBlock(blockModels, block, ModelLocationUtils.getModelLocation(block.value()));
    }

    protected static void simpleBlock(BlockModelGenerators blockModels, Holder<Block> block, ResourceLocation model)
    {
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.value(), model));
    }

    protected static void simpleBlockWithItem(BlockModelGenerators blockModels, Holder<Block> block, ResourceLocation model)
    {
        simpleBlock(blockModels, block, model);
        blockModels.registerSimpleItemModel(block.value(), model);
    }

    protected static void blockFromTemplate(BlockModelGenerators blockModels, Holder<Block> block, ModelTemplate template, TextureMapping textures)
    {
        simpleBlock(blockModels, block, blockModelFromTemplate(blockModels, block, template, textures));
    }

    protected static ResourceLocation blockModelFromTemplate(BlockModelGenerators blockModels, Holder<Block> block, ModelTemplate template, TextureMapping textures)
    {
        ResourceLocation name = ModelLocationUtils.getModelLocation(block.value(), template.suffix.orElse(""));
        return template.create(name, textures, blockModels.modelOutput);
    }

    protected static void blockItemFromTemplate(BlockModelGenerators blockModels, Holder<Block> block, ModelTemplate template, TextureMapping textures)
    {
        ResourceLocation name = ModelLocationUtils.getModelLocation(block.value().asItem(), template.suffix.orElse(""));
        blockModels.registerSimpleItemModel(block.value(), template.create(name, textures, blockModels.modelOutput));
    }

    protected static ResourceLocation makeUnderlayedCube(
            BlockModelGenerators blockModels, Holder<Block> block, ResourceLocation frameTex, ResourceLocation underlayTex, Consumer<ExtendedModelTemplateBuilder> consumer
    )
    {
        ResourceLocation name = ModelLocationUtils.getModelLocation(block.value());
        return makeUnderlayedCube(blockModels, name, frameTex, underlayTex, consumer);
    }

    protected static ResourceLocation makeUnderlayedCube(
            BlockModelGenerators blockModels, ResourceLocation name, ResourceLocation frameTex, ResourceLocation underlayTex, Consumer<ExtendedModelTemplateBuilder> consumer
    )
    {
        ExtendedModelTemplateBuilder builder = ExtendedModelTemplateBuilder.builder()
                .requiredTextureSlot(SLOT_FRAME)
                .requiredTextureSlot(SLOT_UNDERLAY)
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .parent(ResourceLocation.withDefaultNamespace("block/block"))
                .element(elem -> elem.cube(SLOT_UNDERLAY))
                .element(elem -> elem.cube(SLOT_FRAME))
                .renderType("cutout");
        consumer.accept(builder);

        ModelTemplate template = builder.build();
        return template.create(
                name.withSuffix(template.suffix.orElse("")),
                new TextureMapping()
                        .put(SLOT_FRAME, frameTex)
                        .put(SLOT_UNDERLAY, underlayTex)
                        .put(TextureSlot.PARTICLE, frameTex),
                blockModels.modelOutput
        );
    }

    protected static void makeOverlayCube(BlockModelGenerators blockModels, ResourceLocation name, ResourceLocation texture)
    {
        makeOverlayCube(blockModels, name, texture, $ -> {});
    }

    protected static void makeOverlayCube(BlockModelGenerators blockModels, ResourceLocation name, ResourceLocation texture, Consumer<ExtendedModelTemplateBuilder> consumer)
    {
        ExtendedModelTemplateBuilder builder = ExtendedModelTemplateBuilder.builder()
                .requiredTextureSlot(TextureSlot.ALL)
                .parent(ResourceLocation.withDefaultNamespace("block/cube_all"))
                .rootTransforms(xforms -> xforms
                        .scale(1.002F)
                        .origin(TransformationHelper.TransformOrigin.CENTER)
                )
                .renderType("cutout");
        consumer.accept(builder);
        builder.build().create(name, TextureMapping.singleSlot(TextureSlot.ALL, texture), blockModels.modelOutput);
    }

    protected static Variant rotationToVariant(int rotation)
    {
        return horDirToVariant(Direction.from2DDataValue(rotation / 4));
    }

    protected static Variant horDirToVariant(Direction dir)
    {
        VariantProperties.Rotation rotValue = switch (dir)
        {
            case NORTH -> VariantProperties.Rotation.R180;
            case SOUTH -> VariantProperties.Rotation.R0;
            case WEST -> VariantProperties.Rotation.R90;
            case EAST -> VariantProperties.Rotation.R270;
            default -> throw new IllegalArgumentException("Invalid direction for Y rotation: " + dir);
        };
        Variant variant = Variant.variant();
        if (rotValue != VariantProperties.Rotation.R0)
        {
            variant.with(VariantProperties.Y_ROT, rotValue);
        }
        return variant;
    }

    protected static VariantProperties.Rotation rotByIdx(int idx)
    {
        return VariantProperties.Rotation.values()[idx];
    }

    protected static PropertyDispatch createFacingDispatchAlt()
    {
        return PropertyDispatch.property(BlockStateProperties.FACING)
                .select(Direction.DOWN, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
                .select(Direction.UP, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.SOUTH, Variant.variant())
                .select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
    }

    @Override
    protected final Stream<? extends Holder<Item>> getKnownItems()
    {
        return super.getKnownItems().filter(item -> item instanceof BlockItem);
    }

    @Override
    public String getName()
    {
        return "Block Models - " + modId;
    }
}
