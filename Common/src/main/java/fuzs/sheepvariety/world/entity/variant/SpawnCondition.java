package fuzs.sheepvariety.world.entity.variant;

import com.mojang.serialization.MapCodec;

public interface SpawnCondition extends PriorityProvider.SelectorCondition<SpawnContext> {

    MapCodec<? extends SpawnCondition> codec();
}
