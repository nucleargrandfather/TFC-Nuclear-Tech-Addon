package com.voided.tfcnuclear.overwrite_contents.mixin.needEdit;

import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.recipes.ArcFurnaceRecipes;
import com.voided.tfcnuclear.inventory.material.TFCNuclearMats;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(value = ArcFurnaceRecipes.class, remap = false)
public class ArcFurnaceTweaker2 {

    private static final Set<String> BRONZE_PATTERNS = new HashSet<>(Arrays.asList(
            "plate", "castplate", "weldedplate",
            "shell", "ntmpipe",
            "bolt", "billet", "barrel", "receiver",
            "mechanism", "stock", "grip"
    ));

    @Inject(method = "getOutput", at = @At("RETURN"), cancellable = true, remap = false)
    private static void fixGetOutput(ItemStack stack, boolean liquid, CallbackInfoReturnable<ArcFurnaceRecipes.ArcFurnaceRecipe> cir) {
        ArcFurnaceRecipes.ArcFurnaceRecipe recipe = cir.getReturnValue();

        if (recipe == null || !liquid || recipe.fluidOutput == null) return;

        if (!isBronzeItem(stack)) return;

        MaterialStack[] fluidOutputs = recipe.fluidOutput;
        boolean modified = false;

        for (int i = 0; i < fluidOutputs.length; i++) {
            if (fluidOutputs[i].material == Mats.MAT_COPPER) {
                fluidOutputs[i] = new MaterialStack(TFCNuclearMats.MAT_BRONZE, fluidOutputs[i].amount);
                modified = true;
            }
        }

        if (modified) {
            ArcFurnaceRecipes.ArcFurnaceRecipe newRecipe = new ArcFurnaceRecipes.ArcFurnaceRecipe();
            newRecipe.fluid(fluidOutputs);
            if (recipe.solidOutput != null) {
                newRecipe.solid(recipe.solidOutput);
            }
            cir.setReturnValue(newRecipe);
        }
    }

    private static boolean isBronzeItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("isBronzeIron")) {
            return true;
        }

        List<String> oreNames = com.hbm.util.ItemStackUtil.getOreDictNames(stack);

        for (String oreName : oreNames) {
            String lower = oreName.toLowerCase();

            if (!lower.contains("copper")) continue;

            if (lower.startsWith("ingot") || lower.startsWith("block") ||
                    lower.startsWith("nugget") || lower.startsWith("fragment") || lower.startsWith("dust")) continue;

            for (String pattern : BRONZE_PATTERNS) {
                if (lower.contains(pattern) && lower.contains("copper")) {
                    return true;
                }
            }
        }

        return false;
    }
}