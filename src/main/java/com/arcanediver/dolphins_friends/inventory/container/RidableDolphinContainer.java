package com.arcanediver.dolphins_friends.inventory.container;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.init.ModContainers;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.fixes.ItemSpawnEggSplit;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class RidableDolphinContainer extends Container {

    private PlayerInventory playerInventory;
    private IItemHandler inventory;

    public RidableDolphinContainer(int windowID, PlayerInventory playerInventory) {
        this(windowID, playerInventory, new ItemStackHandler());
    }

    public RidableDolphinContainer(int windowID, PlayerInventory playerInventory, IItemHandler inventory) {
        super(ModContainers.RIDABLE_DOLPHIN.get(), windowID);


        this.playerInventory = playerInventory;
        this.inventory = inventory;

        this.addSlot(new SlotSonarRidableDolphin(inventory, 0, 80, 20));
        
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
            }
        }

        for(int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        }

        this.addListener(new IContainerListener() {
            @Override
            public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
                DolphinsFriends.LOGGER.info("all called");
            }

            @Override
            public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
                DolphinsFriends.LOGGER.info("slot called");
            }

            @Override
            public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {
                DolphinsFriends.LOGGER.info("property called");
            }
        });
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack copyStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            copyStack = slotStack.copy();

            if(index < 1) {
                if(!this.mergeItemStack(slotStack, 1, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if(!this.mergeItemStack(slotStack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if(slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }


        return copyStack;
    }
}
