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
import com.voided.tfcnuclear.compat.hbm.SlagStack;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.inventory.fluid.Fluids.fromName;
import static net.dries007.tfc.objects.fluids.FluidsTFC.LIMEWATER;

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
        this.register(new GenericRecipe("chem.limewater").setup(200, 100)
                .inputItems(new RecipesCommon.OreDictStack("dustFlux", 1))
                .inputFluids(new FluidStack(Fluids.WATER, 500))
                .outputFluids(new FluidStack(Fluids.fromName("LIMEWATER"), 500)));
        this.register(new GenericRecipe("chem.mortar").setup(120, 100)
                .inputItems(new RecipesCommon.OreDictStack("sand", 1))
                .inputFluids(new FluidStack(Fluids.fromName("LIMEWATER"), 100))
                .outputItems(new ItemStack(Item.getByNameOrId("tfc:mortar"), 16)));
        this.register(new GenericRecipe("chem.AACS").setup(120, 100)
                .inputItems(new RecipesCommon.ComparableStack(Items.CLAY_BALL, 1),
                            new RecipesCommon.ComparableStack(Item.getByNameOrId("tfc:powder/kaolinite")))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 100))
                .outputFluids(new FluidStack(Fluids.fromName("AACS"), 200)));
        this.register(new GenericRecipe("chem.IS_1").setup(120, 100)
                .inputItems(new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.LIMONITE_SLAG, 1), 100))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 100))
                .outputFluids(new FluidStack(Fluids.fromName("IS"), 300)));
        this.register(new GenericRecipe("chem.IS_2").setup(120, 100)
                .inputItems(new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.HEMATITE_SLAG, 1), 100))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 100))
                .outputFluids(new FluidStack(Fluids.fromName("IS"), 300)));
        this.register(new GenericRecipe("chem.IS_3").setup(120, 100)
                .inputItems(new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.MAGNETITE_SLAG, 1), 100))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 100))
                .outputFluids(new FluidStack(Fluids.fromName("IS"), 300)));
        this.register(new GenericRecipe("chem.GS").setup(120, 100)
                .inputItems(new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.GOLD_SLAG, 1), 100))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 100))
                .outputFluids(new FluidStack(Fluids.fromName("GS"), 300)));
        this.register(new GenericRecipe("chem.CS").setup(120, 100)
                .inputItems(new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.COPPER_SLAG, 1), 100))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 100))
                .outputFluids(new FluidStack(Fluids.fromName("CS"), 300)));
        this.register(new GenericRecipe("chem.LS").setup(120, 100)
                .inputItems(new SlagStack(new ItemStack(com.voided.tfcnuclear.inventory.items.ModItems.GALENA_SLAG, 1), 100))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 100))
                .outputFluids(new FluidStack(Fluids.fromName("LS"), 300)));
    }
}