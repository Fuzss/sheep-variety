package fuzs.sheepvariety.common.client.renderer.entity.state;

import fuzs.sheepvariety.common.world.entity.animal.sheep.SheepVariant;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import org.jspecify.annotations.Nullable;

public class SheepVariantRenderState extends SheepRenderState {
    @Nullable
    public SheepVariant variant;
}
