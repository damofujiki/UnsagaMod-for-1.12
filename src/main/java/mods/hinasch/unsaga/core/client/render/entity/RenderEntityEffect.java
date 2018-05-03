package mods.hinasch.unsaga.core.client.render.entity;

import mods.hinasch.unsaga.core.entity.passive.EntityEffectSpawner;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class RenderEntityEffect extends Render<EntityEffectSpawner>{

	public RenderEntityEffect(RenderManager renderManager) {
		super(renderManager);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityEffectSpawner entity) {
		// TODO 自動生成されたメソッド・スタブ
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
