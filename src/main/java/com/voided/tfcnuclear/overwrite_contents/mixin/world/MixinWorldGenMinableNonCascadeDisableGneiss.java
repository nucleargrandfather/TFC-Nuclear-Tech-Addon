package com.voided.tfcnuclear.overwrite_contents.mixin.world;

import com.hbm.world.feature.WorldGenMinableNonCascade;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Mixin(WorldGenMinableNonCascade.class)
public class MixinWorldGenMinableNonCascadeDisableGneiss {

    @Shadow
    private IBlockState oreBlock;

    private static final Set<String> GNEISS_ORES = new HashSet<>();
    static {
        GNEISS_ORES.add("ore_gneiss_iron");
        GNEISS_ORES.add("ore_gneiss_gold");
        GNEISS_ORES.add("ore_gneiss_copper");
    }

    @Inject(
            method = "postGenerate",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void onPostGenerate(World world, Random rand, long finalOrigin, CallbackInfo ci) {
        Block block = oreBlock.getBlock();
        ResourceLocation registryName = block.getRegistryName();
        if (registryName != null && GNEISS_ORES.contains(registryName.getPath())) {
            ci.cancel();
        }
    }
}