package com.voided.tfcnuclear.overwrite_contents.mixin.needEdit;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.recipes.ArcFurnaceRecipes;
import com.hbm.items.machine.ItemScraps;
import com.voided.tfcnuclear.inventory.material.TFCNuclearMats;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = ArcFurnaceRecipes.class, remap = false)
public class ArcFurnaceJei {

    private static final Set<String> WROUGHT_PATTERNS = new HashSet<>(Arrays.asList(
            "plate", "castplate", "weldedplate",
            "shell", "ntmpipe", "wire", "densewire",
            "bolt", "dusttiny",
            "billet", "barrel", "receiver",
            "mechanism", "stock", "grip"
    ));

    private static final Set<String> BRONZE_PATTERNS = new HashSet<>(Arrays.asList(
            "plate", "castplate", "weldedplate",
            "shell", "ntmpipe",
            "bolt", "dusttiny",
            "billet", "barrel", "receiver",
            "mechanism", "stock", "grip"
    ));

    @Inject(method = "getFluidRecipes", at = @At("RETURN"), cancellable = true, remap = false)
    private static void fixFluidRecipes(CallbackInfoReturnable<HashMap<Object, Object>> cir) {
        HashMap<Object, Object> original = cir.getReturnValue();
        HashMap<Object, Object> modified = new HashMap<>(original);

        for (Map.Entry<Object, Object> entry : original.entrySet()) {
            Object key = entry.getKey();

            if (key instanceof RecipesCommon.OreDictStack) {
                String oreName = ((RecipesCommon.OreDictStack) key).name;

                // Сначала проверяем на бронзу
                if (isBronzeOreName(oreName)) {
                    Object[] outputs = (Object[]) entry.getValue();
                    Object[] modifiedOutputs = new Object[outputs.length];

                    for (int i = 0; i < outputs.length; i++) {
                        if (outputs[i] instanceof ItemStack) {
                            ItemStack stack = (ItemStack) outputs[i];
                            if (stack.getItem() instanceof com.hbm.items.machine.ItemScraps) {
                                MaterialStack mats = ItemScraps.getMats(stack);
                                if (mats.material == Mats.MAT_COPPER) {
                                    modifiedOutputs[i] = ItemScraps.create(
                                            new MaterialStack(TFCNuclearMats.MAT_BRONZE, mats.amount), true);
                                } else {
                                    modifiedOutputs[i] = outputs[i];
                                }
                            } else {
                                modifiedOutputs[i] = outputs[i];
                            }
                        } else {
                            modifiedOutputs[i] = outputs[i];
                        }
                    }

                    modified.put(key, modifiedOutputs);
                    continue;
                }

                // Затем проверяем на кованое железо
                if (isWroughtIronOreName(oreName)) {
                    Object[] outputs = (Object[]) entry.getValue();
                    Object[] modifiedOutputs = new Object[outputs.length];

                    for (int i = 0; i < outputs.length; i++) {
                        if (outputs[i] instanceof ItemStack) {
                            ItemStack stack = (ItemStack) outputs[i];
                            if (stack.getItem() instanceof com.hbm.items.machine.ItemScraps) {
                                MaterialStack mats = ItemScraps.getMats(stack);
                                if (mats.material == Mats.MAT_IRON) {
                                    modifiedOutputs[i] = ItemScraps.create(
                                            new MaterialStack(Mats.MAT_WROUGHTIRON, mats.amount), true);
                                } else {
                                    modifiedOutputs[i] = outputs[i];
                                }
                            } else {
                                modifiedOutputs[i] = outputs[i];
                            }
                        } else {
                            modifiedOutputs[i] = outputs[i];
                        }
                    }

                    modified.put(key, modifiedOutputs);
                }
            }
        }

        cir.setReturnValue(modified);
    }

    private static boolean isWroughtIronOreName(String oreName) {
        String lower = oreName.toLowerCase();

        if (!lower.contains("iron")) return false;

        if (lower.startsWith("ingot") || lower.startsWith("block") ||
                lower.startsWith("nugget") || lower.startsWith("fragment") || lower.startsWith("dust")) return false;

        for (String pattern : WROUGHT_PATTERNS) {
            if (lower.contains(pattern)) return true;
        }

        return false;
    }

    private static boolean isBronzeOreName(String oreName) {
        String lower = oreName.toLowerCase();

        if (!lower.contains("copper")) return false;

        if (lower.startsWith("ingot") || lower.startsWith("block") ||
                lower.startsWith("nugget") || lower.startsWith("fragment") || lower.startsWith("dust")) return false;

        for (String pattern : BRONZE_PATTERNS) {
            if (lower.contains(pattern)) return true;
        }

        return false;
    }
}