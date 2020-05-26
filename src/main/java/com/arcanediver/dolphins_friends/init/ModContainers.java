package com.arcanediver.dolphins_friends.init;


import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.entity.RidableDolphinEnitity;
import com.arcanediver.dolphins_friends.inventory.container.RidableDolphinContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModContainers {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(ForgeRegistries.CONTAINERS, DolphinsFriends.MOD_ID);

    public static final RegistryObject<ContainerType<RidableDolphinContainer>> RIDABLE_DOLPHIN = register("ridable_dolphin", (IContainerFactory<RidableDolphinContainer>) (winID, playerInvententory, data) -> {
        RidableDolphinEnitity dolphin = (RidableDolphinEnitity) playerInvententory.player.world.getEntityByID(data.readInt());
        return new RidableDolphinContainer(winID, playerInvententory, dolphin.getCapability(RidableDolphinEnitity.ITEM_HANDLER_CAPABILITY).orElse(null));
    });

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String id, ContainerType.IFactory<T> factory) {
        return CONTAINER_TYPES.register(id, () -> new ContainerType<>(factory));
    }

}
