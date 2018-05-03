package mods.hinasch.unsaga.core.client.render.entity;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.client.model.ModelSignalTree;
import mods.hinasch.unsaga.core.entity.mob.EntitySignalTree;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSignalTree extends RenderLiving<EntitySignalTree>{

	public static final ResourceLocation RES = new ResourceLocation(UnsagaMod.MODID,"textures/entity/mob/signaltree.png");


	public RenderSignalTree(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelSignalTree(), 1.0F);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySignalTree entity) {
		// TODO 自動生成されたメソッド・スタブ
		return RES;
	}

}
