package com.voided.tfcnuclear.inventory.recipes;

import com.voided.tfcnuclear.TFCNuclear;
import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.ItemHeatHandler;
import net.dries007.tfc.api.recipes.heat.HeatRecipe;
import net.dries007.tfc.api.recipes.heat.HeatRecipeSimple;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;

// Убираем @Mod.EventBusSubscriber - теперь мы сами вызываем методы
public class TFCHeatRecipes {

    // Регистрация рецептов - вызывается из основного класса
    public static void registerHeatRecipes(RegistryEvent.Register<HeatRecipe> event) {
        System.out.println("[TFC Nuclear] === REGISTERING HEAT RECIPES ===");
        addCustomRecipes(event);
    }

    // Инициализация - вызывается из основного класса
    public static void init() {
        System.out.println("[TFC Nuclear] === INIT PHASE - REMOVING HEAT RECIPES ===");
        addHeatCapabilities();
    }

    private static void addCustomRecipes(RegistryEvent.Register<HeatRecipe> event) {
        // 1. ВАНИЛЬНАЯ глина -> Огнеупорная глина HBM при 1500°C
        IIngredient<ItemStack> clayIngredient = IIngredient.of(new ItemStack(Items.CLAY_BALL));
        ItemStack fireclayOutput = new ItemStack(Item.getByNameOrId("hbm:ball_fireclay"));
        float transformTemp = 1500f;

        HeatRecipeSimple clayToFireclay = new HeatRecipeSimple(
                clayIngredient,
                fireclayOutput,
                transformTemp
        );
        clayToFireclay.setRegistryName(TFCNuclear.MOD_ID, "clay_to_fireclay");
        event.getRegistry().register(clayToFireclay);
        System.out.println("[TFC Nuclear] Added recipe: clay_to_fireclay");

        // 2. Необожженный кирпич TFC -> Огнеупорный кирпич HBM при 2000°C
        Item unfiredBrick = Item.getByNameOrId("tfc:ceramics/unfired/fire_brick");
        Item firedBrick = Item.getByNameOrId("hbm:ingot_firebrick");

        if (unfiredBrick != null && firedBrick != null) {
            IIngredient<ItemStack> unfiredBrickIngredient = IIngredient.of(new ItemStack(unfiredBrick));
            ItemStack firedBrickOutput = new ItemStack(firedBrick);

            HeatRecipeSimple unfiredToFired = new HeatRecipeSimple(
                    unfiredBrickIngredient,
                    firedBrickOutput,
                    1500f
            );
            unfiredToFired.setRegistryName(TFCNuclear.MOD_ID, "unfired_to_fired");
            event.getRegistry().register(unfiredToFired);
            System.out.println("[TFC Nuclear] Added recipe: unfired_to_fired");
        } else {
            System.out.println("[TFC Nuclear] WARNING: Could not find TFC unfired brick or HBM firebrick");
        }
    }

    private static void addHeatCapabilities() {
        System.out.println("[TFC Nuclear] === ADDING HEAT CAPABILITIES ===");

        // Добавляем капабилити к ванильной глине
        Item clayBall = Item.getByNameOrId("minecraft:clay_ball");
        if (clayBall != null) {
            CapabilityItemHeat.CUSTOM_ITEMS.put(
                    IIngredient.of(clayBall),
                    () -> new ItemHeatHandler(null, 0.5f, 1500f)
            );
            System.out.println("[TFC Nuclear] Added heat capability to minecraft:clay_ball");
        }

        // Добавляем капабилити к огнеупорному кирпичу HBM
        Item firebrick = Item.getByNameOrId("hbm:ingot_firebrick");
        if (firebrick != null) {
            CapabilityItemHeat.CUSTOM_ITEMS.put(
                    IIngredient.of(firebrick),
                    () -> new ItemHeatHandler(null, 1.0f, 2500f)
            );
            System.out.println("[TFC Nuclear] Added heat capability to hbm:ingot_firebrick");
        }

        // Добавляем капабилити к огнеупорной глине HBM
        Item fireclay = Item.getByNameOrId("hbm:ball_fireclay");
        if (fireclay != null) {
            CapabilityItemHeat.CUSTOM_ITEMS.put(
                    IIngredient.of(fireclay),
                    () -> new ItemHeatHandler(null, 0.8f, 2000f)
            );
            System.out.println("[TFC Nuclear] Added heat capability to hbm:ball_fireclay");
        }

        System.out.println("[TFC Nuclear] Heat capabilities registered!");
    }
}