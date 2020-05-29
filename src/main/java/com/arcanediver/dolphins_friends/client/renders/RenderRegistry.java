package com.arcanediver.dolphins_friends.client.renders;


import com.arcanediver.dolphins_friends.init.ModEntities;
import net.minecraft.client.renderer.entity.DolphinRenderer;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class RenderRegistry {

    public static void registryRenders() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.RIDABLE_DOLPHIN.get(), (IRenderFactory) manager -> new RidableDolphinRenderer(manager));
    }
}
