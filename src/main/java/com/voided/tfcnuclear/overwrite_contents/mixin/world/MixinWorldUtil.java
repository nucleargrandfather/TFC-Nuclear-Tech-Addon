package com.voided.tfcnuclear.overwrite_contents.mixin.world;

import com.google.common.base.Predicate;
import com.hbm.world.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mixin(value = WorldUtil.class, remap = false) // Важно: remap = false
public class MixinWorldUtil {

    // Убираем @Shadow, так как мы работаем через рефлексию в статическом инициализаторе

    @Inject(
            method = "<clinit>",
            at = @At("RETURN")
    )
    private static void onStaticInit(CallbackInfo ci) {
        try {
            // Создаём новый предикат
            Predicate<IBlockState> newPredicate = state -> {
                if (state == null) return false;
                Block block = state.getBlock();

                // Безопасно получаем RegistryName
                ResourceLocation registryName = block.getRegistryName();
                if (registryName == null) return false;
                String name = registryName.toString();

                // Проверяем ванильный камень
                if (block == Blocks.STONE) return true;

                // Проверяем камни TFC по RegistryName
                if (name.startsWith("tfc:raw/")) return true;

                // Проверяем OreDict (безопасно)
                try {
                    int[] ids = OreDictionary.getOreIDs(new net.minecraft.item.ItemStack(block));
                    for (int id : ids) {
                        if (OreDictionary.getOreName(id).equals("stone")) {
                            return true;
                        }
                    }
                } catch (IllegalArgumentException | NullPointerException e) {
                    // Игнорируем ошибки для незарегистрированных блоков
                }

                return false;
            };

            // Находим поле STONE_PREDICATE
            Field field = WorldUtil.class.getDeclaredField("STONE_PREDICATE");
            field.setAccessible(true);

            // Убираем модификатор final
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            // Устанавливаем новое значение
            field.set(null, newPredicate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}