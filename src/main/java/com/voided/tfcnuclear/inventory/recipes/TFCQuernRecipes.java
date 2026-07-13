package com.voided.tfcnuclear.inventory.recipes;

import net.dries007.tfc.api.recipes.quern.QuernRecipe;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class TFCQuernRecipes {

    public static void addQuernRecipe() {
        // 1. Удаляем ВСЕ старые рецепты, связанные с киноварью и криолитом
        removeOldRecipes();

        // 2. Добавляем новые рецепты
        addNewRecipe("tfc:ore/cinnabar", new ItemStack(Items.REDSTONE, 2), "tfcnuclear:ore_cinnabar_quern");
        addNewRecipe("hbm:cinnabar", new ItemStack(Items.REDSTONE, 1), "tfcnuclear:cinnabar_quern");
        addNewRecipe("tfc:ore/lapis_lazuli", new ItemStack(Item.getByNameOrId("hbm:powder_lapis"), 2), "tfcnuclear:ore_lapis_quern");
        addNewRecipe("tfc:ore/borax", new ItemStack(Item.getByNameOrId("hbm:powder_borax"), 1), "tfcnuclear:ore_borax_quern");
        addNewRecipe("tfc:ore/saltpeter", new ItemStack(Item.getByNameOrId("hbm:niter"), 1), "tfcnuclear:ore_niter_quern");
        addNewRecipe("tfc:ore/lignite", new ItemStack(Item.getByNameOrId("hbm:powder_lignite"), 1), "tfcnuclear:ore_lignite_quern");
        addNewRecipe("tfc:ore/kimberlite", new ItemStack(Item.getByNameOrId("tfc:gem/diamond"), 1, 2), "tfcnuclear:ore_diamond_quern");
        addNewRecipe("tfc:ore/graphite", new ItemStack(Item.getByNameOrId("tfc:powder/graphite"), 1), "tfcnuclear:ore_graphite_quern");
        addNewRecipe("tfc:ore/bituminous_coal", new ItemStack(Item.getByNameOrId("hbm:powder_coal"), 1), "tfcnuclear:ore_coal_quern");



        addOreDictRecipe("rockFlux", new ItemStack(Item.getByNameOrId("hbm:powder_flux")),  "tfcnuclear:rock_flux");

    }

    private static void removeOldRecipes() {
        List<ResourceLocation> toRemove = new ArrayList<>();

        for (QuernRecipe recipe : TFCRegistries.QUERN.getValuesCollection()) {
            ResourceLocation name = recipe.getRegistryName();
            if (name == null) continue;

            // Удаляем ВСЕ рецепты, содержащие cinnabar или cryolite
            String path = name.getPath().toLowerCase();
            if (path.contains("cinnabar") || path.contains("cryolite") || path.contains("lapis") || path.contains("borax")
                    || path.contains("flux") || path.contains("saltpeter") || path.contains("sulfur") || path.contains("diamond")
                    || path.contains("charcoal") || path.contains("graphite"))
            {
                toRemove.add(name);
                System.out.println("[TFC-Nuclear] Marked for removal: " + name);
            }
        }

        // Удаляем через ForgeRegistry
        if (!toRemove.isEmpty()) {
            try {
                ForgeRegistry<QuernRecipe> registry = (ForgeRegistry<QuernRecipe>) TFCRegistries.QUERN;
                for (ResourceLocation name : toRemove) {
                    registry.remove(name);
                    System.out.println("[TFC-Nuclear] Removed: " + name);
                }
            } catch (Exception e) {
                System.err.println("[TFC-Nuclear] Failed to remove recipes via ForgeRegistry!");
                e.printStackTrace();

                // Альтернатива: удаляем через рефлексию, если ForgeRegistry не работает
                removeViaReflection(toRemove);
            }
        }
    }

    // Запасной способ удаления через рефлексию
    private static void removeViaReflection(List<ResourceLocation> toRemove) {
        try {
            java.lang.reflect.Field registryMapField = ForgeRegistry.class.getDeclaredField("registryMap");
            registryMapField.setAccessible(true);

            ForgeRegistry<QuernRecipe> registry = (ForgeRegistry<QuernRecipe>) TFCRegistries.QUERN;
            java.util.Map<ResourceLocation, QuernRecipe> registryMap =
                    (java.util.Map<ResourceLocation, QuernRecipe>) registryMapField.get(registry);

            for (ResourceLocation name : toRemove) {
                registryMap.remove(name);
                System.out.println("[TFC-Nuclear] Removed via reflection: " + name);
            }
        } catch (Exception e) {
            System.err.println("[TFC-Nuclear] Failed to remove via reflection!");
            e.printStackTrace();
        }
    }

    /**
     * Добавляет рецепт жернова с использованием OreDict
     *
     * @param oreDictName  Имя в OreDict (например, "oreCinnabar", "gemCinnabar")
     * @param output       Выходной предмет
     * @param registryName Уникальное имя для рецепта
     */
    public static void addOreDictRecipe(String oreDictName, ItemStack output, String registryName) {
        // Проверяем, есть ли предметы с таким OreDict именем
        List<ItemStack> ores = OreDictionary.getOres(oreDictName);
        if (ores.isEmpty()) {
            System.out.println("[TFC-Nuclear] OreDict not found: " + oreDictName);
            return;
        }

        // Создаём ингредиент через анонимный класс с проверкой по OreDict
        IIngredient<ItemStack> input = new IIngredient<ItemStack>() {
            private final String oreName = oreDictName;

            @Override
            public boolean test(ItemStack stack) {
                if (stack.isEmpty()) return false;

                // Проверяем, соответствует ли стек OreDict имени
                int[] oreIds = OreDictionary.getOreIDs(stack);
                int targetId = OreDictionary.getOreID(oreName);

                for (int id : oreIds) {
                    if (id == targetId) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public ItemStack consume(ItemStack stack) {
                stack.shrink(1);
                return stack;
            }

            @Override
            public int getAmount() {
                return 1;
            }

            @Override
            public NonNullList<ItemStack> getValidIngredients() {
                NonNullList<ItemStack> validItems = NonNullList.create();
                for (ItemStack ore : OreDictionary.getOres(oreName)) {
                    validItems.add(ore.copy());
                }
                return validItems;
            }
        };

        QuernRecipe recipe = new QuernRecipe(input, output);
        recipe.setRegistryName(new ResourceLocation(registryName));
        TFCRegistries.QUERN.register(recipe);
        System.out.println("[TFC-Nuclear] ✅ Added OreDict recipe: " + oreDictName + " → " + output.getCount() + "x " + output.getDisplayName());
    }

    /**
     * Добавляет обычный рецепт жернова с конкретным предметом
     */
    private static void addNewRecipe(String inputId, ItemStack output, String registryName) {
        Item inputItem = Item.getByNameOrId(inputId);
        if (inputItem == null) {
            System.out.println("[TFC-Nuclear] Item not found: " + inputId);
            return;
        }

        IIngredient<ItemStack> input = new IIngredient<ItemStack>() {
            private final ItemStack required = new ItemStack(inputItem);

            @Override
            public boolean test(ItemStack stack) {
                return !stack.isEmpty() &&
                        stack.getItem() == required.getItem() &&
                        stack.getCount() >= required.getCount();
            }

            @Override
            public ItemStack consume(ItemStack stack) {
                stack.shrink(required.getCount());
                return stack;
            }

            @Override
            public int getAmount() {
                return required.getCount();
            }

            @Override
            public NonNullList<ItemStack> getValidIngredients() {
                return NonNullList.withSize(1, required.copy());
            }
        };

        QuernRecipe recipe = new QuernRecipe(input, output);
        recipe.setRegistryName(new ResourceLocation(registryName));
        TFCRegistries.QUERN.register(recipe);
        System.out.println("[TFC-Nuclear] ✅ Added: " + inputId + " → " + output.getCount() + "x " + output.getDisplayName());
    }
}