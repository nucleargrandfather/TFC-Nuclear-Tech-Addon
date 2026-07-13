package com.voided.tfcnuclear.world.OreSpawn;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TFCConfigOverwriteHandler {
    private static boolean hasRun = false;
    private static final String TARGET_KEY = "forceDefaultOreGenFile";

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (hasRun) return;
        if (!Loader.isModLoaded("tfc")) return;

        hasRun = true;

        try {
            Path configPath = Paths.get("config/TerraFirmaCraft - General.cfg");
            if (!Files.exists(configPath)) {
                return;
            }

            List<String> lines = Files.readAllLines(configPath, StandardCharsets.UTF_8);
            boolean modified = false;
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                String newLine = line;

                if (line.contains(TARGET_KEY) && line.contains("=true")) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}