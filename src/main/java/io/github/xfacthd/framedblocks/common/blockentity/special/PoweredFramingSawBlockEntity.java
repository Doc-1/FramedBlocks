package io.github.xfacthd.framedblocks.common.blockentity.special;

import io.github.xfacthd.framedblocks.api.util.Utils;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.capability.energy.EntityAwareEnergyStorage;
import io.github.xfacthd.framedblocks.common.capability.item.ExternalItemHandler;
import io.github.xfacthd.framedblocks.common.capability.item.RecipeInputItemStackHandler;
import io.github.xfacthd.framedblocks.common.config.ServerConfig;
import io.github.xfacthd.framedblocks.common.crafting.saw.FramingSawRecipe;
import io.github.xfacthd.framedblocks.common.crafting.saw.FramingSawRecipeAdditive;
import io.github.xfacthd.framedblocks.common.crafting.saw.FramingSawRecipeCache;
import io.github.xfacthd.framedblocks.common.crafting.saw.FramingSawRecipeCalculation;
import io.github.xfacthd.framedblocks.common.crafting.saw.FramingSawRecipeMatchResult;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.menu.FramingSawMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PoweredFramingSawBlockEntity extends BlockEntity
{
    private static final boolean INSERT_ENERGY_DEBUG = true;
    private static final long ACTIVE_TIMEOUT = 40;

    private final RecipeInputItemStackHandler itemHandler = new RecipeInputItemStackHandler(FramingSawMenu.SLOT_RESULT + 1)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            PoweredFramingSawBlockEntity.this.onContentsChanged(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack)
        {
            return PoweredFramingSawBlockEntity.this.isValidItem(slot, stack);
        }
    };
    private final IItemHandler externalItemHandler = new ExternalItemHandler(itemHandler)
    {
        @Override
        protected boolean canExtract(int slot)
        {
            return slot == FramingSawMenu.SLOT_RESULT;
        }
    };
    private final EntityAwareEnergyStorage energyStorage = new EntityAwareEnergyStorage(
            ServerConfig.VIEW.getPoweredSawEnergyCapacity(),
            ServerConfig.VIEW.getPoweredSawMaxInput(),
            0,
            () -> this.needSaving = true
    );
    private final int energyConsumption;
    private final int craftingDuration;
    @UnknownNullability
    private FramingSawRecipeCache cache = null;
    @Nullable
    private ResourceKey<Recipe<?>> selectedRecipeId = null;
    @Nullable
    private RecipeHolder<FramingSawRecipe> selectedRecipe = null;
    private boolean active = false;
    private long lastActive = 0;
    private boolean recipeSatisfied = false;
    @Nullable
    private FramingSawRecipeMatchResult matchResult = null;
    @Nullable
    private FramingSawRecipeCalculation calculation = null;
    private int outputCount = 0;
    private int progress = 0;
    private boolean needSaving = false;
    private boolean inhibitUpdate = false;
    private boolean internalAccess = false;

    public PoweredFramingSawBlockEntity(BlockPos pPos, BlockState pBlockState)
    {
        super(FBContent.BE_TYPE_POWERED_FRAMING_SAW.value(), pPos, pBlockState);
        this.energyConsumption = ServerConfig.VIEW.getPoweredSawConsumption();
        this.craftingDuration = ServerConfig.VIEW.getPoweredSawCraftingDuration();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PoweredFramingSawBlockEntity be)
    {
        if (!Utils.PRODUCTION && INSERT_ENERGY_DEBUG)
        {
            be.energyStorage.receiveEnergy(be.energyStorage.getMaxReceive(), false);
        }

        if ((be.active || level.getGameTime() - be.lastActive > ACTIVE_TIMEOUT) && be.canRun())
        {
            if (!be.active)
            {
                be.active = true;
                level.setBlockAndUpdate(pos, state.setValue(PropertyHolder.ACTIVE, true));
            }

            be.energyStorage.extractEnergyInternal(be.energyConsumption);
            be.progress++;

            if (be.progress >= be.craftingDuration)
            {
                be.progress = 0;

                ItemStack result = Objects.requireNonNull(be.selectedRecipe).value().getResult().copy();
                result.setCount(be.outputCount);
                be.internalAccess = true;
                be.inhibitUpdate = true;
                for (int i = 0; i < be.selectedRecipe.value().getAdditives().size(); i++)
                {
                    int slot = i + FramingSawMenu.SLOT_ADDITIVE_FIRST;
                    be.itemHandler.extractItem(slot, Objects.requireNonNull(be.calculation).getAdditiveCount(i), false);
                }
                be.inhibitUpdate = false;
                be.itemHandler.extractItem(FramingSawMenu.SLOT_INPUT, Objects.requireNonNull(be.calculation).getInputCount(), false);
                be.itemHandler.insertItem(FramingSawMenu.SLOT_RESULT, result, false);
                be.internalAccess = false;
            }
        }
        else if (be.active)
        {
            be.active = false;
            level.setBlockAndUpdate(pos, state.setValue(PropertyHolder.ACTIVE, false));
            be.lastActive = level.getGameTime();

            if (!be.recipeSatisfied)
            {
                be.progress = 0;
            }
        }

        if (be.needSaving)
        {
            level.blockEntityChanged(be.worldPosition);
            be.needSaving = false;
        }
    }

    private boolean canRun()
    {
        if (selectedRecipe == null || !recipeSatisfied) return false;
        if (energyStorage.getEnergyStored() < energyConsumption) return false;

        ItemStack output = itemHandler.getStackInSlot(FramingSawMenu.SLOT_RESULT);
        if (!output.isEmpty())
        {
            if (!ItemStack.isSameItemSameComponents(output, selectedRecipe.value().getResult()))
            {
                return false;
            }
            if (output.getCount() + outputCount > output.getMaxStackSize())
            {
                return false;
            }
        }
        return true;
    }

    private void checkRecipeSatisfied()
    {
        if (selectedRecipe != null)
        {
            matchResult = selectedRecipe.value().matchWithResult(itemHandler, level());
            recipeSatisfied = matchResult.success();
        }
        else
        {
            matchResult = null;
            recipeSatisfied = false;
        }

        if (recipeSatisfied)
        {
            calculation = Objects.requireNonNull(selectedRecipe).value().makeCraftingCalculation(itemHandler, false);
            outputCount = calculation.getOutputCount();
        }
        else
        {
            calculation = null;
            outputCount = 0;
            progress = 0;
        }
    }

    private void onContentsChanged(int slot)
    {
        needSaving = true;
        if (slot != FramingSawMenu.SLOT_RESULT && !inhibitUpdate)
        {
            checkRecipeSatisfied();
        }
    }

    private boolean isValidItem(int slot, ItemStack stack)
    {
        if (slot == FramingSawMenu.SLOT_INPUT)
        {
            return cache.getMaterialValue(stack.getItem()) > 0;
        }
        else if (slot < FramingSawMenu.SLOT_RESULT)
        {
            if (selectedRecipe != null)
            {
                int idx = slot - FramingSawMenu.SLOT_ADDITIVE_FIRST;
                List<FramingSawRecipeAdditive> additives = selectedRecipe.value().getAdditives();
                if (!additives.isEmpty() && idx < additives.size())
                {
                    return additives.get(idx).ingredient().test(stack);
                }
                return false;
            }
            return true;
        }
        else if (slot == FramingSawMenu.SLOT_RESULT)
        {
            return internalAccess;
        }
        throw new IllegalArgumentException("Invalid slot: " + slot);
    }

    public void selectRecipe(@Nullable RecipeHolder<FramingSawRecipe> recipe)
    {
        ResourceKey<Recipe<?>> lastId = selectedRecipeId;
        selectedRecipe = recipe;
        selectedRecipeId = recipe == null ? null : recipe.id();
        checkRecipeSatisfied();
        if (!Objects.equals(lastId, selectedRecipeId))
        {
            needSaving = true;
        }
    }

    @Nullable
    public RecipeHolder<FramingSawRecipe> getSelectedRecipe()
    {
        return selectedRecipe;
    }

    @Nullable
    public FramingSawRecipeMatchResult getMatchResult()
    {
        return matchResult;
    }

    public int getProgress()
    {
        return progress;
    }

    public RecipeInputItemStackHandler getItemHandler()
    {
        return itemHandler;
    }

    public IItemHandler getExternalItemHandler()
    {
        return externalItemHandler;
    }

    public IEnergyStorage getEnergyStorage()
    {
        return energyStorage;
    }

    public int getEnergy()
    {
        return energyStorage.getEnergyStored();
    }

    public int getEnergyCapacity()
    {
        return energyStorage.getCapacity();
    }

    public int getCraftingDuration()
    {
        return craftingDuration;
    }

    public boolean isInputEmpty()
    {
        for (int i = 0; i < FramingSawMenu.SLOT_RESULT; i++)
        {
            if (!itemHandler.getStackInSlot(i).isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onLoad()
    {
        super.onLoad();
        cache = FramingSawRecipeCache.get(level().isClientSide());
        if (level() instanceof ServerLevel serverLevel && selectedRecipeId != null)
        {
            RecipeHolder<FramingSawRecipe> recipe = (RecipeHolder<FramingSawRecipe>) serverLevel.recipeAccess()
                    .byKey(selectedRecipeId)
                    .filter(h -> h.value() instanceof FramingSawRecipe)
                    .orElse(null);
            selectRecipe(recipe);
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level != null)
        {
            inhibitUpdate = true;
            Utils.dropItemHandlerContents(level, pos, itemHandler);
            inhibitUpdate = false;
        }
    }

    private Level level()
    {
        return Objects.requireNonNull(level, "BlockEntity#level accessed before it was set");
    }

    public boolean isUsableByPlayer(Player player)
    {
        if (level().getBlockEntity(worldPosition) != this)
        {
            return false;
        }
        return !(player.distanceToSqr((double)worldPosition.getX() + 0.5D, (double)worldPosition.getY() + 0.5D, (double)worldPosition.getZ() + 0.5D) > 64.0D);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput)
    {
        super.saveAdditional(valueOutput);
        if (selectedRecipe != null)
        {
            valueOutput.putString("recipe", selectedRecipe.id().toString());
        }
        itemHandler.serialize(valueOutput.child("inventory"));
        energyStorage.serialize(valueOutput.child("energy"));
    }

    @Override
    public void loadAdditional(ValueInput valueInput)
    {
        super.loadAdditional(valueInput);
        Optional<String> optRecipe = valueInput.getString("recipe");
        if (optRecipe.isPresent())
        {
            ResourceLocation recipe = ResourceLocation.tryParse(optRecipe.get());
            selectedRecipeId = recipe != null ? ResourceKey.create(Registries.RECIPE, recipe) : null;
        }
        itemHandler.deserialize(valueInput.childOrEmpty("inventory"));
        energyStorage.deserialize(valueInput.childOrEmpty("energy"));
    }
}
