package fuzs.sheepvariety.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.sheepvariety.client.model.geom.ModModelLayers;
import fuzs.sheepvariety.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.world.entity.animal.SheepVariant;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;

public class SheepVariantWoolLayer extends RenderLayer<SheepRenderState, SheepModel> {
    private final EntityModel<SheepRenderState> adultModel;
    private final EntityModel<SheepRenderState> babyModel;

    public SheepVariantWoolLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet entityModelSet) {
        super(renderer);
        this.adultModel = new SheepFurModel(entityModelSet.bakeLayer(ModModelLayers.SHEEP_WOOL));
        this.babyModel = new SheepFurModel(entityModelSet.bakeLayer(ModModelLayers.SHEEP_BABY_WOOL));
    }

    public static LayerDefinition createFurLayer() {
        MeshDefinition meshDefinition = SheepFurModel.createFurLayer().mesh;
        PartDefinition partDefinition = meshDefinition.getRoot();
        CubeListBuilder cubeListBuilder = CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.5F));
        partDefinition.addOrReplaceChild("right_hind_leg",
                cubeListBuilder.mirror(false),
                PartPose.offset(-3.0F, 12.0F, 7.0F));
        partDefinition.addOrReplaceChild("left_hind_leg",
                cubeListBuilder.mirror(true),
                PartPose.offset(3.0F, 12.0F, 7.0F));
        partDefinition.addOrReplaceChild("right_front_leg",
                cubeListBuilder.mirror(false),
                PartPose.offset(-3.0F, 12.0F, -5.0F));
        partDefinition.addOrReplaceChild("left_front_leg",
                cubeListBuilder.mirror(true),
                PartPose.offset(3.0F, 12.0F, -5.0F));
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SheepRenderState renderState, float yRot, float xRot) {
        if (!renderState.isSheared) {
            SheepVariant sheepVariant = ((SheepVariantRenderState) renderState).variant;
            ResourceLocation resourceLocation = sheepVariant.modelAndTexture()
                    .asset()
                    .id()
                    .withPath((String s) -> "textures/" + s + "_fur.png");
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
                int woolColor = SheepOverlayLayer.getSheepWoolColor(renderState);
                coloredCutoutModelCopyLayerRender(entityModel,
                        resourceLocation,
                        poseStack,
                        bufferSource,
                        packedLight,
                        renderState,
                        woolColor);
            }
        }
    }
}
