package com.voided.tfcnuclear.overwrite_contents.mixin.handler;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.ArcFurnaceRecipes;
import com.hbm.util.Tuple;
import com.voided.tfcnuclear.compat.hbm.NbtComparableBloomStack;
import net.dries007.tfc.api.capability.forge.CapabilityForgeable;
import net.dries007.tfc.api.capability.forge.IForgeableMeasurableMetal;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;

@Mixin(value = ArcFurnaceRecipes.class, remap = false)
public class MixinArcFurnaceRecipesGetOutput {

    @Shadow
    private static List<Tuple.Pair<RecipesCommon.AStack, ArcFurnaceRecipes.ArcFurnaceRecipe>> recipeList;

    @Shadow
    private static HashMap<RecipesCommon.ComparableStack, ArcFurnaceRecipes.ArcFurnaceRecipe> fastCacheSolid;

    @Shadow
    private static HashMap<RecipesCommon.ComparableStack, ArcFurnaceRecipes.ArcFurnaceRecipe> fastCacheLiquid;

    /**
     * Переопределяем getOutput для корректной работы с крицей
     */
    @Inject(method = "getOutput", at = @At("HEAD"), cancellable = true)
    private static void onGetOutput(ItemStack stack, boolean liquid, CallbackInfoReturnable<ArcFurnaceRecipes.ArcFurnaceRecipe> cir) {
        if (!Loader.isModLoaded("tfc")) return;
        if (stack == null || stack.isEmpty()) return;

        // Проверяем, является ли предмет крицей
        String name = stack.getItem().getRegistryName().toString();
        if (!name.equals("tfc:bloom/refined") && !name.equals("tfc:bloom/unrefined")) return;

        // Получаем Capability
        IForgeableMeasurableMetal cap = (IForgeableMeasurableMetal)
                stack.getCapability(CapabilityForgeable.FORGEABLE_CAPABILITY, null);
        if (cap == null) return;

        int metalAmount = cap.getMetalAmount();

        // Ищем рецепт с ТОЧНЫМ соответствием
        for (Tuple.Pair<RecipesCommon.AStack, ArcFurnaceRecipes.ArcFurnaceRecipe> entry : recipeList) {
            if (entry.getKey() instanceof NbtComparableBloomStack) {
                NbtComparableBloomStack bloomStack = (NbtComparableBloomStack) entry.getKey();
                if (bloomStack.matchesRecipe(stack, true)) {
                    ArcFurnaceRecipes.ArcFurnaceRecipe recipe = entry.getValue();
                    if ((liquid && recipe.fluidOutput != null) || (!liquid && recipe.solidOutput != null)) {
                        cir.setReturnValue(recipe);
                        return;
                    }
                }
            }
        }

        cir.setReturnValue(null);
    }
}
