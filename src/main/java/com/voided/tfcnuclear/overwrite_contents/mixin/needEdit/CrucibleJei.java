package com.voided.tfcnuclear.overwrite_contents.mixin.needEdit;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.recipes.CrucibleRecipes;
import com.hbm.items.machine.ItemScraps;
import com.voided.tfcnuclear.inventory.material.TFCNuclearMats;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = CrucibleRecipes.class, remap = false)
public class CrucibleJei {

    // Префиксы для кованого железа (включая проволоку)
    private static final Set<String> WROUGHT_PREFIXES = new HashSet<>(Arrays.asList(
            "plate", "castplate", "weldedplate", "shell", "ntmpipe",
            "wire", "densewire", "bolt", "dusttiny", "minecart", "rail",
            "billet", "barrel_light", "barrel_heavy", "receiver_light",
            "receiver_heavy", "mechanism", "stock", "grip"
    ));

    // Префиксы для бронзы (без проволоки)
    private static final Set<String> BRONZE_PREFIXES = new HashSet<>(Arrays.asList(
            "plate", "castplate", "weldedplate", "shell", "ntmpipe",
            // "wire", "densewire" - проволока НЕ переплавляется в бронзу
            "bolt", "dusttiny", "minecart", "rail",
            "billet", "barrel_light", "barrel_heavy", "receiver_light",
            "receiver_heavy", "mechanism", "stock", "grip"
    ));

    @Inject(method = "getSmeltingRecipes", at = @At("RETURN"), cancellable = true, remap = false)
    private static void fixSmeltingRecipes(CallbackInfoReturnable<HashMap<RecipesCommon.AStack, List<ItemStack>>> cir) {
        HashMap<RecipesCommon.AStack, List<ItemStack>> original = cir.getReturnValue();
        HashMap<RecipesCommon.AStack, List<ItemStack>> modified = new HashMap<>();

        for (Map.Entry<RecipesCommon.AStack, List<ItemStack>> entry : original.entrySet()) {
            RecipesCommon.AStack key = entry.getKey();
            List<ItemStack> value = entry.getValue();

            if (key instanceof RecipesCommon.OreDictStack) {
                String oreName = ((RecipesCommon.OreDictStack) key).name;

                // Сначала проверяем на бронзу
                if (shouldBeBronze(oreName)) {
                    List<ItemStack> newStacks = new ArrayList<>();
                    for (ItemStack stack : value) {
                        MaterialStack mats = ItemScraps.getMats(stack);
                        int amount = mats == null ? TFCNuclearMats.MAT_BRONZE.convIn / 2 : mats.amount;

                        ItemStack newStack = ItemScraps.create(
                                new Mats.MaterialStack(TFCNuclearMats.MAT_BRONZE, amount),
                                true);
                        newStacks.add(newStack);
                    }
                    modified.put(key, newStacks);
                    continue;
                }

                // Затем проверяем на кованое железо
                if (shouldBeWroughtIron(oreName)) {
                    List<ItemStack> newStacks = new ArrayList<>();
                    for (ItemStack stack : value) {
                        // ИСПРАВЛЕНО: правильно получаем количество материала
                        MaterialStack mats = ItemScraps.getMats(stack);
                        int amount = mats == null ? Mats.MAT_IRON.convIn : mats.amount;

                        ItemStack newStack = ItemScraps.create(
                                new Mats.MaterialStack(Mats.MAT_WROUGHTIRON, amount),
                                true);
                        newStacks.add(newStack);
                    }
                    modified.put(key, newStacks);
                    continue;
                }
            }
            modified.put(key, value);
        }

        cir.setReturnValue(modified);
    }

    private static boolean shouldBeWroughtIron(String oreName) {
        String lower = oreName.toLowerCase();
        if (!lower.endsWith("iron")) return false;
        if (lower.startsWith("ingot") || lower.startsWith("block") ||
                lower.startsWith("nugget") || lower.startsWith("fragment")) return false;
        for (String prefix : WROUGHT_PREFIXES) {
            if (lower.startsWith(prefix)) return true;
        }
        return false;
    }

    private static boolean shouldBeBronze(String oreName) {
        String lower = oreName.toLowerCase();
        if (!lower.endsWith("copper")) return false;
        if (lower.startsWith("ingot") || lower.startsWith("block") ||
                lower.startsWith("nugget") || lower.startsWith("fragment")) return false;

        for (String prefix : BRONZE_PREFIXES) {
            if (lower.startsWith(prefix)) return true;
        }
        return false;
    }
}