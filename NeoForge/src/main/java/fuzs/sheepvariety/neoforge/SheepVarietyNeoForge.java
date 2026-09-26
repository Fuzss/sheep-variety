package fuzs.sheepvariety.neoforge;

import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v3.core.DataProviderBuilder;
import fuzs.sheepvariety.common.SheepVariety;
import fuzs.sheepvariety.common.init.ModRegistry;
import fuzs.sheepvariety.common.world.entity.animal.sheep.SheepVariants;
import net.neoforged.fml.common.Mod;

@Mod(SheepVariety.MOD_ID)
public class SheepVarietyNeoForge {

    public SheepVarietyNeoForge() {
        ModConstructor.construct(SheepVariety.MOD_ID, SheepVariety::new);
        DataProviderBuilder.of(SheepVariety.MOD_ID)
                .addWorldBootstrap(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY, SheepVariants::bootstrap);
    }
}
