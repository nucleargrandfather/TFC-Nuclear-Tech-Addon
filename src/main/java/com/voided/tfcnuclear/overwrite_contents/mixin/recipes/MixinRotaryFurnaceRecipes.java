package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.material.Mats;
import com.hbm.inventory.recipes.RotaryFurnaceRecipes;
import com.hbm.inventory.recipes.RotaryFurnaceRecipes.RotaryFurnaceRecipe;
import com.hbm.inventory.RecipesCommon;
import com.hbm.items.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.hbm.inventory.material.MaterialShapes.INGOT;
import static com.hbm.inventory.material.Mats.*;

@Mixin(RotaryFurnaceRecipes.class)
public class MixinRotaryFurnaceRecipes {

    @Shadow(remap = false)
    private static List<RotaryFurnaceRecipe> recipes;

    @Inject(method = "registerDefaults", at = @At("TAIL"), remap = false)
    public void modifyRecipes(CallbackInfo ci) {
        removeSteelRecipes();
        addCustomRecipes();
    }

    private void removeSteelRecipes() {
        if (recipes == null) return;

        int before = recipes.size();
        recipes.removeIf(recipe ->
                recipe.output != null &&
                        recipe.output.material == Mats.MAT_STEEL
        );

        int removed = before - recipes.size();
        if (removed > 0) {
            System.out.println("[MixinRotaryFurnaceRecipes] Removed " + removed + " steel recipes");
        }
    }

    private void addCustomRecipes() {
        if (recipes == null) return;

        // Добавляем все те же рецепты...
        recipes.add(new RotaryFurnaceRecipe(
                new Mats.MaterialStack(MAT_WROUGHTIRON, INGOT.q(1)),
                80, 100,
                new RecipesCommon.OreDictStack("ingotPigIron", 1),
                new RecipesCommon.ComparableStack(ModItems.powder_flux, 1)
        ));

        // ... и так далее, как в предыдущем варианте
    }
}