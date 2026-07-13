package com.voided.tfcnuclear.inventory.recipes;

import com.voided.tfcnuclear.inventory.items.ItemSlagBase;
import com.voided.tfcnuclear.inventory.items.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class OreCombineRecipes extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    // Карта: руда → шлак
    private static final Map<String, Item> ORE_TO_SLAG = new HashMap<>();

    static {
        // Гематит
        ORE_TO_SLAG.put("tfc:ore/hematite", ModItems.HEMATITE_SLAG);
        ORE_TO_SLAG.put("tfc:ore/small/hematite", ModItems.HEMATITE_SLAG);

        // Лимонит
        ORE_TO_SLAG.put("tfc:ore/limonite", ModItems.LIMONITE_SLAG);
        ORE_TO_SLAG.put("tfc:ore/small/limonite", ModItems.LIMONITE_SLAG);

        // Магнетит
        ORE_TO_SLAG.put("tfc:ore/magnetite", ModItems.MAGNETITE_SLAG);
        ORE_TO_SLAG.put("tfc:ore/small/magnetite", ModItems.MAGNETITE_SLAG);
    }

    // Карта: значения руды (meta → количество)
    private static final Map<String, Map<Integer, Integer>> ORE_VALUES = new HashMap<>();

    static {
        // Гематит
        Map<Integer, Integer> hematiteValues = new HashMap<>();
        hematiteValues.put(0, 25); // Обычная
        hematiteValues.put(1, 15); // Бедная
        hematiteValues.put(2, 35); // Богатая
        ORE_VALUES.put("tfc:ore/hematite", hematiteValues);
        ORE_VALUES.put("tfc:ore/small/hematite", new HashMap<Integer, Integer>() {{ put(0, 10); }});

        // Лимонит
        Map<Integer, Integer> limoniteValues = new HashMap<>();
        limoniteValues.put(0, 25);
        limoniteValues.put(1, 15);
        limoniteValues.put(2, 35);
        ORE_VALUES.put("tfc:ore/limonite", limoniteValues);
        ORE_VALUES.put("tfc:ore/small/limonite", new HashMap<Integer, Integer>() {{ put(0, 10); }});

        // Магнетит
        Map<Integer, Integer> magnetiteValues = new HashMap<>();
        magnetiteValues.put(0, 25);
        magnetiteValues.put(1, 15);
        magnetiteValues.put(2, 35);
        ORE_VALUES.put("tfc:ore/magnetite", magnetiteValues);
        ORE_VALUES.put("tfc:ore/small/magnetite", new HashMap<Integer, Integer>() {{ put(0, 10); }});
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        int oreCount = 0;
        int slagCount = 0;
        boolean hasHammer = false;
        boolean hasOnlyValidItems = true;
        Item expectedSlag = null;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!stack.isEmpty()) {
                if (isHammer(stack)) {
                    if (hasHammer) return false; // Только один молот
                    hasHammer = true;
                }
                else if (isValidOre(stack)) {
                    oreCount++;
                    // Определяем какой шлак должен получиться
                    Item slag = getSlagForOre(stack);
                    if (expectedSlag == null) {
                        expectedSlag = slag;
                    } else if (expectedSlag != slag) {
                        // Нельзя смешивать разные руды
                        return false;
                    }
                }
                else if (isSlag(stack)) {
                    slagCount++;
                    // Проверяем, что все шлаки одного типа
                    if (expectedSlag == null) {
                        expectedSlag = stack.getItem();
                    } else if (expectedSlag != stack.getItem()) {
                        return false;
                    }
                }
                else {
                    hasOnlyValidItems = false;
                    break;
                }
            }
        }

        int totalItems = oreCount + slagCount;
        return hasOnlyValidItems && hasHammer && totalItems >= 1 && expectedSlag != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        int totalAmount = 0;
        ItemStack existingSlag = ItemStack.EMPTY;
        Item targetSlagItem = null;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!stack.isEmpty()) {
                if (isSlag(stack)) {
                    totalAmount += ItemSlagBase.getAmount(stack);
                    if (existingSlag.isEmpty()) {
                        existingSlag = stack;
                    }
                    targetSlagItem = stack.getItem();
                }
                else if (isValidOre(stack)) {
                    totalAmount += getAmountFromOre(stack);
                    if (targetSlagItem == null) {
                        targetSlagItem = getSlagForOre(stack);
                    }
                }
            }
        }

        if (targetSlagItem == null || totalAmount <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack result;
        if (!existingSlag.isEmpty()) {
            result = existingSlag.copy();
        } else {
            result = new ItemStack(targetSlagItem);
        }

        // Ограничиваем количество до 800
        if (totalAmount > 800) {
            totalAmount = 800;
        }

        ItemSlagBase.setAmount(result, totalAmount);
        return result;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModItems.HEMATITE_SLAG);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        boolean hasHammer = false;

        // Сначала проверяем, есть ли молот в крафте
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && isHammer(stack)) {
                hasHammer = true;
                // Создаем копию молота и уменьшаем его прочность
                ItemStack damagedHammer = stack.copy();
                damagedHammer.setItemDamage(damagedHammer.getItemDamage() + 1);

                // Если прочность стала меньше максимальной, возвращаем поврежденный молот
                if (damagedHammer.getItemDamage() < damagedHammer.getMaxDamage()) {
                    remaining.set(i, damagedHammer);
                }
                // Иначе предмет просто исчезает (прочность исчерпана)
                break; // Молот только один, выходим из цикла
            }
        }

        // ВАЖНО: Все остальные слоты (руды и шлаки) должны быть пустыми
        // чтобы исходные предметы удалялись из сетки крафта
        // NonNullList уже инициализирована EMPTY, поэтому ничего не делаем

        return remaining;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    private boolean isHammer(ItemStack stack) {
        if (stack.isEmpty()) return false;
        int[] oreIds = OreDictionary.getOreIDs(stack);
        for (int id : oreIds) {
            if ("hammer".equals(OreDictionary.getOreName(id))) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidOre(ItemStack stack) {
        if (stack.isEmpty()) return false;
        String itemId = stack.getItem().getRegistryName().toString();
        return ORE_TO_SLAG.containsKey(itemId);
    }

    private boolean isSlag(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() instanceof ItemSlagBase;
    }

    private Item getSlagForOre(ItemStack stack) {
        if (stack.isEmpty()) return null;
        String itemId = stack.getItem().getRegistryName().toString();
        return ORE_TO_SLAG.get(itemId);
    }

    private int getAmountFromOre(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        String itemId = stack.getItem().getRegistryName().toString();
        Map<Integer, Integer> values = ORE_VALUES.get(itemId);
        if (values != null) {
            return values.getOrDefault(stack.getMetadata(), 0);
        }
        return 0;
    }
}