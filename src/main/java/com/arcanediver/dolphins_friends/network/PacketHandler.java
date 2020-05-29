package com.arcanediver.dolphins_friends.network;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.network.message.IMessage;
import com.arcanediver.dolphins_friends.network.message.MessageOpenDolphinInventory;
import com.arcanediver.dolphins_friends.network.message.MessageSyncDolphinInventory;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

public class PacketHandler {

    public static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel instance;
    private static int nextId = 0;

    public static void register()
    {
        instance = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(DolphinsFriends.MOD_ID, "network"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .simpleChannel();

        register(MessageOpenDolphinInventory.class, new MessageOpenDolphinInventory());
        register(MessageSyncDolphinInventory.class, new MessageSyncDolphinInventory());
    }

    private static <T> void register(Class<T> clazz, IMessage<T> message)
    {
        instance.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }

}
