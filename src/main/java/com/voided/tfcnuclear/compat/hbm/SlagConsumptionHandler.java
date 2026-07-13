package com.voided.tfcnuclear.compat.hbm;

import com.hbm.inventory.recipes.BlastFurnaceRecipe;
import com.voided.tfcnuclear.inventory.items.ItemSlagBase;
import com.voided.tfcnuclear.inventory.items.ModItems;
import net.minecraft.item.ItemStack;

public class SlagConsumptionHandler {

    public static ItemStack processSlagConsumption(ItemStack inputStack, BlastFurnaceRecipe recipe) {
        if (inputStack.isEmpty() || !(inputStack.getItem() instanceof ItemSlagBase)) {
            return inputStack;
        }

        int consumed = getConsumedAmount(recipe);
        if (consumed <= 0) {
            return inputStack;
        }

        int currentAmount = ItemSlagBase.getAmount(inputStack);
        int newAmount = currentAmount - consumed;

        if (newAmount <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack result = new ItemStack(inputStack.getItem());
        ItemSlagBase.setAmount(result, newAmount);

        return result;
    }

    private static int getConsumedAmount(BlastFurnaceRecipe recipe) {
        if (recipe.inputItem != null) {
            for (int i = 0; i < recipe.inputItem.length; i++) {
                if (recipe.inputItem[i] instanceof SlagStack) {
                    return ((SlagStack) recipe.inputItem[i]).requiredAmount;
                }
            }
        }

        String recipeName = recipe.toString();
        if (recipeName != null && recipeName.contains("slag")) {
            String[] parts = recipeName.split("_");
            for (String part : parts) {
                if (part.matches("\\d+")) {
                    try {
                        return Integer.parseInt(part);
                    } catch (NumberFormatException e) {
                        return 100;
                    }
                }
            }
        }

        return 100;
    }
}