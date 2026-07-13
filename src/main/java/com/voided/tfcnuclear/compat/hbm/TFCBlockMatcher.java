package com.voided.tfcnuclear.compat.hbm;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;

public class TFCBlockMatcher implements Predicate<IBlockState> {
    private final Block originalTarget;

    public TFCBlockMatcher(Block originalTarget) {
        this.originalTarget = originalTarget;
    }

    @Override
    public boolean apply(@Nullable IBlockState input) {
        if (input == null) return false;

        Block block = input.getBlock();
        ResourceLocation registryName = block.getRegistryName();

        // Если это бедрок - пропускаем (как в оригинальном коде HBM)
        if (block == Blocks.BEDROCK) {
            return true;
        }

        // Если это ванильный камень - пропускаем
        if (block == Blocks.STONE) {
            return true;
        }

        if (registryName != null) {
            String fullPath = registryName.toString();
            if (fullPath.startsWith("tfc:raw/")) {
                return true;
            }
            if (fullPath.startsWith("tfc:rock/")) {
                return true;
            }
        }

        int[] oreIds = OreDictionary.getOreIDs(new ItemStack(block));
        for (int id : oreIds) {
            String oreName = OreDictionary.getOreName(id);
            if ("stone".equals(oreName) ||
                    oreName.startsWith("stone") ||
                    "rock".equals(oreName) ||
                    oreName.startsWith("rock")) {
                return true;
            }
        }

        if (originalTarget != Blocks.STONE && block == originalTarget) {
            return true;
        }

        return false;
    }
}