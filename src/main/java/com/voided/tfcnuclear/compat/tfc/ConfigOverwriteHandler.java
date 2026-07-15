package com.voided.tfcnuclear.compat.tfc;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigOverwriteHandler {
    private static boolean hasRun = false;

    private static final String[] HBM_KEYS = {
            "2.L00_enableHematite",
            "2.L01_enableMalachite",
            "2.L02_enableBauxite"
    };

    private static final String TFC_KEY = "forceDefaultOreGenFile";

    private static final Map<String, Integer> STRUCTURE_CONFIGS = new HashMap<>();

    static {
        STRUCTURE_CONFIGS.put("S:03.01_radioSpawn", 3000);
        STRUCTURE_CONFIGS.put("S:03.02_antennaSpawn", 2300);
        STRUCTURE_CONFIGS.put("S:03.03_atomSpawn", 3000);
        STRUCTURE_CONFIGS.put("S:03.04_vertibirdSpawn", 2500);
        STRUCTURE_CONFIGS.put("S:03.05_dungeonSpawn", 500);
        STRUCTURE_CONFIGS.put("S:03.06_relaySpawn", 2500);
        STRUCTURE_CONFIGS.put("S:03.07_satelliteSpawn", 3000);
        STRUCTURE_CONFIGS.put("S:03.08_bunkerSpawn", 3000);
        STRUCTURE_CONFIGS.put("S:03.09_siloSpawn", 2500);
        STRUCTURE_CONFIGS.put("S:03.10_factorySpawn", 3000);
        STRUCTURE_CONFIGS.put("S:03.11_dudSpawn", 2500);
        STRUCTURE_CONFIGS.put("S:03.12_spaceshipSpawn", 3000);
        STRUCTURE_CONFIGS.put("S:03.13_barrelSpawn", 5000);
        STRUCTURE_CONFIGS.put("S:03.14_broadcasterSpawn", 5000);
        STRUCTURE_CONFIGS.put("S:03.15_landmineSpawn", 64);
        STRUCTURE_CONFIGS.put("S:03.17_radHotsoptSpawn", 5000);
        STRUCTURE_CONFIGS.put("S:03.18_vaultSpawn", 2500);
        STRUCTURE_CONFIGS.put("S:03.20_geyserChlorineSpawn", 3000);
        STRUCTURE_CONFIGS.put("S:03.21_geyserVaporSpawn", 500);
    }

    public static void  applyConfigOverwrites() {
        if (!Loader.isModLoaded("tfc")) return;

        try {
            modifyHbmConfig();
            modifyTFCConfig();
            modifyBedrockOresJson();
            modifyDimensionsConfig();
        } catch (Exception ignored) {}
    }

    private static void modifyHbmConfig() {
        try {
            Path configPath = Paths.get("config/hbm/hbm.cfg");
            if (!Files.exists(configPath)) return;

            List<String> lines = Files.readAllLines(configPath, StandardCharsets.UTF_8);
            boolean modified = false;
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                String newLine = line;
                for (String key : HBM_KEYS) {
                    if (line.contains(key) && line.contains("=true")) {
                        newLine = line.replace("=true", "=false");
                        if (!line.equals(newLine)) {
                            modified = true;
                        }
                        break;
                    }
                }
                newLines.add(newLine);
            }

            if (modified) {
                Files.write(configPath, newLines, StandardCharsets.UTF_8);
            }

        } catch (Exception ignored) {}
    }

    private static void modifyTFCConfig() {
        try {
            Path configPath = Paths.get("config/TerraFirmaCraft - General.cfg");
            if (!Files.exists(configPath)) return;

            List<String> lines = Files.readAllLines(configPath, StandardCharsets.UTF_8);
            boolean modified = false;
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                String newLine = line;

                if (line.contains(TFC_KEY) && line.contains("=true")) {
                    newLine = line.replace("=true", "=false");
                    if (!line.equals(newLine)) {
                        modified = true;
                    }
                }

                newLines.add(newLine);
            }

            if (modified) {
                Files.write(configPath, newLines, StandardCharsets.UTF_8);
            }

        } catch (Exception ignored) {}
    }

    private static void modifyBedrockOresJson() {
        try {
            Path configPath = Paths.get("config/hbm/hbm_bedrock_ores.json");
            if (!Files.exists(configPath)) return;

            String content = new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8);
            String newContent = content;

            if (content.contains("\"dimID\": 0,")) {
                int dimIndex = content.indexOf("\"dimID\": 0,");
                int oreRarityIndex = content.indexOf("\"oreRarity\"", dimIndex);
                if (oreRarityIndex != -1) {
                    int colonIndex = content.indexOf(":", oreRarityIndex);
                    int commaIndex = content.indexOf(",", colonIndex);
                    if (commaIndex != -1) {
                        String oldValue = content.substring(colonIndex + 1, commaIndex).trim();
                        if (oldValue.equals("15")) {
                            newContent = content.substring(0, colonIndex + 1) + " 60," + content.substring(commaIndex + 1);
                        }
                    }
                }
            }

            if (!newContent.equals(content)) {
                Files.write(configPath, newContent.getBytes(StandardCharsets.UTF_8));
            }

        } catch (Exception ignored) {}
    }

    private static void modifyDimensionsConfig() {
        try {
            Path configPath = Paths.get("config/hbm/hbm_dimensions.cfg");
            if (!Files.exists(configPath)) return;

            List<String> lines = Files.readAllLines(configPath, StandardCharsets.UTF_8);
            boolean modified = false;
            List<String> newLines = new ArrayList<>();

            boolean inStructuresSection = false;
            String currentKey = null;
            int bracketDepth = 0;
            boolean foundStructure = false;

            boolean inBedrockOilBlock = false;
            int bedrockOilBracketDepth = 0;

            for (String line : lines) {
                String trimmedLine = line.trim();
                String newLine = line;

                if (trimmedLine.startsWith("S:01.31_bedrockOilSpawnRate")) {
                    inBedrockOilBlock = true;
                    bedrockOilBracketDepth = 0;
                }

                if (inBedrockOilBlock) {
                    bedrockOilBracketDepth += line.chars().filter(ch -> ch == '<').count();
                    bedrockOilBracketDepth -= line.chars().filter(ch -> ch == '>').count();
                }

                if (inBedrockOilBlock && bedrockOilBracketDepth > 0) {
                    if (trimmedLine.matches("^0:200$")) {
                        newLine = line.replace("0:200", "0:5000");
                        modified = true;
                    }
                    if (trimmedLine.matches("^-6:200$")) {
                        newLine = line.replace("-6:200", "-6:5000");
                        modified = true;
                    }
                }

                if (inBedrockOilBlock && bedrockOilBracketDepth == 0 && trimmedLine.contains(">")) {
                    inBedrockOilBlock = false;
                }

                if (trimmedLine.equals("15_structures {")) {
                    inStructuresSection = true;
                    bracketDepth = 0;
                }

                if (inStructuresSection) {
                    bracketDepth += line.chars().filter(ch -> ch == '{').count();
                    bracketDepth -= line.chars().filter(ch -> ch == '}').count();
                }

                if (inStructuresSection && bracketDepth > 0) {
                    for (String key : STRUCTURE_CONFIGS.keySet()) {
                        if (trimmedLine.startsWith(key)) {
                            currentKey = key;
                            foundStructure = true;
                            break;
                        }
                    }
                }

                if (foundStructure && currentKey != null && bracketDepth > 0) {
                    if (trimmedLine.matches("^-?\\d+:\\d+$")) {
                        String[] parts = trimmedLine.split(":");
                        if (parts.length == 2) {
                            try {
                                int dimId = Integer.parseInt(parts[0]);
                                int currentValue = Integer.parseInt(parts[1]);
                                int newValue = STRUCTURE_CONFIGS.get(currentKey);

                                if (currentValue != newValue) {
                                    newLine = line.replace(trimmedLine, dimId + ":" + newValue);
                                    modified = true;
                                }
                            } catch (NumberFormatException ignored) {}
                        }
                        foundStructure = false;
                        currentKey = null;
                    }
                }

                newLines.add(newLine);

                if (inStructuresSection && bracketDepth == 0 && trimmedLine.equals("}")) {
                    inStructuresSection = false;
                    foundStructure = false;
                    currentKey = null;
                }
            }

            if (modified) {
                Files.write(configPath, newLines, StandardCharsets.UTF_8);
            }

        } catch (Exception ignored) {}
    }
}