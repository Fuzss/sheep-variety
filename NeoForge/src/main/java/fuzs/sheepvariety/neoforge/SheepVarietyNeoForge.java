package fuzs.sheepvariety.neoforge;

import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.sheepvariety.common.SheepVariety;
import fuzs.sheepvariety.common.init.ModRegistry;
import net.neoforged.fml.common.Mod;

@Mod(SheepVariety.MOD_ID)
public class SheepVarietyNeoForge {

    public SheepVarietyNeoForge() {
        ModConstructor.construct(SheepVariety.MOD_ID, SheepVariety::new);
        DataProviderHelper.registerDataProviders(SheepVariety.MOD_ID, ModRegistry.REGISTRY_SET_BUILDER);
    }
}
