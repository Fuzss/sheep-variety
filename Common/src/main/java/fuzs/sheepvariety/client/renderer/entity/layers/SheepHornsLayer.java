package fuzs.sheepvariety.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sheepvariety.client.model.geom.ModModelLayers;
import fuzs.sheepvariety.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.world.entity.animal.SheepVariant;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;

public class SheepHornsLayer extends RenderLayer<SheepRenderState, SheepModel> {
    private final EntityModel<SheepRenderState> adultModel;
    private final EntityModel<SheepRenderState> babyModel;

    public SheepHornsLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet entityModelSet) {
        super(renderer);
        this.adultModel = new SheepHornsModel(entityModelSet.bakeLayer(ModModelLayers.SHEEP_HORNS));
        this.babyModel = new SheepHornsModel(entityModelSet.bakeLayer(ModModelLayers.SHEEP_BABY_HORNS));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = SheepModel.createBodyLayer().mesh;
        PartDefinition partDefinition = meshDefinition.getRoot().getChild("head");
        partDefinition.addOrReplaceChild("left_horn",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(1.0F, -6.0F, -4.0F, 4.0F, 5.0F, 5.0F)
                        .texOffs(0, 10)
                        .addBox(4.0F, -3.0F, -6.0F, 1.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        partDefinition.addOrReplaceChild("right_horn",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-5.0F, -6.0F, -4.0F, 4.0F, 5.0F, 5.0F)
                        .texOffs(0, 10)
                        .mirror()
                        .addBox(-5.0F, -3.0F, -6.0F, 1.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SheepRenderState renderState, float yRot, float xRot) {
        SheepVariant sheepVariant = ((SheepVariantRenderState) renderState).variant;
        if (sheepVariant.modelAndTexture().model() == SheepVariant.ModelType.WARM) {
            EntityModel<SheepRenderState> entityModel = renderState.isBaby ? this.babyModel : this.adultModel;
            ResourceLocation resourceLocation = sheepVariant.modelAndTexture()
                    .asset()
                    .id()
                    .withPath((String s) -> "textures/" + s + "_horns.png");
            coloredCutoutModelCopyLayerRender(entityModel,
                    resourceLocation,
                    poseStack,
                    bufferSource,
                    packedLight,
                    renderState,
                    -1);
        }
    }

    private static class SheepHornsModel extends SheepModel {

        public SheepHornsModel(ModelPart root) {
            super(root);
            ModelPart leftHorn = root.getChild("head").getChild("left_horn");
            ModelPart rightHorn = root.getChild("head").getChild("right_horn");
            root.getAllParts().forEach((ModelPart modelPart) -> {
                if (modelPart != leftHorn && modelPart != rightHorn) {
                    modelPart.skipDraw = true;
                }
            });
        }
    }
}
