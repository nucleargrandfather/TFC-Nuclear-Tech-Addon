package com.voided.tfcnuclear.inventory.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemBase extends Item {

    public ItemBase(String name) {
        setRegistryName("tfcnuclear", name);
        setTranslationKey(name);
        setCreativeTab(CreativeTabs.MATERIALS);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this));
        }
    }
}