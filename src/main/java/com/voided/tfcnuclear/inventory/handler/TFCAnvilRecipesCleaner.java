package com.voided.tfcnuclear.inventory.handler;

import net.dries007.tfc.api.recipes.WeldingRecipe;
import net.dries007.tfc.api.recipes.anvil.AnvilRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class TFCAnvilRecipesCleaner {

    private static final String MOD_ID = "tfcnuclear";

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAnvilRegistry(RegistryEvent.Register<AnvilRecipe> event) {
        IForgeRegistryModifiable registry = (IForgeRegistryModifiable) event.getRegistry();

        List<ResourceLocation> toRemove = new ArrayList<>();

        for (AnvilRecipe recipe : (Iterable<AnvilRecipe>) registry.getValuesCollection()) {
            ResourceLocation name = recipe.getRegistryName();
            // Пропускаем свои рецепты
            if (name != null && MOD_ID.equals(name.getNamespace())) {
                continue;
            }

            if (isBismuthAnvilRecipe(recipe)) {
                toRemove.add(name);
            }
            if (isSteelRodAnvilRecipe(recipe)) {
                toRemove.add(name);
            }
            if (isSteelIngotAnvilRecipe(recipe)) {
                toRemove.add(name);
            }
        }

        for (ResourceLocation name : toRemove) {
            registry.remove(name);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onWeldingRegistry(RegistryEvent.Register<WeldingRecipe> event) {
        IForgeRegistryModifiable registry = (IForgeRegistryModifiable) event.getRegistry();

        List<ResourceLocation> toRemove = new ArrayList<>();

        for (WeldingRecipe recipe : (Iterable<WeldingRecipe>) registry.getValuesCollection()) {
            ResourceLocation name = recipe.getRegistryName();
            if (name != null && MOD_ID.equals(name.getNamespace())) {
                continue;
            }

            if (isBismuthWeldingRecipe(recipe)) {
                toRemove.add(name);
                System.out.println("[TFC Nuclear] Marked welding recipe for removal: " + name);
            }
        }

        for (ResourceLocation name : toRemove) {
            registry.remove(name);
        }
    }

    private static boolean isBismuthAnvilRecipe(AnvilRecipe recipe) {
        if (recipe == null || recipe.getOutputs().isEmpty()) return false;
        ItemStack output = recipe.getOutputs().get(0);
        if (output.isEmpty()) return false;
        String registryName = output.getItem().getRegistryName().toString();
        return registryName.contains("bismuth");
    }

    private static boolean isBismuthWeldingRecipe(WeldingRecipe recipe) {
        if (recipe == null || recipe.getOutputs().isEmpty()) return false;
        ItemStack output = recipe.getOutputs().get(0);
        if (output.isEmpty()) return false;
        String registryName = output.getItem().getRegistryName().toString();
        return registryName.contains("bismuth");
    }

    private static boolean isSteelRodAnvilRecipe(AnvilRecipe recipe) {
        if (recipe == null || recipe.getOutputs().isEmpty()) return false;
        ItemStack output = recipe.getOutputs().get(0);
        if (output.isEmpty()) return false;
        String registryName = output.getItem().getRegistryName().toString();
        return registryName.contains("steel") && registryName.contains("rod");
    }

    private static boolean isSteelIngotAnvilRecipe(AnvilRecipe recipe) {
        if (recipe == null || recipe.getOutputs().isEmpty()) return false;
        ItemStack output = recipe.getOutputs().get(0);
        if (output.isEmpty()) return false;
        String registryName = output.getItem().getRegistryName().toString();
        return registryName.contains("steel") && registryName.contains("ingot") && !registryName.contains("blue")
                && !registryName.contains("red") && !registryName.contains("black") && !registryName.contains("carbon");
    }
}