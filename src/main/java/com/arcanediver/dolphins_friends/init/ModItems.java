package com.arcanediver.dolphins_friends.init;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.items.SonarItem;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, DolphinsFriends.MOD_ID);

    public static final RegistryObject<Item> SONAR = registry("sonar", new Item(new Item.Properties()
            .group(DolphinsFriends.TAB)
    ));

    private static <T extends Item> RegistryObject<Item> registry(String id, T item) {
        return ITEMS.register(id, () -> item);
    }
}
