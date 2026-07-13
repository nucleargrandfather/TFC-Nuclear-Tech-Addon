package com.voided.tfcnuclear.overwrite_contents.mixin.handler;

import net.dries007.tfc.api.recipes.BlastFurnaceRecipe;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlastFurnaceRecipe.class)
public class MixinTFCBlastFurnaceRecipes {

    @Inject(method = "isValidInput", at = @At("HEAD"), cancellable = true, remap = false)
    public void onIsValidInput(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack == null || stack.isEmpty()) return;

        String itemName = stack.getItem().getRegistryName().toString();

        // Запрещаем все предметы с wrought_iron
        if (
                itemName.contains("bloom") || itemName.contains("wrought") || itemName.contains("bars")
        ) {
            cir.setReturnValue(false);
        }
    }
}