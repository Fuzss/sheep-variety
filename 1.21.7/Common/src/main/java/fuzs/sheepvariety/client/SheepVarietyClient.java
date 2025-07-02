package fuzs.sheepvariety.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.EntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.sheepvariety.client.model.geom.ModModelLayers;
import fuzs.sheepvariety.client.renderer.entity.SheepVariantRenderer;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.world.entity.EntityType;

public class SheepVarietyClient implements ClientModConstructor {

    @Override
    public void onRegisterEntityRenderers(EntityRenderersContext context) {
        context.registerEntityRenderer(EntityType.SHEEP, SheepVariantRenderer::new);
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(ModModelLayers.WARM_SHEEP, SheepVariantRenderer::createWarmBodyLayer);
        context.registerLayerDefinition(ModModelLayers.WARM_SHEEP_BABY,
                () -> SheepVariantRenderer.createWarmBodyLayer().apply(SheepModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModModelLayers.WARM_SHEEP_WOOL, SheepFurModel::createFurLayer);
        context.registerLayerDefinition(ModModelLayers.WARM_SHEEP_BABY_WOOL,
                () -> SheepFurModel.createFurLayer().apply(SheepModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModModelLayers.WARM_SHEEP_WOOL_UNDERCOAT,
                SheepVariantRenderer::createWarmBodyLayer);
        context.registerLayerDefinition(ModModelLayers.WARM_SHEEP_BABY_WOOL_UNDERCOAT,
                () -> SheepVariantRenderer.createWarmBodyLayer().apply(SheepModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModModelLayers.COLD_SHEEP, SheepModel::createBodyLayer);
        context.registerLayerDefinition(ModModelLayers.COLD_SHEEP_BABY,
                () -> SheepModel.createBodyLayer().apply(SheepModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModModelLayers.COLD_SHEEP_WOOL, SheepVariantRenderer::createColdFurLayer);
        context.registerLayerDefinition(ModModelLayers.COLD_SHEEP_BABY_WOOL,
                () -> SheepVariantRenderer.createColdFurLayer().apply(SheepModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModModelLayers.COLD_SHEEP_WOOL_UNDERCOAT, SheepModel::createBodyLayer);
        context.registerLayerDefinition(ModModelLayers.COLD_SHEEP_BABY_WOOL_UNDERCOAT,
                () -> SheepModel.createBodyLayer().apply(SheepModel.BABY_TRANSFORMER));
    }
}
