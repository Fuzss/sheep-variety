package fuzs.sheepvariety.client.renderer.entity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sheepvariety.client.model.geom.ModModelLayers;
import fuzs.sheepvariety.client.renderer.entity.layers.SheepVariantWoolLayer;
import fuzs.sheepvariety.client.renderer.entity.layers.SheepVariantWoolUndercoatLayer;
import fuzs.sheepvariety.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.init.ModRegistry;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariant;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.layers.SheepWoolUndercoatLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.sheep.Sheep;

import java.util.Map;

public class SheepVariantRenderer extends SheepRenderer {
    private final Map<SheepVariant.ModelType, AdultAndBabyModelPair<SheepModel>> models;

    public SheepVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.models = bakeModels(context);
        this.layers.removeIf((RenderLayer<SheepRenderState, SheepModel> renderLayer) -> {
            return renderLayer instanceof SheepWoolLayer || renderLayer instanceof SheepWoolUndercoatLayer;
        });
        this.addLayer(new SheepVariantWoolUndercoatLayer(this, context));
        this.addLayer(new SheepVariantWoolLayer(this, context));
    }

    private static Map<SheepVariant.ModelType, AdultAndBabyModelPair<SheepModel>> bakeModels(EntityRendererProvider.Context context) {
        return Maps.newEnumMap(Map.of(SheepVariant.ModelType.NORMAL,
                new AdultAndBabyModelPair<>(new SheepModel(context.bakeLayer(ModelLayers.SHEEP)),
                        new SheepModel(context.bakeLayer(ModelLayers.SHEEP_BABY))),
                SheepVariant.ModelType.WARM,
                new AdultAndBabyModelPair<>(new SheepModel(context.bakeLayer(ModModelLayers.WARM_SHEEP)),
                        new SheepModel(context.bakeLayer(ModModelLayers.WARM_SHEEP_BABY))),
                SheepVariant.ModelType.COLD,
                new AdultAndBabyModelPair<>(new SheepModel(context.bakeLayer(ModModelLayers.COLD_SHEEP)),
                        new SheepModel(context.bakeLayer(ModModelLayers.COLD_SHEEP_BABY)))));
    }

    public static LayerDefinition createWarmBodyLayer() {
        MeshDefinition meshDefinition = SheepModel.createBodyLayer().mesh;
        PartDefinition partDefinition = meshDefinition.getRoot().getChild("head");
        partDefinition.addOrReplaceChild("left_horn",
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(1.0F, -6.0F, -4.0F, 4.0F, 5.0F, 5.0F)
                        .texOffs(0, 42)
                        .addBox(4.0F, -3.0F, -6.0F, 1.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        partDefinition.addOrReplaceChild("right_horn",
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .mirror()
                        .addBox(-5.0F, -6.0F, -4.0F, 4.0F, 5.0F, 5.0F)
                        .texOffs(0, 42)
                        .mirror()
                        .addBox(-5.0F, -3.0F, -6.0F, 1.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    public static LayerDefinition createColdFurLayer() {
        MeshDefinition meshDefinition = SheepFurModel.createFurLayer().mesh;
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.getChild("head")
                .addOrReplaceChild("facial_hair",
                        CubeListBuilder.create()
                                .texOffs(0, 32)
                                .addBox(-3.0F, -4.0F, -6.4F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.6F)),
                        PartPose.ZERO);
        partDefinition.getChild("body")
                .addOrReplaceChild("long_wool",
                        CubeListBuilder.create()
                                .texOffs(28, 40)
                                .addBox(-4.0F, -10.0F, -10.15F, 8.0F, 16.0F, 6.0F, new CubeDeformation(1.75F)),
                        PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public ResourceLocation getTextureLocation(SheepRenderState sheepRenderState) {
        return ((SheepVariantRenderState) sheepRenderState).variant == null ? MissingTextureAtlasSprite.getLocation() :
                ((SheepVariantRenderState) sheepRenderState).variant.assetInfo().asset().texturePath();
    }

    @Override
    public SheepRenderState createRenderState() {
        return new SheepVariantRenderState();
    }

    @Override
    public void extractRenderState(Sheep sheep, SheepRenderState sheepRenderState, float partialTick) {
        super.extractRenderState(sheep, sheepRenderState, partialTick);
        ((SheepVariantRenderState) sheepRenderState).variant = ModRegistry.SHEEP_VARIANT_ATTACHMENT_TYPE.get(sheep)
                .value();
    }

    @Override
    public void submit(SheepRenderState sheepRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (((SheepVariantRenderState) sheepRenderState).variant != null) {
            AdultAndBabyModelPair<SheepModel> adultAndBabyModelPair = this.models.get(((SheepVariantRenderState) sheepRenderState).variant.assetInfo()
                    .model());
            this.adultModel = adultAndBabyModelPair.getModel(false);
            this.babyModel = adultAndBabyModelPair.getModel(true);
            super.submit(sheepRenderState, poseStack, submitNodeCollector, cameraRenderState);
        }
    }
}
