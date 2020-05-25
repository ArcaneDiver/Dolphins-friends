package com.arcanediver.dolphins_friends;

import com.arcanediver.dolphins_friends.client.renders.RenderRegistry;
import com.arcanediver.dolphins_friends.client.screen.RidableDolphinScreen;
import com.arcanediver.dolphins_friends.common.entity.EntitySonarDataHandler;
import com.arcanediver.dolphins_friends.event.DolphinEventHandler;
import com.arcanediver.dolphins_friends.event.TickEventHandler;
import com.arcanediver.dolphins_friends.init.ModContainers;
import com.arcanediver.dolphins_friends.init.ModEntities;
import com.arcanediver.dolphins_friends.init.ModItems;
import com.arcanediver.dolphins_friends.inventory.container.RidableDolphinContainer;
import com.arcanediver.dolphins_friends.network.PacketHandler;
import com.arcanediver.dolphins_friends.utils.RegistryHandler;
import mezz.jei.api.JeiPlugin;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.HorseInventoryScreen;
import net.minecraft.inventory.container.HorseInventoryContainer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.structure.IglooStructure;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.EnergyStorage;
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


    public DolphinsFriends() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITY_TYPES.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModContainers.CONTAINER_TYPES.register(eventBus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);

        RegistryHandler.init();


        MinecraftForge.EVENT_BUS.register(DolphinEventHandler.class);
        MinecraftForge.EVENT_BUS.register(TickEventHandler.class);
    }

    public void onClientSetup(FMLCommonSetupEvent event) {
        ClientRegistry.registerKeyBinding(DolphinEventHandler.KEY_SONAR);

        ScreenManager.registerFactory(ModContainers.RIDABLE_DOLPHIN.get(), RidableDolphinScreen::new);
    }

    public void onCommonSetup(FMLClientSetupEvent event) {
        RenderRegistry.registryRenders();
        PacketHandler.register();
        EntitySonarDataHandler.register();
    }


    public static final ItemGroup TAB = new ItemGroup("dolphins_friends") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryHandler.RUBY.get());
        }
    };
}
