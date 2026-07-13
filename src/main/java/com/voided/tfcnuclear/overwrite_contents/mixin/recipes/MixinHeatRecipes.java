package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import net.dries007.tfc.api.recipes.heat.HeatRecipeMetalMelting;
import net.dries007.tfc.api.types.Metal;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = net.dries007.tfc.api.recipes.heat.HeatRecipe.class, remap = false)
public class MixinHeatRecipes {


    @Inject(
            method = "isValidInput",
            at = @At("HEAD"),
            cancellable = true
    )
    private void filterHbmBismuthMelting(ItemStack input, Metal.Tier tier, CallbackInfoReturnable<Boolean> cir) {
        if (input == null || input.isEmpty()) return;

        net.dries007.tfc.api.recipes.heat.HeatRecipe self = (net.dries007.tfc.api.recipes.heat.HeatRecipe) (Object) this;

        if (!(self instanceof HeatRecipeMetalMelting)) return;

        HeatRecipeMetalMelting meltingRecipe = (HeatRecipeMetalMelting) self;
        Metal metal = meltingRecipe.getMetal();

        if (metal != null && (metal.getRegistryName().getPath().equals("bismuth") ||
                metal.getRegistryName().getPath().equals("bismuth_bronze"))) {

            net.minecraft.util.ResourceLocation registryName = input.getItem().getRegistryName();
            if (registryName != null && registryName.getNamespace().equals("hbm")) {
                cir.setReturnValue(false);
            }
        }
        if (metal != null && (metal.getRegistryName().getPath().equals("gold"))) {
            net.minecraft.util.ResourceLocation registryName = input.getItem().getRegistryName();
            if (registryName != null && registryName.getNamespace().equals("minecraft")) {
                cir.setReturnValue(false);
            }
        }
        if (metal != null && metal.getRegistryName().getPath().equals("lead")) {
            net.minecraft.util.ResourceLocation registryName = input.getItem().getRegistryName();
            if (registryName != null && registryName.getNamespace().equals("tfc")) {
                String path = registryName.getPath();
                if (path.contains("ingot") || path.contains("dust") || path.contains("nugget")) {
                    if (!path.contains("double")) {
                        cir.setReturnValue(false);
                    }
                }
            }
        }
        if (metal != null && metal.getRegistryName().getPath().equals("steel")) {
            net.minecraft.util.ResourceLocation registryName = input.getItem().getRegistryName();
            if (registryName != null && registryName.getNamespace().equals("tfc")) {
                String path = registryName.getPath();
                if (path.contains("ingot") || path.contains("dust") || path.contains("nugget")) {
                    if (!path.contains("double")) {
                        cir.setReturnValue(false);
                    }
                }
            }
        }
        if (metal != null && metal.getRegistryName().getPath().equals("copper")) {
            net.minecraft.util.ResourceLocation registryName = input.getItem().getRegistryName();
            if (registryName != null && registryName.getNamespace().equals("tfc")) {
                String path = registryName.getPath();
                if (path.contains("ingot") || path.contains("dust") || path.contains("nugget")) {
                    if (!path.contains("double")) {
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }
}