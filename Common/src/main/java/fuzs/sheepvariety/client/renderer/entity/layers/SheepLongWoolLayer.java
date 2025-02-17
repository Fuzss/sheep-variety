package fuzs.sheepvariety.client.renderer.entity.layers;

import fuzs.sheepvariety.client.model.geom.ModModelLayers;
import fuzs.sheepvariety.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariant;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class SheepLongWoolLayer extends SheepVariantWoolLayer {

    public SheepLongWoolLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet entityModelSet) {
        super(renderer, entityModelSet, ModModelLayers.SHEEP_LONG_WOOL, ModModelLayers.SHEEP_BABY_LONG_WOOL);
        skipDrawForAllExcept(this.adultModel);
        skipDrawForAllExcept(this.babyModel);
    }

    static void skipDrawForAllExcept(Model model) {
        SheepOverlayLayer.skipDrawForAllExcept(model,
                model.root().getChild("head").getChild("facial_hair"),
                model.root().getChild("body").getChild("long_wool"));
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
    protected @Nullable ResourceLocation getTextureLocation(SheepRenderState renderState) {
        SheepVariant sheepVariant = ((SheepVariantRenderState) renderState).variant;
        if (sheepVariant.modelAndTexture().model() == SheepVariant.ModelType.COLD) {
            return sheepVariant.modelAndTexture()
                    .asset()
                    .id()
                    .withPath((String s) -> "textures/" + s + "_long_fur.png");
        } else {
            return null;
        }
    }
}
