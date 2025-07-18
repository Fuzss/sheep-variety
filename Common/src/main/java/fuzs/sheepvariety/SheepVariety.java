package fuzs.sheepvariety;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.DataPackRegistriesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.ServerEntityLevelEvents;
import fuzs.puzzleslib.api.event.v1.entity.living.BabyEntitySpawnCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import fuzs.sheepvariety.handler.SheepSpawnVariantHandler;
import fuzs.sheepvariety.init.ModRegistry;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariant;
import net.minecraft.resources.ResourceLocation;
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
        ServerEntityLevelEvents.LOAD.register(SheepSpawnVariantHandler::onEntitySpawn);
        BabyEntitySpawnCallback.EVENT.register(SheepSpawnVariantHandler::onBabyEntitySpawn);
        PlayerInteractEvents.USE_ENTITY.register(SheepSpawnVariantHandler::onUseEntity);
    }

    @Override
    public void onRegisterDataPackRegistries(DataPackRegistriesContext context) {
        context.registerSyncedRegistry(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY,
                SheepVariant.DIRECT_CODEC,
                SheepVariant.NETWORK_CODEC);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
