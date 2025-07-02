package fuzs.sheepvariety.fabric;

import fuzs.sheepvariety.SheepVariety;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class SheepVarietyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(SheepVariety.MOD_ID, SheepVariety::new);
    }
}
