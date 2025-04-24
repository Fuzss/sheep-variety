package fuzs.sheepvariety.init;

import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import fuzs.puzzleslib.api.network.v3.PlayerSet;
import fuzs.sheepvariety.SheepVariety;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariant;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;

public class ModRegistry {
    public static final ResourceKey<Registry<SheepVariant>> SHEEP_VARIANT_REGISTRY_KEY = ResourceKey.createRegistryKey(
            SheepVariety.id("sheep_variant"));

    static final TagFactory TAGS = TagFactory.make(SheepVariety.MOD_ID);
    public static final TagKey<Biome> SPAWNS_COLD_VARIANT_FARM_ANIMALS_BIOME_TAG = TAGS.registerBiomeTag(
            "spawns_cold_variant_farm_animals");
    public static final TagKey<Biome> SPAWNS_WARM_VARIANT_FARM_ANIMALS_BIOME_TAG = TAGS.registerBiomeTag(
            "spawns_warm_variant_farm_animals");

    public static final DataAttachmentType<Entity, Holder<SheepVariant>> SHEEP_VARIANT_ATTACHMENT_TYPE = DataAttachmentRegistry.<Holder<SheepVariant>>entityBuilder()
            .defaultValue((Entity entity) -> entity.getType() == EntityType.SHEEP, (RegistryAccess registries) -> {
                return registries.lookupOrThrow(SHEEP_VARIANT_REGISTRY_KEY).getOrThrow(SheepVariants.DEFAULT);
            })
            .persistent(SheepVariant.CODEC)
            .networkSynchronized(SheepVariant.STREAM_CODEC, PlayerSet::nearEntity)
            .build(SheepVariety.id("variant"));

    public static void bootstrap() {
        // NO-OP
    }
}
