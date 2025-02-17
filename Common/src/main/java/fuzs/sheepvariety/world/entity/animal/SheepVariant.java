package fuzs.sheepvariety.world.entity.animal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.sheepvariety.init.ModRegistry;
import fuzs.sheepvariety.world.entity.variant.*;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public record SheepVariant(ModelAndTexture<ModelType> modelAndTexture,
                           SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, BiomeCheck> {
    public static final Codec<SheepVariant> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ModelAndTexture.codec(SheepVariant.ModelType.CODEC, SheepVariant.ModelType.NORMAL)
                            .forGetter(SheepVariant::modelAndTexture),
                    SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(SheepVariant::spawnConditions))
            .apply(instance, SheepVariant::new));
    public static final Codec<SheepVariant> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ModelAndTexture.codec(SheepVariant.ModelType.CODEC, SheepVariant.ModelType.NORMAL)
                    .forGetter(SheepVariant::modelAndTexture)).apply(instance, SheepVariant::new));
    public static final Codec<Holder<SheepVariant>> CODEC = RegistryFixedCodec.create(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<SheepVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(
            ModRegistry.SHEEP_VARIANT_REGISTRY_KEY);

    private SheepVariant(ModelAndTexture<SheepVariant.ModelType> modelAndTexture) {
        this(modelAndTexture, SpawnPrioritySelectors.EMPTY);
    }

    @Override
    public List<Selector<SpawnContext, BiomeCheck>> selectors() {
        return this.spawnConditions.selectors();
    }

    public enum ModelType implements StringRepresentable {
        NORMAL("normal"),
        COLD("cold"),
        WARM("warm");

        public static final Codec<SheepVariant.ModelType> CODEC = StringRepresentable.fromEnum(SheepVariant.ModelType::values);
        private final String name;

        ModelType(final String string2) {
            this.name = string2;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
