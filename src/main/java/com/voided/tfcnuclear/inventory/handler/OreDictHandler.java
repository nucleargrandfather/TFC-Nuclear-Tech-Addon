package com.voided.tfcnuclear.inventory.handler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictHandler {

    private static final String[][] BINDINGS = {

            {"oreNormalHematite", "tfc:ore/hematite", "0"},
            {"orePoorHematite", "tfc:ore/hematite", "1"},
            {"oreRichHematite", "tfc:ore/hematite", "2"},
            {"oreSmallHematite", "tfc:ore/small/hematite", "0"},

            {"oreNormalMalachite", "tfc:ore/malachite", "0"},
            {"orePoorMalachite", "tfc:ore/malachite", "1"},
            {"oreRichMalachite", "tfc:ore/malachite", "2"},
            {"oreSmallMalachite", "tfc:ore/small/malachite", "0"},

            {"oreNormalMagnetite", "tfc:ore/magnetite", "0"},
            {"orePoorMagnetite", "tfc:ore/magnetite", "1"},
            {"oreRichMagnetite", "tfc:ore/magnetite", "2"},
            {"oreSmallMagnetite", "tfc:ore/small/magnetite", "0"},

            {"oreNormalLimonite", "tfc:ore/limonite", "0"},
            {"orePoorLimonite", "tfc:ore/limonite", "1"},
            {"oreRichLimonite", "tfc:ore/limonite", "2"},
            {"oreSmallLimonite", "tfc:ore/small/limonite", "0"},

            {"oreNormalNativeCopper", "tfc:ore/native_copper", "0"},
            {"orePoorNativeCopper", "tfc:ore/native_copper", "1"},
            {"oreRichNativeCopper", "tfc:ore/native_copper", "2"},
            {"oreSmallNativeCopper", "tfc:ore/small/native_copper", "0"},

            {"oreNormalCassiterite", "tfc:ore/cassiterite", "0"},
            {"orePoorCassiterite", "tfc:ore/cassiterite", "1"},
            {"oreRichCassiterite", "tfc:ore/cassiterite", "2"},
            {"oreSmallCassiterite", "tfc:ore/small/cassiterite", "0"},

            {"oreNormalGalena", "tfc:ore/galena", "0"},
            {"orePoorGalena", "tfc:ore/galena", "1"},
            {"oreRichGalena", "tfc:ore/galena", "2"},
            {"oreSmallGalena", "tfc:ore/small/galena", "0"},

            {"oreNormalTetrahedrite", "tfc:ore/tetrahedrite", "0"},
            {"orePoorTetrahedrite", "tfc:ore/tetrahedrite", "1"},
            {"oreRichTetrahedrite", "tfc:ore/tetrahedrite", "2"},
            {"oreSmallTetrahedrite", "tfc:ore/small/tetrahedrite", "0"},

            {"oreNormalGarnierite", "tfc:ore/garnierite", "0"},
            {"orePoorGarnierite", "tfc:ore/garnierite", "1"},
            {"oreRichGarnierite", "tfc:ore/garnierite", "2"},
            {"oreSmallGarnierite", "tfc:ore/small/garnierite", "0"},

            {"oreNormalSphalerite", "tfc:ore/sphalerite", "0"},
            {"orePoorSphalerite", "tfc:ore/sphalerite", "1"},
            {"oreRichSphalerite", "tfc:ore/sphalerite", "2"},
            {"oreSmallSphalerite", "tfc:ore/small/sphalerite", "0"},

            {"oreNormalMolybdenum", "tfc:ore/bismuth", "0"},
            {"orePoorMolybdenum", "tfc:ore/bismuth", "1"},
            {"oreRichMolybdenum", "tfc:ore/bismuth", "2"},
            {"oreSmallMolybdenum", "tfc:ore/small/bismuth", "0"},

            {"hbmIngotSteel", "hbm:ingot_steel"},

            {"ingotMolybdenum", "tfc:metal/ingot/bismuth"},
            {"ingotDoubleMolybdenum", "tfc:metal/double_ingot/bismuth"},
            {"nuggetMolybdenum", "tfc:metal/nugget/bismuth"},
            {"dustMolybdenum", "tfc:metal/dust/bismuth"},

            {"ingotElasticCopper", "tfc:metal/ingot/bismuth_bronze"},
            {"ingotDoubleElasticCopper", "tfc:metal/double_ingot/bismuth_bronze"},
            {"nuggetElasticCopper", "tfc:metal/nugget/bismuth_bronze"},
            {"dustElasticCopper", "tfc:metal/dust/bismuth_bronze"},

            {"tfcRedstone", "tfc:ore/cinnabar"},
            {"dustLapisLazuli", "hbm:powder_lapis"},
            {"tfcLapis", "tfc:ore/lapis_lazuli"},
            {"dustFlux", "hbm:powder_flux"},
            {"dustSaltpeter", "hbm:niter"},
            {"tfcSaltpeter", "tfc:ore/saltpeter"},
            {"tfcCryolite", "tfc:ore/cryolite"},
            {"tfcSulfur", "tfc:ore/sulfur"},
            {"tfcLignite", "tfc:ore/lignite"},
            {"tfcCoal", "tfc:ore/bituminous_coal"},
            {"tfcKimberlite", "tfc:ore/kimberlite"},
            {"minecart", "minecraft:minecart"}
    };

    public static void registerOreDict() {
        for (String[] binding : BINDINGS) {
            String oreDictName = binding[0];
            String itemId = binding[1];
            int meta = binding.length > 2 ? Integer.parseInt(binding[2]) : 0;

            ItemStack stack = new ItemStack(
                    ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)),
                    1,
                    meta
            );

            if (!stack.isEmpty()) {
                OreDictionary.registerOre(oreDictName, stack);
            }
        }
    }
}