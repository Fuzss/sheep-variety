package fuzs.sheepvariety.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sheepvariety.client.model.geom.ModModelLayers;
import fuzs.sheepvariety.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariant;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.world.item.DyeColor;

import java.util.Map;

/**
 * @see net.minecraft.client.renderer.entity.layers.SheepWoolUndercoatLayer
 */
public class SheepVariantWoolUndercoatLayer extends RenderLayer<SheepRenderState, SheepModel> {
    private final Map<SheepVariant.ModelType, AdultAndBabyModelPair<SheepModel>> models;

    public SheepVariantWoolUndercoatLayer(RenderLayerParent<SheepRenderState, SheepModel> renderLayerParent, EntityRendererProvider.Context context) {
        super(renderLayerParent);
        this.models = bakeModels(context);
    }

    private static Map<SheepVariant.ModelType, AdultAndBabyModelPair<SheepModel>> bakeModels(EntityRendererProvider.Context context) {
        return Maps.newEnumMap(Map.of(SheepVariant.ModelType.NORMAL,
                new AdultAndBabyModelPair<>(new SheepModel(context.bakeLayer(ModelLayers.SHEEP_WOOL_UNDERCOAT)),
                        new SheepModel(context.bakeLayer(ModelLayers.SHEEP_BABY_WOOL_UNDERCOAT))),
                SheepVariant.ModelType.WARM,
                new AdultAndBabyModelPair<>(new SheepModel(context.bakeLayer(ModModelLayers.WARM_SHEEP_WOOL_UNDERCOAT)),
                        new SheepModel(context.bakeLayer(ModModelLayers.WARM_SHEEP_BABY_WOOL_UNDERCOAT))),
                SheepVariant.ModelType.COLD,
                new AdultAndBabyModelPair<>(new SheepModel(context.bakeLayer(ModModelLayers.COLD_SHEEP_WOOL_UNDERCOAT)),
                        new SheepModel(context.bakeLayer(ModModelLayers.COLD_SHEEP_BABY_WOOL_UNDERCOAT)))));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, SheepRenderState sheepRenderState, float yRot, float xRot) {
        if (!sheepRenderState.isInvisible &&
                (sheepRenderState.isJebSheep() || sheepRenderState.woolColor != DyeColor.WHITE)) {
            EntityModel<SheepRenderState> entityModel = this.models.get(((SheepVariantRenderState) sheepRenderState).variant.assetInfo()
                    .model()).getModel(sheepRenderState.isBaby);
            coloredCutoutModelCopyLayerRender(entityModel,
                    ((SheepVariantRenderState) sheepRenderState).variant.assetInfo().undercoat().texturePath(),
                    poseStack,
                    multiBufferSource,
                    packedLight,
                    sheepRenderState,
                    sheepRenderState.getWoolColor());
        }
    }
}
