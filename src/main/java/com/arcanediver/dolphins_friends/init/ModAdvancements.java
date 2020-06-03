package com.arcanediver.dolphins_friends.init;

import com.arcanediver.dolphins_friends.advancements.RideDolphinTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class ModAdvancements {

    public static final RideDolphinTrigger RIDE_DOLPHIN = CriteriaTriggers.register(new RideDolphinTrigger());

    public static void init() {
    }
}
