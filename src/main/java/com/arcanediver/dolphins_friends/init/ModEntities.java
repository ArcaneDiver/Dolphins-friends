package com.arcanediver.dolphins_friends.init;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.entity.RidableDolphinEnitity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister(ForgeRegistries.ENTITIES, DolphinsFriends.MOD_ID);

    public static final RegistryObject<EntityType<RidableDolphinEnitity>> RIDABLE_DOLPHIN = registry(
            "ridable_dolphin",
            RidableDolphinEnitity::new,
            EntityClassification.WATER_CREATURE
    );

    private static <T extends Entity> RegistryObject<EntityType<T>> registry(String id, BiFunction<EntityType<T>, World, T> function, EntityClassification classification) {
        return ENTITY_TYPES.register(id, () -> EntityType.Builder.create(function::apply, classification).build(String.format("%s:%s", DolphinsFriends.MOD_ID, id)));
    }
}
