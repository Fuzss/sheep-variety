package fuzs.sheepvariety.client.model.geom;

import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import fuzs.sheepvariety.SheepVariety;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModModelLayers {
    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(SheepVariety.MOD_ID);
    public static final ModelLayerLocation SHEEP_HORNS = MODEL_LAYERS.registerModelLayer("sheep", "horns");
    public static final ModelLayerLocation SHEEP_BABY_HORNS = MODEL_LAYERS.registerModelLayer("sheep_baby", "horns");
    public static final ModelLayerLocation SHEEP_LONG_WOOL = MODEL_LAYERS.registerModelLayer("sheep", "long_wool");
    public static final ModelLayerLocation SHEEP_BABY_LONG_WOOL = MODEL_LAYERS.registerModelLayer("sheep_baby",
            "long_wool");
}
