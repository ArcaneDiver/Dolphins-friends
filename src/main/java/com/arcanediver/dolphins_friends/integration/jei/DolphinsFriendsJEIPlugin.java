package com.arcanediver.dolphins_friends.integration.jei;


import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.ModIds;
import net.minecraft.util.ResourceLocation;


public class DolphinsFriendsJEIPlugin implements IModPlugin {

    public static DolphinsFriendsJEIPlugin INSTANCE;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ModIds.JEI_ID, "dolphins_friends");
    }
}
