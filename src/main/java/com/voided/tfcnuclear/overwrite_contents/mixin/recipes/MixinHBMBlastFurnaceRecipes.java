package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.material.Mats;
import com.hbm.inventory.recipes.BlastFurnaceRecipesNT;
import com.hbm.inventory.recipes.BlastFurnaceRecipe;
import com.hbm.inventory.RecipesCommon;
import com.hbm.items.ModItems;
import com.voided.tfcnuclear.compat.hbm.SlagStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlastFurnaceRecipesNT.class)
public class MixinHBMBlastFurnaceRecipes {

    @Inject(method = "registerDefaults", at = @At("TAIL"), remap = false)
    public void modifyRecipes(CallbackInfo ci) {
        removeRecipes();
        addCustomRecipes();
    }

    private void removeRecipes() {
        BlastFurnaceRecipesNT instance = BlastFurnaceRecipesNT.INSTANCE;

        String[] recipesToRemove = {
                "blast.steelFromIngot",
                "blast.steelFromDust",
                "blast.steelFromOre",
                "blast.steelWithFlux",
                "blast.mingrade",
                "blast.mingradeIngot",
                "blast.mingradeDust",
                "blast.mingradeCursed",
                "blast.mingradeOre",
                "blast.firebrick",
                "blast.firebrickLimestone"
        };

        int removed = 0;
        for (String recipeName : recipesToRemove) {
            // Используем встроенный метод removeRecipeByName
            instance.removeRecipeByName(recipeName);
            removed++;
        }
    }

    private void addCustomRecipes() {
        BlastFurnaceRecipesNT instance = BlastFurnaceRecipesNT.INSTANCE;

        // 1. Рецепты: Шлак (100) + Флюс -> Неочищенная крица (100)
        // Гематитовый шлак
        instance.register((BlastFurnaceRecipe) new BlastFurnaceRecipe("blast.bloom_hematite")
                .setDuration(800)
                .inputItems(
                        new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.HEMATITE_SLAG, 1), 100),
                        new RecipesCommon.OreDictStack("sand", 1)
                )
                .outputItems(
                        createBloomWithAmount("tfc:bloom/unrefined", 100)
                ));

        // Магнетитовый шлак
        instance.register((BlastFurnaceRecipe) new BlastFurnaceRecipe("blast.bloom_magnetite")
                .setDuration(800)
                .inputItems(
                        new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.MAGNETITE_SLAG, 1), 100),
                        new RecipesCommon.OreDictStack("sand", 1)
                )
                .outputItems(
                        createBloomWithAmount("tfc:bloom/unrefined", 100)
                ));

        // Лимонитовый шлак
        instance.register((BlastFurnaceRecipe) new BlastFurnaceRecipe("blast.bloom_limonite")
                .setDuration(800)
                .inputItems(
                        new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.LIMONITE_SLAG, 1), 100),
                        new RecipesCommon.OreDictStack("sand", 1)
                )
                .outputItems(
                        createBloomWithAmount("tfc:bloom/unrefined", 100)
                ));

        // 2. Рецепт: Слиток железа + Флюс -> Неочищенная крица (100)
        instance.register((BlastFurnaceRecipe) new BlastFurnaceRecipe("blast.bloom_iron_ingot")
                .setDuration(800)
                .inputItems(
                        new RecipesCommon.OreDictStack("ingotIron", 1),
                        new RecipesCommon.OreDictStack("sand", 1)
                )
                .outputItems(
                        createBloomWithAmount("tfc:bloom/unrefined", 100)
                ));

        // 3. Рецепт: Пыль железа + Флюс -> Неочищенная крица (100)
        instance.register((BlastFurnaceRecipe) new BlastFurnaceRecipe("blast.bloom_iron_dust")
                .setDuration(800)
                .inputItems(
                        new RecipesCommon.OreDictStack("dustIron", 1),
                        new RecipesCommon.OreDictStack("sand", 1)
                )
                .outputItems(
                        createBloomWithAmount("tfc:bloom/unrefined", 100)
                ));

        // Оригинальные рецепты из вашего кода
        // Рецепт: Iron Ingot + Flux -> Pig Iron
        instance.register((BlastFurnaceRecipe) new BlastFurnaceRecipe("blast.tfc_pig_iron_ingot")
                .setDuration(800)
                .inputItems(
                        new RecipesCommon.OreDictStack("ingotIron", 1),
                        new RecipesCommon.ComparableStack(Item.getByNameOrId("hbm:powder_flux"), 1)
                )
                .outputItems(
                        new ItemStack(Item.getByNameOrId("tfc:metal/ingot/pig_iron"), 1),
                        new ItemStack(ModItems.ingot_raw, 1, Mats.MAT_SLAG.id)
                ));

        // Рецепт: Iron Dust + Flux -> Pig Iron
        instance.register((BlastFurnaceRecipe) new BlastFurnaceRecipe("blast.tfc_pig_iron_dust")
                .setDuration(800)
                .inputItems(
                        new RecipesCommon.OreDictStack("dustIron", 1),
                        new RecipesCommon.ComparableStack(Item.getByNameOrId("hbm:powder_flux"), 1)
                )
                .outputItems(
                        new ItemStack(Item.getByNameOrId("tfc:metal/ingot/pig_iron"), 1),
                        new ItemStack(ModItems.ingot_raw, 1, Mats.MAT_SLAG.id)
                ));

        // Рецепты шлаков -> Pig Iron
        instance.register((BlastFurnaceRecipe) new BlastFurnaceRecipe("blast.pig_iron_hematite")
                .setDuration(800)
                .inputItems(
                        new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.HEMATITE_SLAG, 1), 100),
                        new RecipesCommon.ComparableStack(Item.getByNameOrId("hbm:powder_flux"), 1)
                )
                .outputItems(
                        new ItemStack((Item.getByNameOrId("tfc:metal/ingot/pig_iron")), 1),
                        new ItemStack(ModItems.ingot_raw, 1, Mats.MAT_SLAG.id)
                ));

        instance.register((BlastFurnaceRecipe) new BlastFurnaceRecipe("blast.pig_iron_magnetite")
                .setDuration(800)
                .inputItems(
                        new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.MAGNETITE_SLAG, 1), 100),
                        new RecipesCommon.ComparableStack(Item.getByNameOrId("hbm:powder_flux"), 1)
                )
                .outputItems(
                        new ItemStack((Item.getByNameOrId("tfc:metal/ingot/pig_iron")), 1),
                        new ItemStack(ModItems.ingot_raw, 1, Mats.MAT_SLAG.id)
                ));

        instance.register((BlastFurnaceRecipe) new BlastFurnaceRecipe("blast.pig_iron_limonite")
                .setDuration(800)
                .inputItems(
                        new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.LIMONITE_SLAG, 1), 100),
                        new RecipesCommon.ComparableStack(Item.getByNameOrId("hbm:powder_flux"), 1)
                )
                .outputItems(
                        new ItemStack((Item.getByNameOrId("tfc:metal/ingot/pig_iron")), 1),
                        new ItemStack(ModItems.ingot_raw, 1, Mats.MAT_SLAG.id)
                ));
    }

    /**
     * Вспомогательный метод для создания крицы с нужным количеством через Capability
     */
    private ItemStack createBloomWithAmount(String itemId, int amount) {
        ItemStack stack = new ItemStack(Item.getByNameOrId(itemId), 1);
        net.dries007.tfc.api.capability.forge.IForgeableMeasurableMetal cap =
                (net.dries007.tfc.api.capability.forge.IForgeableMeasurableMetal)
                        stack.getCapability(net.dries007.tfc.api.capability.forge.CapabilityForgeable.FORGEABLE_CAPABILITY, null);
        if (cap != null) {
            cap.setMetal(net.dries007.tfc.api.types.Metal.WROUGHT_IRON);
            cap.setMetalAmount(amount);
        }
        return stack;
    }
}