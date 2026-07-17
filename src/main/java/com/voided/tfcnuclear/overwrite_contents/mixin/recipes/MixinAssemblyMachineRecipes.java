package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.inventory.OreDictManager.ANY_PLASTIC;
import static com.hbm.inventory.OreDictManager.DURA;

@Mixin(AssemblyMachineRecipes.class)
public class MixinAssemblyMachineRecipes {

    @Inject(method = "registerDefaults", at = @At("TAIL"), remap = false)
    public void modifyRecipes(CallbackInfo ci) {
        removeRecipes();
        addCustomRecipes();
    }

    private void removeRecipes() {
        AssemblyMachineRecipes instance = AssemblyMachineRecipes.INSTANCE;

        // Список рецептов для удаления
        String[] recipesToRemove = {
                "ass.plateiron",
                "ass.platecopper",
                "ass.plategold",
                "ass.excavator",
                "ass.mininglaser"
        };

        int removed = 0;
        for (String recipeName : recipesToRemove) {
            // Используем встроенный метод removeRecipeByName
            instance.removeRecipeByName(recipeName);
            removed++;
        }
    }

    private void addCustomRecipes() {
        AssemblyMachineRecipes instance = AssemblyMachineRecipes.INSTANCE;

        // Рецепт: Wrought Iron Ingot -> Iron Plate (заменяем удаленный ass.plateiron)
        instance.register(new GenericRecipe("ass.new_plateWroughtIron")
                .setup(60, 100)
                .outputItems(new ItemStack(ModItems.plate_iron, 1))
                .inputItems(new RecipesCommon.OreDictStack("ingotWroughtIron"))
                .setPools(GenericRecipes.POOL_PREFIX_ALT + "plates"));
        instance.register(new GenericRecipe("ass.new_plateBronze")
                .setup(60, 100)
                .outputItems(new ItemStack(ModItems.plate_copper, 1))
                .inputItems(new RecipesCommon.OreDictStack("ingotBronze"))
                .setPools(GenericRecipes.POOL_PREFIX_ALT + "plates"));

        // Рецепт: Экскаватор с кованым железом вместо обычного (заменяем удаленный ass.excavator)
        instance.register(new GenericRecipe("ass.new_excavator")
                .setup(200, 100)
                .outputItems(new ItemStack(ModBlocks.machine_excavator, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack("stoneBrick", 8),
                        new RecipesCommon.OreDictStack(STEEL.ingot(), 8),
                        new RecipesCommon.OreDictStack("ingotWroughtIron", 8),
                        new RecipesCommon.ComparableStack(ModItems.motor, 2),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ANALOG)
                ));
        instance.register(new GenericRecipe("ass.mininglaser_new")
                .setup(400, 100)
                .outputItems(new ItemStack(ModBlocks.machine_mining_laser, 1))
                .inputItems(new RecipesCommon.OreDictStack(STEEL.plate(), 16), new RecipesCommon.OreDictStack(TI.shell(), 4), new RecipesCommon.OreDictStack(DURA.plate(), 4), new RecipesCommon.ComparableStack(ModItems.crystal_redstone, 3), new RecipesCommon.ComparableStack(Item.getByNameOrId("tfc:gem/diamond"), 3, 2), new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8), new RecipesCommon.ComparableStack(ModItems.motor, 3))
                .inputItemsEx(new RecipesCommon.ComparableStack(ModItems.item_expensive, 4, ItemEnums.EnumExpensiveType.HEAVY_FRAME), new RecipesCommon.OreDictStack(DURA.plate(), 4), new RecipesCommon.ComparableStack(ModItems.crystal_redstone, 12), new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 16), new RecipesCommon.ComparableStack(ModItems.motor_desh, 3)));

    }
}