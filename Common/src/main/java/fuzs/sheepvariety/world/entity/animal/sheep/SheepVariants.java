package fuzs.sheepvariety.world.entity.animal.sheep;

import fuzs.sheepvariety.SheepVariety;
import fuzs.sheepvariety.init.ModRegistry;
import fuzs.sheepvariety.world.entity.animal.TemperatureVariants;
import fuzs.sheepvariety.world.entity.variant.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public class SheepVariants {
    public static final ResourceKey<SheepVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
    public static final ResourceKey<SheepVariant> WARM = createKey(TemperatureVariants.WARM);
    public static final ResourceKey<SheepVariant> COLD = createKey(TemperatureVariants.COLD);
    public static final ResourceKey<SheepVariant> DEFAULT = TEMPERATE;

    private static ResourceKey<SheepVariant> createKey(ResourceLocation resourceLocation) {
        return ResourceKey.create(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY, resourceLocation);
    }

    public static void bootstrap(BootstrapContext<SheepVariant> bootstrapContext) {
        register(bootstrapContext,
                TEMPERATE,
                SheepVariant.ModelType.NORMAL,
                "temperate_sheep",
                SpawnPrioritySelectors.fallback(0));
        register(bootstrapContext,
                WARM,
                SheepVariant.ModelType.WARM,
                "warm_sheep",
                ModRegistry.SPAWNS_WARM_VARIANT_FARM_ANIMALS_BIOME_TAG);
        register(bootstrapContext,
                COLD,
                SheepVariant.ModelType.COLD,
                "cold_sheep",
                ModRegistry.SPAWNS_COLD_VARIANT_FARM_ANIMALS_BIOME_TAG);
    }

    private static void register(BootstrapContext<SheepVariant> bootstrapContext, ResourceKey<SheepVariant> resourceKey, SheepVariant.ModelType modelType, String string, TagKey<Biome> tagKey) {
        HolderSet<Biome> holderSet = bootstrapContext.lookup(Registries.BIOME).getOrThrow(tagKey);
        register(bootstrapContext,
                resourceKey,
                modelType,
                string,
                SpawnPrioritySelectors.single(new BiomeCheck(holderSet), 1));
    }

    private static void register(BootstrapContext<SheepVariant> bootstrapContext, ResourceKey<SheepVariant> resourceKey, SheepVariant.ModelType modelType, String string, SpawnPrioritySelectors spawnPrioritySelectors) {
        ResourceLocation resourceLocation = SheepVariety.id("entity/sheep/" + string);
        bootstrapContext.register(resourceKey,
                new SheepVariant(new ModelAndTexture<>(modelType, resourceLocation), spawnPrioritySelectors));
    }

    public static Optional<Reference<SheepVariant>> selectVariantToSpawn(RandomSource randomSource, RegistryAccess registryAccess, SpawnContext spawnContext) {
        return PriorityProvider.pick(registryAccess.lookupOrThrow(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY)
                .listElements(), Holder::value, randomSource, spawnContext);
    }
}
