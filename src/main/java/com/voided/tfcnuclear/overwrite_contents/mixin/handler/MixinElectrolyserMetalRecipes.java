package com.voided.tfcnuclear.overwrite_contents.mixin.handler;

import com.google.gson.JsonElement;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.ElectrolyserMetalRecipes;
import com.hbm.items.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ElectrolyserMetalRecipes.class)
public abstract class MixinElectrolyserMetalRecipes {

    @Shadow
    private static HashMap<RecipesCommon.AStack, ElectrolyserMetalRecipes.ElectrolysisMetalRecipe> recipes;

    /**
     * Удаляем рецепты ДО регистрации (для случая, если JSON нет)
     */
    @Inject(
            method = "registerDefaults",
            at = @At("HEAD"),
            remap = false
    )
    private void onRegisterDefaultsHead(CallbackInfo ci) {
        System.out.println("[TFC Nuclear] Electrolyser recipes before removal (registerDefaults HEAD): " + recipes.size());
        removeVanillaRecipes();
        System.out.println("[TFC Nuclear] Electrolyser recipes after removal (registerDefaults HEAD): " + recipes.size());
    }

    /**
     * Удаляем рецепты ПОСЛЕ регистрации (если JSON не загружен)
     */
    @Inject(
            method = "registerDefaults",
            at = @At("TAIL"),
            remap = false
    )
    private void onRegisterDefaultsTail(CallbackInfo ci) {
        System.out.println("[TFC Nuclear] Electrolyser recipes before removal (registerDefaults TAIL): " + recipes.size());
        removeVanillaRecipes();
        System.out.println("[TFC Nuclear] Electrolyser recipes after removal (registerDefaults TAIL): " + recipes.size());
    }

    /**
     * Удаляем рецепты ПОСЛЕ загрузки из JSON
     * deleteRecipes() вызывается перед загрузкой JSON, а readRecipe() - для каждого рецепта
     * Используем deleteRecipes для очистки и добавления своих рецептов
     */
    @Inject(
            method = "deleteRecipes",
            at = @At("RETURN"),
            remap = false
    )
    private void onDeleteRecipes(CallbackInfo ci) {
        System.out.println("[TFC Nuclear] Electrolyser recipes cleared (deleteRecipes)");
    }

    /**
     * После загрузки JSON, удаляем ненужные рецепты
     * Используем readRecipe - он вызывается для каждого рецепта из JSON
     * Но мы не можем удалять во время чтения, поэтому используем отдельный метод
     */
    @Inject(
            method = "readRecipe",
            at = @At("TAIL"),
            remap = false
    )
    private void onReadRecipe(JsonElement recipe, CallbackInfo ci) {
        // Этот метод вызывается для каждого рецепта из JSON
        // Мы не можем здесь удалять, потому что это вызовет ConcurrentModificationException
    }

    /**
     * Используем статический блок или инициализацию после загрузки
     * Вместо этого, используем инжект в конструктор или в методы, вызываемые после загрузки
     */
    @Inject(
            method = "<init>",
            at = @At("RETURN"),
            remap = false
    )
    private void onInit(CallbackInfo ci) {
        System.out.println("[TFC Nuclear] ElectrolyserMetalRecipes initialized");
    }

    private void removeVanillaRecipes() {
        // Удаляем рецепт для crystal_trixite
        boolean removed = removeRecipe(new ItemStack(ModItems.crystal_trixite));

        if (removed) {
            System.out.println("[TFC Nuclear] Removed electrolyser recipe for crystal_trixite");
        } else {
            System.out.println("[TFC Nuclear] Failed to remove electrolyser recipe for crystal_trixite - trying alternative...");
            // Пробуем удалить через ID
            removed = removeRecipe("hbm:crystal_trixite");
            if (removed) {
                System.out.println("[TFC Nuclear] Removed electrolyser recipe for crystal_trixite by ID");
            }
        }

        // Выводим все рецепты для отладки
        System.out.println("[TFC Nuclear] Current electrolyser recipes (" + recipes.size() + "):");
        int count = 0;
        for (Map.Entry<RecipesCommon.AStack, ElectrolyserMetalRecipes.ElectrolysisMetalRecipe> entry : recipes.entrySet()) {
            RecipesCommon.AStack key = entry.getKey();
            if (key instanceof RecipesCommon.ComparableStack) {
                RecipesCommon.ComparableStack comp = (RecipesCommon.ComparableStack) key;
                if (comp.item != null) {
                    System.out.println("  " + (++count) + ". " + comp.item.getRegistryName() + " (meta: " + comp.meta + ")");
                } else {
                    System.out.println("  " + (++count) + ". NULL item");
                }
            } else if (key instanceof RecipesCommon.OreDictStack) {
                RecipesCommon.OreDictStack ore = (RecipesCommon.OreDictStack) key;
                System.out.println("  " + (++count) + ". oreDict: " + ore.name);
            } else {
                System.out.println("  " + (++count) + ". " + key);
            }
        }
    }

    private boolean removeRecipe(ItemStack input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        RecipesCommon.ComparableStack keyToRemove = null;

        for (Map.Entry<RecipesCommon.AStack, ElectrolyserMetalRecipes.ElectrolysisMetalRecipe> entry : recipes.entrySet()) {
            RecipesCommon.AStack key = entry.getKey();

            if (key instanceof RecipesCommon.ComparableStack) {
                RecipesCommon.ComparableStack comp = (RecipesCommon.ComparableStack) key;

                if (comp.item == input.getItem() && comp.meta == input.getItemDamage()) {
                    keyToRemove = comp;
                    break;
                }
            }
        }

        if (keyToRemove != null) {
            recipes.remove(keyToRemove);
            return true;
        }

        return false;
    }

    private boolean removeRecipe(String itemId) {
        return removeRecipe(itemId, -1);
    }

    private boolean removeRecipe(String itemId, int meta) {
        Item item = Item.getByNameOrId(itemId);
        if (item == null) {
            return false;
        }

        RecipesCommon.ComparableStack keyToRemove = null;

        for (Map.Entry<RecipesCommon.AStack, ElectrolyserMetalRecipes.ElectrolysisMetalRecipe> entry : recipes.entrySet()) {
            RecipesCommon.AStack key = entry.getKey();

            if (key instanceof RecipesCommon.ComparableStack) {
                RecipesCommon.ComparableStack comp = (RecipesCommon.ComparableStack) key;

                if (comp.item == item && (meta == -1 || comp.meta == meta)) {
                    keyToRemove = comp;
                    break;
                }
            }
        }

        if (keyToRemove != null) {
            recipes.remove(keyToRemove);
            return true;
        }

        return false;
    }
}