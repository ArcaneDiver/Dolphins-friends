package com.arcanediver.dolphins_friends.integration.jei;


import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.init.ModItems;
import com.google.common.collect.Lists;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

// WIP
@JeiPlugin
public class DolphinsFriendsJEIPlugin implements IModPlugin {

    public static DolphinsFriendsJEIPlugin INSTANCE;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(DolphinsFriends.MOD_ID, "jei");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
    }
}
