package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.ArcWelderRecipes;
import com.hbm.inventory.recipes.ArcWelderRecipes.ArcWelderRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ArcWelderRecipes.class, remap = false)
public class MixinArcWelderRecipes {

    @Shadow
    private static List<ArcWelderRecipe> recipes;

    @Inject(method = "registerDefaults", at = @At("TAIL"), remap = false)
    public void addCustomRecipes(CallbackInfo ci) {

        String[] metals = {
                "bismuth", "bismuth_bronze", "black_bronze", "black_steel", "blue_steel",
                "brass", "bronze", "gold", "nickel", "pig_iron",
                "platinum", "red_steel", "rose_gold", "silver", "sterling_silver",
                "tin", "weak_blue_steel", "weak_red_steel", "weak_steel", "wrought_iron", "zinc"
        };
        for (String metal : metals) {
            Item doubleIngot = Item.getByNameOrId("tfc:metal/double_ingot/" + metal);
            Item ingot = Item.getByNameOrId("tfc:metal/ingot/" + metal);

            if (doubleIngot != null && ingot != null) {
                recipes.add(new ArcWelderRecipe(
                        new ItemStack(ingot, 2),
                        80,
                        100L,
                        new ComparableStack(doubleIngot, 1)
                ));
            }
        }
        for (String metal : metals) {
            Item doubleSheet = Item.getByNameOrId("tfc:metal/double_sheet/" + metal);
            Item sheet = Item.getByNameOrId("tfc:metal/sheet/" + metal);

            if (doubleSheet != null && sheet != null) {
                recipes.add(new ArcWelderRecipe(
                        new ItemStack(sheet, 2),
                        80,
                        100L,
                        new ComparableStack(doubleSheet, 1)
                ));
            }
        }
        for (String metal : metals) {
            Item doubleIngot = Item.getByNameOrId("tfc:metal/double_ingot/" + metal);
            Item sheet = Item.getByNameOrId("tfc:metal/sheet/" + metal);

            if (doubleIngot != null && sheet != null) {
                recipes.add(new ArcWelderRecipe(
                        new ItemStack(sheet, 1),
                        120,
                        200L,
                        new ComparableStack(doubleIngot, 1)
                ));
            }
        }
        for (String metal : metals) {
            Item sheet = Item.getByNameOrId("tfc:metal/sheet/" + metal);
            Item doubleSheet = Item.getByNameOrId("tfc:metal/double_sheet/" + metal);

            if (sheet != null && doubleSheet != null) {
                recipes.add(new ArcWelderRecipe(
                        new ItemStack(doubleSheet, 1),
                        120,
                        180L,
                        new ComparableStack(sheet, 2)
                ));
            }
        }
        for (String metal : metals) {
            Item ingot = Item.getByNameOrId("tfc:metal/ingot/" + metal);
            Item doubleIngot = Item.getByNameOrId("tfc:metal/double_ingot/" + metal);

            if (ingot != null && doubleIngot != null) {
                recipes.add(new ArcWelderRecipe(
                        new ItemStack(doubleIngot, 1),
                        100,
                        150L,
                        new ComparableStack(ingot, 2)
                ));
            }
        }
        for (String metal : metals) {
            Item doubleSheet = Item.getByNameOrId("tfc:metal/double_sheet/" + metal);
            Item tuyere = Item.getByNameOrId("tfc:metal/tuyere/" + metal);

            if (doubleSheet != null && tuyere != null) {
                recipes.add(new ArcWelderRecipe(
                        new ItemStack(tuyere, 1),
                        100,
                        150L,
                        new ComparableStack(doubleSheet, 1)
                ));
            }
        }
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/tuyere/steel"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/double_sheet/steel"), 1)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/tuyere/copper"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/double_sheet/copper"), 1)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/double_ingot/steel"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("hbm:ingot_steel"), 2)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/double_ingot/lead"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("hbm:ingot_lead"), 2)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/double_ingot/copper"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("hbm:ingot_copper"), 2)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/double_sheet/steel"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/sheet/steel"), 2)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/double_sheet/lead"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/sheet/lead"), 2)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/double_sheet/copper"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/sheet/copper"), 2)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/sheet/steel"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/double_ingot/steel"), 2)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/sheet/lead"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/double_ingot/lead"), 2)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/sheet/copper"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/double_ingot/copper"), 2)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/ingot/high_carbon_black_steel"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/ingot/weak_steel"), 1),
                new ComparableStack(Item.getByNameOrId("tfc:metal/ingot/pig_iron"), 1),
                new ComparableStack(Item.getByNameOrId("hbm:powder_flux"), 1)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("minecraft:iron_bars"), 8),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/sheet/wrought_iron"), 1)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("minecraft:iron_bars"),16),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/double_sheet/wrought_iron"), 1)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/ingot/high_carbon_red_steel"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/ingot/weak_red_steel"), 1),
                new ComparableStack(Item.getByNameOrId("tfc:metal/ingot/black_steel"), 1),
                new ComparableStack(Item.getByNameOrId("hbm:powder_flux"), 1)
        ));
        recipes.add(new ArcWelderRecipe(
                new ItemStack(Item.getByNameOrId("tfc:metal/ingot/high_carbon_blue_steel"), 1),
                100,
                150,
                new ComparableStack(Item.getByNameOrId("tfc:metal/ingot/weak_blue_steel"), 1),
                new ComparableStack(Item.getByNameOrId("tfc:metal/ingot/black_steel"), 1),
                new ComparableStack(Item.getByNameOrId("hbm:powder_flux"), 1)
        ));
    }
}