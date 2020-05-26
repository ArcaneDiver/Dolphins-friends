package com.arcanediver.dolphins_friends.network.message;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.entity.RidableDolphinEnitity;
import com.arcanediver.dolphins_friends.inventory.container.RidableDolphinContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class MessageOpenDolphinInventory implements IMessage<MessageOpenDolphinInventory> {

    private int entityID;

    public MessageOpenDolphinInventory() {}

    public MessageOpenDolphinInventory(int entityID) {
        this.entityID = entityID;
    }

    @Override
    public void encode(MessageOpenDolphinInventory message, PacketBuffer buffer) {
        buffer.writeVarInt(message.entityID);
    }

    @Override
    public MessageOpenDolphinInventory decode(PacketBuffer buffer) {
        return new MessageOpenDolphinInventory(buffer.readVarInt());
    }

    @Override
    public void handle(MessageOpenDolphinInventory message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null) {
                World world = player.world;
                Entity entity = world.getEntityByID(message.entityID);

                if(entity instanceof RidableDolphinEnitity) {
                    DolphinsFriends.LOGGER.info("packet recived");

                    RidableDolphinEnitity dolphin = (RidableDolphinEnitity) world.getEntityByID(message.entityID);

                    NetworkHooks.openGui(player, dolphin, buffer -> buffer.writeInt(dolphin.getEntityId()));
                }

            }
        });

        supplier.get().setPacketHandled(true);
    }
}
