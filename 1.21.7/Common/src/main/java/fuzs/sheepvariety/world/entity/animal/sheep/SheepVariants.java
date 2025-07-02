package fuzs.sheepvariety.world.entity.animal.sheep;

import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.sheepvariety.SheepVariety;
import fuzs.sheepvariety.init.ModRegistry;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.entity.variant.BiomeCheck;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
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
                ResourceLocationHelper.withDefaultNamespace("sheep"),
                SpawnPrioritySelectors.fallback(0));
        register(bootstrapContext,
                WARM,
                SheepVariant.ModelType.WARM,
                SheepVariety.id("warm_sheep"),
                BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
        register(bootstrapContext,
                COLD,
                SheepVariant.ModelType.COLD,
                SheepVariety.id("cold_sheep"),
                BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
    }

    private static void register(BootstrapContext<SheepVariant> bootstrapContext, ResourceKey<SheepVariant> resourceKey, SheepVariant.ModelType modelType, ResourceLocation resourceLocation, TagKey<Biome> tagKey) {
        HolderSet<Biome> holderSet = bootstrapContext.lookup(Registries.BIOME).getOrThrow(tagKey);
        register(bootstrapContext,
                resourceKey,
                modelType,
                resourceLocation,
                SpawnPrioritySelectors.single(new BiomeCheck(holderSet), 1));
    }

    private static void register(BootstrapContext<SheepVariant> bootstrapContext, ResourceKey<SheepVariant> resourceKey, SheepVariant.ModelType modelType, ResourceLocation resourceLocation, SpawnPrioritySelectors spawnPrioritySelectors) {
        ClientAsset assetId = new ClientAsset(resourceLocation.withPath((String s) -> "entity/sheep/" + s));
        ClientAsset woolId = new ClientAsset(resourceLocation.withPath((String s) -> "entity/sheep/" + s + "_wool"));
        ClientAsset undercoatId = new ClientAsset(resourceLocation.withPath((String s) -> "entity/sheep/" + s +
                "_wool_undercoat"));
        bootstrapContext.register(resourceKey,
                new SheepVariant(new SheepVariant.AssetInfo(modelType, assetId, woolId, undercoatId),
                        spawnPrioritySelectors));
    }

    public static Optional<Reference<SheepVariant>> selectVariantToSpawn(RandomSource randomSource, RegistryAccess registryAccess, SpawnContext spawnContext) {
        return PriorityProvider.pick(registryAccess.lookupOrThrow(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY)
                .listElements(), Holder::value, randomSource, spawnContext);
    }
}
