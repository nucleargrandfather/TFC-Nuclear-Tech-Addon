package com.voided.tfcnuclear.overwrite_contents.mixin.world;

import com.hbm.world.gen.component.CivilianFeatures;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(targets = "com.hbm.world.gen.component.CivilianFeatures$RuralHouse1")
public class MixinFurnaceRemover {

    @Inject(
            method = "addComponentParts",
            at = @At("RETURN")
    )
    private void onStructureGenerated(World world, Random rand, StructureBoundingBox box, CallbackInfoReturnable<Boolean> cir) {
        // После генерации проходим по bounding box и заменяем все печи
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = box.minX; x <= box.maxX; x++) {
            for (int y = box.minY; y <= box.maxY; y++) {
                for (int z = box.minZ; z <= box.maxZ; z++) {
                    pos.setPos(x, y, z);
                    if (world.getBlockState(pos).getBlock() == Blocks.FURNACE) {
                        // Заменяем на электрическую печь
                        world.setBlockState(pos, com.hbm.blocks.ModBlocks.machine_electric_furnace_off.getStateFromMeta(3), 2);
                    }
                }
            }
        }
    }
}