package com.voided.tfcnuclear.overwrite_contents.mixin.handler;

import com.hbm.blocks.ModBlocks;
import net.dries007.tfc.objects.blocks.devices.BlockBlastFurnace;
import net.dries007.tfc.util.block.Multiblock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

@Mixin(BlockBlastFurnace.class)
public class MixinTFCBlockBlastFurnace {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void replaceChimney(CallbackInfo ci) {
        try {
            // Создаем новый stoneMatcher для тестирования
            Predicate<IBlockState> newStoneMatcher = state ->
                    state.getBlock() == ModBlocks.brick_fire;

            // Создаем новый мультиблок с заменой
            Multiblock newChimney = createChimney(newStoneMatcher);

            // Получаем поле BLAST_FURNACE_CHIMNEY
            Field chimneyField = BlockBlastFurnace.class.getDeclaredField("BLAST_FURNACE_CHIMNEY");
            chimneyField.setAccessible(true);

            // Убираем модификатор final
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(chimneyField, chimneyField.getModifiers() & ~Modifier.FINAL);

            // Устанавливаем новое значение
            chimneyField.set(null, newChimney);

            System.out.println("[TFC Nuclear] Successfully replaced blast furnace chimney!");

        } catch (Exception e) {
            System.err.println("[TFC Nuclear] Error replacing chimney:");
            e.printStackTrace();
        }
    }

    private static Multiblock createChimney(Predicate<IBlockState> stoneMatcher) {
        // Создаем sheetMatcher
        Predicate<IBlockState> sheetMatcher = state -> {
            if (state.getBlock() instanceof net.dries007.tfc.objects.blocks.metal.BlockMetalSheet) {
                net.dries007.tfc.objects.blocks.metal.BlockMetalSheet block =
                        (net.dries007.tfc.objects.blocks.metal.BlockMetalSheet) state.getBlock();
                return block.getMetal().getTier().isAtLeast(net.dries007.tfc.api.types.Metal.Tier.TIER_III)
                        && block.getMetal().isToolMetal();
            }
            return false;
        };

        return new Multiblock()
                .match(new BlockPos(0, 0, 0), state ->
                        state.getBlock() == net.dries007.tfc.objects.blocks.BlocksTFC.MOLTEN
                                || state.getMaterial().isReplaceable())
                .match(new BlockPos(0, 0, 1), stoneMatcher)
                .match(new BlockPos(0, 0, -1), stoneMatcher)
                .match(new BlockPos(1, 0, 0), stoneMatcher)
                .match(new BlockPos(-1, 0, 0), stoneMatcher)
                .match(new BlockPos(0, 0, -2), sheetMatcher)
                .match(new BlockPos(0, 0, -2), tile -> tile.getFace(net.minecraft.util.EnumFacing.NORTH),
                        net.dries007.tfc.objects.te.TEMetalSheet.class)
                .match(new BlockPos(0, 0, 2), sheetMatcher)
                .match(new BlockPos(0, 0, 2), tile -> tile.getFace(net.minecraft.util.EnumFacing.SOUTH),
                        net.dries007.tfc.objects.te.TEMetalSheet.class)
                .match(new BlockPos(2, 0, 0), sheetMatcher)
                .match(new BlockPos(2, 0, 0), tile -> tile.getFace(net.minecraft.util.EnumFacing.EAST),
                        net.dries007.tfc.objects.te.TEMetalSheet.class)
                .match(new BlockPos(-2, 0, 0), sheetMatcher)
                .match(new BlockPos(-2, 0, 0), tile -> tile.getFace(net.minecraft.util.EnumFacing.WEST),
                        net.dries007.tfc.objects.te.TEMetalSheet.class)
                .match(new BlockPos(-1, 0, -1), sheetMatcher)
                .match(new BlockPos(-1, 0, -1), tile -> tile.getFace(net.minecraft.util.EnumFacing.NORTH)
                                && tile.getFace(net.minecraft.util.EnumFacing.WEST),
                        net.dries007.tfc.objects.te.TEMetalSheet.class)
                .match(new BlockPos(1, 0, -1), sheetMatcher)
                .match(new BlockPos(1, 0, -1), tile -> tile.getFace(net.minecraft.util.EnumFacing.NORTH)
                                && tile.getFace(net.minecraft.util.EnumFacing.EAST),
                        net.dries007.tfc.objects.te.TEMetalSheet.class)
                .match(new BlockPos(-1, 0, 1), sheetMatcher)
                .match(new BlockPos(-1, 0, 1), tile -> tile.getFace(net.minecraft.util.EnumFacing.SOUTH)
                                && tile.getFace(net.minecraft.util.EnumFacing.WEST),
                        net.dries007.tfc.objects.te.TEMetalSheet.class)
                .match(new BlockPos(1, 0, 1), sheetMatcher)
                .match(new BlockPos(1, 0, 1), tile -> tile.getFace(net.minecraft.util.EnumFacing.SOUTH)
                                && tile.getFace(net.minecraft.util.EnumFacing.EAST),
                        net.dries007.tfc.objects.te.TEMetalSheet.class);
    }
}