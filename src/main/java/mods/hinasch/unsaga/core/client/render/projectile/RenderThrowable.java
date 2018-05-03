package mods.hinasch.unsaga.core.client.render.projectile;

import mods.hinasch.lib.client.RenderBase;
import mods.hinasch.lib.entity.EntityThrowableBase;
import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderThrowable extends RenderBase<EntityThrowableBase>{


	final ResourceLocation res;
	public RenderThrowable(String res,RenderManager renderManager) {
		super(renderManager);
		this.res = new ResourceLocation(UnsagaMod.MODID,res);
		// TODO 自動生成されたコンストラクター・スタブ
	}


	@Override
    public void doRender(EntityThrowableBase entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
		this.renderSquare(entity, x, y, z, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityThrowableBase entity) {
		// TODO 自動生成されたメソッド・スタブ
		return res;
	}




}
