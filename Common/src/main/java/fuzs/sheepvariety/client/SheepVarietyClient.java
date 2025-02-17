package fuzs.sheepvariety.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.EntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.sheepvariety.client.model.geom.ModModelLayers;
import fuzs.sheepvariety.client.renderer.entity.SheepVariantRenderer;
import fuzs.sheepvariety.client.renderer.entity.layers.SheepHornsLayer;
import fuzs.sheepvariety.client.renderer.entity.layers.SheepLongWoolLayer;
import fuzs.sheepvariety.client.renderer.entity.layers.SheepVariantWoolLayer;
import net.minecraft.client.model.SheepModel;
import net.minecraft.world.entity.EntityType;

public class SheepVarietyClient implements ClientModConstructor {

    @Override
    public void onRegisterEntityRenderers(EntityRenderersContext context) {
        context.registerEntityRenderer(EntityType.SHEEP, SheepVariantRenderer::new);
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(ModModelLayers.SHEEP_WOOL, SheepVariantWoolLayer::createFurLayer);
        context.registerLayerDefinition(ModModelLayers.SHEEP_BABY_WOOL,
                () -> SheepVariantWoolLayer.createFurLayer().apply(SheepModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModModelLayers.SHEEP_HORNS, SheepHornsLayer::createBodyLayer);
        context.registerLayerDefinition(ModModelLayers.SHEEP_BABY_HORNS,
                () -> SheepHornsLayer.createBodyLayer().apply(SheepModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModModelLayers.SHEEP_LONG_WOOL, SheepLongWoolLayer::createFurLayer);
        context.registerLayerDefinition(ModModelLayers.SHEEP_BABY_LONG_WOOL,
                () -> SheepLongWoolLayer.createFurLayer().apply(SheepModel.BABY_TRANSFORMER));
    }
}
