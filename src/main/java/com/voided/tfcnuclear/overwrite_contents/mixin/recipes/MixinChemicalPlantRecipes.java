package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.GeneralConfig;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.ChemicalPlantRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;

@Mixin(value = ChemicalPlantRecipes.class, remap = false)
public abstract class MixinChemicalPlantRecipes extends GenericRecipes<GenericRecipe> {


    @Inject(method = "registerDefaults", at = @At("RETURN"))
    private void onRegisterDefaults(CallbackInfo ci) {

        ChemicalPlantRecipes.INSTANCE.removeRecipeByName("chem.aggregate");
        ChemicalPlantRecipes.INSTANCE.removeRecipeByName("chem.concrete");
        ChemicalPlantRecipes.INSTANCE.removeRecipeByName("chem.concreteasbestos");
        ChemicalPlantRecipes.INSTANCE.removeRecipeByName("chem.ducrete");
        ChemicalPlantRecipes.INSTANCE.removeRecipeByName("chem.liquidconk");
        ChemicalPlantRecipes.INSTANCE.removeRecipeByName("chem.asphalt");



        this.register(new GenericRecipe("chem.aggregate_new").setupNamed(320, 500).setPools(GenericRecipes.POOL_PREFIX_DISCOVER + ".stone")
                .inputItems(new RecipesCommon.OreDictStack("cobblestone", 16))
                .outputItems(new ItemStack(Blocks.GRAVEL, 8), new ItemStack(Blocks.SAND, 8)));
        this.register(new GenericRecipe("chem.concrete_new").setup(100, 100)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.powder_cement, 1), new RecipesCommon.OreDictStack("gravel", 8), new RecipesCommon.OreDictStack("sand", 8))
                .inputFluids(new FluidStack(Fluids.WATER, 2_000))
                .outputItems(new ItemStack(ModBlocks.concrete_smooth, 16)));
        this.register(new GenericRecipe("chem.concreteasbestos_new").setup(100, 100)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.powder_cement, 4), new RecipesCommon.OreDictStack(ASBESTOS.ingot(), (GeneralConfig.enableLBSM && GeneralConfig.enableLBSMSimpleChemsitry) ? 1 : 4), new RecipesCommon.OreDictStack("sand", 8))
                .inputFluids(new FluidStack(Fluids.WATER, 2_000))
                .outputItems(new ItemStack(ModBlocks.concrete_asbestos, 16)));
        this.register(new GenericRecipe("chem.ducrete_new").setup(150, 100)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.powder_cement, 4), new RecipesCommon.OreDictStack(FERRO.ingot()), new RecipesCommon.OreDictStack("sand", 8))
                .inputFluids(new FluidStack(Fluids.WATER, 2_000))
                .outputItems(new ItemStack(ModBlocks.ducrete_smooth, 8)));
        this.register(new GenericRecipe("chem.liquidconk_new").setup(100, 100)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.powder_cement, 1), new RecipesCommon.OreDictStack("gravel", 8), new RecipesCommon.OreDictStack("sand", 8))
                .inputFluids(new FluidStack(Fluids.WATER, 2_000))
                .outputFluids(new FluidStack(Fluids.CONCRETE, 16_000)));
        this.register(new GenericRecipe("chem.asphalt_new").setup(100, 100)
                .inputItems(new RecipesCommon.OreDictStack("gravel", 2), new RecipesCommon.OreDictStack("sand", 6))
                .inputFluids(new FluidStack(Fluids.BITUMEN, 1_000))
                .outputItems(new ItemStack(ModBlocks.asphalt, 16)));


    }
}