package fuzs.sheepvariety.neoforge.client;

import fuzs.sheepvariety.SheepVariety;
import fuzs.sheepvariety.client.SheepVarietyClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = SheepVariety.MOD_ID, dist = Dist.CLIENT)
public class SheepVarietyNeoForgeClient {

    public SheepVarietyNeoForgeClient() {
        ClientModConstructor.construct(SheepVariety.MOD_ID, SheepVarietyClient::new);
    }
}
