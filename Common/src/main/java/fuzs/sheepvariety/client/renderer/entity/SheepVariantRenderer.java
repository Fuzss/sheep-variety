package fuzs.sheepvariety.client.renderer.entity;

import fuzs.sheepvariety.client.renderer.entity.layers.SheepHornsLayer;
import fuzs.sheepvariety.client.renderer.entity.layers.SheepLongWoolLayer;
import fuzs.sheepvariety.client.renderer.entity.layers.SheepOverlayLayer;
import fuzs.sheepvariety.client.renderer.entity.layers.SheepVariantWoolLayer;
import fuzs.sheepvariety.client.renderer.entity.state.SheepVariantRenderState;
import fuzs.sheepvariety.init.ModRegistry;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;

public class SheepVariantRenderer extends SheepRenderer {

    public SheepVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.layers.removeIf((RenderLayer<SheepRenderState, SheepModel> renderLayer) -> renderLayer instanceof SheepWoolLayer);
        this.addLayer(new SheepOverlayLayer(this));
        this.addLayer(new SheepHornsLayer(this, context.getModelSet()));
        this.addLayer(new SheepVariantWoolLayer(this, context.getModelSet()));
        this.addLayer(new SheepLongWoolLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(SheepRenderState sheepRenderState) {
        return ((SheepVariantRenderState) sheepRenderState).variant == null ? MissingTextureAtlasSprite.getLocation() :
                ((SheepVariantRenderState) sheepRenderState).variant.modelAndTexture().asset().texturePath();
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
}
