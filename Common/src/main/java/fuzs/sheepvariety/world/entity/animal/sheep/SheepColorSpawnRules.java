package fuzs.sheepvariety.world.entity.animal.sheep;

import fuzs.sheepvariety.init.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.biome.Biome;

public class SheepColorSpawnRules {
    private static final SheepColorSpawnRules.SheepColorSpawnConfiguration TEMPERATE_SPAWN_CONFIGURATION = new SheepColorSpawnRules.SheepColorSpawnConfiguration(
            weighted(builder().add(single(DyeColor.BLACK), 5)
                    .add(single(DyeColor.GRAY), 5)
                    .add(single(DyeColor.LIGHT_GRAY), 5)
                    .add(single(DyeColor.BROWN), 3)
                    .add(commonColors(DyeColor.WHITE), 82)
                    .build()));
    private static final SheepColorSpawnRules.SheepColorSpawnConfiguration WARM_SPAWN_CONFIGURATION = new SheepColorSpawnRules.SheepColorSpawnConfiguration(
            weighted(builder().add(single(DyeColor.WHITE), 5)
                    .add(single(DyeColor.GRAY), 5)
                    .add(single(DyeColor.LIGHT_GRAY), 5)
                    .add(single(DyeColor.BLACK), 3)
                    .add(commonColors(DyeColor.BROWN), 82)
                    .build()));
    private static final SheepColorSpawnRules.SheepColorSpawnConfiguration COLD_SPAWN_CONFIGURATION = new SheepColorSpawnRules.SheepColorSpawnConfiguration(
            weighted(builder().add(single(DyeColor.BROWN), 5)
                    .add(single(DyeColor.GRAY), 5)
                    .add(single(DyeColor.LIGHT_GRAY), 5)
                    .add(single(DyeColor.WHITE), 3)
                    .add(commonColors(DyeColor.BLACK), 82)
                    .build()));

    private static SheepColorSpawnRules.SheepColorProvider commonColors(DyeColor dyeColor) {
        return weighted(builder().add(single(dyeColor), 499).add(single(DyeColor.PINK), 1).build());
    }

    public static DyeColor getSheepColor(Holder<Biome> holder, RandomSource randomSource) {
        SheepColorSpawnRules.SheepColorSpawnConfiguration sheepColorSpawnConfiguration = getSheepColorConfiguration(
                holder);
        return sheepColorSpawnConfiguration.colors().get(randomSource);
    }

    private static SheepColorSpawnRules.SheepColorSpawnConfiguration getSheepColorConfiguration(Holder<Biome> holder) {
        if (holder.is(ModRegistry.SPAWNS_WARM_VARIANT_FARM_ANIMALS_BIOME_TAG)) {
            return WARM_SPAWN_CONFIGURATION;
        } else {
            return holder.is(ModRegistry.SPAWNS_COLD_VARIANT_FARM_ANIMALS_BIOME_TAG) ? COLD_SPAWN_CONFIGURATION :
                    TEMPERATE_SPAWN_CONFIGURATION;
        }
    }

    private static SheepColorSpawnRules.SheepColorProvider weighted(SimpleWeightedRandomList<SheepColorProvider> weightedList) {
        if (weightedList.isEmpty()) {
            throw new IllegalArgumentException("List must be non-empty");
        } else {
            return randomSource -> weightedList.getRandomValue(randomSource).orElseThrow().get(randomSource);
        }
    }

    private static SheepColorSpawnRules.SheepColorProvider single(DyeColor dyeColor) {
        return randomSource -> dyeColor;
    }

    private static SimpleWeightedRandomList.Builder<SheepColorSpawnRules.SheepColorProvider> builder() {
        return SimpleWeightedRandomList.builder();
    }

    @FunctionalInterface
    interface SheepColorProvider {
        DyeColor get(RandomSource randomSource);
    }

    static record SheepColorSpawnConfiguration(SheepColorSpawnRules.SheepColorProvider colors) {
    }
}
