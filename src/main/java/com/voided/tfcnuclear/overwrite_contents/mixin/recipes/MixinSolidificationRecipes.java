package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.SolidificationRecipes;
import com.hbm.util.Tuple;
import com.voided.tfcnuclear.inventory.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SolidificationRecipes.class)
public abstract class MixinSolidificationRecipes {

    @Shadow
    public static void registerRecipe(FluidType type, int quantity, ItemStack output) {}

    @Shadow
    public static void registerSFAuto(FluidType fluid) {}

    @Shadow
    public static void registerSFAuto(FluidType fluid, long tuPerSF, Item fuel) {}

    @Inject(method = "registerDefaults", at = @At("TAIL"), remap = false)
    public void onRegisterDefaults(CallbackInfo ci) {

        registerRecipe(Fluids.fromName("AACS"), 100, new ItemStack(ModItems.ACID_ACTIVATED_CLAY));

    }
}
