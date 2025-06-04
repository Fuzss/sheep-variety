package fuzs.sheepvariety.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import fuzs.sheepvariety.init.ModRegistry;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariant;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariants;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SheepSpawnVariantHandler {

    public static EventResult onEntitySpawn(Entity entity, ServerLevel serverLevel, @Nullable EntitySpawnReason entitySpawnReason) {
        if (entitySpawnReason != null && entity instanceof Sheep) {
            SheepVariants.selectVariantToSpawn(serverLevel.random,
                            serverLevel.registryAccess(),
                            SpawnContext.create(serverLevel, entity.blockPosition()))
                    .ifPresent((Holder.Reference<SheepVariant> holder) -> {
                        ModRegistry.SHEEP_VARIANT_ATTACHMENT_TYPE.set(entity, holder);
                    });
        }
        return EventResult.PASS;
    }

    public static EventResult onBabyEntitySpawn(Mob partnerMob, Mob otherPartnerMob, MutableValue<AgeableMob> childMob) {
        if (childMob.get() instanceof Sheep sheep) {
            Holder<SheepVariant> holder = getSheepVariant(
                    sheep.getRandom().nextBoolean() ? partnerMob : otherPartnerMob);
            ModRegistry.SHEEP_VARIANT_ATTACHMENT_TYPE.set(sheep, holder);
        }

        return EventResult.PASS;
    }

    public static Holder<SheepVariant> getSheepVariant(Mob mob) {
        if (ModRegistry.SHEEP_VARIANT_ATTACHMENT_TYPE.has(mob)) {
            return ModRegistry.SHEEP_VARIANT_ATTACHMENT_TYPE.get(mob);
        } else {
            return mob.registryAccess()
                    .lookupOrThrow(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY)
                    .getOrThrow(SheepVariants.DEFAULT);
        }
    }

    public static EventResultHolder<InteractionResult> onUseEntity(Player player, Level level, InteractionHand interactionHand, Entity entity) {
        if (level instanceof ServerLevel serverLevel && entity instanceof Sheep sheep) {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (itemInHand.getItem() instanceof SpawnEggItem item) {
                Optional<Mob> optional = item.spawnOffspringFromSpawnEgg(player,
                        sheep,
                        (EntityType<? extends Mob>) item.getType(serverLevel.registryAccess(), itemInHand),
                        serverLevel,
                        sheep.position(),
                        itemInHand);
                if (optional.isPresent()) {
                    Holder<SheepVariant> holder = getSheepVariant(sheep);
                    if (holder != null) {
                        ModRegistry.SHEEP_VARIANT_ATTACHMENT_TYPE.set(optional.get(), holder);
                    }
                    return EventResultHolder.interrupt(InteractionResult.SUCCESS_SERVER);
                }
            }
        }

        return EventResultHolder.pass();
    }
}
