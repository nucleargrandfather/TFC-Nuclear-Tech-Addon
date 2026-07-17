package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.recipes.CrucibleRecipe;
import com.voided.tfcnuclear.inventory.material.TFCNuclearMats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = com.hbm.inventory.recipes.CrucibleRecipes.class, remap = false)
public class MixinCrucibleRecipes {

    @Inject(method = "registerDefaults", at = @At("RETURN"), remap = false)

    private void addCustomRecipesAndRemoveRecipe(CallbackInfo ci) {
        com.hbm.inventory.recipes.CrucibleRecipes instance = com.hbm.inventory.recipes.CrucibleRecipes.INSTANCE;

        com.hbm.inventory.recipes.CrucibleRecipes.INSTANCE.removeRecipeByName("crucible.steel");
        com.hbm.inventory.recipes.CrucibleRecipes.INSTANCE.removeRecipeByName("crucible.hematite");
        com.hbm.inventory.recipes.CrucibleRecipes.INSTANCE.removeRecipeByName("crucible.malachite");

        int n = MaterialShapes.NUGGET.q(1);
        int i = MaterialShapes.INGOT.q(1);
        int q = 1;

        instance.register(new CrucibleRecipe("bronze")
                .setup(10, new ItemStack(Item.getByNameOrId("tfc:metal/ingot/bronze")))
                .inputs(new MaterialStack(Mats.MAT_COPPER, 7 * q), new MaterialStack(TFCNuclearMats.MAT_TIN, q))
                .outputs(new MaterialStack(TFCNuclearMats.MAT_BRONZE, n)
                ));
        instance.register(new CrucibleRecipe("magnetite")
                .setup(15, new ItemStack(Item.getByNameOrId("tfc:ore/magnetite")))
                .inputs(new MaterialStack(TFCNuclearMats.MAT_MAGNETITE, n * 2), new MaterialStack(Mats.MAT_FLUORITE, n * 2), new MaterialStack(Mats.MAT_FLUX, n * 3))
                .outputs(new MaterialStack(Mats.MAT_IRON, n * 6), new MaterialStack(Mats.MAT_TITANIUM, n * 3), new MaterialStack(Mats.MAT_SLAG, n)
                ));
        instance.register(new CrucibleRecipe("limonite")
                .setup(15, new ItemStack(Item.getByNameOrId("tfc:ore/limonite")))
                .inputs(new MaterialStack(TFCNuclearMats.MAT_LIMONITE, n * 2), new MaterialStack(Mats.MAT_FLUX, n))
                .outputs(new MaterialStack(Mats.MAT_IRON, n * 2),new MaterialStack(Mats.MAT_COBALT, q * 4),new MaterialStack(Mats.MAT_SLAG, n * 3)
                ));
        instance.register(new CrucibleRecipe("hematite")
                .setup(15, new ItemStack(Item.getByNameOrId("tfc:ore/hematite")))
                .inputs(new MaterialStack(Mats.MAT_HEMATITE, n * 2),new MaterialStack(Mats.MAT_SILICON, n * 2), new MaterialStack(Mats.MAT_FLUX, n))
                .outputs(new MaterialStack(Mats.MAT_IRON, n * 4), new MaterialStack(Mats.MAT_TUNGSTEN, n * 2), new MaterialStack(Mats.MAT_SLAG, n * 2)
                ));
        instance.register(new CrucibleRecipe("malachite")
                .setup(15, new ItemStack(Item.getByNameOrId("tfc:ore/malachite")))
                .inputs(new MaterialStack(Mats.MAT_MALACHITE, n * 2),new MaterialStack(Mats.MAT_SILICON, n * 2), new MaterialStack(Mats.MAT_FLUX, n))
                .outputs(new MaterialStack(Mats.MAT_COPPER, n * 4), new MaterialStack(Mats.MAT_SLAG, n * 2)
                ));
        instance.register(new CrucibleRecipe("red steel")
                .setup(10, new ItemStack(Item.getByNameOrId("tfc:metal/ingot/red_steel")))
                .inputs(new MaterialStack(TFCNuclearMats.MAT_BLACKSTEEL, n * 5), new MaterialStack(Mats.MAT_STEEL, n * 2), new MaterialStack(TFCNuclearMats.MAT_BRASS, n), new MaterialStack(TFCNuclearMats.MAT_ROSEGOLD, n))
                .outputs(new MaterialStack(TFCNuclearMats.MAT_REDSTEEL, i)
                ));
        instance.register(new CrucibleRecipe("blue steel")
                .setup(10, new ItemStack(Item.getByNameOrId("tfc:metal/ingot/blue_steel")))
                .inputs(new MaterialStack(TFCNuclearMats.MAT_BLACKSTEEL, n * 5), new MaterialStack(Mats.MAT_STEEL, n * 2), new MaterialStack(TFCNuclearMats.MAT_ELASTICCOPPER, n), new MaterialStack(TFCNuclearMats.MAT_STERLINGSILVER, n))
                .outputs(new MaterialStack(TFCNuclearMats.MAT_BLUESTEEL, i)
                ));
        instance.register(new CrucibleRecipe("rose gold")
                .setup(10, new ItemStack(Item.getByNameOrId("tfc:metal/ingot/rose_gold")))
                .inputs(new MaterialStack(Mats.MAT_COPPER, n * 2), new MaterialStack(Mats.MAT_GOLD, n * 7))
                .outputs(new MaterialStack(TFCNuclearMats.MAT_ROSEGOLD, i)
                ));
        instance.register(new CrucibleRecipe("elastic copper")
                .setup(10, new ItemStack(Item.getByNameOrId("tfc:metal/ingot/bismuth_bronze")))
                .inputs(new MaterialStack(TFCNuclearMats.MAT_ZINC, n * 2), new MaterialStack(Mats.MAT_COPPER, n * 5), new MaterialStack(TFCNuclearMats.MAT_MOLYBDENUM, n))
                .outputs(new MaterialStack(TFCNuclearMats.MAT_ELASTICCOPPER, i)
                ));
        instance.register(new CrucibleRecipe("black bronze")
                .setup(10, new ItemStack(Item.getByNameOrId("tfc:metal/ingot/black_bronze")))
                .inputs(new MaterialStack(TFCNuclearMats.MAT_SILVER, n * 2), new MaterialStack(Mats.MAT_COPPER, n * 5), new MaterialStack(Mats.MAT_GOLD, n * 2))
                .outputs(new MaterialStack(TFCNuclearMats.MAT_BLACKBRONZE, i)
                ));
        instance.register(new CrucibleRecipe("brass")
                .setup(10, new ItemStack(Item.getByNameOrId("tfc:metal/ingot/brass")))
                .inputs(new MaterialStack(Mats.MAT_COPPER, n * 7), new MaterialStack(TFCNuclearMats.MAT_ZINC, n * 2))
                .outputs(new MaterialStack(TFCNuclearMats.MAT_BRASS, i)
                ));
        instance.register(new CrucibleRecipe("sterling silver")
                .setup(10, new ItemStack(Item.getByNameOrId("tfc:metal/ingot/sterling_silver")))
                .inputs(new MaterialStack(Mats.MAT_COPPER, n * 3), new MaterialStack(TFCNuclearMats.MAT_SILVER, n * 6))
                .outputs(new MaterialStack(TFCNuclearMats.MAT_STERLINGSILVER, i)
                ));
    }
}

