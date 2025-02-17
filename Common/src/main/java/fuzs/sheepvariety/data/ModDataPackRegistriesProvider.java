package fuzs.sheepvariety.data;

import fuzs.puzzleslib.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.sheepvariety.init.ModRegistry;
import fuzs.sheepvariety.world.entity.animal.SheepVariants;

public class ModDataPackRegistriesProvider extends AbstractDatapackRegistriesProvider {

    public ModDataPackRegistriesProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBootstrap(RegistryBoostrapConsumer consumer) {
        consumer.add(ModRegistry.SHEEP_VARIANT_REGISTRY_KEY, SheepVariants::bootstrap);
    }
}
