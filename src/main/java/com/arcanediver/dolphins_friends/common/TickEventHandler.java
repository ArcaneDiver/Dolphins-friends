package com.arcanediver.dolphins_friends.common;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.common.entity.EntitySonarDataHandler.*;
import com.arcanediver.dolphins_friends.common.entity.EntitySonarDataHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TickEventHandler {

    @SubscribeEvent
    public static void onTickSonarCheck(TickEvent.PlayerTickEvent e) {

        if(e.phase == TickEvent.Phase.START) {
            PlayerEntity player = e.player;
            World world = player.world;

            ISonarData data = player.getCapability(EntitySonarDataHandler.CAPABILITY_SONAR_DATA).orElse(null);


            if (data != null && data.getTimeLeft().isPresent() && data.getEntities().isPresent()) {
                if (data.getTimeLeft().get() >= 0) {
                    data.setTimeLeft(data.getTimeLeft().get() - 1);


                } else {
                    for (int id : data.getEntities().get()) {
                        Entity entity = world.getEntityByID(id);

                        if (entity instanceof LivingEntity) {
                            entity.setGlowing(false);
                        }
                    }
                    data.setTimeLeft(null);
                    data.setEntities(null);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSonarCooldown(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;

        if(event.phase == TickEvent.Phase.START) {
            ISonarData data = player.getCapability(EntitySonarDataHandler.CAPABILITY_SONAR_DATA).orElse(null);
            if (data != null) {
                if (data.getCooldown() > 0) {
                    data.setCooldown(data.getCooldown() - 1);
                }
            }
        }
    }
}
