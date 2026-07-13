package com.voided.tfcnuclear.inventory.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Базовый класс для шлаков с NBT-количеством
 */
public abstract class ItemSlagBase extends Item {

    protected static final String NBT_KEY = "amount";
    protected static final int MAX_STACK_SIZE = 4; // Максимум 4 штуки в стаке
    protected static int MAX_AMOUNT = 800; // Максимум 800 единиц

    public ItemSlagBase() {
        super();
        this.setMaxStackSize(MAX_STACK_SIZE);
        this.setHasSubtypes(false);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int amount = getAmount(stack);
        tooltip.add(TextFormatting.GRAY + "Amount: " + TextFormatting.WHITE + amount + " units");

        // Показываем максимальное количество
        if (amount >= MAX_AMOUNT) {
            tooltip.add(TextFormatting.GOLD + "Максимум!");
        }
    }

    /**
     * Получить количество из NBT
     */
    public static int getAmount(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }

        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null && nbt.hasKey(NBT_KEY)) {
            return nbt.getInteger(NBT_KEY);
        }
        return 0;
    }

    /**
     * Установить количество в NBT с проверкой на максимум
     */
    public static void setAmount(ItemStack stack, int amount) {
        if (stack.isEmpty()) {
            return;
        }

        // Ограничиваем количество
        if (amount > MAX_AMOUNT) {
            amount = MAX_AMOUNT;
        }

        if (amount <= 0) {
            stack.setCount(0);
            return;
        }

        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }
        nbt.setInteger(NBT_KEY, amount);
        stack.setTagCompound(nbt);
    }

    /**
     * Добавить количество к существующему с проверкой на максимум
     */
    public static void addAmount(ItemStack stack, int amount) {
        int current = getAmount(stack);
        int newAmount = current + amount;

        // Если превышает максимум, оставляем остаток
        if (newAmount > MAX_AMOUNT) {
            newAmount = MAX_AMOUNT;
        }

        setAmount(stack, newAmount);
    }

    /**
     * Проверяет, можно ли объединить два стека
     */
    public static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty() || stack2.isEmpty()) {
            return true;
        }

        // Проверяем, что это одинаковые предметы
        if (stack1.getItem() != stack2.getItem()) {
            return false;
        }

        int amount1 = getAmount(stack1);
        int amount2 = getAmount(stack2);

        // Проверяем, не превысит ли сумма максимум
        return (amount1 + amount2) <= MAX_AMOUNT;
    }

    /**
     * Объединяет два стека в один
     */
    public static ItemStack combine(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty()) return stack2.copy();
        if (stack2.isEmpty()) return stack1.copy();

        // Проверяем, что это одинаковые предметы
        if (stack1.getItem() != stack2.getItem()) {
            return stack1;
        }

        int total = getAmount(stack1) + getAmount(stack2);
        if (total > MAX_AMOUNT) {
            total = MAX_AMOUNT;
        }

        ItemStack result = stack1.copy();
        setAmount(result, total);
        return result;
    }
}