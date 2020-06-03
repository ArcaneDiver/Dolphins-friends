package com.arcanediver.dolphins_friends.entity;

import com.arcanediver.dolphins_friends.DolphinsFriends;
import com.arcanediver.dolphins_friends.inventory.container.RidableDolphinContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.coremod.api.ASMAPI;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.List;
import java.util.UUID;

public class RidableDolphinEnitity extends DolphinEntity implements INamedContainerProvider {

    @CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    protected IItemHandler inventory = new ItemStackHandler();
    private boolean tamed = true;

    @Nullable
    private UUID owner = null;

    public RidableDolphinEnitity(EntityType<RidableDolphinEnitity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        compound.put("inventory", inventory.getStackInSlot(0).write(new CompoundNBT()));
        compound.putBoolean("tamed", tamed);

        if(owner != null) {
            compound.putUniqueId("owner", owner);
        }

    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);

        this.inventory.insertItem(0, ItemStack.read(compound.getCompound("inventory")), false);

        this.tamed = compound.getBoolean("tamed");

        if(compound.contains("owner")) {
            owner = compound.getUniqueId("owner");
        }
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();

        this.entityDropItem(inventory.getStackInSlot(0));
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
            if(player.isShiftKeyDown()) { // TEST
                NetworkHooks.openGui((ServerPlayerEntity) player, this, buffer -> buffer.writeInt(this.getEntityId()));
            } else {
                if(this.isTame()) {
                    player.startRiding(this);
                } else {
                    ItemStack itemStack = player.getHeldItem(hand);

                    if(itemStack.getItem() == Items.SALMON || itemStack.getItem() == Items.COOKED_SALMON) {
                        this.setOwner(player.getUniqueID());
                        this.tamed = true;

                        this.spawnTamedParticles();
                    }
                }
            }

            return true;
        }

        return super.processInteract(player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnTamedParticles() {
        IParticleData particleData = ParticleTypes.HEART;

        for(int i = 0; i < 7; i++) {
            double xSpeed = this.rand.nextGaussian() * 0.02D;
            double ySpeed = this.rand.nextGaussian() * 0.02D;
            double zSpeed = this.rand.nextGaussian() * 0.02D;

            this.world.addParticle(
                    particleData,
                    this.getPosXRandom(1.0D),
                    this.getPosYRandom() + 0.5D,
                    this.getPosZRandom(1.0D),
                    xSpeed,
                    ySpeed,
                    zSpeed
            );
        }
    }

    protected void handleEating(PlayerEntity player, Item item) {
        DolphinsFriends.LOGGER.info("eating");
    }

    public boolean isTame() {
        return this.tamed;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
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
