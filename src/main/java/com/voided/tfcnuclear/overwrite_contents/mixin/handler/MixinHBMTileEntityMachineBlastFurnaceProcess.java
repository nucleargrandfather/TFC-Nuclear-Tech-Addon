package com.voided.tfcnuclear.overwrite_contents.mixin.handler;

import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.tileentity.machine.TileEntityMachineBlastFurnace;
import com.voided.tfcnuclear.compat.hbm.SlagStack;
import com.voided.tfcnuclear.inventory.items.ItemSlagBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TileEntityMachineBlastFurnace.class)
public class MixinHBMTileEntityMachineBlastFurnaceProcess {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void process(GenericRecipe recipe) {
        TileEntityMachineBlastFurnace furnace = (TileEntityMachineBlastFurnace) (Object) this;

        // Проверяем, есть ли в рецепте наш шлак
        int slagSlot = -1;
        int requiredAmount = 0;
        ItemStack slagStack = null;

        for (int i = 0; i < recipe.inputItem.length; i++) {
            if (recipe.inputItem[i] instanceof SlagStack) {
                SlagStack slagRecipe = (SlagStack) recipe.inputItem[i];
                requiredAmount = slagRecipe.requiredAmount;

                // Проверяем слот 1
                if (!furnace.inventory.getStackInSlot(1).isEmpty() &&
                        furnace.inventory.getStackInSlot(1).getItem() instanceof ItemSlagBase) {
                    if (furnace.inventory.getStackInSlot(1).getItem() == slagRecipe.slagItem) {
                        slagSlot = 1;
                        slagStack = furnace.inventory.getStackInSlot(1);
                        break;
                    }
                }
                // Проверяем слот 2
                if (!furnace.inventory.getStackInSlot(2).isEmpty() &&
                        furnace.inventory.getStackInSlot(2).getItem() instanceof ItemSlagBase) {
                    if (furnace.inventory.getStackInSlot(2).getItem() == slagRecipe.slagItem) {
                        slagSlot = 2;
                        slagStack = furnace.inventory.getStackInSlot(2);
                        break;
                    }
                }
                break;
            }
        }

        // Если есть шлак - обрабатываем его
        if (slagSlot != -1 && slagStack != null) {
            int currentAmount = ItemSlagBase.getAmount(slagStack);
            int newAmount = currentAmount - requiredAmount;

            if (newAmount <= 0) {
                furnace.inventory.setStackInSlot(slagSlot, ItemStack.EMPTY);
            } else {
                ItemSlagBase.setAmount(slagStack, newAmount);
            }
        }

        // Обрабатываем обычные ингредиенты (не шлак)
        for (int i = 0; i < recipe.inputItem.length; i++) {
            if (!(recipe.inputItem[i] instanceof SlagStack)) {
                // Проверяем слот 1
                if (recipe.inputItem[i].matchesRecipe(furnace.inventory.getStackInSlot(1), false)) {
                    furnace.inventory.getStackInSlot(1).shrink(recipe.inputItem[i].stacksize);
                }
                // Проверяем слот 2
                else if (recipe.inputItem[i].matchesRecipe(furnace.inventory.getStackInSlot(2), false)) {
                    furnace.inventory.getStackInSlot(2).shrink(recipe.inputItem[i].stacksize);
                }
            }
        }

        // Вывод результатов (слоты 3 и 4)
        for (int i = 0; i < Math.min(recipe.outputItem.length, 2); i++) {
            ItemStack output = recipe.outputItem[i].collapse();
            int slot = 3 + i;
            if (!furnace.inventory.getStackInSlot(slot).isEmpty()) {
                furnace.inventory.getStackInSlot(slot).grow(output.getCount());
            } else {
                furnace.inventory.setStackInSlot(slot, output);
            }
        }
    }
}