package com.voided.tfcnuclear.overwrite_contents.mixin.recipes;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.material.MatDistribution;
import com.hbm.inventory.material.Mats;
import com.voided.tfcnuclear.inventory.material.TFCNuclearMats;
import net.dries007.tfc.objects.items.ItemBloom;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.hbm.inventory.material.MaterialShapes.*;
import static com.hbm.inventory.material.Mats.*;
import static com.voided.tfcnuclear.inventory.material.TFCNuclearMats.MAT_LIMONITE;
import static com.voided.tfcnuclear.inventory.material.TFCNuclearMats.MAT_MAGNETITE;

@Mixin(value = MatDistribution.class, remap = false)
public class MixinMatDistributionRecipes {

    @Inject(method = "registerDefaults", at = @At("RETURN"))
    private void onRegisterDefaults(CallbackInfo ci) {
        if (!Loader.isModLoaded("tfc")) {
            return;
        }

        removeDefaultOreRecipes();

        addTFCOreRecipes();

        addBloomRecipe();

        addTFCMetalRecipes();
    }


    private void removeDefaultOreRecipes() {
        try {
            Field materialOreEntriesField = Mats.class.getDeclaredField("materialOreEntries");
            materialOreEntriesField.setAccessible(true);
            Map<String, List<Mats.MaterialStack>> materialOreEntries =
                    (Map<String, List<Mats.MaterialStack>>) materialOreEntriesField.get(null);

            String[] oresToRemove = {
                    "oreIron", "oreGold", "oreCoal", "oreRedstone",
                    "oreCopper", "oreAluminum", "oreLead",
                    "oreHematite", "oreMalachite",
                    "oreTitanium", "oreTungsten", "oreUranium",
                    "oreThorium", "oreBeryllium", "oreCobalt"
            };

            Iterator<Map.Entry<String, List<Mats.MaterialStack>>> iterator = materialOreEntries.entrySet().iterator();
            int removed = 0;

            while (iterator.hasNext()) {
                Map.Entry<String, List<Mats.MaterialStack>> entry = iterator.next();
                String key = entry.getKey();

                for (String targetKey : oresToRemove) {
                    if (key.equals(targetKey)) {
                        iterator.remove();
                        removed++;
                        break;
                    }
                }
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }

    // ========== Рецепты для TFC руд ==========

    private void addTFCOreRecipes() {

        MatDistribution.registerOre("oreNormalHematite", MAT_HEMATITE, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichHematite", MAT_HEMATITE, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorHematite", MAT_HEMATITE, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallHematite", MAT_HEMATITE, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalLimonite", MAT_LIMONITE, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichLimonite", MAT_LIMONITE, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorLimonite", MAT_LIMONITE, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallLimonite", MAT_LIMONITE, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalMagnetite", MAT_MAGNETITE, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichMagnetite", MAT_MAGNETITE, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorMagnetite", MAT_MAGNETITE, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallMagnetite", MAT_MAGNETITE, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalMalachite", MAT_MALACHITE, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichMalachite", MAT_MALACHITE, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorMalachite", MAT_MALACHITE, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallMalachite", MAT_MALACHITE, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalCassiterite", TFCNuclearMats.MAT_TIN, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichCassiterite", TFCNuclearMats.MAT_TIN, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorCassiterite", TFCNuclearMats.MAT_TIN, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallCassiterite", TFCNuclearMats.MAT_TIN, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalGalena", MAT_LEAD, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichGalena", MAT_LEAD, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorGalena", MAT_LEAD, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallGalena", MAT_LEAD, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalSphalerite", TFCNuclearMats.MAT_ZINC, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichSphalerite", TFCNuclearMats.MAT_ZINC, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorSphalerite", TFCNuclearMats.MAT_ZINC, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallSphalerite", TFCNuclearMats.MAT_ZINC, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalGarnierite", TFCNuclearMats.MAT_NICKEL, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichGarnierite", TFCNuclearMats.MAT_NICKEL, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorGarnierite", TFCNuclearMats.MAT_NICKEL, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallGarnierite", TFCNuclearMats.MAT_NICKEL, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalTetrahedrite", MAT_COPPER, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichTetrahedrite", MAT_COPPER, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorTetrahedrite", MAT_COPPER, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallTetrahedrite", MAT_COPPER, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalPlatinum", TFCNuclearMats.MAT_PLATINUM, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichPlatinum", TFCNuclearMats.MAT_PLATINUM, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorPlatinum", TFCNuclearMats.MAT_PLATINUM, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallPlatinum", TFCNuclearMats.MAT_PLATINUM, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalSilver", TFCNuclearMats.MAT_SILVER, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichSilver", TFCNuclearMats.MAT_SILVER, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorSilver", TFCNuclearMats.MAT_SILVER, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallSilver", TFCNuclearMats.MAT_SILVER, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalGold", MAT_GOLD, QUANTUM.q(11), MAT_LEAD, QUANTUM.q(5), MAT_STONE, QUANTUM.q(3));
        MatDistribution.registerOre("oreRichGold", MAT_GOLD, QUANTUM.q(16), MAT_LEAD, QUANTUM.q(6), MAT_STONE, QUANTUM.q(4));
        MatDistribution.registerOre("orePoorGold", MAT_GOLD, QUANTUM.q(7), MAT_LEAD, QUANTUM.q(3), MAT_STONE, QUANTUM.q(2));
        MatDistribution.registerOre("oreSmallGold", MAT_GOLD, QUANTUM.q(5), MAT_LEAD, QUANTUM.q(2), MAT_STONE, QUANTUM.q(1));

        MatDistribution.registerOre("oreNormalNativeCopper", MAT_COPPER, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichNativeCopper", MAT_COPPER, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorNativeCopper", MAT_COPPER, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallNativeCopper", MAT_COPPER, QUANTUM.q(8));

        MatDistribution.registerOre("oreNormalMolybdenum", TFCNuclearMats.MAT_MOLYBDENUM, QUANTUM.q(18));
        MatDistribution.registerOre("oreRichMolybdenum", TFCNuclearMats.MAT_MOLYBDENUM, QUANTUM.q(26));
        MatDistribution.registerOre("orePoorMolybdenum", TFCNuclearMats.MAT_MOLYBDENUM, QUANTUM.q(12));
        MatDistribution.registerOre("oreSmallMolybdenum", TFCNuclearMats.MAT_MOLYBDENUM, QUANTUM.q(8));

        MatDistribution.registerOre("ironFlatStamp", MAT_WROUGHTIRON, INGOT.q(3));
        MatDistribution.registerOre("tfcCryolite", MAT_ALUMINIUM, QUANTUM.q(72), MAT_SODIUM, NUGGET.q(3), MAT_STONE, INGOT.q(1));
        MatDistribution.registerOre("tfcCoal", MAT_CARBON, INGOT.q(3), MAT_STONE, INGOT.q(1));
        MatDistribution.registerOre("tfcRedstone", MAT_REDSTONE, INGOT.q(4), MAT_STONE, INGOT.q(1));
    }

    private void addBloomRecipe() {
        Item bloomItem = Item.getByNameOrId("tfc:bloom/refined");
        if (bloomItem == null) return;

        // Используем registerEntry для регистрации предмета
        MatDistribution.registerEntry(
                bloomItem,
                MAT_WROUGHTIRON, QUANTUM.q(72)  // 100 TFC = 72 HBM кванта (заглушка для JEI)
        );
    }

    // ========== Рецепты для TFC металлов ==========

    private void addTFCMetalRecipes() {
        MatDistribution.registerOre("ingotDoubleBismuth", TFCNuclearMats.MAT_MOLYBDENUM, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleBismuthBronze", TFCNuclearMats.MAT_ELASTICCOPPER, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleBlackBronze", TFCNuclearMats.MAT_BLACKBRONZE, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleBrass", TFCNuclearMats.MAT_BRASS, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleBronze", TFCNuclearMats.MAT_BRONZE, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleCopper", MAT_COPPER, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleGold", MAT_GOLD, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleLead", MAT_LEAD, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleNickel", TFCNuclearMats.MAT_NICKEL, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleRoseGold", TFCNuclearMats.MAT_ROSEGOLD, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleSilver", TFCNuclearMats.MAT_SILVER, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleTin", TFCNuclearMats.MAT_TIN, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleZinc", TFCNuclearMats.MAT_ZINC, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleSterlingSilver", TFCNuclearMats.MAT_STERLINGSILVER, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleWroughtIron", MAT_WROUGHTIRON, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoublePigIron", MAT_PIGIRON, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleSteel", MAT_STEEL, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoublePlatinum", TFCNuclearMats.MAT_PLATINUM, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleBlackSteel", TFCNuclearMats.MAT_BLACKSTEEL, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleBlueSteel", TFCNuclearMats.MAT_BLUESTEEL, QUANTUM.q(144));
        MatDistribution.registerOre("ingotDoubleRedSteel", TFCNuclearMats.MAT_REDSTEEL, QUANTUM.q(144));

        // Double Sheets
        MatDistribution.registerOre("sheetDoubleBismuth", TFCNuclearMats.MAT_MOLYBDENUM, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleBismuthBronze", TFCNuclearMats.MAT_ELASTICCOPPER, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleBlackBronze", TFCNuclearMats.MAT_BLACKBRONZE, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleBrass", TFCNuclearMats.MAT_BRASS, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleBronze", TFCNuclearMats.MAT_BRONZE, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleCopper", MAT_COPPER, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleGold", MAT_GOLD, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleLead", MAT_LEAD, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleNickel", TFCNuclearMats.MAT_NICKEL, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleRoseGold", TFCNuclearMats.MAT_ROSEGOLD, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleSilver", TFCNuclearMats.MAT_SILVER, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleTin", TFCNuclearMats.MAT_TIN, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleZinc", TFCNuclearMats.MAT_ZINC, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleSterlingSilver", TFCNuclearMats.MAT_STERLINGSILVER, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleWroughtIron", MAT_WROUGHTIRON, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoublePigIron", MAT_PIGIRON, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleSteel", MAT_STEEL, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoublePlatinum", TFCNuclearMats.MAT_PLATINUM, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleBlackSteel", TFCNuclearMats.MAT_BLACKSTEEL, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleBlueSteel", TFCNuclearMats.MAT_BLUESTEEL, QUANTUM.q(288));
        MatDistribution.registerOre("sheetDoubleRedSteel", TFCNuclearMats.MAT_REDSTEEL, QUANTUM.q(288));

        // Sheets
        MatDistribution.registerOre("sheetBismuth", TFCNuclearMats.MAT_MOLYBDENUM, QUANTUM.q(144));
        MatDistribution.registerOre("sheetBismuthBronze", TFCNuclearMats.MAT_ELASTICCOPPER, QUANTUM.q(144));
        MatDistribution.registerOre("sheetBlackBronze", TFCNuclearMats.MAT_BLACKBRONZE, QUANTUM.q(144));
        MatDistribution.registerOre("sheetBrass", TFCNuclearMats.MAT_BRASS, QUANTUM.q(144));
        MatDistribution.registerOre("sheetBronze", TFCNuclearMats.MAT_BRONZE, QUANTUM.q(144));
        MatDistribution.registerOre("sheetCopper", MAT_COPPER, QUANTUM.q(144));
        MatDistribution.registerOre("sheetGold", MAT_GOLD, QUANTUM.q(144));
        MatDistribution.registerOre("sheetLead", MAT_LEAD, QUANTUM.q(144));
        MatDistribution.registerOre("sheetNickel", TFCNuclearMats.MAT_NICKEL, QUANTUM.q(144));
        MatDistribution.registerOre("sheetRoseGold", TFCNuclearMats.MAT_ROSEGOLD, QUANTUM.q(144));
        MatDistribution.registerOre("sheetSilver", TFCNuclearMats.MAT_SILVER, QUANTUM.q(144));
        MatDistribution.registerOre("sheetTin", TFCNuclearMats.MAT_TIN, QUANTUM.q(144));
        MatDistribution.registerOre("sheetZinc", TFCNuclearMats.MAT_ZINC, QUANTUM.q(144));
        MatDistribution.registerOre("sheetSterlingSilver", TFCNuclearMats.MAT_STERLINGSILVER, QUANTUM.q(144));
        MatDistribution.registerOre("sheetWroughtIron", MAT_WROUGHTIRON, QUANTUM.q(144));
        MatDistribution.registerOre("sheetPigIron", MAT_PIGIRON, QUANTUM.q(144));
        MatDistribution.registerOre("sheetSteel", MAT_STEEL, QUANTUM.q(144));
        MatDistribution.registerOre("sheetPlatinum", TFCNuclearMats.MAT_PLATINUM, QUANTUM.q(144));
        MatDistribution.registerOre("sheetBlackSteel", TFCNuclearMats.MAT_BLACKSTEEL, QUANTUM.q(144));
        MatDistribution.registerOre("sheetBlueSteel", TFCNuclearMats.MAT_BLUESTEEL, QUANTUM.q(144));
        MatDistribution.registerOre("sheetRedSteel", TFCNuclearMats.MAT_REDSTEEL, QUANTUM.q(144));

        // Scrap
        MatDistribution.registerOre("scrapBismuth", TFCNuclearMats.MAT_MOLYBDENUM, QUANTUM.q(72));
        MatDistribution.registerOre("scrapBismuthBronze", TFCNuclearMats.MAT_ELASTICCOPPER, QUANTUM.q(72));
        MatDistribution.registerOre("scrapBlackBronze", TFCNuclearMats.MAT_BLACKBRONZE, QUANTUM.q(72));
        MatDistribution.registerOre("scrapBrass", TFCNuclearMats.MAT_BRASS, QUANTUM.q(72));
        MatDistribution.registerOre("scrapBronze", TFCNuclearMats.MAT_BRONZE, QUANTUM.q(72));
        MatDistribution.registerOre("scrapCopper", MAT_COPPER, QUANTUM.q(72));
        MatDistribution.registerOre("scrapGold", MAT_GOLD, QUANTUM.q(72));
        MatDistribution.registerOre("scrapLead", MAT_LEAD, QUANTUM.q(72));
        MatDistribution.registerOre("scrapNickel", TFCNuclearMats.MAT_NICKEL, QUANTUM.q(72));
        MatDistribution.registerOre("scrapRoseGold", TFCNuclearMats.MAT_ROSEGOLD, QUANTUM.q(72));
        MatDistribution.registerOre("scrapSilver", TFCNuclearMats.MAT_SILVER, QUANTUM.q(72));
        MatDistribution.registerOre("scrapTin", TFCNuclearMats.MAT_TIN, QUANTUM.q(72));
        MatDistribution.registerOre("scrapZinc", TFCNuclearMats.MAT_ZINC, QUANTUM.q(72));
        MatDistribution.registerOre("scrapSterlingSilver", TFCNuclearMats.MAT_STERLINGSILVER, QUANTUM.q(72));
        MatDistribution.registerOre("scrapWroughtIron", MAT_WROUGHTIRON, QUANTUM.q(72));
        MatDistribution.registerOre("scrapPigIron", MAT_PIGIRON, QUANTUM.q(72));
        MatDistribution.registerOre("scrapSteel", MAT_STEEL, QUANTUM.q(72));
        MatDistribution.registerOre("scrapPlatinum", TFCNuclearMats.MAT_PLATINUM, QUANTUM.q(72));
        MatDistribution.registerOre("scrapBlackSteel", TFCNuclearMats.MAT_BLACKSTEEL, QUANTUM.q(72));
        MatDistribution.registerOre("scrapBlueSteel", TFCNuclearMats.MAT_BLUESTEEL, QUANTUM.q(72));
        MatDistribution.registerOre("scrapRedSteel", TFCNuclearMats.MAT_REDSTEEL, QUANTUM.q(72));
    }
}