package com.voided.tfcnuclear.compat.hbm;

import com.hbm.inventory.RecipesCommon;
import net.dries007.tfc.api.capability.forge.CapabilityForgeable;
import net.dries007.tfc.api.capability.forge.IForgeable;
import net.dries007.tfc.api.capability.forge.IForgeableMeasurableMetal;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.items.ItemBloom;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BloomAStack extends RecipesCommon.AStack {

    private final int displayAmount;

    public BloomAStack() {
        this(100);
    }

    public BloomAStack(int displayAmount) {
        this.displayAmount = displayAmount;
        this.stacksize = 1;
    }

    @Override
    public boolean matchesRecipe(ItemStack stack, boolean ignoreSize) {
        if (stack == null || stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof ItemBloom)) return false;

        IForgeable cap = stack.getCapability(CapabilityForgeable.FORGEABLE_CAPABILITY, null);
        if (!(cap instanceof IForgeableMeasurableMetal)) return false;

        IForgeableMeasurableMetal metalCap = (IForgeableMeasurableMetal) cap;
        return metalCap.getMetal() == Metal.WROUGHT_IRON && metalCap.getMetalAmount() > 0;
    }

    @Override
    public RecipesCommon.AStack copy() {
        return new BloomAStack(this.displayAmount);
    }

    @Override
    public ItemStack getStack() {
        return createBloomExample(displayAmount);
    }

    @Override
    public List<ItemStack> getStackList() {
        List<ItemStack> examples = new ArrayList<>();
        for (int amount : getValidAmounts()) {
            examples.add(createBloomExample(amount));
        }
        return examples;
    }

    @Override
    public List<ItemStack> extractForJEI() {
        return getStackList();
    }

    @Override
    public int compareTo(@NotNull RecipesCommon.AStack o) {
        if (o instanceof BloomAStack) {
            return Integer.compare(this.displayAmount, ((BloomAStack) o).displayAmount);
        }
        return 1;
    }

    @Override
    public int hashCode() {
        return "TFCBloomWroughtIron".hashCode() * 31 + displayAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof BloomAStack)) return false;
        BloomAStack other = (BloomAStack) obj;
        return this.displayAmount == other.displayAmount;
    }

    public static int[] getValidAmounts() {
        return new int[]{100, 200, 300, 400};
    }

    private ItemStack createBloomExample(int amount) {
        ItemStack stack = new ItemStack(ItemBloom.getByNameOrId("tfc:bloom/refined"));
        IForgeable cap = stack.getCapability(CapabilityForgeable.FORGEABLE_CAPABILITY, null);
        if (cap instanceof IForgeableMeasurableMetal) {
            IForgeableMeasurableMetal metalCap = (IForgeableMeasurableMetal) cap;
            metalCap.setMetal(Metal.WROUGHT_IRON);
            metalCap.setMetalAmount(amount);
        }
        return stack;
    }
}