package fuzs.sheepvariety.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.sheepvariety.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariant;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class SheepVariantWoolLayer extends RenderLayer<SheepRenderState, SheepModel> {
    protected final EntityModel<SheepRenderState> adultModel;
    protected final EntityModel<SheepRenderState> babyModel;

    public SheepVariantWoolLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet entityModelSet) {
        this(renderer, entityModelSet, ModelLayers.SHEEP_WOOL, ModelLayers.SHEEP_BABY_WOOL);
    }

    protected SheepVariantWoolLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet entityModelSet, ModelLayerLocation adultModelLayer, ModelLayerLocation babyModelLayer) {
        super(renderer);
        this.adultModel = new SheepFurModel(entityModelSet.bakeLayer(adultModelLayer));
        this.babyModel = new SheepFurModel(entityModelSet.bakeLayer(babyModelLayer));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SheepRenderState renderState, float yRot, float xRot) {
        if (!renderState.isSheared) {
            ResourceLocation resourceLocation = this.getTextureLocation(renderState);
            if (resourceLocation != null) {
                EntityModel<SheepRenderState> entityModel = renderState.isBaby ? this.babyModel : this.adultModel;
                if (renderState.isInvisible) {
                    if (renderState.appearsGlowing) {
                        entityModel.setupAnim(renderState);
                        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.outline(resourceLocation));
                        entityModel.renderToBuffer(poseStack,
                                vertexConsumer,
                                packedLight,
                                LivingEntityRenderer.getOverlayCoords(renderState, 0.0F),
                                -16777216);
                    }
                } else {
                    int color = SheepOverlayLayer.getSheepWoolColor(renderState);
                    coloredCutoutModelCopyLayerRender(entityModel,
                            resourceLocation,
                            poseStack,
                            bufferSource,
                            packedLight,
                            renderState,
                            color);
                }
            }
        }
    }

    @Nullable
    protected ResourceLocation getTextureLocation(SheepRenderState renderState) {
        SheepVariant sheepVariant = ((SheepVariantRenderState) renderState).variant;
        return sheepVariant.modelAndTexture().asset().id().withPath((String s) -> "textures/" + s + "_fur.png");
    }
}
