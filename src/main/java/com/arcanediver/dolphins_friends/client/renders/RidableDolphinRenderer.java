package com.arcanediver.dolphins_friends.client.renders;

import com.arcanediver.dolphins_friends.entity.RidableDolphinEnitity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.layers.DolphinCarriedItemLayer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.client.renderer.entity.model.DolphinModel;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.item.SaddleItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.HorseSaddle;

public class RidableDolphinRenderer extends MobRenderer<RidableDolphinEnitity, DolphinModel<RidableDolphinEnitity>> {

    private static final ResourceLocation DOLPHIN_LOCATION = new ResourceLocation("textures/entity/dolphin.png");

    public RidableDolphinRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DolphinModel<>(), 0.7F);

    }

    public ResourceLocation getEntityTexture(RidableDolphinEnitity entity) {
        return DOLPHIN_LOCATION;
    }

}
