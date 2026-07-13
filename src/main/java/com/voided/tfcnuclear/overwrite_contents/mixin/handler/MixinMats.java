package com.voided.tfcnuclear.overwrite_contents.mixin.handler;

import com.hbm.inventory.material.Mats;
import net.dries007.tfc.api.capability.forge.CapabilityForgeable;
import net.dries007.tfc.api.capability.forge.IForgeable;
import net.dries007.tfc.api.capability.forge.IForgeableMeasurableMetal;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.items.ItemBloom;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = Mats.class, remap = false)
public class MixinMats {

    /**
     * Настройка цвета расплавленного железа при инициализации класса
     */
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        Mats.MAT_IRON.setMoltenColor(0x604C44);
    }

    /**
     * Перехватываем getMaterialsFromItem для динамической обработки крицы
     */
    @Inject(
            method = "getMaterialsFromItem",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private static void onGetMaterialsFromItem(ItemStack stack, CallbackInfoReturnable<List<Mats.MaterialStack>> cir) {
        if (stack == null || stack.isEmpty()) return;
        if (!(stack.getItem() instanceof ItemBloom)) return;

        IForgeable cap = stack.getCapability(CapabilityForgeable.FORGEABLE_CAPABILITY, null);
        if (!(cap instanceof IForgeableMeasurableMetal)) return;

        IForgeableMeasurableMetal metalCap = (IForgeableMeasurableMetal) cap;

        if (metalCap.getMetal() != Metal.WROUGHT_IRON) return;

        int tfcAmount = metalCap.getMetalAmount();
        if (tfcAmount <= 0) return;

        int hbmQuanta = (tfcAmount * 72) / 100;
        if (hbmQuanta <= 0) return;

        List<Mats.MaterialStack> result = new ArrayList<>();
        result.add(new Mats.MaterialStack(Mats.MAT_WROUGHTIRON, hbmQuanta));
        cir.setReturnValue(result);
    }
}