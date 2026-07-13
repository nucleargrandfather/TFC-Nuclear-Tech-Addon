package com.voided.tfcnuclear.compat.hbm;

import com.hbm.inventory.RecipesCommon;
import net.dries007.tfc.api.capability.forge.CapabilityForgeable;
import net.dries007.tfc.api.capability.forge.IForgeableMeasurableMetal;
import net.dries007.tfc.api.types.Metal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BloomStack extends RecipesCommon.AStack {

    public final Item item;
    public final int metalAmount;
    public final Metal metal;

    public BloomStack(Item item, int metalAmount) {
        this(item, metalAmount, Metal.WROUGHT_IRON);
    }

    public BloomStack(Item item, int metalAmount, Metal metal) {
        this.item = item;
        this.metalAmount = metalAmount;
        this.metal = metal;
        this.stacksize = 1;
    }

    @Override
    public boolean matchesRecipe(ItemStack stack, boolean ignoreSize) {
        if (stack == null || stack.isEmpty()) return false;
        if (stack.getItem() != this.item) return false;
        if (!ignoreSize && stack.getCount() < this.stacksize) return false;

        IForgeableMeasurableMetal cap = (IForgeableMeasurableMetal)
                stack.getCapability(CapabilityForgeable.FORGEABLE_CAPABILITY, null);
        if (cap == null) return false;

        // ТОЧНОЕ сравнение количества
        boolean matches = cap.getMetalAmount() == this.metalAmount &&
                cap.getMetal() == this.metal;

        return matches;
    }

    @Override
    public RecipesCommon.AStack copy() {
        return new BloomStack(item, metalAmount, metal);
    }

    @Override
    public ItemStack getStack() {
        ItemStack stack = new ItemStack(item, stacksize);
        IForgeableMeasurableMetal cap = (IForgeableMeasurableMetal)
                stack.getCapability(CapabilityForgeable.FORGEABLE_CAPABILITY, null);
        if (cap != null) {
            cap.setMetal(metal);
            cap.setMetalAmount(metalAmount);
        }
        return stack;
    }

    @Override
    public List<ItemStack> getStackList() {
        return Collections.singletonList(getStack());
    }

    @Override
    public int compareTo(RecipesCommon.AStack o) {
        if (o instanceof BloomStack) {
            BloomStack other = (BloomStack) o;
            int itemComp = this.item.getRegistryName().toString()
                    .compareTo(other.item.getRegistryName().toString());
            if (itemComp != 0) return itemComp;
            return Integer.compare(this.metalAmount, other.metalAmount);
        }
        return 0;
    }

    @Override
    public List<ItemStack> extractForJEI() {
        ItemStack stack = getStack();
        return Collections.singletonList(stack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, metalAmount, metal);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BloomStack that = (BloomStack) obj;
        return metalAmount == that.metalAmount &&
                Objects.equals(item, that.item) &&
                metal == that.metal;
    }
}