package com.cutepigeons.entity;

import com.cutepigeons.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class CutePigeonEntity extends TamableAnimal {
    private int flapTimer;
    private int perchCooldown;

    public CutePigeonEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 18, true);
        this.setNoGravity(true);
    }

    public static boolean canSpawn(EntityType<CutePigeonEntity> type, Level level, net.minecraft.world.entity.MobSpawnType reason, BlockPos pos, net.minecraft.util.RandomSource random) {
        return level.getBlockState(pos.below()).isSolid() && pos.getY() > level.getMinBuildHeight() + 5 && pos.getY() < 190;
    }

    @Override protected void registerGoals() {
        this.goalSelector.addGoal(1, new PigeonFollowOwnerGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.15D, Items.WHEAT_SEEDS, false));
        this.goalSelector.addGoal(3, new PigeonFlockGoal(this));
        this.goalSelector.addGoal(4, new PigeonWanderGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 7.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override public boolean isFood(ItemStack stack) { return stack.is(Items.WHEAT_SEEDS) || stack.is(Items.BEETROOT_SEEDS) || stack.is(Items.PUMPKIN_SEEDS) || stack.is(Items.MELON_SEEDS); }

    @Override public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isFood(stack) && !isTame()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (!level().isClientSide) {
                if (random.nextFloat() < 0.35F) {
                    tame(player);
                    level().broadcastEntityEvent(this, (byte)7);
                } else level().broadcastEntityEvent(this, (byte)6);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (isFood(stack) && isTame()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            heal(2.0F);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override public void aiStep() {
        super.aiStep();
        flapTimer++;
        if (onGround()) setDeltaMovement(getDeltaMovement().add(0, 0.08, 0));
        if (getY() < level().getMinBuildHeight() + 2) setPos(getX(), level().getMinBuildHeight() + 2, getZ());
    }

    @Override public void addAdditionalSaveData(CompoundTag tag) { super.addAdditionalSaveData(tag); }
    @Override public void readAdditionalSaveData(CompoundTag tag) { super.readAdditionalSaveData(tag); }

    public float wingFlap(float partialTicks) { return (float)Math.sin((flapTimer + partialTicks) * 0.55F) * 0.35F; }

    @Override public boolean causeFallDamage(float distance, float multiplier, net.minecraft.world.damagesource.DamageSource source) { return false; }

    @Override public boolean isInvulnerableTo(net.minecraft.world.damagesource.DamageSource source) {
        return super.isInvulnerableTo(source);
    }

    static class PigeonWanderGoal extends Goal {
        private final CutePigeonEntity pigeon;
        private int cooldown;
        PigeonWanderGoal(CutePigeonEntity pigeon) { this.pigeon = pigeon; this.setFlags(EnumSet.of(Flag.MOVE)); }
        @Override public boolean canUse() { return !pigeon.isOrderedToSit() && cooldown-- <= 0; }
        @Override public boolean canContinueToUse() { return pigeon.getNavigation().isInProgress(); }
        @Override public void start() {
            cooldown = 30 + pigeon.random.nextInt(80);
            Vec3 p = pigeon.position().add(pigeon.random.nextInt(17) - 8, pigeon.random.nextInt(9) - 3, pigeon.random.nextInt(17) - 8);
            if (p.getY() < 2) p = p.add(0, 4, 0);
            pigeon.getMoveControl().setWantedPosition(p.x, p.y, p.z, 0.8D);
        }
    }

    static class PigeonFlockGoal extends Goal {
        private final CutePigeonEntity pigeon;
        PigeonFlockGoal(CutePigeonEntity pigeon) { this.pigeon = pigeon; this.setFlags(EnumSet.of(Flag.MOVE)); }
        @Override public boolean canUse() { return !pigeon.isOrderedToSit() && pigeon.getRandom().nextInt(35) == 0; }
        @Override public boolean canContinueToUse() { return false; }
        @Override public void start() {
            List<CutePigeonEntity> nearby = pigeon.level().getEntitiesOfClass(CutePigeonEntity.class, pigeon.getBoundingBox().inflate(12));
            CutePigeonEntity leader = null;
            double best = Double.MAX_VALUE;
            for (CutePigeonEntity other : nearby) if (other != pigeon) {
                double d = pigeon.distanceToSqr(other);
                if (d < best) { best = d; leader = other; }
            }
            if (leader != null) {
                Vec3 target = leader.position().add(pigeon.random.nextInt(7)-3, pigeon.random.nextInt(5)-2, pigeon.random.nextInt(7)-3);
                pigeon.getMoveControl().setWantedPosition(target.x, target.y, target.z, 1.0D);
            }
        }
    }

    static class PigeonFollowOwnerGoal extends Goal {
        private final CutePigeonEntity pigeon;
        PigeonFollowOwnerGoal(CutePigeonEntity pigeon) { this.pigeon = pigeon; this.setFlags(EnumSet.of(Flag.MOVE)); }
        @Override public boolean canUse() { return pigeon.isTame() && pigeon.getOwner() != null && pigeon.distanceToSqr(pigeon.getOwner()) > 36; }
        @Override public boolean canContinueToUse() { return canUse(); }
        @Override public void tick() {
            Entity owner = pigeon.getOwner();
            if (owner != null) {
                Vec3 target = owner.position().add(0, 1.5, 0);
                pigeon.getMoveControl().setWantedPosition(target.x, target.y, target.z, 1.15D);
            }
        }
    }
}
