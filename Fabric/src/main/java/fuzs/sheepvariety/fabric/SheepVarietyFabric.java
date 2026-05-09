package fuzs.sheepvariety.fabric;

import fuzs.sheepvariety.common.SheepVariety;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class SheepVarietyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(SheepVariety.MOD_ID, SheepVariety::new);
    }
}
