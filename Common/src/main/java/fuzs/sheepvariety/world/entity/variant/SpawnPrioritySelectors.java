package fuzs.sheepvariety.world.entity.variant;

import com.mojang.serialization.Codec;

import java.util.List;

public record SpawnPrioritySelectors(List<PriorityProvider.Selector<SpawnContext, BiomeCheck>> selectors) {
    public static final SpawnPrioritySelectors EMPTY = new SpawnPrioritySelectors(List.of());
    public static final Codec<SpawnPrioritySelectors> CODEC = PriorityProvider.Selector.codec(BiomeCheck.MAP_CODEC.codec())
            .listOf()
            .xmap(SpawnPrioritySelectors::new, SpawnPrioritySelectors::selectors);

    public static SpawnPrioritySelectors single(BiomeCheck spawnCondition, int i) {
        return new SpawnPrioritySelectors(PriorityProvider.single(spawnCondition, i));
    }

    public static SpawnPrioritySelectors fallback(int i) {
        return new SpawnPrioritySelectors(PriorityProvider.alwaysTrue(i));
    }
}
