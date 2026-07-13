package com.voided.tfcnuclear.overwrite_contents.mixin.world;

import com.google.common.base.Predicate;
import com.hbm.world.feature.DepthDeposit;
import com.voided.tfcnuclear.compat.hbm.TFCBlockMatcher;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(DepthDeposit.class)
public class MixinDepthDeposit {

    @Shadow
    private Block oreBlock;

    @Shadow
    private Block filler;

    @Shadow
    private int size;

    @Shadow
    private double fill;

    private static final Predicate<IBlockState> TFC_PREDICATE = new TFCBlockMatcher(Blocks.STONE);

    @Inject(
            method = "generateSphere",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void onGenerateSphere(World world, int cx, int cy, int cz, Random rand, CallbackInfo ci) {
        if (world.isRemote) return;

        ci.cancel();

        generateTFCSphere(world, cx, cy, cz, rand);
    }

    private void generateTFCSphere(World world, int cx, int cy, int cz, Random rand) {
        MutableBlockPos pos = new MutableBlockPos();
        int blocksPlaced = 0;
        int blocksChecked = 0;

        for (int ix = cx - size; ix <= cx + size; ix++) {
            int dx = ix - cx;
            for (int jy = cy - size; jy <= cy + size; jy++) {
                if (jy < 1 || jy > 126) continue;

                int dy = jy - cy;
                for (int kz = cz - size; kz <= cz + size; kz++) {
                    int dz = kz - cz;
                    blocksChecked++;

                    pos.setPos(ix, jy, kz);

                    IBlockState state = world.getBlockState(pos);
                    Block current = state.getBlock();

                    boolean isReplaceable = current.isReplaceableOreGen(state, world, pos, TFC_PREDICATE);

                    if (!isReplaceable) {
                        continue;
                    }

                    double len = Math.sqrt(dx * (double) dx + dy * (double) dy + dz * (double) dz);

                    if (len + rand.nextInt(2) < size * fill) {
                        world.setBlockState(pos, oreBlock.getDefaultState(), 2 | 16);
                        blocksPlaced++;
                    } else if (len + rand.nextInt(2) <= size) {
                        world.setBlockState(pos, filler.getDefaultState(), 2 | 16);
                        blocksPlaced++;
                    }
                }
            }
        }
    }
}