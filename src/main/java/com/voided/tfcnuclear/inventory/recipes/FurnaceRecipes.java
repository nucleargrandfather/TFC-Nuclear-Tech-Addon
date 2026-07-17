package com.voided.tfcnuclear.inventory.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FurnaceRecipes {

    private static boolean recipesAdded = false;

    // Список ResourceLocation для удаления (печки)
    private static final List<ResourceLocation> RECIPES_TO_REMOVE = Arrays.asList(
            // Vanilla
            new ResourceLocation("minecraft", "iron_ingot"),
            new ResourceLocation("minecraft", "gold_ingot"),
            new ResourceLocation("minecraft", "gold_nugget"),
            new ResourceLocation("minecraft", "diamond"),
            new ResourceLocation("minecraft", "emerald"),
            new ResourceLocation("minecraft", "redstone"),
            new ResourceLocation("minecraft", "dye"),
            new ResourceLocation("minecraft", "coal"),
            new ResourceLocation("minecraft", "brick"),
            new ResourceLocation("minecraft", "stonebrick"),

            // HBM
            new ResourceLocation("hbm", "ingot_copper"),
            new ResourceLocation("hbm", "ingot_lead"),
            new ResourceLocation("hbm", "ingot_firebrick")
    );

    private static final Map<String, FurnaceRecipeEntry> RECIPES = new HashMap<>();

    static {
        // Обычные рецепты с ID предмета
        RECIPES.put("hbm:powder_gold", new FurnaceRecipeEntry("tfc:metal/ingot/gold", 1, 1.0f));
        RECIPES.put("hbm:crystal_gold", new FurnaceRecipeEntry("tfc:metal/ingot/gold", 2, 0.7f));
        RECIPES.put("hbm:crystal_redstone", new FurnaceRecipeEntry("minecraft:redstone", 6, 0.5f));
        RECIPES.put("hbm:powder_lapis", new FurnaceRecipeEntry("minecraft:dye", 1, 4, 0.5f));
        RECIPES.put("tfc:ore/cryolite", new FurnaceRecipeEntry("hbm:chunk_ore", 1, 2, 0.5f));
        RECIPES.put("tfc:ceramics/unfired/clay_brick", new FurnaceRecipeEntry("minecraft:brick", 1, 0, 0.5f));
        RECIPES.put("minecraft:clay_ball", new FurnaceRecipeEntry("hbm:ball_fireclay", 1, 0, 0.5f));
        RECIPES.put("tfc:ceramics/unfired/fire_brick", new FurnaceRecipeEntry("hbm:ingot_firebrick", 1, 0, 0.5f));
        RECIPES.put("tfc:glass_shard", new FurnaceRecipeEntry("minecraft:glass", 1, 0.7f));
        RECIPES.put("hbm:catalyst_clay", new FurnaceRecipeEntry("tfcnuclear:fired_catalyst_clay", 1, 0.7f));

        // OreDict рецепты
        addOreDictFurnaceRecipe("sand", "minecraft:glass", 1, 0.7f);
        addOreDictFurnaceRecipe("gravel", "minecraft:cobblestone", 1, 0.7f);
    }

    public static void addRecipes() {
        if (recipesAdded) return;

        removeFurnaceRecipes();
        registerFurnaceRecipes();

        recipesAdded = true;
    }

    private static void removeFurnaceRecipes() {
        Map<ItemStack, ItemStack> smeltingList = net.minecraft.item.crafting.FurnaceRecipes.instance().getSmeltingList();
        List<ItemStack> toRemove = new java.util.ArrayList<>();

        for (Map.Entry<ItemStack, ItemStack> entry : smeltingList.entrySet()) {
            ItemStack output = entry.getValue();
            ResourceLocation outputName = output.getItem().getRegistryName();

            if (outputName != null) {
                for (ResourceLocation toRemoveLoc : RECIPES_TO_REMOVE) {
                    if (outputName.toString().equals(toRemoveLoc.toString())) {
                        toRemove.add(entry.getKey());
                        break;
                    }
                }
            }
        }

        for (ItemStack input : toRemove) {
            smeltingList.remove(input);
        }

        System.out.println("[TFC Nuclear] Removed " + toRemove.size() + " furnace recipes");
    }

    private static void registerFurnaceRecipes() {
        for (Map.Entry<String, FurnaceRecipeEntry> entry : RECIPES.entrySet()) {
            String inputId = entry.getKey();
            FurnaceRecipeEntry recipe = entry.getValue();

            if (inputId.startsWith("oreDict:")) {
                String oreDictName = inputId.substring(8);
                addOreDictFurnaceRecipe(oreDictName, recipe.outputId, recipe.count, recipe.meta, recipe.experience);
                continue;
            }

            Item inputItem = Item.getByNameOrId(inputId);
            Item outputItem = Item.getByNameOrId(recipe.outputId);

            if (inputItem != null && outputItem != null) {
                ItemStack input = new ItemStack(inputItem);
                ItemStack output = new ItemStack(outputItem, recipe.count, recipe.meta);

                if (!input.isEmpty() && !output.isEmpty()) {
                    GameRegistry.addSmelting(input, output, recipe.experience);
                }
            }
        }
    }

    private static void addOreDictFurnaceRecipe(String oreDictName, String outputId, int count, int meta, float experience) {
        List<ItemStack> ores = OreDictionary.getOres(oreDictName);

        if (ores.isEmpty()) {
            return;
        }

        Item outputItem = Item.getByNameOrId(outputId);
        if (outputItem == null) {
            return;
        }

        ItemStack output = new ItemStack(outputItem, count, meta);
        if (output.isEmpty()) {
            return;
        }

        for (ItemStack input : ores) {
            if (!input.isEmpty()) {
                GameRegistry.addSmelting(input.copy(), output.copy(), experience);
            }
        }
    }

    private static void addOreDictFurnaceRecipe(String oreDictName, String outputId, int count, float experience) {
        addOreDictFurnaceRecipe(oreDictName, outputId, count, 0, experience);
    }

    private static class FurnaceRecipeEntry {
        final String outputId;
        final int count;
        final int meta;
        final float experience;

        FurnaceRecipeEntry(String outputId, int count, float experience) {
            this(outputId, count, 0, experience);
        }

        FurnaceRecipeEntry(String outputId, int count, int meta, float experience) {
            this.outputId = outputId;
            this.count = count;
            this.meta = meta;
            this.experience = experience;
        }
    }
}