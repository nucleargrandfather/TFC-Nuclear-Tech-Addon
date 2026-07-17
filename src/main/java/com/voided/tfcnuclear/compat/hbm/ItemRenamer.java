package com.voided.tfcnuclear.compat.hbm;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для динамического переименования предметов с поддержкой локализации
 * Автоматически генерирует ключи перевода на основе ID предмета и метаданных
 */
public class ItemRenamer {

    // Карта: "modid:item_name:meta" -> "translation.key"
    private static final Map<String, String> RENAME_MAP = new HashMap<>();
    // Карта: "modid:item_name" -> "translation.key" (для любых метаданных)
    private static final Map<String, String> RENAME_MAP_ANY_META = new HashMap<>();

    static {

        addRename("hbm:plate_copper", 0, "hbm_plate_bronze");
        addRename("hbm:shell", 2900, "hbm_shell_bronze");
        addRename("hbm:pipe", 2900, "hbm_pipe_bronze");
        addRename("hbm:plate_cast", 2900, "hbm_plate_cast_bronze");
        addRename("hbm:plate_welded", 2900, "hbm_plate_welded_bronze");

        addRename("hbm:plate_iron", 0, "hbm_plate_wrought_iron");
        addRename("hbm:pipe", 2600, "hbm_pipe_wrought_iron");
        addRename("hbm:plate_cast", 2600, "hbm_plate_cast_wrought_iron");
        addRename("hbm:plate_welded", 2600, "hbm_plate_welded_wrought_iron");
        addRename("hbm:stamp_iron_flat", 0, "hbm_stamp_wrought_iron_flat");
        addRename("hbm:stamp_iron_plate", 0, "hbm_stamp_wrought_iron_plate");
        addRename("hbm:stamp_iron_wire", 0, "hbm_stamp_wrought_iron_wire");
        addRename("hbm:stamp_iron_circuit", 0, "hbm_stamp_wrought_iron_circuit");
        addRename("hbm:stamp_iron_circuit", 0, "hbm_stamp_wrought_iron_circuit");

        addRename("minecraft:iron_ingot", 0, "mc_raw_iron_ingot");
        addRename("minecraft:iron_block", 0, "mc_raw_iron_block");
        addRename("minecraft:iron_nugget", 0, "mc_raw_iron_nugget");

    }

    private static void addRename(String itemId, int meta, String keyPart) {
        String fullKey = itemId + ":" + meta;
        RENAME_MAP.put(fullKey, "item." + keyPart);
    }

    /**
     * Добавляет переименование для всех метаданных предмета
     * @param itemId ID предмета (например, "hbm:ingot_copper")
     * @param keyPart Базовая часть ключа (будет дополнена префиксом "item.")
     */
    private static void addRenameAnyMeta(String itemId, String keyPart) {
        RENAME_MAP_ANY_META.put(itemId, "item." + keyPart);
    }

    /**
     * Получает ключ локализации для предмета
     * @param stack Предмет
     * @return Ключ локализации или null, если предмет не в списке
     */
    private static String getTranslationKey(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }

        String itemId = stack.getItem().getRegistryName().toString();
        int meta = stack.getMetadata();

        // 1. Сначала проверяем точное совпадение с метой
        String fullKey = itemId + ":" + meta;
        if (RENAME_MAP.containsKey(fullKey)) {
            return RENAME_MAP.get(fullKey);
        }

        // 2. Если нет точного совпадения, проверяем "любую мету"
        if (RENAME_MAP_ANY_META.containsKey(itemId)) {
            return RENAME_MAP_ANY_META.get(itemId);
        }

        return null;
    }

    /**
     * Получает локализованное название для предмета
     * @param stack Предмет
     * @return Локализованное название или null, если предмет не в списке
     */
    public static String getLocalizedName(ItemStack stack) {
        String translationKey = getTranslationKey(stack);
        if (translationKey != null) {
            // I18n.translateToLocal автоматически подставляет язык игрока
            return I18n.translateToLocal(translationKey);
        }
        return null;
    }

    /**
     * Получает локализованное название с параметрами
     * @param stack Предмет
     * @param params Параметры для форматирования строки
     * @return Локализованное название или null
     */
    public static String getLocalizedNameFormatted(ItemStack stack, Object... params) {
        String translationKey = getTranslationKey(stack);
        if (translationKey != null) {
            return I18n.translateToLocalFormatted(translationKey, params);
        }
        return null;
    }

    /**
     * Проверяет, есть ли переименование для предмета
     * @param stack Предмет
     * @return true если предмет будет переименован
     */
    public static boolean hasRename(ItemStack stack) {
        return getTranslationKey(stack) != null;
    }

    /**
     * Событие для замены названий в тултипе (при наведении)
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        String localizedName = getLocalizedName(stack);

        if (localizedName != null && !event.getToolTip().isEmpty()) {
            // Заменяем первую строку (название предмета)
            event.getToolTip().set(0, localizedName);
        }
    }
}