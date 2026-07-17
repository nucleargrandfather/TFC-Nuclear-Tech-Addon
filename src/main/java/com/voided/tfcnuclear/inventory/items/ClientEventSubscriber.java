package com.voided.tfcnuclear.inventory.items;

import com.voided.tfcnuclear.TFCNuclear;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = TFCNuclear.MOD_ID, value = Side.CLIENT)
public class ClientEventSubscriber {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        registerModel(ModItems.HEMATITE_SLAG);
        registerModel(ModItems.LIMONITE_SLAG);
        registerModel(ModItems.MAGNETITE_SLAG);
        registerModel(ModItems.GOLD_SLAG);
        registerModel(ModItems.GALENA_SLAG);
        registerModel(ModItems.COPPER_SLAG);
        registerModel(ModItems.FIRED_CATALYST_CLAY);
        registerModel(ModItems.ACID_ACTIVATED_CLAY);
    }

    private static void registerModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(
                item,
                0,
                new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory")
        );
    }
}