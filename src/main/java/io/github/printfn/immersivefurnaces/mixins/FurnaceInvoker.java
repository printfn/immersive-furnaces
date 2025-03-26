package io.github.printfn.immersivefurnaces.mixins;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface FurnaceInvoker {
    @Accessor
    NonNullList<ItemStack> getItems();

    @Accessor
    void setLitTime(int litTime);
    @Accessor
    void setLitDuration(int litDuration);

    @Accessor
    RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> getQuickCheck();

    @Invoker
    boolean invokeCanBurn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipe, NonNullList<ItemStack> inventory, int maxStackSize, AbstractFurnaceBlockEntity furnace);

    @Invoker
    boolean invokeIsLit();

    @Invoker
    int invokeGetBurnDuration(ItemStack fuel);
}
