package com.arcanediver.dolphins_friends.entity;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.inventory.container.RidableDolphinContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.List;

public class RidableDolphinEnitity extends DolphinEntity implements INamedContainerProvider {

    @CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    protected IItemHandler inventory = new ItemStackHandler();

    public RidableDolphinEnitity(EntityType<RidableDolphinEnitity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        // I dont won't AI
    }


    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);


        compound.put("inventory", inventory.getStackInSlot(0).write(new CompoundNBT()));
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);


        this.inventory.insertItem(0, ItemStack.read(compound.getCompound("inventory")), false);

    }

    @Override
    protected void dropInventory() {
        super.dropInventory();

        this.entityDropItem(inventory.getStackInSlot(0));
    }

    @Override
    public void travel(Vec3d initialDir) {
        if(this.isBeingRidden()) {
            this.setNoAI(true);


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
            this.setNoAI(false);
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
            if(player.getHeldItem(hand).getItem() == Items.GOLDEN_CARROT) { // TEST
                NetworkHooks.openGui((ServerPlayerEntity) player, this, buffer -> buffer.writeInt(this.getEntityId()));

                return true;
            } else {
                player.startRiding(this);
                return true;
            }
        }

        return super.processInteract(player, hand);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if(cap == ITEM_HANDLER_CAPABILITY) {
            return ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() ->inventory));
        } else {
            return super.getCapability(cap);
        }
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new RidableDolphinContainer(id, inventory, this.inventory, this.getEntityId());
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
