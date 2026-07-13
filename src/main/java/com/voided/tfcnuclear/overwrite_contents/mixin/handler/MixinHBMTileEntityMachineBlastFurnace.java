package com.voided.tfcnuclear.overwrite_contents.mixin.handler;

import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.tileentity.machine.TileEntityMachineBlastFurnace;
import com.voided.tfcnuclear.compat.hbm.SlagStack;
import com.voided.tfcnuclear.inventory.items.ItemSlagBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TileEntityMachineBlastFurnace.class)
public class MixinHBMTileEntityMachineBlastFurnace {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public boolean hasQuantities(GenericRecipe recipe) {
        TileEntityMachineBlastFurnace furnace = (TileEntityMachineBlastFurnace) (Object) this;
        ItemStack slot1 = furnace.inventory.getStackInSlot(1);
        ItemStack slot2 = furnace.inventory.getStackInSlot(2);

        // Проверяем, есть ли в рецепте наш шлак
        for (int i = 0; i < recipe.inputItem.length; i++) {
            if (recipe.inputItem[i] instanceof SlagStack) {
                SlagStack slagRecipe = (SlagStack) recipe.inputItem[i];

                // Проверяем слот 1
                if (!slot1.isEmpty() && slot1.getItem() instanceof ItemSlagBase) {
                    if (slot1.getItem() == slagRecipe.slagItem) {
                        int amount = ItemSlagBase.getAmount(slot1);
                        // Проверяем остальные ингредиенты
                        if (amount >= slagRecipe.requiredAmount && checkOtherIngredients(recipe, i, slot1, slot2)) {
                            return true;
                        }
                    }
                }

                // Проверяем слот 2
                if (!slot2.isEmpty() && slot2.getItem() instanceof ItemSlagBase) {
                    if (slot2.getItem() == slagRecipe.slagItem) {
                        int amount = ItemSlagBase.getAmount(slot2);
                        if (amount >= slagRecipe.requiredAmount && checkOtherIngredients(recipe, i, slot2, slot1)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        // Стандартная проверка для обычных рецептов
        if (recipe.inputItem.length == 2) {
            if (recipe.inputItem[0].matchesRecipe(slot1, false) && recipe.inputItem[1].matchesRecipe(slot2, false)) return true;
            if (recipe.inputItem[0].matchesRecipe(slot2, false) && recipe.inputItem[1].matchesRecipe(slot1, false)) return true;
        } else if (recipe.inputItem.length == 1) {
            if (recipe.inputItem[0].matchesRecipe(slot1, false) || recipe.inputItem[0].matchesRecipe(slot2, false)) return true;
        }

        return false;
    }

    private boolean checkOtherIngredients(GenericRecipe recipe, int slagIndex, ItemStack slagSlot, ItemStack otherSlot) {
        for (int i = 0; i < recipe.inputItem.length; i++) {
            if (i != slagIndex) {
                if (!recipe.inputItem[i].matchesRecipe(otherSlot, false)) {
                    return false;
                }
            }
        }
        return true;
    }
}