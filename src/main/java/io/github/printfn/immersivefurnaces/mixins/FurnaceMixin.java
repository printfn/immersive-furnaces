package io.github.printfn.immersivefurnaces.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class FurnaceMixin extends BaseContainerBlockEntity {
    protected FurnaceMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void serverTickMixin(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        var f = (FurnaceInvoker)blockEntity;
        var recipeInput = f.getItems().get(0);
        var fuel = f.getItems().get(1);
        if (f.invokeIsLit() || fuel.isEmpty()) {
            return;
        }
        if (!recipeInput.isEmpty()) {
            var recipeHolder = f.getQuickCheck().getRecipeFor(new SingleRecipeInput(recipeInput), level).orElse(null);
            if (f.invokeCanBurn(level.registryAccess(), recipeHolder, f.getItems(), blockEntity.getMaxStackSize(), blockEntity)) {
                return;
            }
        }
        var burnDuration = f.invokeGetBurnDuration(fuel);
        f.setLitTime(burnDuration);
        f.setLitDuration(burnDuration);
        if (f.invokeIsLit()) {
            if (fuel.hasCraftingRemainingItem()) {
                // e.g. using a lava bucket will leave behind an empty bucket
                f.getItems().set(1, fuel.getCraftingRemainingItem());
            } else {
                fuel.shrink(1);
            }
            blockEntity.setChanged();
            state = state.setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(true));
            level.setBlock(pos, state, 3);
            setChanged(level, pos, state);
        }
    }
}
