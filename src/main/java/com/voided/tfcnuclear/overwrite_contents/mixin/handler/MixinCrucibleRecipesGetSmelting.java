package com.voided.tfcnuclear.overwrite_contents.mixin.handler;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.CrucibleRecipes;
import com.hbm.inventory.material.Mats;
import com.hbm.items.machine.ItemScraps;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.voided.tfcnuclear.compat.hbm.BloomAStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mixin(value = CrucibleRecipes.class, remap = false)
public class MixinCrucibleRecipesGetSmelting {

    @Inject(
            method = "getSmeltingRecipes",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void onGetSmeltingRecipes(CallbackInfoReturnable<HashMap<RecipesCommon.AStack, List<ItemStack>>> cir) {
        HashMap<RecipesCommon.AStack, List<ItemStack>> map = cir.getReturnValue();

        // Удаляем старые рецепты крицы
        map.entrySet().removeIf(entry -> entry.getKey() instanceof BloomAStack);

        // Добавляем рецепт для каждого количества ДЛЯ JEI
        for (int amount : new int[]{100, 200, 300, 400}) {
            int hbmQuanta = (amount * 72) / 100;
            if (hbmQuanta <= 0) continue;

            BloomAStack bloomAStack = new BloomAStack(amount);

            List<ItemStack> outputs = new ArrayList<>();
            outputs.add(
                    ItemScraps.create(
                            new Mats.MaterialStack(Mats.MAT_WROUGHTIRON, hbmQuanta),
                            true
                    )
            );
            map.put(bloomAStack, outputs);
        }

        cir.setReturnValue(map);
    }
}