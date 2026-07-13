package com.voided.tfcnuclear.overwrite_contents.mixin.needEdit;

import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.recipes.ArcFurnaceRecipes;
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
public class ArcFurnaceTweaker {

    private static final Set<String> WROUGHT_PATTERNS = new HashSet<>(Arrays.asList(
            "plate", "castplate", "weldedplate",
            "shell", "ntmpipe", "wire", "densewire",
            "bolt", "billet", "barrel", "receiver",
            "mechanism", "stock", "grip"
    ));

    @Inject(method = "getOutput", at = @At("RETURN"), cancellable = true, remap = false)
    private static void fixGetOutput(ItemStack stack, boolean liquid, CallbackInfoReturnable<ArcFurnaceRecipes.ArcFurnaceRecipe> cir) {
        ArcFurnaceRecipes.ArcFurnaceRecipe recipe = cir.getReturnValue();

        if (recipe == null || !liquid || recipe.fluidOutput == null) return;

        if (!isWroughtIronItem(stack)) return;

        MaterialStack[] fluidOutputs = recipe.fluidOutput;
        boolean modified = false;

        for (int i = 0; i < fluidOutputs.length; i++) {
            if (fluidOutputs[i].material == Mats.MAT_IRON) {
                fluidOutputs[i] = new MaterialStack(Mats.MAT_WROUGHTIRON, fluidOutputs[i].amount);
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

    private static boolean isWroughtIronItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("isWroughtIron")) {
            return true;
        }

        List<String> oreNames = com.hbm.util.ItemStackUtil.getOreDictNames(stack);

        for (String oreName : oreNames) {
            String lower = oreName.toLowerCase();

            if (!lower.contains("iron")) continue;

            if (lower.startsWith("ingot") || lower.startsWith("block") ||
                    lower.startsWith("nugget") || lower.startsWith("fragment") || lower.startsWith("dust")) continue;

            for (String pattern : WROUGHT_PATTERNS) {
                if (lower.contains(pattern) && lower.contains("iron")) {
                    return true;
                }
            }
        }

        return false;
    }
}