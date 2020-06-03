package com.arcanediver.dolphins_friends.common;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.common.entity.EntitySonarDataHandler;
import com.arcanediver.dolphins_friends.entity.RidableDolphinEnitity;
import net.minecraft.client.main.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;

public class CommonEvents {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(DolphinsFriends.MOD_ID, "sonar_data"), new EntitySonarDataHandler.Provider());
        }
    }
}
