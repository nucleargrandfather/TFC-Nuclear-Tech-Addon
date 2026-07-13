package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.CentrifugeRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(CentrifugeRecipes.class)
public abstract class MixinCentrifugeRecipes {

    @Shadow
    private static HashMap<RecipesCommon.AStack, ItemStack[]> recipes;

    @Shadow
    public static void addRecipe(RecipesCommon.AStack key, ItemStack[] value) {}

    @Shadow
    public static void removeRecipe(RecipesCommon.AStack key) {}

    /**
     * Инжектируемся в конец метода registerDefaults()
     * чтобы удалить и добавить свои рецепты
     */
    @Inject(
            method = "registerDefaults",
            at = @At("TAIL"),
            remap = false
    )
    private void onRegisterDefaults(CallbackInfo ci) {
        removeVanillaRecipes();
        addCustomRecipes();
    }

    /**
     * Удаление всех рецептов с ванильными рудами
     */
    private void removeVanillaRecipes() {
        // Список всех ванильных руд для удаления
        String[] vanillaOres = {
                "oreCoal", "oreIron", "oreGold", "oreRedstone", "oreLapis",
                "oreDiamond", "oreEmerald"
        };

        // Дополнительные руды из HBM
        String[] hbmOres = {
                "oreSaltpeter", "oreSulfur", "oreCopper", "oreLead", "oreAluminum", "oreLignite"
        };

        // Удаляем все руды
        for (String ore : vanillaOres) {
            removeByOreDict(ore);
        }
        for (String ore : hbmOres) {
            removeByOreDict(ore);
        }

    }

    /**
     * Удаление всех рецептов с бедрок рудами
     */
    private void removeBedrockOreRecipes() {
        HashMap<RecipesCommon.AStack, ItemStack[]> toRemove = new HashMap<>();

        for (Map.Entry<RecipesCommon.AStack, ItemStack[]> entry : recipes.entrySet()) {
            RecipesCommon.AStack key = entry.getKey();
            if (key instanceof ComparableStack) {
                ComparableStack stack = (ComparableStack) key;
                String itemId = Item.REGISTRY.getNameForObject(stack.item).toString();
                if (itemId.equals("hbm:bedrock_ore_new") || itemId.equals("hbm:bedrock_ore")) {
                    toRemove.put(key, entry.getValue());
                }
            }
        }

        for (RecipesCommon.AStack key : toRemove.keySet()) {
            recipes.remove(key);
        }
    }

    /**
     * Добавление кастомных рецептов
     */
    private void addCustomRecipes() {
        recipes.put(new RecipesCommon.OreDictStack("tfcRedstone"), new ItemStack[] {
                new ItemStack(Items.REDSTONE, 3),
                new ItemStack(Items.REDSTONE, 3),
                new ItemStack(ModItems.ingot_mercury, 1),
                new ItemStack(Blocks.GRAVEL, 1) });

        recipes.put(new RecipesCommon.OreDictStack("tfcCryolite"), new ItemStack[] {
                new ItemStack(ModItems.chunk_ore, 2, ItemEnums.EnumChunkType.CRYOLITE.ordinal()),
                new ItemStack(ModItems.powder_titanium, 1),
                new ItemStack(ModItems.powder_iron, 1),
                new ItemStack(Blocks.GRAVEL, 1) });

        recipes.put(new RecipesCommon.OreDictStack("tfcLignite"), new ItemStack[] {
                new ItemStack(ModItems.powder_lignite, 2),
                new ItemStack(ModItems.powder_lignite, 2),
                new ItemStack(ModItems.powder_lignite, 2),
                new ItemStack(Blocks.GRAVEL, 1) });

        recipes.put(new RecipesCommon.OreDictStack("tfcCoal"), new ItemStack[] {
                new ItemStack(ModItems.powder_coal, 2),
                new ItemStack(ModItems.powder_coal, 2),
                new ItemStack(ModItems.powder_coal, 2),
                new ItemStack(Blocks.GRAVEL, 1) });

        recipes.put(new RecipesCommon.OreDictStack("tfcLapis"), new ItemStack[] {
                new ItemStack(ModItems.powder_lapis, 6),
                new ItemStack(ModItems.powder_cobalt_tiny, 1),
                new ItemStack(ModItems.gem_sodalite, 1),
                new ItemStack(Blocks.GRAVEL, 1) });

        recipes.put(new RecipesCommon.OreDictStack("tfcKimberlite"), new ItemStack[] {
                new ItemStack(ModItems.powder_diamond, 1),
                new ItemStack(ModItems.powder_diamond, 1),
                new ItemStack(ModItems.powder_diamond, 1),
                new ItemStack(Blocks.GRAVEL, 1) });
    }

    // ========== Вспомогательные методы для удаления ==========

    private void removeByOreDict(String dictKey) {
        HashMap<RecipesCommon.AStack, ItemStack[]> toRemove = new HashMap<>();

        for (Map.Entry<RecipesCommon.AStack, ItemStack[]> entry : recipes.entrySet()) {
            RecipesCommon.AStack key = entry.getKey();
            if (key instanceof RecipesCommon.OreDictStack) {
                RecipesCommon.OreDictStack oreDict = (RecipesCommon.OreDictStack) key;
                if (oreDict.name.equals(dictKey)) {
                    toRemove.put(key, entry.getValue());
                }
            }
        }

        for (RecipesCommon.AStack key : toRemove.keySet()) {
            recipes.remove(key);
        }
    }

    private void removeByItem(Item item) {
        removeByItem(item, -1);
    }

    private void removeByItem(Item item, int meta) {
        String itemName = Item.REGISTRY.getNameForObject(item).toString();
        removeByItemId(itemName, meta);
    }

    private void removeByItem(String itemId) {
        removeByItemId(itemId, -1);
    }

    private void removeByItemId(String itemId, int meta) {
        HashMap<RecipesCommon.AStack, ItemStack[]> toRemove = new HashMap<>();

        for (Map.Entry<RecipesCommon.AStack, ItemStack[]> entry : recipes.entrySet()) {
            RecipesCommon.AStack key = entry.getKey();
            if (key instanceof ComparableStack) {
                ComparableStack stack = (ComparableStack) key;
                String id = Item.REGISTRY.getNameForObject(stack.item).toString();
                if (id.equals(itemId) && (meta == -1 || stack.meta == meta)) {
                    toRemove.put(key, entry.getValue());
                }
            }
        }

        for (RecipesCommon.AStack key : toRemove.keySet()) {
            recipes.remove(key);
        }
    }

    private void removeByBlock(String blockId) {
        removeByItemId(blockId, -1);
    }
}