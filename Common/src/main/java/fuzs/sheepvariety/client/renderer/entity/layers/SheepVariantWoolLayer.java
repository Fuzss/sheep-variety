package fuzs.sheepvariety.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sheepvariety.client.model.geom.ModModelLayers;
import fuzs.sheepvariety.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariant;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import java.util.Map;

/**
 * @see net.minecraft.client.renderer.entity.layers.SheepWoolLayer
 */
public class SheepVariantWoolLayer extends RenderLayer<SheepRenderState, SheepModel> {
    private final Map<SheepVariant.ModelType, AdultAndBabyModelPair<SheepFurModel>> models;

    public SheepVariantWoolLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.models = bakeModels(context);
    }

    private static Map<SheepVariant.ModelType, AdultAndBabyModelPair<SheepFurModel>> bakeModels(EntityRendererProvider.Context context) {
        return Maps.newEnumMap(Map.of(SheepVariant.ModelType.NORMAL,
                new AdultAndBabyModelPair<>(new SheepFurModel(context.bakeLayer(ModelLayers.SHEEP_WOOL)),
                        new SheepFurModel(context.bakeLayer(ModelLayers.SHEEP_BABY_WOOL))),
                SheepVariant.ModelType.WARM,
                new AdultAndBabyModelPair<>(new SheepFurModel(context.bakeLayer(ModModelLayers.WARM_SHEEP_WOOL)),
                        new SheepFurModel(context.bakeLayer(ModModelLayers.WARM_SHEEP_BABY_WOOL))),
                SheepVariant.ModelType.COLD,
                new AdultAndBabyModelPair<>(new SheepFurModel(context.bakeLayer(ModModelLayers.COLD_SHEEP_WOOL)),
                        new SheepFurModel(context.bakeLayer(ModModelLayers.COLD_SHEEP_BABY_WOOL)))));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, SheepRenderState sheepRenderState, float yRot, float xRot) {
        if (!sheepRenderState.isSheared) {
            EntityModel<SheepRenderState> entityModel = this.models.get(((SheepVariantRenderState) sheepRenderState).variant.assetInfo()
                    .model()).getModel(sheepRenderState.isBaby);
            if (sheepRenderState.isInvisible) {
                if (sheepRenderState.appearsGlowing()) {
                    RenderType renderType = RenderTypes.outline(((SheepVariantRenderState) sheepRenderState).variant.assetInfo()
                            .wool()
                            .texturePath());
                    submitNodeCollector.submitModel(entityModel,
                            sheepRenderState,
                            poseStack,
                            renderType,
                            packedLight,
                            LivingEntityRenderer.getOverlayCoords(sheepRenderState, 0.0F),
                            -16777216,
                            null,
                            sheepRenderState.outlineColor,
                            null);
                }
            } else {
                coloredCutoutModelCopyLayerRender(entityModel,
                        ((SheepVariantRenderState) sheepRenderState).variant.assetInfo().wool().texturePath(),
                        poseStack,
                        submitNodeCollector,
                        packedLight,
                        sheepRenderState,
                        sheepRenderState.getWoolColor(),
                        1);
            }
        }
    }
}
