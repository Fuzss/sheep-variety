package fuzs.sheepvariety.common;

import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.common.api.core.v1.context.DataPackRegistriesContext;
import fuzs.puzzleslib.common.api.event.v1.entity.ServerEntityEvents;
import fuzs.puzzleslib.common.api.event.v1.entity.living.BabyEntitySpawnCallback;
import fuzs.puzzleslib.common.api.event.v1.entity.player.PlayerInteractEvents;
import fuzs.sheepvariety.common.handler.SheepSpawnVariantHandler;
import fuzs.sheepvariety.common.init.ModRegistry;
import fuzs.sheepvariety.common.world.entity.animal.sheep.SheepVariant;
import net.minecraft.resources.Identifier;
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
        ServerEntityEvents.LOAD.register(SheepSpawnVariantHandler::onEntityLoad);
        BabyEntitySpawnCallback.EVENT.register(SheepSpawnVariantHandler::onBabyEntitySpawn);
        PlayerInteractEvents.USE_ENTITY.register(SheepSpawnVariantHandler::onUseEntity);
    }

    @Override
    public void onRegisterDataPackRegistries(DataPackRegistriesContext context) {
        context.registerSyncedRegistry(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY,
                SheepVariant.DIRECT_CODEC,
                SheepVariant.NETWORK_CODEC);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
