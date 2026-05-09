package fuzs.sheepvariety.common.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sheepvariety.common.client.model.geom.ModModelLayers;
import fuzs.sheepvariety.common.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.common.world.entity.animal.sheep.SheepVariant;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.SheepWoolUndercoatLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.world.item.DyeColor;

import java.util.Map;

/**
 * @see net.minecraft.client.renderer.entity.layers.SheepWoolUndercoatLayer
 */
public class SheepVariantWoolUndercoatLayer extends SheepWoolUndercoatLayer {
    private final Map<SheepVariant.ModelType, SheepModel> models;

    public SheepVariantWoolUndercoatLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityRendererProvider.Context context) {
        super(renderer, context.getModelSet());
        this.models = bakeModels(context);
    }

    private static Map<SheepVariant.ModelType, SheepModel> bakeModels(EntityRendererProvider.Context context) {
        return Maps.newEnumMap(Map.of(SheepVariant.ModelType.NORMAL,
                new SheepModel(context.bakeLayer(ModelLayers.SHEEP_WOOL_UNDERCOAT)),
                SheepVariant.ModelType.WARM,
                new SheepModel(context.bakeLayer(ModModelLayers.WARM_SHEEP_WOOL_UNDERCOAT)),
                SheepVariant.ModelType.COLD,
                new SheepModel(context.bakeLayer(ModModelLayers.COLD_SHEEP_WOOL_UNDERCOAT))));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SheepRenderState state, float yRot, float xRot) {
        if (((SheepVariantRenderState) state).variant != null) {
            if (state.isBaby) {
                super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
            } else if (!state.isInvisible && (state.isJebSheep || state.woolColor != DyeColor.WHITE)) {
                EntityModel<SheepRenderState> entityModel = this.models.get(((SheepVariantRenderState) state).variant.assetInfo()
                        .model());
                coloredCutoutModelCopyLayerRender(entityModel,
                        ((SheepVariantRenderState) state).variant.assetInfo().undercoat().texturePath(),
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
