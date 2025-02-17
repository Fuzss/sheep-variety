package fuzs.sheepvariety;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.DataPackRegistriesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.entity.ServerEntityLevelEvents;
import fuzs.sheepvariety.init.ModRegistry;
import fuzs.sheepvariety.world.entity.animal.SheepVariant;
import fuzs.sheepvariety.world.entity.animal.SheepVariants;
import fuzs.sheepvariety.world.entity.variant.SpawnContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.animal.Sheep;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SheepVariety implements ModConstructor {
    public static final String MOD_ID = "sheepvariety";
    public static final String MOD_NAME = "Sheep Variety";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ServerEntityLevelEvents.SPAWN.register((Entity entity, ServerLevel serverLevel, @Nullable EntitySpawnReason entitySpawnReason) -> {
            if (entitySpawnReason != null && entity instanceof Sheep) {
                SheepVariants.selectVariantToSpawn(serverLevel.random,
                                serverLevel.registryAccess(),
                                SpawnContext.create(serverLevel, entity.blockPosition()))
                        .ifPresent((Holder.Reference<SheepVariant> holder) -> {
                            ModRegistry.SHEEP_VARIANT_ATTACHMENT_TYPE.set(entity, holder);
                        });
            }
            return EventResult.PASS;
        });
    }

    @Override
    public void onDataPackRegistriesContext(DataPackRegistriesContext context) {
        context.registerSyncedRegistry(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY, SheepVariant.DIRECT_CODEC);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
