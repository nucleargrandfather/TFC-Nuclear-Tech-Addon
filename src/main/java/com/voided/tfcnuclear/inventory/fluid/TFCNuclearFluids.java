package com.voided.tfcnuclear.inventory.fluid;

import com.hbm.api.fluidmk2.IFluidRegisterListener;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.render.misc.EnumSymbol;

public class TFCNuclearFluids implements IFluidRegisterListener {

    public static FluidType LIMEWATER;

    @Override
    public void onFluidsLoad() {
        System.out.println("[TFC Nuclear] Регистрация кастомных жидкостей...");

        if (Fluids.fromName("LIMEWATER") != Fluids.NONE) {
            System.out.println("[TFC Nuclear] LIMEWATER уже зарегистрирована!");
            LIMEWATER = Fluids.fromName("LIMEWATER");
            return;
        }

        // Создаем жидкость
        LIMEWATER = new FluidType(
                "LIMEWATER",
                0xFFF5E6,
                0, 0, 0,
                EnumSymbol.NONE
        )
                .addTraits(Fluids.LIQUID)
                .setTemp(20)
                .addContainers(new Fluids.CD_Canister(0xFFF5E6))
                .noFF(false);

        // Добавляем в foreignFluids - HBM сам зарегистрирует и добавит в metaOrder
        Fluids.foreignFluids.add(LIMEWATER);

        System.out.println("[TFC Nuclear] LIMEWATER добавлена в foreignFluids");
        System.out.println("[TFC Nuclear] Текущий размер metaOrder: " + Fluids.metaOrder.size());
    }
}