package mods.hinasch.unsaga.core.client.render.entity;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.client.model.ModelStormEater;
import mods.hinasch.unsaga.core.entity.mob.EntityPoisonEater;
import mods.hinasch.unsaga.core.entity.mob.EntityStormEater;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderStormEater extends RenderLiving<EntityStormEater>{


	public Class<? extends Entity> parentEntity;
	public static final ResourceLocation STORM1 = new ResourceLocation(UnsagaMod.MODID,"textures/entity/mob/storm1.png");
	public static final ResourceLocation STORM2 = new ResourceLocation(UnsagaMod.MODID,"textures/entity/mob/storm2.png");
	public static final ResourceLocation STORM3 = new ResourceLocation(UnsagaMod.MODID,"textures/entity/mob/storm3.png");
	public static final ResourceLocation POISON = new ResourceLocation(UnsagaMod.MODID,"textures/entity/mob/poison.png");

	public RenderStormEater(RenderManager rendermanagerIn,Class<? extends Entity> parent) {
		super(rendermanagerIn, new ModelStormEater(), 0.5F);
		this.parentEntity = parent;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityStormEater entity) {
		if(parentEntity==EntityStormEater.class){
			if(entity.getAnimationTick()>=90){
				return STORM3;
			}
			if(entity.getAnimationTick()>=80){
				return STORM2;
			}
			if(entity.getAnimationTick()>=0){
				return STORM1;
			}
			return STORM1;
		}
		if(parentEntity==EntityPoisonEater.class){
			return POISON;
		}
		return POISON;
	}

}
