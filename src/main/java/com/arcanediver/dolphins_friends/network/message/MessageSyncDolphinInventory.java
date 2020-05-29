package com.arcanediver.dolphins_friends.network.message;

import com.arcanediver.dolphins_friends.entity.RidableDolphinEnitity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncDolphinInventory implements IMessage<MessageSyncDolphinInventory> {

    private ItemStack stack;
    private int dolphinID;

    public MessageSyncDolphinInventory() {}

    public MessageSyncDolphinInventory(ItemStack stack, int dolphinID) {
        this.stack = stack;
        this.dolphinID = dolphinID;
    }

    @Override
    public void encode(MessageSyncDolphinInventory message, PacketBuffer buffer) {
        buffer.writeItemStack(message.stack);
        buffer.writeInt(message.dolphinID);
    }

    @Override
    public MessageSyncDolphinInventory decode(PacketBuffer buffer) {
        return new MessageSyncDolphinInventory(buffer.readItemStack(), buffer.readInt());
    }

    @Override
    public void handle(MessageSyncDolphinInventory message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = supplier.get().getSender();

            if(playerEntity != null) {
                World world = playerEntity.world;

                Entity entity = world.getEntityByID(message.dolphinID);

                if(entity instanceof RidableDolphinEnitity) {
                    RidableDolphinEnitity dolphin = (RidableDolphinEnitity) entity;

                    dolphin.getCapability(RidableDolphinEnitity.ITEM_HANDLER_CAPABILITY).orElse(null).insertItem(0, message.stack, false);
                }
            }
        });

        supplier.get().setPacketHandled(true);
    }
}
