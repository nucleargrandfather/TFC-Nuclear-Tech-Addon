package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.ShredderRecipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(value = ShredderRecipes.class, remap = false)
public abstract class MixinShredderRecipes {

    @Shadow
    public static HashMap<ComparableStack, ItemStack> shredderRecipes;

    @Shadow
    public abstract String getComment();

    private static final String[] INPUT_ITEMS_TO_REMOVE = {
            "hbm:ingot_bismuth",
            "hbm:block_bismuth",
            "hbm:ingot_bismuth_bronze",
            "hbm:ingot_steel",
            "hbm:block_steel",
            "hbm:plate_steel",
            "hbm:plate_gold",
            "hbm:ore_gneiss_gold",
            "hbm:plate_copper",
            "hbm:ore_gneiss_copper",
            "hbm:ingot_copper",
            "hbm:block_copper",
            "hbm:ore_copper",
            "hbm:ingot_lead",
            "hbm:plate_lead",
            "hbm:block_lead",
            "hbm:ore_lead",
            "hbm:ore_niter",
            "hbm:ore_lignite",
            "hbm:lignite",
            "hbm:ore_sulfur",
            "hbm:ore_aluminium",
            "hbm:ore_sellafield_emerald",
            "hbm:ore_sellafield_diamond",
            "hbm:ore_gneiss_iron",
            "hbm:ore_gneiss_copper",
            "hbm:ore_gneiss_gold",
            "hbm:stone_resource",
            "hbm:crystal_trixite",

            "tfc:metal/ingot/bronze",
            "tfc:metal/ingot/black_bronze",
            "tfc:metal/ingot/bismuth_bronze",
            "tfc:metal/ingot/steel",
            "tfc:metal/dust/steel",
            "tfc:metal/ingot/gold",
            "tfc:metal/dust/gold",
            "tfc:metal/ingot/copper",
            "tfc:metal/dust/copper",
            "tfc:metal/ingot/lead",
            "tfc:metal/dust/lead",
            "tfc:ore/lapis_lazuli",
            "tfc:ore/saltpeter",
            "tfc:ore/sulfur",
            "tfc:ore/borax",
            "tfc:ore/graphite",
            "tfc:powder_sulfur",
            "tfc:ore/kaolinite",

            "minecraft:gold_ore",
            "minecraft:gold_ingot",
            "minecraft:gold_block",
            "minecraft:redstone_ore",
            "minecraft:diamond_ore",
            "minecraft:dye_ore",
            "minecraft:coal_ore",
            "minecraft:iron_ore",
            "minecraft:lapis_ore",
            "minecraft:lapis_block",
            "minecraft:emerald_ore",
            "minecraft:sandstone_stairs",
            "minecraft:sandstone",
            "minecraft:gravel",
            "minecraft:sand",
            "minecraft:cobblestone",
            "minecraft:stonebrick"
    };

    // Массив OreDict имён для удаления
    private static final String[] ORE_DICT_TO_REMOVE = {
            "ingotMalachite",
            "gemPetCoke",
            "gemLigniteCoke",
            "gemCoalCoke",
            "blockCoalCoke",
            "blockLigniteCoke",
            "blockPetCoke"
    };

    @Inject(method = "registerPost", at = @At("RETURN"))
    private void onRegisterPost(CallbackInfo ci) {
        // 1. Удаляем рецепты по конкретным предметам
        removeRecipesByItem();

        // 2. Удаляем рецепты по OreDict
        removeRecipesByOreDict();

        // 3. Добавляем свои рецепты
        addCustomRecipes();

        // 4. Очищаем JEI кеш
        clearJeiCache();
    }

    /**
     * Удаляет рецепты по конкретным ID предметов
     */
    private void removeRecipesByItem() {
        List<ComparableStack> toRemove = new ArrayList<>();
        for (Map.Entry<ComparableStack, ItemStack> entry : shredderRecipes.entrySet()) {
            ItemStack input = entry.getKey().toStack();
            if (input.isEmpty()) continue;
            if (matchesInputItem(input)) {
                toRemove.add(entry.getKey());
            }
        }
        for (ComparableStack key : toRemove) {
            shredderRecipes.remove(key);
        }
    }

    /**
     * Удаляет рецепты по OreDict именам
     */
    private void removeRecipesByOreDict() {
        List<ComparableStack> toRemove = new ArrayList<>();

        for (String oreDictName : ORE_DICT_TO_REMOVE) {
            List<ItemStack> ores = OreDictionary.getOres(oreDictName);

            if (ores.isEmpty()) {
                System.out.println("[TFC-Nuclear] OreDict not found for removal: " + oreDictName);
                continue;
            }

            // Для каждого предмета из OreDict проверяем наличие рецепта
            for (ItemStack ore : ores) {
                if (ore.isEmpty()) continue;

                ComparableStack comp = new ComparableStack(ore).makeSingular();

                // Проверяем точное совпадение
                if (shredderRecipes.containsKey(comp)) {
                    toRemove.add(comp);
                }

                // Проверяем с wildcard метой (если есть)
                ComparableStack wildcardComp = new ComparableStack(ore.getItem(), 1, OreDictionary.WILDCARD_VALUE);
                if (shredderRecipes.containsKey(wildcardComp)) {
                    toRemove.add(wildcardComp);
                }
            }
        }

        // Удаляем найденные рецепты
        for (ComparableStack key : toRemove) {
            shredderRecipes.remove(key);
        }

        if (!toRemove.isEmpty()) {
            System.out.println("[TFC-Nuclear] Removed " + toRemove.size() + " shredder recipes via OreDict");
        }
    }

    /**
     * Добавляет все кастомные рецепты
     */
    private void addCustomRecipes() {
        addRecipeByItem("tfc:metal/ingot/black_bronze", 0, "tfc:metal/dust/black_bronze", 0, 1);
        addRecipeByItem("tfc:metal/ingot/bronze", 0, "tfc:metal/dust/bronze", 0, 1);
        addRecipeByItem("tfc:metal/ingot/bismuth_bronze", 0, "tfc:metal/dust/bismuth_bronze", 0, 1);
        addRecipeByItem("hbm:plate_steel", 0, "hbm:powder_steel", 0, 1);
        addRecipeByItem("hbm:block_steel", 0, "hbm:powder_steel", 0, 9);
        addRecipeByItem("hbm:ingot_steel", 0, "hbm:powder_steel", 0, 1);
        addRecipeByItem("tfc:metal/ingot/gold", 0, "hbm:powder_gold", 0, 1);
        addRecipeByItem("hbm:plate_gold", 0, "hbm:powder_gold", 0, 1);
        addRecipeByItem("minecraft:gold_block", 0, "hbm:powder_gold", 0, 9);
        addRecipeByItem("hbm:ingot_copper", 0, "hbm:powder_copper", 0, 1);
        addRecipeByItem("hbm:plate_copper", 0, "tfc:powder/bronze", 0, 1);
        addRecipeByItem("hbm:block_copper", 0, "hbm:powder_copper", 0, 9);
        addRecipeByItem("hbm:ingot_lead", 0, "hbm:powder_lead", 0, 1);
        addRecipeByItem("hbm:plate_lead", 0, "hbm:powder_lead", 0, 1);
        addRecipeByItem("hbm:block_lead", 0, "hbm:powder_lead", 0, 9);
        addRecipeByItem("tfc:ore/cinnabar", 0, "minecraft:redstone", 0, 4);
        addRecipeByItem("tfc:ore/kimberlite", 0, "hbm:gravel_diamond", 0, 2);
        addRecipeByItem("tfc:ore/lapis_lazuli", 0, "hbm:powder_lapis", 0, 4);
        addRecipeByItem("minecraft:lapis_block", 0, "hbm:powder_lapis", 0, 36);
        addRecipeByItem("tfc:ore/saltpeter", 0, "hbm:niter", 0, 2);
        addRecipeByItem("tfc:ore/cryolite", 0, "hbm:chunk_ore", 2, 2);
        addRecipeByItem("tfc:ore/sulfur", 0, "hbm:sulfur", 0, 2);
        addRecipeByItem("tfc:ore/borax", 0, "hbm:powder_borax", 0, 2);
        addRecipeByItem("tfc:ore/lignite", 0, "hbm:powder_lignite", 0, 2);
        addRecipeByItem("tfc:ore/graphite", 0, "tfc:powder/graphite", 0, 2);
        addRecipeByItem("tfc:ore/bituminous_coal", 0, "hbm:powder_coal", 0, 2);
        addRecipeByItem("tfc:ore/petryfied_wood", 0, "hbm:powder_coal_tiny", 0, 4);
        addRecipeByItem("hbm:stone_resource", 1, "hbm:powder_asbestos", 0, 2);
        addRecipeByItem("hbm:stone_resource", 0, "hbm:sulfur", 0, 2);
        addRecipeByItem("hbm:stone_resource", 4, "hbm:powder_limestone", 0, 4);
        addRecipeByItem("tfc:ore/kaolinite", 0, "tfc:powder/kaolinite", 0, 6);
        addRecipeByItem("tfc:ore/petrified_wood", 0, "hbm:powder_coal_tiny", 0, 4);

        addOreDictRecipe("rockFlux", "hbm:powder_flux", 0, 1);
        addOreDictRecipe("sand", "hbm:dust", 0, 2);
        addOreDictRecipe("gravel", "minecraft:sand", 0, 1);
        addOreDictRecipe("stone", "minecraft:gravel", 0, 1);
        addOreDictRecipe("cobblestone", "minecraft:gravel", 0, 1);
        addOreDictRecipe("stoneBrick", "minecraft:gravel", 0, 1);
    }

    /**
     * Очищает кеш JEI
     */
    private void clearJeiCache() {
        try {
            java.lang.reflect.Field jeiField = ShredderRecipes.class.getDeclaredField("jeiShredderRecipes");
            jeiField.setAccessible(true);
            jeiField.set(null, null);
        } catch (Exception e) {
            // Игнорируем ошибки
        }
    }

    private static boolean matchesInputItem(ItemStack input) {
        String registryName = input.getItem().getRegistryName().toString();
        for (String target : INPUT_ITEMS_TO_REMOVE) {
            if (registryName.equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Добавляет рецепт для шредера с указанным предметом
     */
    private void addRecipeByItem(String inputItemId, int inputMeta, String outputItemId, int outputMeta, int outputCount) {
        ItemStack input = getItemStack(inputItemId, inputMeta, 1);
        ItemStack output = getItemStack(outputItemId, outputMeta, outputCount);
        if (!input.isEmpty() && !output.isEmpty()) {
            ShredderRecipes.setRecipe(input, output);
        }
    }

    private ItemStack getItemStack(String itemId, int meta, int count) {
        net.minecraft.item.Item item = net.minecraft.item.Item.getByNameOrId(itemId);
        if (item != null) {
            return new ItemStack(item, count, meta);
        }
        return ItemStack.EMPTY;
    }

    /**
     * Добавляет рецепт для шредера с использованием OreDict
     */
    private void addOreDictRecipe(String oreDictName, String outputItemId, int outputMeta, int outputCount) {
        List<ItemStack> ores = OreDictionary.getOres(oreDictName);
        if (ores.isEmpty()) {
            System.out.println("[TFC-Nuclear] OreDict not found for Shredder: " + oreDictName);
            return;
        }

        ItemStack output = getItemStack(outputItemId, outputMeta, outputCount);
        if (output.isEmpty()) {
            System.out.println("[TFC-Nuclear] Output item not found: " + outputItemId);
            return;
        }

        for (ItemStack input : ores) {
            if (!input.isEmpty()) {
                ShredderRecipes.setRecipe(input.copy(), output.copy());
            }
        }
    }
}