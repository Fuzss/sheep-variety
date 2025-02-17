package fuzs.sheepvariety.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sheepvariety.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.world.entity.animal.sheep.SheepVariant;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Predicate;

public class SheepOverlayLayer extends RenderLayer<SheepRenderState, SheepModel> {

    public SheepOverlayLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SheepRenderState renderState, float yRot, float xRot) {
        if (!renderState.isInvisible) {
            SheepVariant sheepVariant = ((SheepVariantRenderState) renderState).variant;
            ResourceLocation resourceLocation = sheepVariant.modelAndTexture()
                    .asset()
                    .id()
                    .withPath((String s) -> "textures/" + s + "_overlay.png");
            int color = getSheepWoolColor(renderState);
            coloredCutoutModelCopyLayerRender(this.getParentModel(),
                    resourceLocation,
                    poseStack,
                    bufferSource,
                    packedLight,
                    renderState,
                    color);
        }
    }

    static int getSheepWoolColor(SheepRenderState renderState) {
        if (renderState.customName != null && "jeb_".equals(renderState.customName.getString())) {
            int tickCount = Mth.floor(renderState.ageInTicks);
            int l = tickCount / 25 + renderState.id;
            int m = DyeColor.values().length;
            int n = l % m;
            int o = (l + 1) % m;
            float h = ((float) (tickCount % 25) + Mth.frac(renderState.ageInTicks)) / 25.0F;
            int primaryColor = Sheep.getColor(DyeColor.byId(n));
            int secondaryColor = Sheep.getColor(DyeColor.byId(o));
            return ARGB.lerp(h, primaryColor, secondaryColor);
        } else {
            return Sheep.getColor(renderState.woolColor);
        }
    }

    static void skipDrawForAllExcept(Model model, ModelPart... modelParts) {
        HashSet<ModelPart> set = new HashSet<>(Arrays.asList(modelParts));
        model.root()
                .getAllParts()
                .filter(Predicate.not(set::contains))
                .forEach((ModelPart modelPart) -> modelPart.skipDraw = true);
    }
}
