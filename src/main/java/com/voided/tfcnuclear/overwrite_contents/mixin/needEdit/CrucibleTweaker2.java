package com.voided.tfcnuclear.overwrite_contents.mixin.needEdit;

import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.voided.tfcnuclear.inventory.material.TFCNuclearMats;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = Mats.class, remap = false)
public class CrucibleTweaker2 {

    private static final Set<String> BRONZE_PREFIXES = new HashSet<>(Arrays.asList(
            "plate", "castplate", "weldedplate",
            "shell", "ntmpipe",
            "bolt", "dusttiny", "minecart", "rail",
            "billet", "barrel_light", "barrel_heavy",
            "receiver_light", "receiver_heavy",
            "mechanism", "stock", "grip"
    ));

    @Inject(method = "getMaterialsFromItem", at = @At("RETURN"), cancellable = true, remap = false)
    private static void fixMaterialsFromItem(ItemStack stack, CallbackInfoReturnable<List<MaterialStack>> cir) {
        List<MaterialStack> results = cir.getReturnValue();
        if (results == null || results.isEmpty()) return;

        if (isBronzeItem(stack)) {
            List<MaterialStack> modified = new ArrayList<>();
            for (MaterialStack ms : results) {
                if (ms.material == Mats.MAT_COPPER) {
                    modified.add(new MaterialStack(TFCNuclearMats.MAT_BRONZE, ms.amount));
                } else {
                    modified.add(ms);
                }
            }
            cir.setReturnValue(modified);
        }
    }

    @Inject(method = "getSmeltingMaterialsFromItem", at = @At("RETURN"), cancellable = true, remap = false)
    private static void fixSmeltingMaterialsFromItem(ItemStack stack, CallbackInfoReturnable<List<MaterialStack>> cir) {
        List<MaterialStack> results = cir.getReturnValue();
        if (results == null || results.isEmpty()) return;

        if (isBronzeItem(stack)) {
            List<MaterialStack> modified = new ArrayList<>();
            for (MaterialStack ms : results) {
                if (ms.material == Mats.MAT_COPPER) {
                    modified.add(new MaterialStack(TFCNuclearMats.MAT_BRONZE, ms.amount));
                } else {
                    modified.add(ms);
                }
            }
            cir.setReturnValue(modified);
        }
    }

    private static boolean isBronzeItem(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("isBronzeIron")) {
            return true;
        }

        List<String> oreNames = com.hbm.util.ItemStackUtil.getOreDictNames(stack);

        for (String oreName : oreNames) {
            String lower = oreName.toLowerCase();

            if (!lower.endsWith("copper")) continue;

            if (lower.startsWith("ingot") || lower.startsWith("block") ||
                    lower.startsWith("nugget") || lower.startsWith("fragment")) continue;

            for (String prefix : BRONZE_PREFIXES) {
                if (lower.startsWith(prefix)) return true;
            }
        }

        return false;
    }
}