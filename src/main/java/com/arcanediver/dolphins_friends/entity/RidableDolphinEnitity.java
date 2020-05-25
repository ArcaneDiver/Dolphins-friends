package com.arcanediver.dolphins_friends.entity;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.inventory.container.RidableDolphinContainer;
import com.arcanediver.dolphins_friends.utils.RegistryHandler;
import com.mojang.brigadier.Message;
import net.minecraft.client.gui.screen.inventory.HorseInventoryScreen;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.HorseInventoryContainer;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.swing.*;

public class RidableDolphinEnitity extends DolphinEntity implements INamedContainerProvider {

    protected IInventory inventory = new Inventory(1);

    public RidableDolphinEnitity(EntityType<RidableDolphinEnitity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        // I dont won't AI
    }

    public void setInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    public IInventory getInventory() {
        return inventory;
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();

        for(int i = 0; i < this.inventory.getSizeInventory(); i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            if(!stack.isEmpty())
                this.entityDropItem(stack);
        }
    }

    @Override
    public void travel(Vec3d initialDir) {
        if(this.isBeingRidden()) {
            LivingEntity passenger = (LivingEntity) getPassengers().get(0);

            rotationYaw = passenger.rotationYaw;
            prevRotationYaw = rotationYaw;
            rotationPitch = passenger.rotationPitch * 0.5F;
            setRotation(rotationYaw, rotationPitch);
            renderYawOffset = rotationYaw;
            rotationYawHead = renderYawOffset;

            float f = passenger.moveStrafing * 0.5F;
            float f1 = passenger.moveForward;

            if (f1 <= 0.0F) {
                f1 *= 0.25F;
            }

            Vec3d dir;

            Vec3d lookDir = new Vec3d(passenger.getLookVec().x, 0, passenger.getLookVec().z);

            if(passenger.moveStrafing != 0.0 && passenger.moveForward != 0.0) {
                dir = new Vec3d(f, passenger.getLookVec().y, (float) lookDir.length());
            } else if(passenger.moveStrafing != 0.0) {
                dir = new Vec3d(f, 0, 0);
            } else if(passenger.moveForward != 0.0) {
                if(passenger.moveForward > 0) {
                    dir = new Vec3d(0, passenger.getLookVec().y, (float) lookDir.length());
                } else {
                    dir = new Vec3d(0, -passenger.getLookVec().y, (float) -lookDir.length());
                }
            } else {
                dir = Vec3d.ZERO;
            }

            this.setAIMoveSpeed(0.1f);
            super.travel(dir);
        } else {
            super.travel(initialDir);
        }
    }


    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);

        if(passenger instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) passenger;
            passenger.setPosition(getPosX(), getPosY(), getPosZ());

            player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 5));
            player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 5));

        }
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        if(!world.isRemote) {
            if(player.getHeldItem(hand).getItem() == Items.GOLDEN_CARROT) {
                DolphinsFriends.LOGGER.debug("Interaction");
                NetworkHooks.openGui((ServerPlayerEntity) player, this, buffer -> buffer.writeInt(this.getEntityId()));

                return true;
            } else {
                player.startRiding(this);
                return true;
            }
        }

        return super.processInteract(player, hand);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new RidableDolphinContainer(id, inventory, getInventory());
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getTrueSource();
        return this.isBeingRidden() && entity != null && this.isRidingOrBeingRiddenBy(entity) ? false : super.attackEntityFrom(source, amount);
    }

    @Override
    public ITextComponent getDisplayName() {
        return TextComponentUtils.toTextComponent(() -> "Dolphin Inventory");
    }

}
