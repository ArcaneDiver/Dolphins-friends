package com.arcanediver.dolphins_friends.event;


import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.client.screen.RidableDolphinScreen;
import com.arcanediver.dolphins_friends.common.entity.EntitySonarDataHandler;
import com.arcanediver.dolphins_friends.entity.RidableDolphinEnitity;
import com.arcanediver.dolphins_friends.init.ModEntities;
import com.arcanediver.dolphins_friends.inventory.container.RidableDolphinContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.HorseInventoryScreen;
import net.minecraft.client.renderer.entity.DolphinRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DolphinEventHandler {

    public static final KeyBinding KEY_SONAR = new KeyBinding(String.format("key.%s.sonar", DolphinsFriends.MOD_ID), GLFW.GLFW_KEY_U, String.format("key.categories.%s", DolphinsFriends.MOD_ID));

    @SubscribeEvent
    public static void onDolphinInteract(PlayerInteractEvent.EntityInteract e) {

        if (e.getTarget() != null && e.getTarget() instanceof DolphinEntity && !(e.getTarget() instanceof RidableDolphinEnitity) ) {
            World worldIn = e.getWorld();
            DolphinEntity dolphinEntity = (DolphinEntity) e.getTarget();
            PlayerEntity player = e.getPlayer();

            if (player != null && player.getRidingEntity() == null && !worldIn.isRemote) {

                RidableDolphinEnitity sobstitue = new RidableDolphinEnitity(ModEntities.RIDABLE_DOLPHIN.get(), worldIn);
                sobstitue.setPosition(dolphinEntity.getPosX(), dolphinEntity.getPosY(), dolphinEntity.getPosZ());
                worldIn.addEntity(sobstitue);
                player.startRiding(sobstitue);
                dolphinEntity.remove();
            }

        }
    }

    /*@SubscribeEvent
    public static void unMountRidableDolphin(EntityMountEvent e) {
        if (e.isDismounting() && e.getEntityBeingMounted() instanceof RidableDolphinEnitity && e.getEntityBeingMounted().isAlive()) {
            World world = e.getWorldObj();
            RidableDolphinEnitity dolphin = (RidableDolphinEnitity) e.getEntityBeingMounted();
            DolphinEntity realDolphin = new DolphinEntity(EntityType.DOLPHIN, world);

            if (!world.isRemote && dolphin.isAlive()) {
                realDolphin.setPosition(dolphin.lastTickPosX, dolphin.lastTickPosY, dolphin.lastTickPosZ);
                dolphin.remove();

                world.addEntity(realDolphin);
            }

        }
    }*/

    @SubscribeEvent
    public static void onSonarActivation(InputEvent.KeyInputEvent event) {
        if(event.getAction() == GLFW.GLFW_PRESS && event.getKey() == KEY_SONAR.getKey().getKeyCode()) {

            ClientPlayerEntity player = Minecraft.getInstance().player;

            if(player != null && player.getRidingEntity() != null && player.getRidingEntity() instanceof RidableDolphinEnitity) {
                RidableDolphinEnitity dolphin = (RidableDolphinEnitity) player.getRidingEntity();

                World world = Minecraft.getInstance().world;

                if (world != null) {
                    AxisAlignedBB box = new AxisAlignedBB(
                            dolphin.getPosX() - 30,
                            dolphin.getPosY() - 30,
                            dolphin.getPosZ() - 30,
                            dolphin.getPosX() + 30,
                            dolphin.getPosY() + 30,
                            dolphin.getPosZ() + 30
                    );

                    List<LivingEntity> entities = new ArrayList<>();
                    List<Integer> entityIDs = new ArrayList<>();

                    for (LivingEntity e : world.getEntitiesWithinAABB(LivingEntity.class, box)) {
                        if (!(e instanceof PlayerEntity) && !(e instanceof RidableDolphinEnitity)) {
                            entities.add(e);
                            entityIDs.add(e.getEntityId());
                        }
                    }

                    EntitySonarDataHandler.ISonarData data = player.getCapability(EntitySonarDataHandler.CAPABILITY_SONAR_DATA).orElse(null);


                    if(data != null) {
                        for (LivingEntity e : entities) {
                            e.setGlowing(true);
                        }

                        data.setTimeLeft((long) (3 * 20));
                        data.setEntities(entityIDs);
                    }



                }
            }
        }
    }

    @SubscribeEvent
    public static void onDolphinContainerOpen(PlayerContainerEvent.Open event) {
        DolphinsFriends.LOGGER.info("Open Inventory server");

        ServerPlayerEntity playerEntity = (ServerPlayerEntity) event.getPlayer();

        if(playerEntity.getRidingEntity() instanceof RidableDolphinEnitity) {
            RidableDolphinEnitity dolphin = (RidableDolphinEnitity) playerEntity.getRidingEntity();

            DolphinsFriends.LOGGER.info("Hook fired");

            playerEntity.openContainer = new RidableDolphinContainer(playerEntity.currentWindowId, playerEntity.inventory, dolphin.getInventory());
        }
    }

    @SubscribeEvent
    public static void onDolphinGuiOpen(GuiOpenEvent event) {
        DolphinsFriends.LOGGER.info("Open inventory client");
        ClientPlayerEntity player = Minecraft.getInstance().player;


        if(player != null && player.getRidingEntity() instanceof RidableDolphinEnitity && event.getGui() instanceof ContainerScreen) {
            ContainerScreen<?> oldScreen = (ContainerScreen) event.getGui();
            Container oldContainer = oldScreen.getContainer();

            RidableDolphinEnitity dolphin = (RidableDolphinEnitity) player.getRidingEntity();

            RidableDolphinContainer container = new RidableDolphinContainer(oldContainer.windowId, player.inventory, dolphin.getInventory());
            RidableDolphinScreen screen = new RidableDolphinScreen(container, player.inventory, dolphin.getDisplayName());

            player.openContainer = container;
            event.setGui(screen);


        }
    }
}
