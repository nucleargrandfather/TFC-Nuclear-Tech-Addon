package com.voided.tfcnuclear.inventory.handler;

import com.hbm.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class SmeltingRemover {

    public static void removeRecipes() {
        FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
        Map<ItemStack, ItemStack> smeltingList = furnaceRecipes.getSmeltingList();

        Iterator<Map.Entry<ItemStack, ItemStack>> iterator = smeltingList.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<ItemStack, ItemStack> entry = iterator.next();
            ItemStack key = entry.getKey();

            boolean shouldRemove = false;

            if (key.getItem() == ModItems.crystal_trixite) {
                shouldRemove = true;
            }

            if (shouldRemove) {
                iterator.remove();
                removeExperience(furnaceRecipes, key);
                System.out.println("Удален рецепт печи: " + key.getDisplayName());
            }
        }
    }

    private static void removeExperience(FurnaceRecipes furnaceRecipes, ItemStack key) {
        try {
            Field field = FurnaceRecipes.class.getDeclaredField("experienceList");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<ItemStack, Float> experienceMap = (Map<ItemStack, Float>) field.get(furnaceRecipes);
            if (experienceMap != null) {
                experienceMap.remove(key);
            }
        } catch (Exception ignored) {}
    }
}