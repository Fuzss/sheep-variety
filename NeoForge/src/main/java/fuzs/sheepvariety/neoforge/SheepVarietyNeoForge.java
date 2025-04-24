package fuzs.sheepvariety.neoforge;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.sheepvariety.SheepVariety;
import fuzs.sheepvariety.data.ModDataPackRegistriesProvider;
import net.neoforged.fml.common.Mod;

@Mod(SheepVariety.MOD_ID)
public class SheepVarietyNeoForge {

    public SheepVarietyNeoForge() {
        ModConstructor.construct(SheepVariety.MOD_ID, SheepVariety::new);
        DataProviderHelper.registerDataProviders(SheepVariety.MOD_ID, ModDataPackRegistriesProvider::new);
    }
}
