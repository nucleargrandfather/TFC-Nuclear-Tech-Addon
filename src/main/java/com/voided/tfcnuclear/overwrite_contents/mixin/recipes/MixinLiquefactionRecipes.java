package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.LiquefactionRecipes;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(value = LiquefactionRecipes.class, remap = false)
public class MixinLiquefactionRecipes {

    @Shadow
    public static HashMap<Object, FluidStack> recipes;

    @Inject(method = "registerDefaults", at = @At("RETURN"))
    private void onRegisterDefaults(CallbackInfo ci) {

        removeRecipeByInput(new RecipesCommon.ComparableStack(Blocks.STONE));
        removeRecipeByInput(new RecipesCommon.ComparableStack(Blocks.COBBLESTONE));


        recipes.put("stone", new FluidStack(250, Fluids.LAVA));
        recipes.put("cobblestone", new FluidStack(250, Fluids.LAVA));

    }

    private static void removeRecipeByInput(RecipesCommon.ComparableStack input) {
        if (recipes.containsKey(input)) {
            recipes.remove(input);
            System.out.println("[TFC-Nuclear] Removed liquefaction recipe for: " + input.item.getRegistryName());
        } else {
            System.out.println("[TFC-Nuclear] Liquefaction recipe not found for: " + input.item.getRegistryName());
        }
    }

    /**
     * Удаляет рецепт по OreDict (удаляет все рецепты с этим ключом)
     */
    private static void removeRecipeByOreDict(String oreDictName) {
        if (recipes.containsKey(oreDictName)) {
            recipes.remove(oreDictName);
            System.out.println("[TFC-Nuclear] Removed liquefaction recipe for OreDict: " + oreDictName);
        } else {
            System.out.println("[TFC-Nuclear] Liquefaction recipe not found for OreDict: " + oreDictName);
        }
    }

    /**
     * Удаляет рецепт по OreDict (перегрузка для String)
     */
    private static void removeRecipeByOreDict(String[] oreDictNames) {
        for (String name : oreDictNames) {
            removeRecipeByOreDict(name);
        }
    }
}