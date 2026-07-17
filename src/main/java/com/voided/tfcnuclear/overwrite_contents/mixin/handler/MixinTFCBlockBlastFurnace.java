package com.voided.tfcnuclear.overwrite_contents.mixin.handler;

import com.hbm.blocks.ModBlocks;
import net.dries007.tfc.objects.blocks.devices.BlockBlastFurnace;
import net.dries007.tfc.objects.blocks.metal.BlockMetalSheet;
import net.dries007.tfc.objects.te.TEMetalSheet;
import net.dries007.tfc.util.block.Multiblock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Predicate;

@Mixin(BlockBlastFurnace.class)
public class MixinTFCBlockBlastFurnace {

    /**
     * @author TFCNuclear
     * @reason Replace stone matcher with custom one (brick_fire instead of fire_brick)
     */
    @Overwrite(remap = false)
    public static int getChimneyLevels(World world, BlockPos pos) {
        // Заменяем stoneMatcher на наш кастомный
        Predicate<IBlockState> stoneMatcher = state ->
                state.getBlock() == ModBlocks.brick_fire;

        // sheetMatcher из оригинального кода
        Predicate<IBlockState> sheetMatcher = state -> {
            if (state.getBlock() instanceof BlockMetalSheet) {
                BlockMetalSheet block = (BlockMetalSheet) state.getBlock();
                return block.getMetal().getTier().isAtLeast(net.dries007.tfc.api.types.Metal.Tier.TIER_III)
                        && block.getMetal().isToolMetal();
            }
            return false;
        };

        // Полностью копируем структуру мультиблока с нашим stoneMatcher
        Multiblock chimney = new Multiblock()
                .match(new BlockPos(0, 0, 0), state ->
                        state.getBlock() == net.dries007.tfc.objects.blocks.BlocksTFC.MOLTEN
                                || state.getMaterial().isReplaceable())
                .match(new BlockPos(0, 0, 1), stoneMatcher)
                .match(new BlockPos(0, 0, -1), stoneMatcher)
                .match(new BlockPos(1, 0, 0), stoneMatcher)
                .match(new BlockPos(-1, 0, 0), stoneMatcher)
                .match(new BlockPos(0, 0, -2), sheetMatcher)
                .match(new BlockPos(0, 0, -2), tile -> tile.getFace(EnumFacing.NORTH), TEMetalSheet.class)
                .match(new BlockPos(0, 0, 2), sheetMatcher)
                .match(new BlockPos(0, 0, 2), tile -> tile.getFace(EnumFacing.SOUTH), TEMetalSheet.class)
                .match(new BlockPos(2, 0, 0), sheetMatcher)
                .match(new BlockPos(2, 0, 0), tile -> tile.getFace(EnumFacing.EAST), TEMetalSheet.class)
                .match(new BlockPos(-2, 0, 0), sheetMatcher)
                .match(new BlockPos(-2, 0, 0), tile -> tile.getFace(EnumFacing.WEST), TEMetalSheet.class)
                .match(new BlockPos(-1, 0, -1), sheetMatcher)
                .match(new BlockPos(-1, 0, -1), tile -> tile.getFace(EnumFacing.NORTH) && tile.getFace(EnumFacing.WEST), TEMetalSheet.class)
                .match(new BlockPos(1, 0, -1), sheetMatcher)
                .match(new BlockPos(1, 0, -1), tile -> tile.getFace(EnumFacing.NORTH) && tile.getFace(EnumFacing.EAST), TEMetalSheet.class)
                .match(new BlockPos(-1, 0, 1), sheetMatcher)
                .match(new BlockPos(-1, 0, 1), tile -> tile.getFace(EnumFacing.SOUTH) && tile.getFace(EnumFacing.WEST), TEMetalSheet.class)
                .match(new BlockPos(1, 0, 1), sheetMatcher)
                .match(new BlockPos(1, 0, 1), tile -> tile.getFace(EnumFacing.SOUTH) && tile.getFace(EnumFacing.EAST), TEMetalSheet.class);

        // Оригинальная логика проверки уровней дымохода
        for (int i = 1; i < 6; i++) {
            BlockPos center = pos.up(i);
            if (!chimney.test(world, center)) {
                return i - 1;
            }
        }
        return 5;
    }
}