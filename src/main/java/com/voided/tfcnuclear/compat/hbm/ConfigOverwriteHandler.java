package com.voided.tfcnuclear.compat.hbm;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigOverwriteHandler {
    private static boolean hasRun = false;
    private static final String[] TARGET_KEYS = {
            "2.L00_enableHematite",
            "2.L01_enableMalachite",
            "2.L02_enableBauxite"
    };

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (hasRun) return;
        if (!Loader.isModLoaded("tfc")) return;

        hasRun = true;

        try {
            Path configPath = Paths.get("config/hbm/hbm.cfg");
            if (!Files.exists(configPath)) return;

            List<String> lines = Files.readAllLines(configPath, StandardCharsets.UTF_8);
            boolean modified = false;
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                String newLine = line;
                for (String key : TARGET_KEYS) {
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
}