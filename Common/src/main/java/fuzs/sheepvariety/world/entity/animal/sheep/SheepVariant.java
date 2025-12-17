package fuzs.sheepvariety.world.entity.animal.sheep;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.sheepvariety.init.ModRegistry;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

import java.util.List;
import java.util.Locale;

public record SheepVariant(AssetInfo assetInfo,
                           SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<SheepVariant> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    AssetInfo.CODEC.fieldOf("assets").forGetter(SheepVariant::assetInfo),
                    SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(SheepVariant::spawnConditions))
            .apply(instance, SheepVariant::new));
    public static final Codec<SheepVariant> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AssetInfo.CODEC.fieldOf("assets").forGetter(SheepVariant::assetInfo)).apply(instance, SheepVariant::new));
    public static final Codec<Holder<SheepVariant>> CODEC = RegistryFixedCodec.create(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<SheepVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(
            ModRegistry.SHEEP_VARIANT_REGISTRY_KEY);

    private SheepVariant(AssetInfo assetInfo) {
        this(assetInfo, SpawnPrioritySelectors.EMPTY);
    }

    @Override
    public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }

    public record AssetInfo(ModelType model,
                            ClientAsset.ResourceTexture asset,
                            ClientAsset.ResourceTexture wool,
                            ClientAsset.ResourceTexture undercoat) {
        public static final Codec<SheepVariant.AssetInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        SheepVariant.ModelType.CODEC.optionalFieldOf("model", SheepVariant.ModelType.NORMAL)
                                .forGetter(AssetInfo::model),
                        ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(SheepVariant.AssetInfo::asset),
                        ClientAsset.ResourceTexture.CODEC.fieldOf("wool_id").forGetter(SheepVariant.AssetInfo::wool),
                        ClientAsset.ResourceTexture.CODEC.fieldOf("undercoat_id").forGetter(SheepVariant.AssetInfo::undercoat))
                .apply(instance, SheepVariant.AssetInfo::new));
    }

    public enum ModelType implements StringRepresentable {
        NORMAL,
        COLD,
        WARM;

        public static final Codec<SheepVariant.ModelType> CODEC = StringRepresentable.fromEnum(SheepVariant.ModelType::values);

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
