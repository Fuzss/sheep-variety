package fuzs.sheepvariety.common.client.renderer.entity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sheepvariety.common.client.model.geom.ModModelLayers;
import fuzs.sheepvariety.common.client.renderer.entity.layers.SheepVariantWoolLayer;
import fuzs.sheepvariety.common.client.renderer.entity.layers.SheepVariantWoolUndercoatLayer;
import fuzs.sheepvariety.common.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.common.init.ModRegistry;
import fuzs.sheepvariety.common.world.entity.animal.sheep.SheepVariant;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
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
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.sheep.Sheep;

import java.util.Map;
import java.util.Set;

public class SheepVariantRenderer extends SheepRenderer {
    /**
     * Copied from {@code SheepModel#BABY_TRANSFORMER} from Minecraft 1.21.11.
     */
    public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(false,
            8.0F,
            4.0F,
            2.0F,
            2.0F,
            24.0F,
            Set.of("head"));

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
    public Identifier getTextureLocation(SheepRenderState state) {
        if (((SheepVariantRenderState) state).variant == null) {
            return MissingTextureAtlasSprite.getLocation();
        } else if (state.isBaby) {
            return super.getTextureLocation(state);
        } else {
            return ((SheepVariantRenderState) state).variant.assetInfo().asset().texturePath();
        }
    }

    @Override
    public SheepRenderState createRenderState() {
        return new SheepVariantRenderState();
    }

    @Override
    public void extractRenderState(Sheep sheep, SheepRenderState state, float partialTick) {
        super.extractRenderState(sheep, state, partialTick);
        ((SheepVariantRenderState) state).variant = ModRegistry.SHEEP_VARIANT_ATTACHMENT_TYPE.get(sheep).value();
    }

    @Override
    public void submit(SheepRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (((SheepVariantRenderState) state).variant != null) {
            AdultAndBabyModelPair<SheepModel> adultAndBabyModelPair = this.models.get(((SheepVariantRenderState) state).variant.assetInfo()
                    .model());
            this.adultModel = adultAndBabyModelPair.getModel(false);
//            this.babyModel = adultAndBabyModelPair.getModel(true);
            super.submit(state, poseStack, submitNodeCollector, camera);
        }
    }
}
