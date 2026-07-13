package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.SILEXRecipes;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemFELCrystal;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(SILEXRecipes.class)
public class MixinSILEXRecipes {

    @Shadow
    public static LinkedHashMap<Object, SILEXRecipes.SILEXRecipe> recipes;

    // TFC золотой самородок — загружаем один раз статически
    private static final Item TFC_GOLD_NUGGET = Item.getByNameOrId("tfc:metal/nugget/gold");

    // Удаляем оригинальные рецепты ДО того, как они будут добавлены
    @Inject(
            method = "register",
            at = @At("HEAD"),
            remap = false
    )
    private static void onRegisterHead(CallbackInfo ci) {
        // Удаляем все 10 оригинальных рецептов HEAUS (0-4 и 5-9)
        for (int meta = 0; meta < 10; meta++) {
            RecipesCommon.ComparableStack key = new RecipesCommon.ComparableStack(ModItems.rbmk_pellet_heaus, 1, meta);
            recipes.remove(key);
        }
    }

    // Добавляем свои рецепты ПОСЛЕ того, как отработала оригинальная регистрация
    @Inject(
            method = "register",
            at = @At("RETURN"),
            remap = false
    )
    private static void onRegisterReturn(CallbackInfo ci) {

        recipes.put("gravel",
                new SILEXRecipes.SILEXRecipe(1000, 250, ItemFELCrystal.EnumWavelengths.VISIBLE)
                        .addOut(new ItemStack(Items.FLINT), 80)
                        .addOut(new ItemStack(ModItems.powder_boron), 5)
                        .addOut(new ItemStack(ModItems.powder_lithium), 10)
                        .addOut(new ItemStack(ModItems.fluorite), 5)
        );

        // Проверяем, что TFC предмет найден
        if (TFC_GOLD_NUGGET == null) {
            throw new RuntimeException("TFC gold nugget not found! Check item ID: tfc:metal/nugget/gold");
        }

        // Добавляем рецепты для всех 10 состояний (0-4 и 5-9)
        for (int i = 0; i < 10; i++) {

            // Для мета-значений 0-4 (свежие таблетки)
            if (i < 5) {
                SILEXRecipes.SILEXRecipe recipe = new SILEXRecipes.SILEXRecipe(600, 100, 2)
                        .addOut(new ItemStack(ModItems.nugget_australium_greater), 90 - i * 20)
                        .addOut(new ItemStack(ModItems.nugget_au198), 5 + 10 * i)
                        .addOut(new ItemStack(TFC_GOLD_NUGGET), 3 + 6 * i)      // ← TFC золото
                        .addOut(new ItemStack(ModItems.nugget_pb209), 2 + 4 * i);

                recipes.put(new RecipesCommon.ComparableStack(ModItems.rbmk_pellet_heaus, 1, i), recipe);
            }

            // Для мета-значений 5-9 (таблетки с ксеноном)
            if (i >= 5) {
                int j = i - 5; // j от 0 до 4
                SILEXRecipes.SILEXRecipe recipe = new SILEXRecipes.SILEXRecipe(600, 100, 2)
                        .addOut(new ItemStack(ModItems.powder_xe135_tiny), 1)
                        .addOut(new ItemStack(ModItems.nugget_australium_greater), 89 - j * 20)
                        .addOut(new ItemStack(ModItems.nugget_au198), 5 + 10 * j)
                        .addOut(new ItemStack(TFC_GOLD_NUGGET), 3 + 6 * j)      // ← TFC золото
                        .addOut(new ItemStack(ModItems.nugget_pb209), 2 + 4 * j);

                recipes.put(new RecipesCommon.ComparableStack(ModItems.rbmk_pellet_heaus, 1, i), recipe);
            }
        }
    }
}