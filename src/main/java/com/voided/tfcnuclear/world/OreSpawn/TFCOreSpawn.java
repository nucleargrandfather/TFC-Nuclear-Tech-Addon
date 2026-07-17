package com.voided.tfcnuclear.world.OreSpawn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TFCOreSpawn {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "tfc_ore_spawn_data.json";

    public static void generate(FMLPreInitializationEvent event) {
        File configDir = event.getModConfigurationDirectory();
        File targetFile = new File(configDir, "tfc/" + FILE_NAME);
        targetFile.getParentFile().mkdirs();

        Map<String, Object> oreData = new LinkedHashMap<>();

        addOre(oreData, "hematite_1", "tfc:hematite", "tfc:ore/small/hematite", 16, 8, "cluster", 120, 110, 140, 40, "tfc:claystone", "tfc:shale", "tfc:conglomerate", "tfc:chalk");
        addOre(oreData, "hematite_2", "tfc:hematite", "tfc:ore/hematite", 20, 10, "cluster", 170, 45, 80, 60, "tfc:igneous_intrusive", "tfc:metamorphic");
        addOre(oreData, "limonite", "tfc:limonite", "tfc:ore/small/limonite", 50, 6, "sphere", 70, 110, 130, 25, "tfc:sedimentary");
        addOre(oreData, "magnetite_1", "tfc:magnetite", "tfc:ore/magnetite", 14, 22, "cluster", 200, 10, 55, 30, "tfc:igneous_intrusive");
        addOre(oreData, "magnetite_2", "tfc:magnetite", "tfc:ore/magnetite", 26, 10, "cluster", 300, 60, 90, 85, "tfc:sedimentary", "tfc:igneous_intrusive");

        addOre(oreData, "native_copper_1", "tfc:native_copper", "tfc:ore/native_copper", 18, 12, "cluster", 120, 60, 90, 70, "tfc:basalt", "tfc:andesite");
        addOre(oreData, "native_copper_2", "tfc:native_copper", "tfc:ore/small/native_copper", 70, 6, "sphere", 70, 110, 140, 30, "tfc:claystone", "tfc:conglomerate", "tfc:shale", "tfc:phillite");
        addOre(oreData, "malachite", "tfc:malachite", "tfc:ore/malachite", 20, 12, "cluster", 140, 70, 110, 20, "tfc:limestone", "tfc:dolomite", "tfc:chalk", "tfc:marble");

        addOre(oreData, "cassiterite_cluster1", "tfc:cassiterite", "tfc:ore/small/cassiterite", 8, 20, "cluster", 250, 5, 45, 70, "tfc:granite");
        addOre(oreData, "cassiterite_cluster2", "tfc:cassiterite", "tfc:ore/cassiterite", 18, 8, "cluster", 105, 110, 135, 40, "tfc:sedimentary");

        addOre(oreData, "galena_cluster1", "tfc:galena", "tfc:ore/galena", 18, 12, "cluster", 200, 25, 55, 65, "tfc:slate", "tfc:phyllite", "tfc:schist", "tfc:gneiss", "tfc:quartzite");
        addOre(oreData, "galena_cluster2", "tfc:galena", "tfc:ore/small/galena", 50, 5, "sphere", 105, 70, 100, 30, "tfc:limestone", "tfc:dolomite", "tfc:chalk");

        addOre(oreData, "gold_hydrothermal", "tfc:native_gold", "tfc:ore/native_gold", 16, 8, "cluster", 300, 15, 60, 60, "tfc:metamorphic");
        addOre(oreData, "gold_placer", "tfc:native_gold", "tfc:ore/small/native_gold", 50, 5, "sphere", 165, 110, 135, 15, "tfc:sedimentary");

        addOre(oreData, "sphalerite_hydrothermal", "tfc:sphalerite", "tfc:ore/sphalerite", 14,8, "cluster", 130, 20,50, 60, "tfc:metamorphic");
        addOre(oreData, "sphalerite_mvt", "tfc:sphalerite", "tfc:ore/small/sphalerite", 50, 5, "sphere", 220, 70, 100, 30, "tfc:limestone", "tfc:dolomite", "tfc:chalk");
        addOre(oreData, "sphalerite_slate", "tfc:sphalerite", "tfc:ore/small/sphalerite", 30, 6, "sphere",145,110, 140, 20, "tfc:shale", "tfc:claystone");

        addOre(oreData, "garnierite_main", "tfc:garnierite", "tfc:ore/garnierite", 18, 8, "cluster", 150, 60, 90, 50, "tfc:igneous_intrusive");
        addOre(oreData, "garnierite_hydro", "tfc:garnierite", "tfc:ore/garnierite", 8, 16, "cluster", 330, 20, 50, 80, "tfc:metamorphic" );
        addOre(oreData, "garnierite_sedimentary", "tfc:garnierite", "tfc:ore/small/garnierite", 30, 4, "sphere", 280, 110, 130, 15,"tfc:sedimentary");

        addOre(oreData, "platinum_igneous", "tfc:native_platinum", "tfc:ore/native_platinum", 16, 10, "cluster", 320, 5, 40, 60, "tfc:igneous_intrusive");
        addOre(oreData, "platinum_sedimentary", "tfc:native_platinum", "tfc:ore/small/native_platinum", 50, 4, "sphere", 200, 110, 135, 10, "tfc:claystone", "tfc:conglomerate", "tfc:schist", "tfc:slate", "tfc:phyllite");

        addOre(oreData, "molybdenite_intrusive", "tfc:bismuthinite", "tfc:ore/small/bismuthinite", 8, 16, "sphere", 110, 20, 40, 30, "tfc:granite", "tfc:diorite");
        addOre(oreData, "molybdenite_quartz", "tfc:bismuthinite", "tfc:ore/bismuthinite", 10,18, "cluster", 265, 10, 50, 80,"tfc:granite", "tfc:gneiss", "tfc:quartzite");
        addOre(oreData, "molybdenite_extrusive", "tfc:bismuthinite", "tfc:ore/small/bismuthinite", 16, 8, "cluster", 180, 60, 80, 20, "tfc:rhyolite", "tfc:dacite");

        addOre(oreData, "silver_hydrothermal", "tfc:native_silver", "tfc:ore/native_silver", 16, 10, "cluster", 300, 30, 70, 80, "tfc:metamorphic");
        addOre(oreData, "silver_sedimentary", "tfc:native_silver", "tfc:ore/small/native_silver", 50, 4, "sphere", 175, 120, 140, 15,"tfc:sedimentary");
        addOre(oreData, "silver_intrusive", "tfc:native_silver", "tfc:ore/native_silver", 13, 8, "cluster", 255, 5, 30, 40, "tfc:gabbro", "tfc:diorite");

        addOre(oreData, "tetrahedrite_hydro", "tfc:tetrahedrite", "tfc:ore/tetrahedrite", 16, 12, "cluster", 235, 20, 50, 60, "tfc:slate", "tfc:phyllite", "tfc:schist", "tfc:gneiss", "tfc:quartzite");
        addOre(oreData, "tetrahedrite_sedimentary", "tfc:tetrahedrite", "tfc:ore/small/tetrahedrite", 50, 5, "sphere", 115, 80, 100, 30, "tfc:shale", "tfc:claystone", "tfc:limestone", "tfc:dolomite");

        addOre(oreData, "malachite_surface", "tfc:malachite", "tfc:ore/small/malachite", 4, 10, "sphere", 10, 170, 190, 40, "tfc:limestone", "tfc:dolomite", "tfc:marble", "tfc:schist", "tfc:phyllite");
        addOre(oreData, "silver_surface", "tfc:native_silver", "tfc:ore/small/native_silver", 8, 5, "sphere", 10, 170, 200, 50, "tfc:limestone", "tfc:dolomite", "tfc:marble", "tfc:chalk");
        addOre(oreData, "gold_surface", "tfc:native_gold", "tfc:ore/small/native_gold", 4, 10, "sphere", 10, 170, 190, 50, "tfc:slate", "tfc:phyllite", "tfc:schist", "tfc:gneiss", "tfc:quartzite");
        addOre(oreData, "redstone_surface", "tfc:cinnabar", "minecraft:redstone", 8, 6, "sphere", 10, 170, 200, 30, "tfc:slate", "tfc:phyllite", "tfc:schist", "tfc:quartzite");
        addOre(oreData, "lapis_surface", "tfc:lapis_lazuli", "tfc:ore/lapis_lazuli", 8, 8, "sphere", 10, 170, 190, 30, "tfc:sedimentary");
        addOre(oreData, "kimberlite_surface", "tfc:kimberlite", "tfc:ore/kimberlite", 8, 6, "sphere", 10, 170, 180, 30, "tfc:gneiss", "tfc:gabbro");
        addOre(oreData, "saltpeter_surface", "tfc:saltpeter", "hbm:niter", 7, 9, "sphere", 10, 170, 180, 45, "tfc:claystone");
        addOre(oreData, "borax_surface", "tfc:borax", "tfc:ore/borax", 6, 8, "sphere", 10, 170, 180, 30, "tfc:igneous_extrusive");

        addOre(oreData, "redstone_quartz", "tfc:cinnabar", "minecraft:redstone", 18, 12, "cluster", 205, 80, 100, 30, "tfc:quartzite", "tfc:slate", "tfc:phyllite", "tfc:schist");
        addOre(oreData, "redstone_rich", "tfc:cinnabar", "minecraft:redstone", 12, 20, "cluster", 320, 20, 50, 80, "tfc:igneous_intrusive");
        addOre(oreData, "redstone_extrusive", "tfc:cinnabar", "minecraft:redstone", 14, 10, "cluster", 120, 60, 80, 50, "tfc:igneous_extrusive");

        addOre(oreData, "lapis_rich", "tfc:lapis_lazuli", "tfc:ore/lapis_lazuli", 8, 16, "cluster", 85, 60, 100, 70, "tfc:marble");
        addOre(oreData, "lapis_poor", "tfc:lapis_lazuli", "tfc:ore/lapis_lazuli", 30, 10, "sphere", 200, 10, 40, 40,"tfc:igneous_intrusive");

        addOre(oreData, "bit_coal_1", "tfc:bituminous_coal", "hbm:powder_coal_tiny", 50, 4, "sphere", 145, 80, 100, 80, "tfc:limestone", "tfc:dolomite");
        addOre(oreData, "bit_coal_2", "tfc:bituminous_coal", "hbm:powder_coal_tiny", 25, 10, "cluster", 80, 60, 75, 90, "tfc:slate", "tfc:shale", "tfc:claystone");

        addOre(oreData, "lignite_main", "tfc:lignite", "tfc:ore/petrified_wood", 60, 4, "sphere", 70, 110, 135, 50, "tfc:shale");
        addOre(oreData, "lignite_rich", "tfc:lignite", "tfc:ore/petrified_wood", 25, 5, "cluster", 60, 80, 100, 70, "tfc:conglomerate", "tfc:claystone");

        addOre(oreData, "graphite_1", "tfc:graphite", "tfc:ore/graphite", 16, 10, "sphere", 120, 60, 80, 40, "tfc:gneiss", "tfc:marble");
        addOre(oreData, "graphite_2", "tfc:graphite", "tfc:ore/graphite", 24, 14, "sphere", 70, 10, 40, 30, "tfc:igneous_intrusive");
        addOre(oreData, "graphite_3", "tfc:graphite", "tfc:ore/graphite", 35, 6, "sphere", 175, 40, 80, 20,"tfc:slate", "tfc:phyllite", "tfc:schist", "tfc:quartzite");

        addOre(oreData, "kaolinite_1", "tfc:kaolinite", "tfc:ore/kaolinite", 25, 4, "sphere", 100, 60, 90, 70, "tfc:gneiss", "tfc:slate", "tfc:schist", "tfc:quartzite");
        addOre(oreData, "kaolinite_2", "tfc:kaolinite", "tfc:ore/kaolinite", 50, 5, "sphere", 70, 80, 110, 30, "tfc:shale", "tfc:claystone", "tfc:conglomerate");

        addOre(oreData, "kimberlite", "tfc:kimberlite", "tfc:ore/kimberlite", 8, 18, "cluster", 160, 5, 45, 40, "tfc:gabbro");

        addOre(oreData, "borax_main", "tfc:borax", "tfc:ore/borax", 35, 5, "sphere", 100, 110, 140, 40, "tfc:rocksalt");
        addOre(oreData, "borax_crystal", "tfc:borax", "tfc:ore/borax", 14, 6, "cluster", 200, 70, 100, 70, "tfc:limestone", "tfc:dolomite", "tfc:chalk", "tfc:marble");

        addOre(oreData, "sylvite", "tfc:sylvite", "tfc:ore/sylvite", 40, 5, "cluster", 200, 100, 110, 30, "tfc:rocksalt", "tfc:shale");

        addOre(oreData, "sulfur", "tfc:sulfur", "tfc:ore/sulfur", 14, 20, "cluster", 100, 60, 90, 80, "tfc:igneous_extrusive");

        addOre(oreData, "cryolite_main", "tfc:cryolite", "tfc:ore/cryolite", 16,14, "cluster", 100, 10, 50, 80, "tfc:granite", "tfc:gneiss");
        addOre(oreData, "cryolite_poor", "tfc:cryolite", "tfc:ore/cryolite", 40, 10, "sphere", 150, 60, 80, 40, "tfc:rhyolite", "tfc:andesite");

        addOre(oreData, "saltpeter_main", "tfc:saltpeter", "hbm:niter", 50, 6, "sphere", 120, 110,130, 40, "tfc:rocksalt", "tfc:claystone");
        addOre(oreData, "saltpeter_rich", "tfc:saltpeter", "tfc:ore/saltpeter", 16, 8, "cluster", 230, 70, 105, 90, "tfc:limestone", "tfc:dolomite", "tfc:chalk", "tfc:marble");

        try (FileWriter writer = new FileWriter(targetFile)) {
            GSON.toJson(oreData, writer);
        } catch (IOException e) {
        }
    }

    private static void addOre(Map<String, Object> oreData, String name, String oreBlock, String loose,
                               int width, int height, String shape, int rarity,
                               int minimum_height, int maximum_height, int density, String... base_rocks) {
        Map<String, Object> ore = new LinkedHashMap<>();

        ore.put("ore", oreBlock);
        ore.put("loose", loose);
        ore.put("width", width);
        ore.put("height", height);
        ore.put("shape", shape);
        ore.put("rarity", rarity);
        ore.put("minimum_height", minimum_height);
        ore.put("maximum_height", maximum_height);
        ore.put("density", density);
        ore.put("base_rocks", Arrays.asList(base_rocks));

        oreData.put(name, ore);
    }

    private static final String ORE_SPAWN_DATA = "ore_spawn_data.json";

    public static void clean(FMLPreInitializationEvent event) {
        File configDir = event.getModConfigurationDirectory();
        File tfcFolder = new File(configDir, "tfc");

        if (!tfcFolder.exists()) {
            tfcFolder.mkdirs();
        }

        File oldFile = new File(tfcFolder, ORE_SPAWN_DATA);
        if (oldFile.exists()) {
            oldFile.delete();
        }
    }
}