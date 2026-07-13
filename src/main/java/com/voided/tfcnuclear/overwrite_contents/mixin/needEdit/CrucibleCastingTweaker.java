package com.voided.tfcnuclear.overwrite_contents.mixin.needEdit;

import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.NTMMaterial;
import com.hbm.items.machine.ItemMold;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(value = ItemMold.MoldShape.class, remap = false)
public abstract class CrucibleCastingTweaker {

    @Shadow
    MaterialShapes shape;

    @Shadow
    int amount;  // ← ДОБАВЬ! Это количество предметов на выходе

    private static final Set<MaterialShapes> IRON_ALLOWED = new HashSet<>(Arrays.asList(
            MaterialShapes.BLOCK, MaterialShapes.INGOT, MaterialShapes.NUGGET
    ));

    private static final Set<MaterialShapes> WROUGHT_AS_IRON = new HashSet<>(Arrays.asList(
            MaterialShapes.PLATE, MaterialShapes.CASTPLATE, MaterialShapes.WELDEDPLATE,
            MaterialShapes.SHELL, MaterialShapes.PIPE, MaterialShapes.WIRE,
            MaterialShapes.DENSEWIRE, MaterialShapes.BOLT, MaterialShapes.DUST,
            MaterialShapes.DUSTTINY, MaterialShapes.BILLET, MaterialShapes.LIGHTBARREL,
            MaterialShapes.HEAVYBARREL, MaterialShapes.LIGHTRECEIVER, MaterialShapes.HEAVYRECEIVER,
            MaterialShapes.MECHANISM, MaterialShapes.STOCK, MaterialShapes.GRIP
    ));

    private static final Set<MaterialShapes> WROUGHT_OWN = new HashSet<>(Arrays.asList(
            MaterialShapes.BLOCK, MaterialShapes.INGOT, MaterialShapes.NUGGET
    ));

    @Inject(method = "getOutput", at = @At("HEAD"), cancellable = true, remap = false)
    private void filterMoldOutput(NTMMaterial mat, CallbackInfoReturnable<ItemStack> cir) {
        if (mat == Mats.MAT_IRON) {
            if (!IRON_ALLOWED.contains(shape)) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        } else if (mat == Mats.MAT_WROUGHTIRON) {
            if (WROUGHT_AS_IRON.contains(shape)) {
                // Используем железную форму, но сохраняем множитель amount
                ItemStack result = getIronOutputWithAmount();
                if (!result.isEmpty()) {
                    result.setCount(this.amount);  // ← Устанавливаем правильное количество!
                }
                cir.setReturnValue(result);
            } else if (!WROUGHT_OWN.contains(shape)) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        }
    }

    private ItemStack getIronOutputWithAmount() {
        // Ищем железный предмет по OreDict
        for (String name : Mats.MAT_IRON.names) {
            String od = shape.name() + name;
            List<ItemStack> ores = OreDictionary.getOres(od);
            if (!ores.isEmpty()) {
                for (ItemStack ore : ores) {
                    if (net.minecraft.item.Item.REGISTRY.getNameForObject(ore.getItem()).toString().startsWith("hbm")) {
                        ItemStack copy = ore.copy();
                        copy.setCount(1);  // Количество будет установлено в filterMoldOutput
                        return copy;
                    }
                }
                ItemStack copy = ores.get(0).copy();
                copy.setCount(1);
                return copy;
            }
        }
        return ItemStack.EMPTY;
    }
}