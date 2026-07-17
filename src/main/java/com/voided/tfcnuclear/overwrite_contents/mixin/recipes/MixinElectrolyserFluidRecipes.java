package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.ElectrolyserFluidRecipes;
import com.hbm.items.ModItems;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(ElectrolyserFluidRecipes.class)
public abstract class MixinElectrolyserFluidRecipes {

    @Shadow
    public static HashMap<FluidType, ElectrolyserFluidRecipes.ElectrolysisRecipe> recipes;


    @Inject(method = "registerDefaults", at = @At("RETURN"), remap = false)
    private void onRegisterDefaults(CallbackInfo ci) {
        addCustomRecipes();
    }

    private void addCustomRecipes() {
        recipes.put(Fluids.fromName("CS"), new ElectrolyserFluidRecipes.ElectrolysisRecipe(
                150,
                new FluidStack(Fluids.SULFURIC_ACID, 20),
                new FluidStack(Fluids.OXYGEN, 30),
                25,
                new ItemStack(ModItems.crystal_copper)
        ));
        recipes.put(Fluids.fromName("IS"), new ElectrolyserFluidRecipes.ElectrolysisRecipe(
                150,
                new FluidStack(Fluids.SULFURIC_ACID, 20),
                new FluidStack(Fluids.OXYGEN, 30),
                25,
                new ItemStack(ModItems.crystal_iron)
        ));
        recipes.put(Fluids.fromName("GS"), new ElectrolyserFluidRecipes.ElectrolysisRecipe(
                150,
                new FluidStack(Fluids.SULFURIC_ACID, 20),
                new FluidStack(Fluids.OXYGEN, 30),
                25,
                new ItemStack(ModItems.crystal_gold)
        ));
        recipes.put(Fluids.fromName("LS"), new ElectrolyserFluidRecipes.ElectrolysisRecipe(
                150,
                new FluidStack(Fluids.SULFURIC_ACID, 20),
                new FluidStack(Fluids.OXYGEN, 30),
                25,
                new ItemStack(ModItems.crystal_lead)
        ));
    }
}
