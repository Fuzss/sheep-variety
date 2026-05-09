package fuzs.sheepvariety.common.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sheepvariety.common.client.model.geom.ModModelLayers;
import fuzs.sheepvariety.common.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.common.world.entity.animal.sheep.SheepVariant;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import java.util.Map;

/**
 * @see net.minecraft.client.renderer.entity.layers.SheepWoolLayer
 */
public class SheepVariantWoolLayer extends SheepWoolLayer {
    private final Map<SheepVariant.ModelType, AdultAndBabyModelPair<SheepFurModel>> models;

    public SheepVariantWoolLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityRendererProvider.Context context) {
        super(renderer, context.getModelSet());
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
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SheepRenderState state, float yRot, float xRot) {
        if (((SheepVariantRenderState) state).variant != null) {
            if (state.isBaby) {
                super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
            } else if (!state.isSheared) {
                EntityModel<SheepRenderState> model = this.models.get(((SheepVariantRenderState) state).variant.assetInfo()
                        .model()).getModel(state.isBaby);
                if (state.isInvisible) {
                    if (state.appearsGlowing()) {
                        RenderType renderType = RenderTypes.outline(((SheepVariantRenderState) state).variant.assetInfo()
                                .wool()
                                .texturePath());
                        submitNodeCollector.submitModel(model,
                                state,
                                poseStack,
                                renderType,
                                lightCoords,
                                LivingEntityRenderer.getOverlayCoords(state, 0.0F),
                                0XFF000000,
                                null,
                                state.outlineColor,
                                null);
                    }
                } else {
                    coloredCutoutModelCopyLayerRender(model,
                            ((SheepVariantRenderState) state).variant.assetInfo().wool().texturePath(),
                            poseStack,
                            submitNodeCollector,
                            lightCoords,
                            state,
                            state.getWoolColor(),
                            1);
                }
            }
        }
    }
}
