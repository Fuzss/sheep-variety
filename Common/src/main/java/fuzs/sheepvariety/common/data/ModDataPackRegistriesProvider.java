package fuzs.sheepvariety.common.data;

import fuzs.puzzleslib.common.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.sheepvariety.common.init.ModRegistry;
import fuzs.sheepvariety.common.world.entity.animal.sheep.SheepVariants;

public class ModDataPackRegistriesProvider extends AbstractDatapackRegistriesProvider {

    public ModDataPackRegistriesProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBootstrap(RegistryBoostrapConsumer consumer) {
        consumer.add(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY, SheepVariants::bootstrap);
    }
}
