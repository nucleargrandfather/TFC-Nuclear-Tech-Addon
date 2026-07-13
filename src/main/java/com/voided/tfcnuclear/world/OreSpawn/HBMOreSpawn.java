package com.voided.tfcnuclear.world.OreSpawn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class HBMOreSpawn {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "ntm_ore_spawn_data.json";

    public static void generate(FMLPreInitializationEvent event) {
        File configDir = event.getModConfigurationDirectory();
        File targetFile = new File(configDir, "tfc/" + FILE_NAME);
        targetFile.getParentFile().mkdirs();

        Map<String, Object> oreData = new LinkedHashMap<>();

        addOre(oreData, "uranium_sedimentary", "hbm:ore_uranium", "hbm:nugget_uranium", 55, 5, "cluster", 200, 60, 80, 20, 0,"tfc:conglomerate", "tfc:claystone");
        addOre(oreData, "uranium_metamorphic", "hbm:ore_uranium", "hbm:nugget_uranium", 40, 12, "cluster", 370, 20, 40, 40, 0,"tfc:phyllite", "tfc:slate", "tfc:shale");

        addOre(oreData, "thorium_intrusive", "hbm:ore_thorium", "hbm:nugget_th232", 20, 15, "cluster", 200, 10, 30, 30,0, "tfc:igneous_intrusive");
        addOre(oreData, "thorium_metamorphic", "hbm:ore_thorium", "hbm:nugget_th232", 16, 12, "cluster", 370, 30, 50, 50, 0,"tfc:gneiss");

        addOre(oreData, "titanium_intrusive", "hbm:ore_titanium", "hbm:powder_titanium", 35, 25, "cluster", 150, 5, 30, 30, 0,"tfc:gabbro", "tfc:gneiss", "tfc:schist", "tfc:quartzite");
        addOre(oreData, "titanium_sedimentary", "hbm:ore_titanium", "hbm:powder_titanium", 50, 3, "cluster", 350, 80, 110, 80, 0,"tfc:dolomite", "tfc:conglomerate");

        addOre(oreData, "tungsten_metamorphic", "hbm:ore_tungsten", "hbm:powder_tungsten", 12, 20, "cluster", 135, 10, 30, 50,0, "tfc:gneiss", "tfc:quartzite", "tfc:schist", "tfc:marble");
        addOre(oreData, "tungsten_intrusive", "hbm:ore_tungsten", "hbm:powder_tungsten", 25, 10, "cluster", 360, 35, 50, 85, 0,"tfc:granite");

        addOre(oreData, "beryllium_1", "hbm:ore_beryllium", "hbm:nugget_beryllium", 14, 12, "cluster", 170, 5, 35, 30, 0,"tfc:granite", "tfc:gneiss");
        addOre(oreData, "beryllium_2", "hbm:ore_beryllium", "hbm:nugget_beryllium", 12, 10, "cluster", 290, 60, 80, 50, 0,"tfc:quartzite", "tfc:schist");

        addOre(oreData, "cobalt_metamorphic", "hbm:ore_cobalt", "hbm:powder_cobalt_tiny", 12, 20, "cluster", 320, 10, 40, 60, 0,"tfc:slate", "tfc:phyllite", "tfc:schist", "tfc:gneiss", "tfc:quartzite");
        addOre(oreData, "cobalt_intrusive", "hbm:ore_cobalt", "hbm:nugget_cobalt", 30, 20, "cluster", 140, 5, 30, 20, 0,"tfc:gabbro", "tfc:diorite");

        addOre(oreData, "cinnabar_extrusive", "hbm:ore_cinnabar", "hbm:nugget_mercury", 12, 10, "cluster", 140, 60, 80, 60, 0,"tfc:igneous_extrusive");
        addOre(oreData, "cinnabar_sedimentary", "hbm:ore_cinnabar", "hbm:nugget_mercury_tiny", 40, 4, "cluster", 60,70, 90, 25, 0,"tfc:limestone", "tfc:dolomite", "tfc:chalk");

        addOre(oreData, "fluorite_1", "hbm:ore_fluorite", "hbm:fluorite", 12, 18, "cluster", 170, 10, 50, 40, 0,"tfc:granite", "tfc:gneiss", "tfc:quartzite", "tfc:schist");
        addOre(oreData, "fluorite_2", "hbm:ore_fluorite", "hbm:fluorite", 40, 4, "cluster", 100, 110, 150, 15,  0,"tfc:conglomerate", "tfc:claystone", "tfc:shale");

        addOre(oreData, "asbestos_intrusive", "hbm:ore_asbestos", "hbm:powder_asbestos", 15, 8, "cluster", 200, 25, 50, 60, 0,"tfc:gabbro");
        addOre(oreData, "asbestos_metamorphic", "hbm:ore_asbestos", "hbm:powder_asbestos", 10, 20, "cluster", 150, 5, 45, 20, 0, "tfc:slate", "tfc:phyllite", "tfc:schist", "tfc:gneiss");
        addOre(oreData, "asbestos_surface", "hbm:ore_asbestos", "hbm:powder_asbestos", 12, 6, "sphere", 10,160, 190, 10, 0, "tfc:gabbro", "tfc:schist");

        addOre(oreData, "rare_earth_1", "hbm:ore_rare", "hbm:chunk_ore", 18, 20, "cluster", 230, 5, 40, 30, 0, "tfc:igneous_intrusive");
        addOre(oreData, "rare_earth_2", "hbm:ore_rare", "hbm:chunk_ore", 50, 3, "cluster", 170, 120, 150, 10, 0, "tfc:sedimentary");
        addOre(oreData, "rare_earth_surface", "hbm:ore_rare", "hbm:chunk_ore", 14, 10, "sphere", 10, 160, 200, 5, 0,"tfc:igneous_intrusive", "tfc:metamorphic");

        addOre(oreData, "coltan", "hbm:ore_coltan", null, 10, 10, "sphere", 3000, 5, 55, 10, 0, "tfc:sedimentary", "tfc:igneous_intrusive", "tfc:igneous_extrusive", "tfc:metamorphic");
        addOre(oreData, "austalium", "hbm:ore_australium", "hbm:nugget_australium", 15, 25, "sphere", 6000, 10, 100, 80,0, "tfc:sedimentary", "tfc:igneous_intrusive", "tfc:igneous_extrusive", "tfc:metamorphic");
        addOre(oreData, "alex", "hbm:ore_alexandrite", null, 5,5, "sphere", 3000, 10, 100, 5, 0, "tfc:sedimentary", "tfc:igneous_intrusive", "tfc:igneous_extrusive", "tfc:metamorphic");

        addOre(oreData, "bauxite", "hbm:stone_resource", "minecraft:iron_nugget", 45, 6, "cluster", 250, 70, 130, 50, 5, "tfc:granite", "tfc:claystone", "tfc:chalk", "tfc:marble", "tfc:dolomite", "tfc:limestone");
        addOre(oreData, "bauxite_surface", "hbm:stone_resource", "minecraft:iron_nugget", 30, 3, "sphere", 15, 160, 190, 20, 5, "tfc:granite", "tfc:gneiss");

        addOre(oreData, "oil_1", "hbm:ore_oil",null, 50, 6, "sphere", 90, 60, 70, 100, 0, "tfc:rocksalt", "tfc:claystone");
        addOre(oreData, "oil_2", "hbm:ore_oil", null, 30, 15, "sphere", 220, 70, 90, 100, 0, "tfc:dolomite", "tfc:marble");
        addOre(oreData, "oil_rich", "hbm:ore_oil", null, 100, 65, "sphere", 350, 60, 90, 100, 0, "tfc:limestone");

        addOre(oreData, "hbm_limestone", "hbm:stone_resource", "hbm:powder_limestone", 20, 2, "sphere", 100, 120, 150, 100, 4, "tfc:limestone");


        try (FileWriter writer = new FileWriter(targetFile)) {
            GSON.toJson(oreData, writer);
        } catch (IOException e) {
        }
    }

    private static void addOre(Map<String, Object> oreData, String name, String oreBlock, String loose,
                               int width, int height, String shape, int rarity,
                               int minimum_height, int maximum_height, int density, int meta, String... base_rocks) {
        Map<String, Object> ore = new LinkedHashMap<>();

        ore.put("ore", oreBlock);
        if (loose != null && !loose.isEmpty()) {
            ore.put("loose", loose);
        }
        ore.put("width", width);
        ore.put("height", height);
        ore.put("shape", shape);
        ore.put("rarity", rarity);
        ore.put("minimum_height", minimum_height);
        ore.put("maximum_height", maximum_height);
        ore.put("density", density);
        ore.put("meta", meta);
        ore.put("base_rocks", Arrays.asList(base_rocks));
        oreData.put(name, ore);
    }
}