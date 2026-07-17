package com.voided.tfcnuclear;

import com.hbm.api.fluidmk2.IFluidRegisterListener;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.render.misc.EnumSymbol;
import com.voided.tfcnuclear.compat.tfc.ConfigOverwriteHandler;
import com.voided.tfcnuclear.compat.hbm.ItemRenamer;
import com.voided.tfcnuclear.inventory.fluids.TFCNuclearFluids;
import com.voided.tfcnuclear.inventory.handler.*;
import com.voided.tfcnuclear.inventory.recipes.*;
import com.voided.tfcnuclear.inventory.material.TFCNuclearMats;
import com.voided.tfcnuclear.proxy.CommonProxy;
import com.voided.tfcnuclear.world.OreSpawn.HBMOreSpawn;
import com.voided.tfcnuclear.world.OreSpawn.TFCOreSpawn;
import net.dries007.tfc.api.recipes.heat.HeatRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod(modid = TFCNuclear.MOD_ID,
        name = TFCNuclear.NAME,
        version = TFCNuclear.VERSION)
public class TFCNuclear {
    public static final String MOD_ID = "tfcnuclear";
    public static final String NAME = "TFC Nuclear Tech Addon";
    public static final String VERSION = "1.0.1";


    @SidedProxy(clientSide = "com.voided.tfcnuclear.proxy.ClientProxy", serverSide = "com.voided.tfcnuclear.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                TFCNuclearFluids.writeFluidToConfig();
            } catch (InterruptedException ignored) {}
        }).start();

        TFCNuclearMats.init();

        proxy.registerModels();
        HBMOreSpawn.generate(event);
        TFCOreSpawn.generate(event);
        TFCOreSpawn.clean(event);


        OreDictHandler.registerOreDict();
        new Thread(() -> {
            try {
                Thread.sleep(4000); // 5 секунд задержки
                ConfigOverwriteHandler.applyConfigOverwrites();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        SmeltingRemover.removeRecipes();

        OreDictHandler.registerOreDict();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                TFCOreDictCleaner.cleanOreDict();
                HBMOreDictCleaner.cleanOreDict();
                VanillaOreDictCleaner.cleanOreDict();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        MinecraftForge.EVENT_BUS.register(new ItemRenamer());


        CraftingRecipes.addRecipes();
        FurnaceRecipes.addRecipes();
        TFCQuernRecipes.addQuernRecipe();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                TFCOreDictCleaner.cleanOreDict();
                HBMOreDictCleaner.cleanOreDict();
                VanillaOreDictCleaner.cleanOreDict();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        FurnaceRecipes.addRecipes();
    }

    @Mod.EventBusSubscriber(modid = MOD_ID)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onHeatRegistry(RegistryEvent.Register<HeatRecipe> event) {
            TFCHeatRecipes.registerHeatRecipes(event);
        }
    }
}