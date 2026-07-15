package com.voided.tfcnuclear.inventory.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.Arrays;
import java.util.List;

public class CraftingRecipes {

    private static final String MOD_ID = "tfcnuclear";
    private static boolean recipesAdded = false;

    private static ItemStack item(String id) {
        Item item = Item.getByNameOrId(id);
        return item != null ? new ItemStack(item) : ItemStack.EMPTY;
    }

    private static ItemStack item(String id, int count) {
        Item item = Item.getByNameOrId(id);
        return item != null ? new ItemStack(item, count) : ItemStack.EMPTY;
    }

    private static ItemStack item(String id, int count, int meta) {
        Item item = Item.getByNameOrId(id);
        return item != null ? new ItemStack(item, count, meta) : ItemStack.EMPTY;
    }

    private static Object ore(String name) {
        return name;
    }

    // Метод для добавления shaped рецепта с молотом
    private static void addHammerShapedRecipe(String name, ItemStack result, Object... ingredients) {
        if (result.isEmpty() || ingredients == null || ingredients.length < 3) return;

        // Первые 3 элемента - это строки паттерна
        String[] pattern = new String[3];
        for (int i = 0; i < 3 && i < ingredients.length; i++) {
            if (ingredients[i] instanceof String) {
                pattern[i] = (String) ingredients[i];
            } else {
                return;
            }
        }

        // Определяем размеры сетки
        int width = 0;
        int height = 0;
        for (String row : pattern) {
            if (row != null && !row.isEmpty()) {
                height++;
                width = Math.max(width, row.length());
            }
        }

        if (height == 0 || width == 0) return;

        // Создаем карту для преобразования char -> Ingredient
        java.util.Map<Character, Ingredient> charMap = new java.util.HashMap<>();

        for (int i = 3; i < ingredients.length; i += 2) {
            if (i + 1 >= ingredients.length) break;
            Object key = ingredients[i];
            Object value = ingredients[i + 1];

            if (key instanceof Character) {
                char c = (Character) key;
                if (value instanceof String && ((String) value).equals("hammer")) {
                    // Для молота создаем Ingredient на основе OreDict
                    NonNullList<ItemStack> hammerStacks = NonNullList.create();
                    for (ItemStack stack : OreDictionary.getOres("hammer", false)) {
                        hammerStacks.add(stack.copy());
                    }
                    if (!hammerStacks.isEmpty()) {
                        charMap.put(c, Ingredient.fromStacks(hammerStacks.toArray(new ItemStack[0])));
                    }
                } else if (value instanceof String) {
                    // Для OreDict
                    NonNullList<ItemStack> oreStacks = NonNullList.create();
                    for (ItemStack stack : OreDictionary.getOres((String) value, false)) {
                        oreStacks.add(stack.copy());
                    }
                    if (!oreStacks.isEmpty()) {
                        charMap.put(c, Ingredient.fromStacks(oreStacks.toArray(new ItemStack[0])));
                    }
                } else if (value instanceof ItemStack) {
                    charMap.put(c, Ingredient.fromStacks((ItemStack) value));
                }
            }
        }

        // Заполняем ингредиенты по паттерну
        NonNullList<Ingredient> inputList = NonNullList.create();
        for (String row : pattern) {
            if (row != null) {
                for (char c : row.toCharArray()) {
                    Ingredient ing = charMap.get(c);
                    if (ing == null) {
                        inputList.add(Ingredient.EMPTY);
                    } else {
                        inputList.add(ing);
                    }
                }
            }
        }
    }

    private static class RecipeBuilder {
        private final String name;
        private final ItemStack result;
        private Object[] ingredients;
        private int resultCount = 1;
        private int resultMeta = 0;
        private boolean useHammerRecipe = false;

        RecipeBuilder(String name, ItemStack result) {
            this.name = name;
            this.result = result;
        }

        RecipeBuilder count(int count) {
            this.resultCount = count;
            return this;
        }

        RecipeBuilder meta(int meta) {
            this.resultMeta = meta;
            return this;
        }

        RecipeBuilder shape(String pattern, Object... ingredients) {
            String[] rows = pattern.split("/");
            this.ingredients = new Object[rows.length + ingredients.length];
            System.arraycopy(rows, 0, this.ingredients, 0, rows.length);
            System.arraycopy(ingredients, 0, this.ingredients, rows.length, ingredients.length);
            return this;
        }

        RecipeBuilder withHammer() {
            this.useHammerRecipe = true;
            return this;
        }

        void register() {
            if (result.isEmpty() || ingredients == null) return;

            ItemStack finalResult = result.copy();
            finalResult.setCount(resultCount);
            if (resultMeta > 0) {
                finalResult.setItemDamage(resultMeta);
            }

            // Если рецепт с молотом, используем специальный метод
            if (useHammerRecipe) {
                addHammerShapedRecipe(name, finalResult, ingredients);
                return;
            }

            GameRegistry.addShapedRecipe(
                    new ResourceLocation(MOD_ID, name),
                    new ResourceLocation(MOD_ID, name),
                    finalResult,
                    ingredients
            );
        }
    }

    private static final List<ResourceLocation> RECIPES_TO_REMOVE = Arrays.asList(
            // Vanilla
            new ResourceLocation("minecraft", "golden_carrot"),
            new ResourceLocation("minecraft", "flint_and_steel"),

            // HBM
            new ResourceLocation("hbm", "big_sword"),
            new ResourceLocation("hbm", "bobmazon"),
            new ResourceLocation("hbm", "bismuth_helmet"),
            new ResourceLocation("hbm", "ball_dynamite"),
            new ResourceLocation("hbm", "waste_trinitite"),
            new ResourceLocation("hbm", "ammo_standard_2"),
            new ResourceLocation("hbm", "machine_ammo_press"),
            new ResourceLocation("hbm", "machine_furnace_brick_off"),
            new ResourceLocation("hbm", "lightstone_2"),
            new ResourceLocation("hbm", "press_preheater"),
            new ResourceLocation("hbm", "reinforced_stone_2"),
            new ResourceLocation("hbm", "foundry_channel"),
            new ResourceLocation("hbm", "foundry_mold"),
            new ResourceLocation("hbm", "foundry_basin"),
            new ResourceLocation("hbm", "machine_armor_table"),
            new ResourceLocation("hbm", "machine_autocrafter"),
            new ResourceLocation("hbm", "machine_weapon_table"),
            new ResourceLocation("hbm", "bucket"),
            new ResourceLocation("hbm", "brick_fire_2"),
            new ResourceLocation("hbm", "machine_press"),
            new ResourceLocation("hbm", "furnace_iron"),
            new ResourceLocation("hbm", "machine_wood_burner"),
            new ResourceLocation("hbm", "machine_electric_furnace_off"),
            new ResourceLocation("hbm", "gold_ingot"),
            new ResourceLocation("hbm", "ball_fireclay_1"),
            new ResourceLocation("hbm", "ball_fireclay_2"),
            new ResourceLocation("hbm", "brick_concrete_2"),
            new ResourceLocation("hbm", "brick_concrete_3"),
            new ResourceLocation("hbm", "ducrete_brick_2"),
            new ResourceLocation("hbm", "ducrete_brick_3"),

            // TFC
            new ResourceLocation("tfc", "vanilla/flint_and_steel"),
            new ResourceLocation("tfc", "fire_clay"),
            new ResourceLocation("tfc", "fire_bricks"),

            // Дополнительные крафты для удаления
            new ResourceLocation("minecraft", "torch"),
            new ResourceLocation("minecraft", "iron_door"),
            new ResourceLocation("minecraft", "gold_ingot"),
            new ResourceLocation("minecraft", "gold_ingot_from_block"),
            new ResourceLocation("minecraft", "gold_ingot_from_nuggets"),

            // TFC крафты
            new ResourceLocation("tfc", "flux"),
            // HBM броня и инструменты
            new ResourceLocation("hbm", "steel_helmet"),
            new ResourceLocation("hbm", "steel_plate"),
            new ResourceLocation("hbm", "steel_legs"),
            new ResourceLocation("hbm", "steel_boots"),
            new ResourceLocation("hbm", "steel_sword"),
            new ResourceLocation("hbm", "steel_pickaxe"),
            new ResourceLocation("hbm", "steel_axe"),
            new ResourceLocation("hbm", "steel_shovel"),
            new ResourceLocation("hbm", "steel_hoe"),
            new ResourceLocation("hbm", "crane_unboxer"),

            new ResourceLocation("hbm", "ball_fireclay"),
            new ResourceLocation("hbm", "ingot_firebrick"),
            new ResourceLocation("hbm", "trapdoor_steel"),

            new ResourceLocation("hbm", "metal/dust/gold_9to1_odm_0"),
            new ResourceLocation("hbm", "ore/cryolite_9to1_odm_0"),
            new ResourceLocation("hbm", "metal/dust/copper_9to1_odm_0"),
            new ResourceLocation("hbm", "metal/dust/lead_9to1_odm_0"),
            new ResourceLocation("hbm", "metal/dust/bismuth_9to1_odm_0"),
            new ResourceLocation("hbm", "ore/cinnabar_9to1_odm_0"),
            new ResourceLocation("hbm", "powder/saltpeter_9to1_odm_0"),
            new ResourceLocation("hbm", "powder/sulfur_9to1_odm_0"),
            new ResourceLocation("hbm", "powder/bismuth_9to1_odm_0"),
            new ResourceLocation("hbm", "scraps_3"),
            new ResourceLocation("hbm", "scraps_4"),
            new ResourceLocation("hbm", "anvil_iron")
    );

    private static void registerAllRecipes() {

        new RecipeBuilder("powder_bismuth", item("hbm:powder_bismuth"))
                .shape("XXX/XXX/XXX", 'X', ore("bedrockorefragmentBismuth")).register();

        new RecipeBuilder("powder_gold", item("hbm:powder_gold"))
                .shape("XXX/XXX/XXX", 'X', ore("bedrockorefragmentGold")).register();

        new RecipeBuilder("powder_copper", item("hbm:powder_copper"))
                .shape("XXX/XXX/XXX", 'X', ore("bedrockorefragmentCopper")).register();

        new RecipeBuilder("powder_lead", item("hbm:powder_lead"))
                .shape("XXX/XXX/XXX", 'X', ore("bedrockorefragmentLead")).register();

        new RecipeBuilder("cinnabar", item("hbm:cinnabar"))
                .shape("XXX/XXX/XXX", 'X', ore("bedrockorefragmentCinnabar")).register();

        new RecipeBuilder("niter", item("hbm:niter"))
                .shape("XXX/XXX/XXX", 'X', ore("bedrockorefragmentSaltpeter")).register();

        new RecipeBuilder("sulfur", item("hbm:sulfur"))
                .shape("XXX/XXX/XXX", 'X', ore("bedrockorefragmentSulfur")).register();

        new RecipeBuilder("cryolite", item("hbm:chunk_ore", 1, 2))
                .shape("XXX/XXX/XXX", 'X', ore("bedrockorefragmentCryolite")).register();

        // === Проволока в слитки (OreDict) ===
        new RecipeBuilder("ingot_gold", item("tfc:metal/ingot/gold"))
                .shape("XXX/XXX/XXX", 'X', ore("wireFineGold")).register();

        // === Инструменты и предметы (ItemStack + OreDict) ===
        new RecipeBuilder("big_sword", item("hbm:big_sword"))
                .shape("QXQ/QXQ/GYG",
                        'X', item("tfc:metal/ingot/wrought_iron"),
                        'Q', item("minecraft:quartz"),
                        'Y', item("minecraft:stick"),
                        'G', item("tfc:metal/ingot/gold")).register();

        new RecipeBuilder("golden_carrot", item("minecraft:golden_carrot"))
                .shape("XXX/XCX/XXX",
                        'X', item("tfc:metal/nugget/gold"),
                        'C', item("minecraft:carrot")).register();

        new RecipeBuilder("bobmazon", item("hbm:bobmazon"))
                .shape("BN /SL /   ",
                        'B', item("minecraft:book"),
                        'N', item("tfc:metal/nugget/gold"),
                        'S', item("minecraft:string"),
                        'L', ore("dyeBlue")).register();

        new RecipeBuilder("flint_and_steel", item("minecraft:flint_and_steel"))
                .shape("S / F",
                        'S', item("hbm:ingot_steel"),
                        'F', item("minecraft:flint")).register();

        new RecipeBuilder("bismuth_helmet", item("hbm:bismuth_helmet"))
                .shape("GBB/B  /RBB",
                        'B', item("hbm:plate_bismuth"),
                        'G', item("tfc:metal/ingot/gold"),
                        'R', item("hbm:rag")).register();

        // === Рецепты с молотом (теперь с правильным расходом прочности) ===

        new RecipeBuilder("powder_flux", item("hbm:powder_flux"))
                .shape("HR/  ", 'H', "hammer", 'R', ore("rockFlux"))
                .register();

        new RecipeBuilder("lignite", item("hbm:lignite"))
                .shape("HR/  ", 'H', "hammer", 'R', ore("tfcLignite"))
                .register();

        new RecipeBuilder("coal", item("minecraft:coal"))
                .shape("HR/  ", 'H', "hammer", 'R', item("tfc:ore/bituminous_coal"))
                .register();

        new RecipeBuilder("powder_lapis", item("minecraft:dye", 2, 4))
                .shape("HR/  ", 'H', "hammer", 'R', item("tfc:ore/lapis_lazuli", 1))
                .register();

        // === Прочие рецепты ===
        new RecipeBuilder("bolt", item("hbm:bolt"))
                .count(16).meta(30)
                .shape(" S/ S", 'S', item("hbm:ingot_steel")).register();

        new RecipeBuilder("fire_clay", item("tfc:ceramics/fire_clay"))
                .shape("KGK/GSG/KGK",
                        'S', item("hbm:ball_fireclay"),
                        'G', item("tfc:powder/graphite"),
                        'K', item("tfc:powder/kaolinite")).register();

        new RecipeBuilder("brick_fire", item("hbm:brick_fire"))
                .count(2)
                .shape("FMF/MFM/FMF",
                        'F', item("hbm:ingot_firebrick"),
                        'M', item("tfc:mortar")).register();

        new RecipeBuilder("ball_dynamite", item("hbm:ball_dynamite"))
                .shape("NM/SB",
                        'N', item("hbm:niter"),
                        'M', item("minecraft:sugar"),
                        'S', ore("sand"),
                        'B', ore("ntmchemistryset")).register();

        new RecipeBuilder("waste_trinitite", item("hbm:waste_trinitite"))
                .shape("ST/  ",
                        'T', item("hbm:trinitite"),
                        'S', ore("sand")).register();

        new RecipeBuilder("ammo_standard", item("hbm:ammo_standard"))
                .count(1).meta(3)
                .shape(" G / P / M ",
                        'G', ore("gravel"),
                        'M', item("minecraft:gunpowder"),
                        'P', ore("paper")).register();

        // === Машины и блоки ===
        new RecipeBuilder("machine_ammo_press", item("hbm:machine_ammo_press"))
                .shape("WPW/B B/SSS",
                        'W', ore("ingotWroughtIron"),
                        'P', item("minecraft:piston"),
                        'B', ore("ingotBronze"),
                        'S', ore("stone")).register();

        new RecipeBuilder("machine_furnace_brick_off", item("hbm:machine_furnace_brick_off"))
                .shape("BBB/B B/SSS",
                        'B', item("minecraft:brick"),
                        'S', ore("stoneBrick")).register();

        new RecipeBuilder("reinforced_stone", item("hbm:reinforced_stone"))
                .count(4)
                .shape("FMF/MFM/FMF",
                        'F', ore("cobblestone"),
                        'M', ore("stone")).register();

        new RecipeBuilder("lightstone", item("hbm:lightstone"))
                .shape("SS/SL",
                        'S', ore("stone"),
                        'L', ore("dustLimestone")).register();

        new RecipeBuilder("press_preheater", item("hbm:press_preheater"))
                .shape("CCC/SLS/TST",
                        'S', ore("stone"),
                        'C', ore("plateCopper"),
                        'L', ore("container1000lava"),
                        'T', ore("ingotTungsten")).register();

        new RecipeBuilder("sandstone", item("minecraft:sandstone"))
                .count(4)
                .shape("FMF/MFM/FMF",
                        'F', ore("sand"),
                        'M', ore("stone")).register();

        new RecipeBuilder("stone_slab", item("minecraft:stone_slab"))
                .count(6).meta(3)
                .shape("   /CCC/   ",
                        'C', item("minecraft:cobblestone")).register();

        new RecipeBuilder("stone_stairs", item("minecraft:stone_stairs"))
                .count(8)
                .shape("C  /CC /CCC",
                        'C', item("minecraft:cobblestone")).register();

        new RecipeBuilder("cobblestone_wall", item("minecraft:cobblestone_wall"))
                .count(8)
                .shape("   /CCC/CCC",
                        'C', item("minecraft:cobblestone")).register();

        new RecipeBuilder("foundry_channel", item("hbm:foundry_channel"))
                .shape("B B/ S ",
                        'B', item("hbm:ingot_firebrick"),
                        'S', ore("slabStoneBrick")).register();

        new RecipeBuilder("foundry_mold", item("hbm:foundry_mold"))
                .shape("B B/BSB",
                        'B', item("hbm:ingot_firebrick"),
                        'S', ore("slabStoneBrick")).register();

        new RecipeBuilder("foundry_basin", item("hbm:foundry_basin"))
                .shape("B B/B B/BSB",
                        'B', item("hbm:ingot_firebrick"),
                        'S', ore("slabStoneBrick")).register();

        new RecipeBuilder("machine_autocrafter", item("hbm:machine_autocrafter"))
                .shape("PCP/MWM/PCP",
                        'P', ore("plateSteel"),
                        'C', item("hbm:circuit"),
                        'M', item("hbm:motor"),
                        'W', ore("craftingTableWood")).register();

        new RecipeBuilder("machine_weapon_table", item("hbm:machine_weapon_table"))
                .shape("GGG/SWS/SBS",
                        'G', ore("plateGunMetal"),
                        'S', ore("ingotSteel"),
                        'B', ore("blockSteel"),
                        'W', ore("craftingTableWood")).register();

        new RecipeBuilder("machine_armor_table", item("hbm:machine_armor_table"))
                .shape("GGG/SWS/SBS",
                        'G', ore("plateSteel"),
                        'S', ore("ingotTungsten"),
                        'B', ore("blockSteel"),
                        'W', ore("craftingTableWood")).register();

        new RecipeBuilder("crane_unboxer", item("hbm:crane_unboxer"))
                .shape("SSS/SNS/CCC",
                        'S', ore("stickWood"),
                        'N', ore("shears"),
                        'C', item("hbm:conveyor_wand")).register();

        new RecipeBuilder("trapdoor_steel", item("hbm:trapdoor_steel"))
                .shape("TS/  ",
                        'T', ore("trapdoorWood"),
                        'S', item("hbm:ingot_steel")).register();

        new RecipeBuilder("anvil_iron", item("hbm:anvil_iron"))
                .shape("WWW/ D /WWW",
                        'W', ore("ingotWroughtIron"),
                        'D', ore("ingotDoubleWroughtIron")).register();
        new RecipeBuilder("brick_concrete_2", item("hbm:brick_concrete"))
                .shape(" C /CMC/ C ",
                        'C', item("hbm:concrete_smooth"),
                        'M', ore("mortar")).register();
        new RecipeBuilder("brick_concrete_3", item("hbm:brick_concrete"))
                .shape(" C /CMC/ C ",
                        'C', item("hbm:concrete"),
                        'M', ore("mortar")).register();
        new RecipeBuilder("ducrete_brick_2", item("hbm:ducrete_brick"))
                .shape("MDM/DLD/MDM",
                        'D', item("hbm:ducrete_smooth"),
                        'M', ore("mortar"),
                        'L', ore("plateLead")).register();
        new RecipeBuilder("ducrete_brick_3", item("hbm:ducrete_brick"))
                .shape("MDM/DLD/MDM",
                        'D', item("hbm:ducrete"),
                        'M', ore("mortar"),
                        'L', ore("plateLead")).register();

    }

    public static void addRecipes() {
        if (recipesAdded) return;

        removeRecipes();
        registerAllRecipes();

        recipesAdded = true;
    }

    private static void removeRecipes() {
        IForgeRegistryModifiable registry = (IForgeRegistryModifiable) ForgeRegistries.RECIPES;
        RECIPES_TO_REMOVE.stream()
                .filter(registry::containsKey)
                .forEach(registry::remove);
    }
}