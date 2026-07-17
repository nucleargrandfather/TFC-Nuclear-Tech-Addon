package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.RecipesCommon.OreDictStack;
import com.hbm.items.machine.ItemStamp;
import com.hbm.util.Tuple.Pair;
import com.voided.tfcnuclear.compat.hbm.BloomStack;
import com.voided.tfcnuclear.inventory.items.ModItems;
import net.dries007.tfc.api.capability.forge.CapabilityForgeable;
import net.dries007.tfc.api.capability.forge.IForgeableMeasurableMetal;
import net.dries007.tfc.api.types.Metal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(value = com.hbm.inventory.recipes.PressRecipes.class, remap = false)
public abstract class MixinPressRecipes {

    @Shadow
    public static HashMap<Pair<RecipesCommon.AStack, ItemStamp.StampType>, ItemStack> recipes;

    @Shadow
    public static void makeRecipe(ItemStamp.StampType type, RecipesCommon.AStack in, ItemStack out) {}

    private static final String[] INPUT_ITEMS_TO_REMOVE = {
            "minecraft:iron_ingot",
            "hbm:ingot_copper",
            "hbm:powder_diamond"
    };

    private static final String[] OUTPUT_ITEMS_TO_REMOVE = {
            "hbm:plate_iron",
            "hbm:plate_copper",
            "minecraft:diamond"
    };

    @Inject(method = "registerDefaults", at = @At("RETURN"))
    private void onRegister(CallbackInfo ci) {
        // 1. Удаляем рецепты по входу
        List<Pair<RecipesCommon.AStack, ItemStamp.StampType>> toRemoveByInput = new ArrayList<>();
        for (Map.Entry<Pair<RecipesCommon.AStack, ItemStamp.StampType>, ItemStack> entry : recipes.entrySet()) {
            RecipesCommon.AStack input = entry.getKey().getKey();
            if (matchesInputItem(input)) {
                toRemoveByInput.add(entry.getKey());
            }
        }
        for (Pair<RecipesCommon.AStack, ItemStamp.StampType> key : toRemoveByInput) {
            recipes.remove(key);
        }

        // 2. Удаляем рецепты по выходу
        List<Pair<RecipesCommon.AStack, ItemStamp.StampType>> toRemoveByOutput = new ArrayList<>();
        for (Map.Entry<Pair<RecipesCommon.AStack, ItemStamp.StampType>, ItemStack> entry : recipes.entrySet()) {
            ItemStack output = entry.getValue();
            if (matchesOutputItem(output)) {
                toRemoveByOutput.add(entry.getKey());
            }
        }
        for (Pair<RecipesCommon.AStack, ItemStamp.StampType> key : toRemoveByOutput) {
            recipes.remove(key);
        }

        // 3. Добавляем рецепты для крицы (100, 200, 300, 400)
        Item unrefinedBloom = Item.getByNameOrId("tfc:bloom/unrefined");
        Item refinedBloom = Item.getByNameOrId("tfc:bloom/refined");

        if (unrefinedBloom != null && refinedBloom != null) {
            // Рецепты для каждого количества
            addBloomRecipe(unrefinedBloom, refinedBloom, 100);
            addBloomRecipe(unrefinedBloom, refinedBloom, 200);
            addBloomRecipe(unrefinedBloom, refinedBloom, 300);
            addBloomRecipe(unrefinedBloom, refinedBloom, 400);
        }

        // 4. Остальные рецепты
        makeRecipe(ItemStamp.StampType.PLATE, new OreDictStack("ingotWroughtIron"), new ItemStack(Item.getByNameOrId("hbm:plate_iron"), 1));
        makeRecipe(ItemStamp.StampType.PLATE, new OreDictStack("ingotBronze"), new ItemStack(Item.getByNameOrId("hbm:plate_copper"), 1));

        makeRecipe(ItemStamp.StampType.FLAT, new OreDictStack("ingotPigIron"), new ItemStack(Item.getByNameOrId("tfc:metal/ingot/high_carbon_steel"), 1));
        makeRecipe(ItemStamp.StampType.FLAT, new OreDictStack("ingotHighCarbonSteel"), new ItemStack(Item.getByNameOrId("hbm:ingot_steel"), 1));
        makeRecipe(ItemStamp.StampType.FLAT, new OreDictStack("ingotWeakBlueSteel"), new ItemStack(Item.getByNameOrId("tfc:metal/ingot/high_carbon_blue_steel"), 1));
        makeRecipe(ItemStamp.StampType.FLAT, new OreDictStack("ingotHighCarbonBlueSteel"), new ItemStack(Item.getByNameOrId("tfc:metal/ingot/blue_steel"), 1));
        makeRecipe(ItemStamp.StampType.FLAT, new OreDictStack("ingotWeakRedSteel"), new ItemStack(Item.getByNameOrId("tfc:metal/ingot/high_carbon_red_steel"), 1));
        makeRecipe(ItemStamp.StampType.FLAT, new OreDictStack("ingotHighCarbonRedSteel"), new ItemStack(Item.getByNameOrId("tfc:metal/ingot/red_steel"), 1));
        makeRecipe(ItemStamp.StampType.FLAT, new OreDictStack("ingotHighCarbonBlackSteel"), new ItemStack(Item.getByNameOrId("tfc:metal/ingot/black_steel"), 1));
        makeRecipe(ItemStamp.StampType.FLAT, new OreDictStack("fireClay"), new ItemStack(Item.getByNameOrId("tfc:ceramics/unfired/fire_brick"), 4));
        makeRecipe(ItemStamp.StampType.FLAT, new OreDictStack("clay"), new ItemStack(Item.getByNameOrId("tfc:ceramics/unfired/clay_brick"), 4));
        makeRecipe(ItemStamp.StampType.FLAT, new OreDictStack("dustDiamond"), new ItemStack(Item.getByNameOrId("tfc:gem/diamond"), 1, 2));
        makeRecipe(ItemStamp.StampType.FLAT, new ComparableStack(ModItems.ACID_ACTIVATED_CLAY), new ItemStack(com.hbm.items.ModItems.catalyst_clay));
    }

    // Вспомогательный метод для добавления рецепта крицы
    private void addBloomRecipe(Item input, Item output, int amount) {
        // Создаем выходной ItemStack с нужным количеством через Capability
        ItemStack outputStack = new ItemStack(output, 1);
        IForgeableMeasurableMetal capOutput = (IForgeableMeasurableMetal) outputStack.getCapability(CapabilityForgeable.FORGEABLE_CAPABILITY, null);
        if (capOutput != null) {
            capOutput.setMetal(Metal.WROUGHT_IRON);
            capOutput.setMetalAmount(amount);
        }

        // Добавляем рецепт
        makeRecipe(
                ItemStamp.StampType.FLAT,
                new BloomStack(input, amount),
                outputStack
        );
    }

    private static boolean matchesInputItem(RecipesCommon.AStack input) {
        if (input instanceof ComparableStack) {
            ComparableStack compStack = (ComparableStack) input;
            ItemStack stack = compStack.toStack();
            if (stack != null && !stack.isEmpty()) {
                String registryName = stack.getItem().getRegistryName().toString();
                for (String target : INPUT_ITEMS_TO_REMOVE) {
                    if (registryName.equals(target)) {
                        return true;
                    }
                }
            }
        }

        if (input instanceof OreDictStack) {
            OreDictStack oreStack = (OreDictStack) input;
            try {
                java.lang.reflect.Field nameField = OreDictStack.class.getDeclaredField("name");
                nameField.setAccessible(true);
                String oreName = ((String) nameField.get(oreStack)).toLowerCase();
                for (String target : INPUT_ITEMS_TO_REMOVE) {
                    String targetName = target.contains(":") ? target.split(":")[1] : target;
                    if (oreName.equals(targetName) || oreName.equals(target)) {
                        return true;
                    }
                }
            } catch (Exception e) {}
        }

        return false;
    }

    private static boolean matchesOutputItem(ItemStack output) {
        if (output == null || output.isEmpty()) return false;
        String registryName = output.getItem().getRegistryName().toString();
        for (String target : OUTPUT_ITEMS_TO_REMOVE) {
            if (registryName.equals(target)) {
                return true;
            }
        }
        return false;
    }
}