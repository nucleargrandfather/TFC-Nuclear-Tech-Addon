package com.voided.tfcnuclear.overwrite_contents.mixin.needEdit;

import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.NTMMaterial;
import com.hbm.items.machine.ItemMold;
import com.voided.tfcnuclear.inventory.material.TFCNuclearMats;
import net.minecraft.item.ItemStack;
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
public abstract class CrucibleCastingTweaker2 {

    @Shadow
    MaterialShapes shape;

    @Shadow
    int amount;  // ← ДОБАВИТЬ!

    private static final Set<MaterialShapes> COPPER_ALLOWED = new HashSet<>(Arrays.asList(
            MaterialShapes.BLOCK, MaterialShapes.INGOT, MaterialShapes.NUGGET
    ));

    private static final Set<MaterialShapes> BRONZE_AS_COPPER = new HashSet<>(Arrays.asList(
            MaterialShapes.PLATE, MaterialShapes.CASTPLATE, MaterialShapes.WELDEDPLATE,
            MaterialShapes.SHELL, MaterialShapes.PIPE,
            MaterialShapes.BOLT, MaterialShapes.DUST,
            MaterialShapes.DUSTTINY, MaterialShapes.BILLET, MaterialShapes.LIGHTBARREL,
            MaterialShapes.HEAVYBARREL, MaterialShapes.LIGHTRECEIVER, MaterialShapes.HEAVYRECEIVER,
            MaterialShapes.MECHANISM, MaterialShapes.STOCK, MaterialShapes.GRIP
    ));

    private static final Set<MaterialShapes> BRONZE_OWN = new HashSet<>(Arrays.asList(
            MaterialShapes.BLOCK, MaterialShapes.INGOT, MaterialShapes.NUGGET
    ));

    @Inject(method = "getOutput", at = @At("HEAD"), cancellable = true, remap = false)
    private void filterMoldOutput(NTMMaterial mat, CallbackInfoReturnable<ItemStack> cir) {
        if (mat == Mats.MAT_COPPER) {
            if (!COPPER_ALLOWED.contains(shape)) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        } else if (mat == TFCNuclearMats.MAT_BRONZE) {
            if (BRONZE_AS_COPPER.contains(shape)) {
                ItemStack result = getBronzeOutput();
                if (!result.isEmpty()) {
                    result.setCount(this.amount);  // ← Устанавливаем правильное количество!
                }
                cir.setReturnValue(result);
            } else if (!BRONZE_OWN.contains(shape)) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        }
    }

    private ItemStack getBronzeOutput() {
        ItemStack copperResult = findOutputFor(Mats.MAT_COPPER);
        if (!copperResult.isEmpty()) {
            ItemStack bronzeResult = copperResult.copy();

            if (!bronzeResult.hasTagCompound()) {
                bronzeResult.setTagCompound(new net.minecraft.nbt.NBTTagCompound());
            }

            bronzeResult.getTagCompound().setBoolean("isBronze", true);
            bronzeResult.getTagCompound().setString("material", "bronze");

            bronzeResult.setCount(1);  // Базовое количество, будет переопределено в filterMoldOutput
            return bronzeResult;
        }
        return ItemStack.EMPTY;
    }

    private ItemStack findOutputFor(NTMMaterial mat) {
        for (String name : mat.names) {
            String od = shape.name() + name;
            List<ItemStack> ores = net.minecraftforge.oredict.OreDictionary.getOres(od);
            if (!ores.isEmpty()) {
                for (ItemStack ore : ores) {
                    if (net.minecraft.item.Item.REGISTRY.getNameForObject(ore.getItem()).toString().startsWith("hbm")) {
                        ItemStack copy = ore.copy();
                        copy.setCount(1);
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