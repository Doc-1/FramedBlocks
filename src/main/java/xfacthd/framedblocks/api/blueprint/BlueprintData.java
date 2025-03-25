package xfacthd.framedblocks.api.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.util.CamoList;
import xfacthd.framedblocks.api.util.Utils;

import java.util.Optional;
import java.util.function.Consumer;

public record BlueprintData(
        Block block,
        CamoList camos,
        boolean glowing,
        boolean intangible,
        boolean reinforced,
        boolean emissive,
        BlockItemStateProperties blockState,
        Optional<AuxBlueprintData<?>> auxData
) implements TooltipProvider
{
    public static final Codec<BlueprintData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(BlueprintData::block),
            CamoList.CODEC.fieldOf("camos").forGetter(BlueprintData::camos),
            Codec.BOOL.fieldOf("glowing").forGetter(BlueprintData::glowing),
            Codec.BOOL.fieldOf("intangible").forGetter(BlueprintData::intangible),
            Codec.BOOL.fieldOf("reinforced").forGetter(BlueprintData::reinforced),
            Codec.BOOL.fieldOf("emissive").forGetter(BlueprintData::emissive),
            BlockItemStateProperties.CODEC.optionalFieldOf("blockstate", BlockItemStateProperties.EMPTY).forGetter(BlueprintData::blockState),
            AuxBlueprintData.CODEC.optionalFieldOf("aux_data").forGetter(BlueprintData::auxData)
    ).apply(inst, BlueprintData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlueprintData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.BLOCK),
            BlueprintData::block,
            CamoList.STREAM_CODEC,
            BlueprintData::camos,
            ByteBufCodecs.BOOL,
            BlueprintData::glowing,
            ByteBufCodecs.BOOL,
            BlueprintData::intangible,
            ByteBufCodecs.BOOL,
            BlueprintData::reinforced,
            ByteBufCodecs.BOOL,
            BlueprintData::emissive,
            BlockItemStateProperties.STREAM_CODEC,
            BlueprintData::blockState,
            ByteBufCodecs.optional(AuxBlueprintData.STREAM_CODEC),
            BlueprintData::auxData,
            BlueprintData::new
    );
    public static final BlueprintData EMPTY = new BlueprintData(Blocks.AIR, CamoList.EMPTY, false, false, false, false, BlockItemStateProperties.EMPTY, Optional.empty());
    public static final String CONTAINED_BLOCK = "desc.framedblocks.blueprint_block";
    public static final String CAMO_BLOCK = "desc.framedblocks.blueprint_camo";
    public static final String IS_ILLUMINATED = "desc.framedblocks.blueprint_illuminated";
    public static final String IS_INTANGIBLE = "desc.framedblocks.blueprint_intangible";
    public static final String IS_REINFORCED = "desc.framedblocks.blueprint_reinforced";
    public static final String IS_EMISSIVE = "desc.framedblocks.blueprint_emissive";
    public static final String MISSING_MATERIALS = Utils.translationKey("desc", "blueprint_missing_materials");
    public static final MutableComponent BLOCK_NONE = Utils.translate("desc", "blueprint_none").withStyle(ChatFormatting.RED);
    public static final MutableComponent BLOCK_INVALID = Utils.translate("desc", "blueprint_invalid").withStyle(ChatFormatting.RED);
    public static final MutableComponent FALSE = Utils.translate("desc", "blueprint_false").withStyle(ChatFormatting.RED);
    public static final MutableComponent TRUE = Utils.translate("desc", "blueprint_true").withStyle(ChatFormatting.GREEN);
    public static final MutableComponent CANT_COPY = Utils.translate("desc", "blueprint_cant_copy").withStyle(ChatFormatting.RED);

    @SuppressWarnings("unchecked")
    public <T extends AuxBlueprintData<T>> T getAuxDataOrDefault(T _default)
    {
        if (auxData.isPresent() && _default.type() == auxData.get().type())
        {
            return (T) auxData.get();
        }
        return _default;
    }

    public boolean isEmpty()
    {
        return block.defaultBlockState().isAir();
    }

    public BlueprintData withBlockState(BlockItemStateProperties newBlockState)
    {
        return new BlueprintData(block, camos, glowing, intangible, reinforced, emissive, newBlockState, auxData);
    }

    public BlueprintData withAuxData(AuxBlueprintData<?> newAuxData)
    {
        return new BlueprintData(block, camos, glowing, intangible, reinforced, emissive, blockState, Optional.of(newAuxData));
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> appender, TooltipFlag tooltipFlag, DataComponentGetter p_399520_)
    {
        if (isEmpty())
        {
            appender.accept(Component.translatable(CONTAINED_BLOCK, BLOCK_NONE).withStyle(ChatFormatting.GOLD));
            return;
        }

        Block block = block();
        Component blockName = block == Blocks.AIR ? BLOCK_INVALID : block.getName().withStyle(ChatFormatting.WHITE);

        Component camoName = !(block instanceof IFramedBlock fb) ? BLOCK_NONE : fb.printCamoBlock(this).orElse(BLOCK_NONE);
        Component illuminated = glowing ? TRUE : FALSE;
        Component intangible = this.intangible ? TRUE : FALSE;
        Component reinforced = this.reinforced ? TRUE : FALSE;
        Component emissive = this.emissive ? TRUE : FALSE;

        appender.accept(Component.translatable(CONTAINED_BLOCK, blockName).withStyle(ChatFormatting.GOLD));
        appender.accept(Component.translatable(CAMO_BLOCK, camoName).withStyle(ChatFormatting.GOLD));
        appender.accept(Component.translatable(IS_ILLUMINATED, illuminated).withStyle(ChatFormatting.GOLD));
        appender.accept(Component.translatable(IS_INTANGIBLE, intangible).withStyle(ChatFormatting.GOLD));
        appender.accept(Component.translatable(IS_REINFORCED, reinforced).withStyle(ChatFormatting.GOLD));
        appender.accept(Component.translatable(IS_EMISSIVE, emissive).withStyle(ChatFormatting.GOLD));
    }
}
