package com.arcanediver.dolphins_friends.inventory.container;

import com.arcanediver.dolphins_friends.items.SonarItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotSonarRidableDolphin extends SlotItemHandler {
    public SlotSonarRidableDolphin(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof SonarItem;
    }
}
