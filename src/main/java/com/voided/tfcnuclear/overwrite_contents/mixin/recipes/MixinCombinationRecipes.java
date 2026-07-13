package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.recipes.CombinationRecipes;
import com.hbm.items.ModItems;
import com.hbm.util.Tuple;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(value = CombinationRecipes.class, remap = false)
public abstract class MixinCombinationRecipes {

    @Shadow
    public static HashMap<Object, Tuple.Pair<ItemStack, FluidStack>> recipes;

    /**
     * Добавляет кастомные рецепты в CombinationRecipes
     */
    @Inject(method = "registerDefaults", at = @At("RETURN"))
    private void addCustomRecipes(CallbackInfo ci) {

        // === ПРИМЕР 1: TFC флюорит → Флюс + Серная кислота ===
        Item tfcGraphite = Item.getByNameOrId("tfc:powder/graphite");
        if (tfcGraphite != null) {
            recipes.put(
                    new ComparableStack(tfcGraphite),
                    new Tuple.Pair<>(new ItemStack(ModItems.ingot_graphite, 1), null));
        }
    }
}