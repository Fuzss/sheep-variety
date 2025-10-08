package fuzs.sheepvariety.client.model.geom;

import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import fuzs.sheepvariety.SheepVariety;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModModelLayers {
    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(SheepVariety.MOD_ID);
    public static final ModelLayerLocation WARM_SHEEP = MODEL_LAYERS.registerModelLayer("warm_sheep");
    public static final ModelLayerLocation WARM_SHEEP_BABY = MODEL_LAYERS.registerModelLayer("warm_sheep_baby");
    public static final ModelLayerLocation WARM_SHEEP_WOOL = MODEL_LAYERS.registerModelLayer("warm_sheep", "wool");
    public static final ModelLayerLocation WARM_SHEEP_BABY_WOOL = MODEL_LAYERS.registerModelLayer("warm_sheep_baby",
            "wool");
    public static final ModelLayerLocation WARM_SHEEP_WOOL_UNDERCOAT = MODEL_LAYERS.registerModelLayer("warm_sheep",
            "wool_undercoat");
    public static final ModelLayerLocation WARM_SHEEP_BABY_WOOL_UNDERCOAT = MODEL_LAYERS.registerModelLayer(
            "warm_sheep_baby",
            "wool_undercoat");
    public static final ModelLayerLocation COLD_SHEEP = MODEL_LAYERS.registerModelLayer("cold_sheep");
    public static final ModelLayerLocation COLD_SHEEP_BABY = MODEL_LAYERS.registerModelLayer("cold_sheep_baby");
    public static final ModelLayerLocation COLD_SHEEP_WOOL = MODEL_LAYERS.registerModelLayer("cold_sheep", "wool");
    public static final ModelLayerLocation COLD_SHEEP_BABY_WOOL = MODEL_LAYERS.registerModelLayer("cold_sheep_baby",
            "wool");
    public static final ModelLayerLocation COLD_SHEEP_WOOL_UNDERCOAT = MODEL_LAYERS.registerModelLayer("cold_sheep",
            "wool_undercoat");
    public static final ModelLayerLocation COLD_SHEEP_BABY_WOOL_UNDERCOAT = MODEL_LAYERS.registerModelLayer(
            "cold_sheep_baby",
            "wool_undercoat");
}
