package fuzs.sheepvariety.fabric.client;

import fuzs.sheepvariety.SheepVariety;
import fuzs.sheepvariety.client.SheepVarietyClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class SheepVarietyFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(SheepVariety.MOD_ID, SheepVarietyClient::new);
    }
}
