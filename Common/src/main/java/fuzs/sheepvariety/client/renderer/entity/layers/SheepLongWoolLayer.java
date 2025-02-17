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
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;

public class SheepLongWoolLayer extends RenderLayer<SheepRenderState, SheepModel> {
    private final EntityModel<SheepRenderState> adultModel;
    private final EntityModel<SheepRenderState> babyModel;

    public SheepLongWoolLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet entityModelSet) {
        super(renderer);
        this.adultModel = new SheepLongWoolModel(entityModelSet.bakeLayer(ModModelLayers.SHEEP_LONG_WOOL));
        this.babyModel = new SheepLongWoolModel(entityModelSet.bakeLayer(ModModelLayers.SHEEP_BABY_LONG_WOOL));
    }

    public static LayerDefinition createFurLayer() {
        MeshDefinition meshDefinition = SheepFurModel.createFurLayer().mesh;
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.getChild("head")
                .addOrReplaceChild("facial_hair",
                        CubeListBuilder.create()
                                .texOffs(0, 0)
                                .addBox(-3.0F, -4.0F, -6.4F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.6F)),
                        PartPose.ZERO);
        partDefinition.getChild("body")
                .addOrReplaceChild("long_wool",
                        CubeListBuilder.create()
                                .texOffs(28, 8)
                                .addBox(-4.0F, -10.0F, -10.15F, 8.0F, 16.0F, 6.0F, new CubeDeformation(1.75F)),
                        PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SheepRenderState renderState, float yRot, float xRot) {
        if (!renderState.isSheared) {
            SheepVariant sheepVariant = ((SheepVariantRenderState) renderState).variant;
            if (sheepVariant.modelAndTexture().model() == SheepVariant.ModelType.COLD) {
                ResourceLocation resourceLocation = sheepVariant.modelAndTexture()
                        .asset()
                        .id()
                        .withPath((String s) -> "textures/" + s + "_long_fur.png");
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

    private static class SheepLongWoolModel extends SheepModel {

        public SheepLongWoolModel(ModelPart root) {
            super(root);
            ModelPart facialHair = root.getChild("head").getChild("facial_hair");
            ModelPart longWool = root.getChild("body").getChild("long_wool");
            root.getAllParts().forEach((ModelPart modelPart) -> {
                if (modelPart != facialHair && modelPart != longWool) {
                    modelPart.skipDraw = true;
                }
            });
        }
    }
}
