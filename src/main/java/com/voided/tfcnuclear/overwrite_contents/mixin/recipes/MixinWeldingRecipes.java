package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import net.dries007.tfc.api.types.Metal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.dries007.tfc.api.recipes.WeldingRecipe.class)
public class MixinWeldingRecipes {

    @Inject(method = "get", at = @At("RETURN"), cancellable = true, remap = false)
    private static void onGet(ItemStack stack1, ItemStack stack2, Metal.Tier tier, CallbackInfoReturnable<net.dries007.tfc.api.recipes.WeldingRecipe> cir) {
        net.dries007.tfc.api.recipes.WeldingRecipe recipe = cir.getReturnValue();
        if (recipe == null) return;

        ResourceLocation name = recipe.getRegistryName();
        if (name != null && name.getPath().contains("bismuth")) {
            cir.setReturnValue(null);
        }
    }
}