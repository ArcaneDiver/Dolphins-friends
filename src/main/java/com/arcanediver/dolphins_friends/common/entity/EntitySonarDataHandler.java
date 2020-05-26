package com.arcanediver.dolphins_friends.common.entity;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntitySonarDataHandler {

    @CapabilityInject(ISonarData.class)
    public static final Capability<ISonarData> CAPABILITY_SONAR_DATA = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(ISonarData.class, new EntitySonarDataHandler.Storage(), SonarData::new);


        MinecraftForge.EVENT_BUS.register(EntitySonarDataHandler.class);
    }



    public interface ISonarData {

        void setTimeLeft(Long time);
        void setEntities(List<Integer> entities);

        Optional<Long> getTimeLeft();
        Optional<List<Integer>>  getEntities();
    }

    public static class SonarData implements ISonarData {
        private Long timeLeft = null;
        private List<Integer> entities = null;

        @Override
        public void setTimeLeft(Long time) {
            timeLeft = time;
        }

        @Override
        public void setEntities(List<Integer> entities) {
            this.entities = entities;
        }

        @Override
        public Optional<Long> getTimeLeft() {
            return Optional.ofNullable(timeLeft);
        }

        @Override
        public Optional<List<Integer>> getEntities() {
            return Optional.ofNullable(entities);
        }
    }

    public static class Storage implements Capability.IStorage<ISonarData> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<ISonarData> capability, ISonarData instance, Direction side) {
            CompoundNBT compoundNBT = new CompoundNBT();
            ListNBT list = new ListNBT();

            if(instance.getEntities().isPresent()) {
                for (Integer entityId : instance.getEntities().get()) {
                    list.add(IntNBT.valueOf(entityId));
                }

                compoundNBT.put("player_list", list);

            }

            if(instance.getTimeLeft().isPresent()) {
                compoundNBT.putLong("time_left", instance.getTimeLeft().get());
            }

            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<ISonarData> capability, ISonarData instance, Direction side, INBT nbt) {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;
            World world = Minecraft.getInstance().world;

            if(compoundNBT.hasUniqueId("player_list")) {
                ListNBT entityList = (ListNBT) compoundNBT.get("player_list");
                List<Integer> entities = new ArrayList<>();

                for (INBT inbt : entityList) {
                    entities.add(
                            ((IntNBT) inbt).getInt()
                    );
                }

                instance.setEntities(entities);
            }

            if(compoundNBT.hasUniqueId("time_left")) {
                long timeLeft = compoundNBT.getLong("time_left");

                instance.setTimeLeft(timeLeft);
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT> {

        public final ISonarData INSTANCE = CAPABILITY_SONAR_DATA.getDefaultInstance();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY_SONAR_DATA.orEmpty(cap, LazyOptional.of(() -> INSTANCE));
        }

        @Override
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) CAPABILITY_SONAR_DATA.getStorage().writeNBT(CAPABILITY_SONAR_DATA, INSTANCE, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            CAPABILITY_SONAR_DATA.getStorage().readNBT(CAPABILITY_SONAR_DATA, INSTANCE, null, nbt);
        }
    }
}
