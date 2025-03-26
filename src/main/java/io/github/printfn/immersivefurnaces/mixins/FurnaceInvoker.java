package io.github.printfn.immersivefurnaces.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface FurnaceInvoker {
    @Accessor
    NonNullList<ItemStack> getItems();

    @Accessor
    void setLitTime(int litTime);
    @Accessor
    void setLitDuration(int litDuration);

    @Invoker
    boolean invokeIsLit();

    @Invoker
    int invokeGetBurnDuration(ItemStack fuel);
}
