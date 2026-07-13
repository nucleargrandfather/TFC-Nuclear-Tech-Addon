package com.voided.tfcnuclear.inventory.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

@Mod.EventBusSubscriber(modid = "tfcnuclear")
public class VanillaOreDictCleaner {

    private static final Set<String> BLOCKED_MODS = new HashSet<>(Arrays.asList("minecraft"));
    private static final Set<String> BLOCKED_ORES = new HashSet<>(Arrays.asList(
            "ingotGold", "nuggetGold",
            "gemLapis", "gemDiamond"

    ));
    private static boolean cleaned = false;

    /**
     * Вызови этот метод в postInit твоего главного класса
     */
    public static void cleanOreDict() {
        if (cleaned) return;

        for (String oreName : BLOCKED_ORES) {
            if (!OreDictionary.doesOreNameExist(oreName)) {
                continue;
            }

            // Получаем список предметов в OreDict
            List<ItemStack> allOres = OreDictionary.getOres(oreName, true);

            // Создаем новый список без TFC предметов
            List<ItemStack> filtered = new ArrayList<>();
            int removedCount = 0;

            for (ItemStack stack : allOres) {
                if (stack.isEmpty()) continue;

                if (stack.getItem().getRegistryName() == null) {
                    filtered.add(stack);
                    continue;
                }

                String modId = stack.getItem().getRegistryName().getNamespace();
                if (BLOCKED_MODS.contains(modId)) {
                    removedCount++;
                } else {
                    filtered.add(stack);
                }
            }

            // Очищаем и заменяем список
            allOres.clear();
            allOres.addAll(filtered);

            System.out.println("[TFC Nuclear] OreDict '" + oreName + "' cleaned: removed " + removedCount + " items, " + filtered.size() + " remain");
        }

        cleaned = true;
        System.out.println("[TFC Nuclear] OreDict cleanup finished!");
    }

    /**
     * Дополнительная очистка при заходе игрока (на всякий случай)
     */
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!cleaned) {
            cleanOreDict();
        }
    }
}