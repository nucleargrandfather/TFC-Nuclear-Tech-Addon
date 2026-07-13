package com.voided.tfcnuclear.inventory.material;

import com.hbm.inventory.material.NTMMaterial;

import static com.hbm.inventory.material.Mats.*;

public class TFCNuclearMats {

    public static final NTMMaterial MAT_BRONZE;
    public static final NTMMaterial MAT_TIN;
    public static final NTMMaterial MAT_SILVER;
    public static final NTMMaterial MAT_PLATINUM;
    public static final NTMMaterial MAT_BLACKBRONZE;
    public static final NTMMaterial MAT_BRASS;
    public static final NTMMaterial MAT_ROSEGOLD;
    public static final NTMMaterial MAT_STERLINGSILVER;
    public static final NTMMaterial MAT_MAGNETITE;
    public static final NTMMaterial MAT_LIMONITE;
    public static final NTMMaterial MAT_ZINC;
    public static final NTMMaterial MAT_NICKEL;
    public static final NTMMaterial MAT_MOLYBDENUM;
    public static final NTMMaterial MAT_ELASTICCOPPER;
    public static final NTMMaterial MAT_BLACKSTEEL;
    public static final NTMMaterial MAT_REDSTEEL;
    public static final NTMMaterial MAT_BLUESTEEL;

    static {
        MAT_BRONZE = makeSmeltable(25000, df("Bronze"), 0xCD7F32).m();
        MAT_TIN = makeSmeltable(25002, df("Tin"), 0xACB5B9).m();
        MAT_SILVER = makeSmeltable(25003, df("Silver"), 0xC0C0C0).m();
        MAT_PLATINUM = makeSmeltable(25004, df("Platinum"), 0xD9D9D9).m();
        MAT_BLACKBRONZE = makeSmeltable(25005, df("BlackBronze"), 0x3A0A1F).m();
        MAT_BRASS = makeSmeltable(25006, df("Brass"), 0xC3A343).m();
        MAT_ROSEGOLD = makeSmeltable(25007, df("RoseGold"), 0xB76E79).m();
        MAT_STERLINGSILVER = makeSmeltable(25008, df("SterlingSilver"), 0xB8BFC7).m();
        MAT_MAGNETITE = makeSmeltable(25009, df("Magnetite"), 0x2f3640).m();
        MAT_LIMONITE = makeSmeltable(25010, df("Limonite"), 0xbe7f51).m();
        MAT_ZINC = makeSmeltable(25011, df("Zinc"), 0xE5F0F3).m();
        MAT_NICKEL = makeSmeltable(25012, df("Nickel"), 0xC2C7B6).m();
        MAT_MOLYBDENUM = makeSmeltable(25013, df("Molybdenum"), 0xB0B5B9).m();
        MAT_ELASTICCOPPER = makeSmeltable(25014, df("ElasticCopper"), 0x0A3D2F).m();
        MAT_BLACKSTEEL = makeSmeltable(25015, df("BlackSteel"), 0x1A1D1F).m();
        MAT_REDSTEEL = makeSmeltable(25016, df("WeakRedSteel"), 0x4A2C2A).m();
        MAT_BLUESTEEL = makeSmeltable(25017, df("WeakBlueSteel"), 0x1E3A5F).m();
    }

    public static void init() {
    }
}