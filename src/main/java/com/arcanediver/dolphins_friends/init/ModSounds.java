package com.arcanediver.dolphins_friends.init;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, DolphinsFriends.MOD_ID);


    public static final RegistryObject<SoundEvent> ECHO = SOUNDS.register("echo", () -> new SoundEvent(new ResourceLocation(DolphinsFriends.MOD_ID, "entity.echo" )));
}
