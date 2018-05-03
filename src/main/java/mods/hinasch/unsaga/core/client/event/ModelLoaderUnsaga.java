package mods.hinasch.unsaga.core.client.event;

import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class ModelLoaderUnsaga implements ICustomModelLoader{

	public static final ModelLoaderUnsaga INSTANCE = new ModelLoaderUnsaga();
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		// TODO 自動生成されたメソッド・スタブ
		if(!modelLocation.getResourceDomain().equals(UnsagaMod.MODID)){
			return false;
		}


		String path = modelLocation.getResourcePath();
		UnsagaMod.logger.trace("models", path);
		if(path.equals("models/item/axe")){

		}
		return false;
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}




//	@SubscribeEvent
//	public void onBakeModel(ModelBakeEvent ev){
//		ModelLoaderRegistry.getModel(location)
//	}


}
