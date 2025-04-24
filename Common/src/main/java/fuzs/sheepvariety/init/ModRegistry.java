package fuzs.sheepvariety.init;

import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.network.v4.PlayerSet;
import fuzs.sheepvariety.SheepVariety;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariant;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ModRegistry {
    public static final ResourceKey<Registry<SheepVariant>> SHEEP_VARIANT_REGISTRY_KEY = ResourceKey.createRegistryKey(
            SheepVariety.id("sheep_variant"));
    /**
     * Using this for data generation generates nothing for some reason, so stick with the dedicated data provider for
     * now.
     */
    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder().add(
            SHEEP_VARIANT_REGISTRY_KEY,
            SheepVariants::bootstrap);

    public static final DataAttachmentType<Entity, Holder<SheepVariant>> SHEEP_VARIANT_ATTACHMENT_TYPE = DataAttachmentRegistry.<Holder<SheepVariant>>entityBuilder()
            .defaultValue((Entity entity) -> entity.getType() == EntityType.SHEEP, (RegistryAccess registries) -> {
                return registries.lookupOrThrow(SHEEP_VARIANT_REGISTRY_KEY).getOrThrow(SheepVariants.DEFAULT);
            })
            .persistent(SheepVariant.CODEC)
            .networkSynchronized(SheepVariant.STREAM_CODEC, PlayerSet::nearEntity)
            .build(SheepVariety.id("sheep_variant"));

    public static void bootstrap() {
        // NO-OP
    }
}
