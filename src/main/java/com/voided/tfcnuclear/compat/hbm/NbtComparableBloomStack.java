package com.voided.tfcnuclear.compat.hbm;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import net.dries007.tfc.api.capability.forge.CapabilityForgeable;
import net.dries007.tfc.api.capability.forge.IForgeableMeasurableMetal;
import net.dries007.tfc.api.types.Metal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Objects;

public class NbtComparableBloomStack extends ComparableStack {

    public final int metalAmount;
    public final Metal metal;

    public NbtComparableBloomStack(Item item, int metalAmount, Metal metal) {
        super(new ItemStack(item));
        this.metalAmount = metalAmount;
        this.metal = metal;
        this.stacksize = 1;
    }

    public NbtComparableBloomStack(ItemStack stack) {
        super(stack);
        IForgeableMeasurableMetal cap = (IForgeableMeasurableMetal)
                stack.getCapability(CapabilityForgeable.FORGEABLE_CAPABILITY, null);
        if (cap != null) {
            this.metalAmount = cap.getMetalAmount();
            this.metal = cap.getMetal();
        } else {
            this.metalAmount = 0;
            this.metal = Metal.UNKNOWN;
        }
    }

    public boolean matchesRecipe(ItemStack stack, boolean ignoreSize) {
        if (stack == null || stack.isEmpty()) return false;
        if (stack.getItem() != this.item) return false;
        if (!ignoreSize && stack.getCount() < this.stacksize) return false;

        IForgeableMeasurableMetal cap = (IForgeableMeasurableMetal)
                stack.getCapability(CapabilityForgeable.FORGEABLE_CAPABILITY, null);
        if (cap == null) return false;

        return cap.getMetalAmount() == this.metalAmount &&
                cap.getMetal() == this.metal;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NbtComparableBloomStack that = (NbtComparableBloomStack) obj;
        return metalAmount == that.metalAmount &&
                Objects.equals(item, that.item) &&
                metal == that.metal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, metalAmount, metal);
    }

    @Override
    public ComparableStack copy() {
        return new NbtComparableBloomStack(item, metalAmount, metal);
    }
    @Override
    public List<ItemStack> extractForJEI() {
        ItemStack stack = new ItemStack(item, stacksize);
        IForgeableMeasurableMetal cap = (IForgeableMeasurableMetal)
                stack.getCapability(CapabilityForgeable.FORGEABLE_CAPABILITY, null);
        if (cap != null) {
            cap.setMetal(metal);
            cap.setMetalAmount(metalAmount);
        }
        return java.util.Collections.singletonList(stack);
    }
}