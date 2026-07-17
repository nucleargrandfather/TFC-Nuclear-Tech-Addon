package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.CentrifugeRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

    @Inject(
            method = "registerDefaults",
            at = @At("TAIL"),
            remap = false
    )
    private void onRegisterDefaults(CallbackInfo ci) {
        removeVanillaRecipes();
        addCustomRecipes();
    }

    private void removeVanillaRecipes() {

        String[] vanillaOres = {
                "oreCoal", "oreIron", "oreGold", "oreRedstone", "oreLapis",
                "oreDiamond", "oreEmerald"
        };

        String[] hbmOres = {
                "oreSaltpeter", "oreSulfur", "oreCopper", "oreLead", "oreAluminum", "oreLignite", "oreTrixite"
        };

        for (String ore : vanillaOres) {
            removeByOreDict(ore);
        }
        for (String ore : hbmOres) {
            removeByOreDict(ore);
        }

        removeByComparableStack(new RecipesCommon.ComparableStack(ModBlocks.ore_tikite));
        removeByComparableStack(new RecipesCommon.ComparableStack(ModItems.crystal_trixite));
    }

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

    private void removeByOreDict(String dictKey) {
        RecipesCommon.OreDictStack keyToRemove = null;

        for (Map.Entry<RecipesCommon.AStack, ItemStack[]> entry : recipes.entrySet()) {
            RecipesCommon.AStack key = entry.getKey();
            if (key instanceof RecipesCommon.OreDictStack) {
                RecipesCommon.OreDictStack oreDict = (RecipesCommon.OreDictStack) key;
                if (oreDict.name.equals(dictKey)) {
                    keyToRemove = oreDict;
                    break;
                }
            }
        }

        if (keyToRemove != null) {
            recipes.remove(keyToRemove);
        }
    }

    private void removeByComparableStack(RecipesCommon.ComparableStack comparableStack) {
        recipes.remove(comparableStack);
    }
}