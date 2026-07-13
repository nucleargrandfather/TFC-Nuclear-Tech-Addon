package com.voided.tfcnuclear.inventory.recipes;

import net.dries007.tfc.api.recipes.WeldingRecipe;
import net.dries007.tfc.api.recipes.anvil.AnvilRecipe;
import net.dries007.tfc.api.recipes.heat.HeatRecipe;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.dries007.tfc.util.forge.ForgeRule;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TFCAnvilRecipes {

    private static final String MOD_ID = "tfcnuclear";

    // ==================== HELPER METHODS ====================

    public static ItemStack getTFCItem(String path) {
        net.minecraft.item.Item item = net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS.getValue(
                new ResourceLocation("tfc", path)
        );
        return item != null ? new ItemStack(item) : ItemStack.EMPTY;
    }
    public static ItemStack getHBMItem(String path) {
        net.minecraft.item.Item item = net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS.getValue(
                new ResourceLocation("hbm", path)
        );
        return item != null ? new ItemStack(item) : ItemStack.EMPTY;
    }

    private static Metal getTFCMetal(String name) {
        return net.dries007.tfc.api.registries.TFCRegistries.METALS.getValue(new ResourceLocation("tfc", name));
    }

    // ==================== ANVIL RECIPES ====================

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onAnvilRegistry(RegistryEvent.Register<AnvilRecipe> event) {
        IForgeRegistry<AnvilRecipe> registry = event.getRegistry();

        addBismuthAnvilRecipes(registry);
        addBismuthBronzeAnvilRecipes(registry);
        addSteelRodAnvilRecipes(registry);
    }

    private static void addBismuthAnvilRecipes(IForgeRegistry<AnvilRecipe> registry) {
        ItemStack bismuthIngot = getTFCItem("metal/ingot/bismuth");
        ItemStack bismuthDoubleIngot = getTFCItem("metal/double_ingot/bismuth");
        ItemStack bismuthSheet = getTFCItem("metal/sheet/bismuth");
        ItemStack bismuthLamp = getTFCItem("metal/lamp/bismuth");
        ItemStack bismuthTrapdoor = getTFCItem("metal/trapdoor/bismuth");

        // 1. Двойной слиток -> лист (3 удара)
        if (!bismuthDoubleIngot.isEmpty() && !bismuthSheet.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_double_ingot_to_sheet"),
                    IIngredient.of(bismuthDoubleIngot),
                    bismuthSheet,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth double ingot -> Bismuth sheet");
        }

        // 2. Слиток -> лампа (2 удара)
        if (!bismuthIngot.isEmpty() && !bismuthLamp.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_ingot_to_lamp"),
                    IIngredient.of(bismuthIngot),
                    bismuthLamp,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth ingot -> Bismuth lamp");
        }

        // 3. Лист -> люк (2 удара)
        if (!bismuthSheet.isEmpty() && !bismuthTrapdoor.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_sheet_to_trapdoor"),
                    IIngredient.of(bismuthSheet),
                    bismuthTrapdoor,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth sheet -> Bismuth trapdoor");
        }
    }

    private static void addBismuthBronzeAnvilRecipes(IForgeRegistry<AnvilRecipe> registry) {
        ItemStack bronzeIngot = getTFCItem("metal/ingot/bismuth_bronze");
        ItemStack bronzeDoubleIngot = getTFCItem("metal/double_ingot/bismuth_bronze");
        ItemStack bronzeSheet = getTFCItem("metal/sheet/bismuth_bronze");
        ItemStack bronzeDoubleSheet = getTFCItem("metal/double_sheet/bismuth_bronze");
        ItemStack bronzeLamp = getTFCItem("metal/lamp/bismuth_bronze");
        ItemStack bronzeTrapdoor = getTFCItem("metal/trapdoor/bismuth_bronze");
        ItemStack bronzeTuyere = getTFCItem("metal/tuyere/bismuth_bronze");

        ItemStack bronzeUnfinishedHelmet = getTFCItem("metal/unfinished_helmet/bismuth_bronze");
        ItemStack bronzeUnfinishedChestplate = getTFCItem("metal/unfinished_chestplate/bismuth_bronze");
        ItemStack bronzeUnfinishedGreaves = getTFCItem("metal/unfinished_greaves/bismuth_bronze");
        ItemStack bronzeUnfinishedBoots = getTFCItem("metal/unfinished_boots/bismuth_bronze");
        ItemStack bronzeShield = getTFCItem("metal/shield/bismuth_bronze");

        // 1. Двойной слиток -> лист (3 удара)
        if (!bronzeDoubleIngot.isEmpty() && !bronzeSheet.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_double_ingot_to_sheet"),
                    IIngredient.of(bronzeDoubleIngot),
                    bronzeSheet,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze double ingot -> Bismuth Bronze sheet");
        }

        // 2. Слиток -> лампа (2 удара)
        if (!bronzeIngot.isEmpty() && !bronzeLamp.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_ingot_to_lamp"),
                    IIngredient.of(bronzeIngot),
                    bronzeLamp,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze ingot -> Bismuth Bronze lamp");
        }

        // 3. Лист -> люк (2 удара)
        if (!bronzeSheet.isEmpty() && !bronzeTrapdoor.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_sheet_to_trapdoor"),
                    IIngredient.of(bronzeSheet),
                    bronzeTrapdoor,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze sheet -> Bismuth Bronze trapdoor");
        }

        // 4. Двойной лист -> труба/труба (3 удара)
        if (!bronzeDoubleSheet.isEmpty() && !bronzeTuyere.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_double_sheet_to_tuyere"),
                    IIngredient.of(bronzeDoubleSheet),
                    bronzeTuyere,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze double sheet -> Bismuth Bronze tuyere");
        }

        // 5. Двойной лист -> незаконченный шлем (3 удара)
        if (!bronzeDoubleSheet.isEmpty() && !bronzeUnfinishedHelmet.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_double_sheet_to_unfinished_helmet"),
                    IIngredient.of(bronzeDoubleSheet),
                    bronzeUnfinishedHelmet,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze double sheet -> Bismuth Bronze unfinished helmet");
        }

        // 6. Двойной лист -> незаконченный нагрудник (3 удара)
        if (!bronzeDoubleSheet.isEmpty() && !bronzeUnfinishedChestplate.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_double_sheet_to_unfinished_chestplate"),
                    IIngredient.of(bronzeDoubleSheet),
                    bronzeUnfinishedChestplate,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze double sheet -> Bismuth Bronze unfinished chestplate");
        }

        // 7. Двойной лист -> незаконченные поножи (3 удара)
        if (!bronzeDoubleSheet.isEmpty() && !bronzeUnfinishedGreaves.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_double_sheet_to_unfinished_greaves"),
                    IIngredient.of(bronzeDoubleSheet),
                    bronzeUnfinishedGreaves,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze double sheet -> Bismuth Bronze unfinished greaves");
        }

        // 8. Лист -> незаконченные ботинки (3 удара)
        if (!bronzeSheet.isEmpty() && !bronzeUnfinishedBoots.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_sheet_to_unfinished_boots"),
                    IIngredient.of(bronzeSheet),
                    bronzeUnfinishedBoots,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze sheet -> Bismuth Bronze unfinished boots");
        }

        // 9. Двойной лист -> щит (3 удара)
        if (!bronzeDoubleSheet.isEmpty() && !bronzeShield.isEmpty()) {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_double_sheet_to_shield"),
                    IIngredient.of(bronzeDoubleSheet),
                    bronzeShield,
                    Metal.Tier.TIER_I,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze double sheet -> Bismuth Bronze shield");
        }
    }

    // ==================== WELDING RECIPES ====================

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onWeldingRegistry(RegistryEvent.Register<WeldingRecipe> event) {
        IForgeRegistry<WeldingRecipe> registry = event.getRegistry();

        addBismuthWeldingRecipes(registry);
        addBismuthBronzeWeldingRecipes(registry);

        System.out.println("[TFC Nuclear] All welding recipes added successfully!");
    }

    private static void addBismuthWeldingRecipes(IForgeRegistry<WeldingRecipe> registry) {
        ItemStack bismuthIngot = getTFCItem("metal/ingot/bismuth");
        ItemStack bismuthDoubleIngot = getTFCItem("metal/double_ingot/bismuth");
        ItemStack bismuthSheet = getTFCItem("metal/sheet/bismuth");
        ItemStack bismuthDoubleSheet = getTFCItem("metal/double_sheet/bismuth");

        // 1. Слиток + слиток = двойной слиток
        if (!bismuthIngot.isEmpty() && !bismuthDoubleIngot.isEmpty()) {
            registry.register(new WeldingRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_ingot_to_double"),
                    IIngredient.of(bismuthIngot),
                    IIngredient.of(bismuthIngot),
                    bismuthDoubleIngot,
                    Metal.Tier.TIER_I
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth ingot + Bismuth ingot = Bismuth double ingot");
        }

        // 2. Лист + лист = двойной лист
        if (!bismuthSheet.isEmpty() && !bismuthDoubleSheet.isEmpty()) {
            registry.register(new WeldingRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_sheet_to_double"),
                    IIngredient.of(bismuthSheet),
                    IIngredient.of(bismuthSheet),
                    bismuthDoubleSheet,
                    Metal.Tier.TIER_I
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth sheet + Bismuth sheet = Bismuth double sheet");
        }
    }

    private static void addBismuthBronzeWeldingRecipes(IForgeRegistry<WeldingRecipe> registry) {
        ItemStack bronzeIngot = getTFCItem("metal/ingot/bismuth_bronze");
        ItemStack bronzeDoubleIngot = getTFCItem("metal/double_ingot/bismuth_bronze");
        ItemStack bronzeSheet = getTFCItem("metal/sheet/bismuth_bronze");
        ItemStack bronzeDoubleSheet = getTFCItem("metal/double_sheet/bismuth_bronze");

        ItemStack bronzeKnifeBlade = getTFCItem("metal/knife_blade/bismuth_bronze");
        ItemStack bronzeShears = getTFCItem("metal/shears/bismuth_bronze");

        ItemStack bronzeUnfinishedHelmet = getTFCItem("metal/unfinished_helmet/bismuth_bronze");
        ItemStack bronzeHelmet = getTFCItem("metal/helmet/bismuth_bronze");

        ItemStack bronzeUnfinishedChestplate = getTFCItem("metal/unfinished_chestplate/bismuth_bronze");
        ItemStack bronzeChestplate = getTFCItem("metal/chestplate/bismuth_bronze");

        ItemStack bronzeUnfinishedGreaves = getTFCItem("metal/unfinished_greaves/bismuth_bronze");
        ItemStack bronzeGreaves = getTFCItem("metal/greaves/bismuth_bronze");

        ItemStack bronzeUnfinishedBoots = getTFCItem("metal/unfinished_boots/bismuth_bronze");
        ItemStack bronzeBoots = getTFCItem("metal/boots/bismuth_bronze");

        // 1. Слиток + слиток = двойной слиток
        if (!bronzeIngot.isEmpty() && !bronzeDoubleIngot.isEmpty()) {
            registry.register(new WeldingRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_ingot_to_double"),
                    IIngredient.of(bronzeIngot),
                    IIngredient.of(bronzeIngot),
                    bronzeDoubleIngot,
                    Metal.Tier.TIER_I
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze ingot + Bismuth Bronze ingot = Bismuth Bronze double ingot");
        }

        // 2. Лист + лист = двойной лист
        if (!bronzeSheet.isEmpty() && !bronzeDoubleSheet.isEmpty()) {
            registry.register(new WeldingRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_sheet_to_double"),
                    IIngredient.of(bronzeSheet),
                    IIngredient.of(bronzeSheet),
                    bronzeDoubleSheet,
                    Metal.Tier.TIER_I
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze sheet + Bismuth Bronze sheet = Bismuth Bronze double sheet");
        }

        // 3. Два лезвия ножа -> ножницы
        if (!bronzeKnifeBlade.isEmpty() && !bronzeShears.isEmpty()) {
            registry.register(new WeldingRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_knife_blades_to_shears"),
                    IIngredient.of(bronzeKnifeBlade),
                    IIngredient.of(bronzeKnifeBlade),
                    bronzeShears,
                    Metal.Tier.TIER_I
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze knife blade + Bismuth Bronze knife blade = Bismuth Bronze shears");
        }

        // 4. Незаконченный шлем + лист = шлем
        if (!bronzeUnfinishedHelmet.isEmpty() && !bronzeSheet.isEmpty() && !bronzeHelmet.isEmpty()) {
            registry.register(new WeldingRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_helmet"),
                    IIngredient.of(bronzeUnfinishedHelmet),
                    IIngredient.of(bronzeSheet),
                    bronzeHelmet,
                    Metal.Tier.TIER_I
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze unfinished helmet + Bismuth Bronze sheet = Bismuth Bronze helmet");
        }

        // 5. Незаконченный нагрудник + лист = нагрудник
        if (!bronzeUnfinishedChestplate.isEmpty() && !bronzeSheet.isEmpty() && !bronzeChestplate.isEmpty()) {
            registry.register(new WeldingRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_chestplate"),
                    IIngredient.of(bronzeUnfinishedChestplate),
                    IIngredient.of(bronzeSheet),
                    bronzeChestplate,
                    Metal.Tier.TIER_I
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze unfinished chestplate + Bismuth Bronze sheet = Bismuth Bronze chestplate");
        }

        // 6. Незаконченные поножи + лист = поножи
        if (!bronzeUnfinishedGreaves.isEmpty() && !bronzeSheet.isEmpty() && !bronzeGreaves.isEmpty()) {
            registry.register(new WeldingRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_greaves"),
                    IIngredient.of(bronzeUnfinishedGreaves),
                    IIngredient.of(bronzeSheet),
                    bronzeGreaves,
                    Metal.Tier.TIER_I
            ));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze unfinished greaves + Bismuth Bronze sheet = Bismuth Bronze greaves");
        }

        // 7. Незаконченные ботинки + лист = ботинки
        if (!bronzeUnfinishedBoots.isEmpty() && !bronzeSheet.isEmpty() && !bronzeBoots.isEmpty()) {
            registry.register(new WeldingRecipe(
                    new ResourceLocation(MOD_ID, "bismuth_bronze_boots"),
                    IIngredient.of(bronzeUnfinishedBoots),
                    IIngredient.of(bronzeSheet),
                    bronzeBoots,
                    Metal.Tier.TIER_I
            ));
        }
    }
    private static void addSteelRodAnvilRecipes(IForgeRegistry<AnvilRecipe> registry) {

        ItemStack steelIngot = getHBMItem("ingot_steel");
        ItemStack carbonSteelIngot = getTFCItem("metal/ingot/high_carbon_steel");
        {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "ingot_steel_to_steel_rod"),
                    IIngredient.of(steelIngot),
                    new ItemStack(Item.getByNameOrId("tfc:metal/rod/steel"), 2),
                    Metal.Tier.TIER_III,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
        }
        {
            registry.register(new AnvilRecipe(
                    new ResourceLocation(MOD_ID, "ingot_carbon_steel_to_steel_ingot"),
                    IIngredient.of(carbonSteelIngot),
                    steelIngot,
                    Metal.Tier.TIER_III,
                    null,
                    ForgeRule.HIT_ANY, ForgeRule.HIT_ANY, ForgeRule.HIT_ANY
            ));
        }
    }

    // ==================== HEAT RECIPES (PLAVKA) ====================

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onHeatRegistry(RegistryEvent.Register<HeatRecipe> event) {
        IForgeRegistry<HeatRecipe> registry = event.getRegistry();

        addBismuthHeatRecipes(registry);
        addBismuthBronzeHeatRecipes(registry);

        System.out.println("[TFC Nuclear] All heat recipes added successfully!");
    }

    private static void addBismuthHeatRecipes(IForgeRegistry<HeatRecipe> registry) {
        Metal bismuthMetal = getTFCMetal("bismuth");
        if (bismuthMetal == null) {
            System.out.println("[TFC Nuclear] Warning: Bismuth metal not found, skipping heat recipes");
            return;
        }

        ItemStack bismuthIngot = getTFCItem("metal/ingot/bismuth");
        ItemStack bismuthDoubleIngot = getTFCItem("metal/double_ingot/bismuth");
        ItemStack bismuthSheet = getTFCItem("metal/sheet/bismuth");
        ItemStack bismuthDoubleSheet = getTFCItem("metal/double_sheet/bismuth");

        float meltTemp = bismuthMetal.getMeltTemp();

        if (!bismuthIngot.isEmpty()) {
            registry.register(new HeatRecipeMelting(MOD_ID, "melt_bismuth_ingot", bismuthIngot, meltTemp, bismuthMetal, 100));
        }
        if (!bismuthDoubleIngot.isEmpty()) {
            registry.register(new HeatRecipeMelting(MOD_ID, "melt_bismuth_double_ingot", bismuthDoubleIngot, meltTemp, bismuthMetal, 200));
        }
        if (!bismuthSheet.isEmpty()) {
            registry.register(new HeatRecipeMelting(MOD_ID, "melt_bismuth_sheet", bismuthSheet, meltTemp, bismuthMetal, 100));
        }
        if (!bismuthDoubleSheet.isEmpty()) {
            registry.register(new HeatRecipeMelting(MOD_ID, "melt_bismuth_double_sheet", bismuthDoubleSheet, meltTemp, bismuthMetal, 200));
        }
    }

    private static void addBismuthBronzeHeatRecipes(IForgeRegistry<HeatRecipe> registry) {
        Metal bismuthBronzeMetal = getTFCMetal("bismuth_bronze");
        if (bismuthBronzeMetal == null) {
            System.out.println("[TFC Nuclear] Warning: Bismuth bronze metal not found, skipping heat recipes");
            return;
        }

        ItemStack bronzeIngot = getTFCItem("metal/ingot/bismuth_bronze");
        ItemStack bronzeDoubleIngot = getTFCItem("metal/double_ingot/bismuth_bronze");
        ItemStack bronzeSheet = getTFCItem("metal/sheet/bismuth_bronze");
        ItemStack bronzeDoubleSheet = getTFCItem("metal/double_sheet/bismuth_bronze");

        float meltTemp = bismuthBronzeMetal.getMeltTemp();

        if (!bronzeIngot.isEmpty()) {
            registry.register(new HeatRecipeMelting(MOD_ID, "melt_bismuth_bronze_ingot", bronzeIngot, meltTemp, bismuthBronzeMetal, 100));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze ingot -> Molten bismuth bronze (100 mB)");
        }
        if (!bronzeDoubleIngot.isEmpty()) {
            registry.register(new HeatRecipeMelting(MOD_ID, "melt_bismuth_bronze_double_ingot", bronzeDoubleIngot, meltTemp, bismuthBronzeMetal, 200));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze double ingot -> Molten bismuth bronze (200 mB)");
        }
        if (!bronzeSheet.isEmpty()) {
            registry.register(new HeatRecipeMelting(MOD_ID, "melt_bismuth_bronze_sheet", bronzeSheet, meltTemp, bismuthBronzeMetal, 100));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze sheet -> Molten bismuth bronze (100 mB)");
        }
        if (!bronzeDoubleSheet.isEmpty()) {
            registry.register(new HeatRecipeMelting(MOD_ID, "melt_bismuth_bronze_double_sheet", bronzeDoubleSheet, meltTemp, bismuthBronzeMetal, 200));
            System.out.println("[TFC Nuclear] Added: Bismuth Bronze double sheet -> Molten bismuth bronze (200 mB)");
        }
    }

    // ==================== INNER CLASS FOR HEAT RECIPE ====================

    private static class HeatRecipeMelting extends HeatRecipe {
        private final Metal metal;
        private final int amount;

        public HeatRecipeMelting(String modId, String name, ItemStack input, float transformTemp, Metal metal, int amount) {
            super(IIngredient.of(input), transformTemp, metal.getTier());
            this.metal = metal;
            this.amount = amount;
            setRegistryName(new ResourceLocation(modId, name));
        }

        @Override
        @Nullable
        public FluidStack getOutputFluid(ItemStack input) {
            if (isValidInput(input, metal.getTier())) {
                return new FluidStack(FluidsTFC.getFluidFromMetal(metal), amount);
            }
            return null;
        }

        public Metal getMetal() {
            return metal;
        }
    }
}