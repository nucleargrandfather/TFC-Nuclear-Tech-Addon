package com.voided.tfcnuclear.inventory.items;

import com.voided.tfcnuclear.TFCNuclear;
import com.voided.tfcnuclear.inventory.recipes.OreCombineRecipes;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TFCNuclear.MOD_ID)
public class ModItems {

    // Существующие шлаки
    public static final Item HEMATITE_SLAG = new ItemHematiteSlag();
    public static final Item LIMONITE_SLAG = new ItemLimoniteSlag();
    public static final Item MAGNETITE_SLAG = new ItemMagnetiteSlag();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                HEMATITE_SLAG,
                LIMONITE_SLAG,
                MAGNETITE_SLAG
        );
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(
                new OreCombineRecipes().setRegistryName(TFCNuclear.MOD_ID, "ore_combine")
        );
    }
}