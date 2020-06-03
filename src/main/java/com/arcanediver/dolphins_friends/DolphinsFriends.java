package com.arcanediver.dolphins_friends;

import com.arcanediver.dolphins_friends.client.renders.RenderRegistry;
import com.arcanediver.dolphins_friends.client.screen.RidableDolphinScreen;
import com.arcanediver.dolphins_friends.common.CommonEvents;
import com.arcanediver.dolphins_friends.common.entity.EntitySonarDataHandler;
import com.arcanediver.dolphins_friends.common.DolphinEventHandler;
import com.arcanediver.dolphins_friends.common.TickEventHandler;
import com.arcanediver.dolphins_friends.init.*;
import com.arcanediver.dolphins_friends.network.PacketHandler;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(DolphinsFriends.MOD_ID)
public class DolphinsFriends {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "dolphins_friends";
    public static final int TICK_PER_SECOND = 20;


    public DolphinsFriends() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITY_TYPES.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModContainers.CONTAINER_TYPES.register(eventBus);
        ModSounds.SOUNDS.register(eventBus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);


        MinecraftForge.EVENT_BUS.register(DolphinEventHandler.class);
        MinecraftForge.EVENT_BUS.register(TickEventHandler.class);
        MinecraftForge.EVENT_BUS.register(CommonEvents.class);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        RenderRegistry.registryRenders();
        PacketHandler.register();
        EntitySonarDataHandler.register();

        ModAdvancements.init();
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(DolphinEventHandler.KEY_SONAR);
        ClientRegistry.registerKeyBinding(DolphinEventHandler.KEY_INVENTORY);
        ScreenManager.registerFactory(ModContainers.RIDABLE_DOLPHIN.get(), RidableDolphinScreen::new);
    }


    public static final ItemGroup TAB = new ItemGroup("dolphins_friends") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.SONAR.get());
        }
    };
}
