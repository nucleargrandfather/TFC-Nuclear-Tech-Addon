package com.voided.tfcnuclear.inventory.fluids;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hbm.main.MainRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TFCNuclearFluids {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void writeFluidToConfig() {
        File configDir = MainRegistry.configHbmDir;
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        File fluidConfig = new File(configDir, "hbmFluidTypes.json");
        JsonObject root;

        try {
            if (fluidConfig.exists()) {
                try (FileReader reader = new FileReader(fluidConfig)) {
                    root = GSON.fromJson(reader, JsonObject.class);
                }
            } else {
                root = new JsonObject();
            }

            if (root.has("LIMEWATER") && root.has("AACS") && root.has("IS") &&
                    root.has("GS") && root.has("LS") && root.has("CS")) {
                return;
            }

            JsonObject fluid_1 = new JsonObject();
            fluid_1.addProperty("name", "Limewater");
            fluid_1.addProperty("id", 1001);
            fluid_1.addProperty("color", 16775910);
            fluid_1.addProperty("tint", 16775910);
            fluid_1.addProperty("p", 0);
            fluid_1.addProperty("f", 0);
            fluid_1.addProperty("r", 0);
            fluid_1.addProperty("symbol", "NONE");
            fluid_1.addProperty("texture", "limewater");
            fluid_1.addProperty("temperature", 20);

            root.add("LIMEWATER", fluid_1);

            JsonObject fluid_2 = new JsonObject();
            fluid_2.addProperty("name", "Acid-activated Clay Slurry");
            fluid_2.addProperty("id", 1002);
            fluid_2.addProperty("color", 15238218);
            fluid_2.addProperty("tint", 15238218);
            fluid_2.addProperty("p", 0);
            fluid_2.addProperty("f", 0);
            fluid_2.addProperty("r", 0);
            fluid_2.addProperty("symbol", "NONE");
            fluid_2.addProperty("texture", "AACS");
            fluid_2.addProperty("temperature", 100);

            root.add("AACS", fluid_2);

            JsonObject fluid_3 = new JsonObject();
            fluid_3.addProperty("name", "Iron Sulfate");
            fluid_3.addProperty("id", 1003);
            fluid_3.addProperty("color", 12632256);
            fluid_3.addProperty("tint", 12632256);
            fluid_3.addProperty("p", 0);
            fluid_3.addProperty("f", 0);
            fluid_3.addProperty("r", 0);
            fluid_3.addProperty("symbol", "NONE");
            fluid_3.addProperty("texture", "IS");
            fluid_3.addProperty("temperature", 64);

            root.add("IS", fluid_3);

            JsonObject fluid_4 = new JsonObject();
            fluid_4.addProperty("name", "Gold Sulfate");
            fluid_4.addProperty("id", 1004);
            fluid_4.addProperty("color",  16766720);
            fluid_4.addProperty("tint",  16766720);
            fluid_4.addProperty("p", 0);
            fluid_4.addProperty("f", 0);
            fluid_4.addProperty("r", 0);
            fluid_4.addProperty("symbol", "NONE");
            fluid_4.addProperty("texture", "GS");
            fluid_4.addProperty("temperature", 45);

            root.add("GS", fluid_4);

            JsonObject fluid_5 = new JsonObject();
            fluid_5.addProperty("name", "Lead Sulfate");
            fluid_5.addProperty("id", 1005);
            fluid_5.addProperty("color", 2825037);
            fluid_5.addProperty("tint", 2825037);
            fluid_5.addProperty("p", 0);
            fluid_5.addProperty("f", 0);
            fluid_5.addProperty("r", 0);
            fluid_5.addProperty("symbol", "NONE");
            fluid_5.addProperty("texture", "LS");
            fluid_5.addProperty("temperature", 320);

            root.add("LS", fluid_5);

            JsonObject fluid_6 = new JsonObject();
            fluid_6.addProperty("name", "Copper Sulfate");
            fluid_6.addProperty("id", 1006);
            fluid_6.addProperty("color", 13467442);
            fluid_6.addProperty("tint", 13467442);
            fluid_6.addProperty("p", 0);
            fluid_6.addProperty("f", 0);
            fluid_6.addProperty("r", 0);
            fluid_6.addProperty("symbol", "NONE");
            fluid_6.addProperty("texture", "CS");
            fluid_6.addProperty("temperature", 110);

            root.add("CS", fluid_6);

            try (FileWriter writer = new FileWriter(fluidConfig)) {
                GSON.toJson(root, writer);
            }

        } catch (Exception ignored) {}
    }
}