package mods.hinasch.unsaga.core.client.render.entity;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.client.model.ModelRuffleTree;
import mods.hinasch.unsaga.core.entity.mob.EntityRuffleTree;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderRuffleTree extends RenderLiving<EntityRuffleTree>{

    private static final ResourceLocation tex = new ResourceLocation(UnsagaMod.MODID+":textures/entity/mob/ruffletree.png");
	public RenderRuffleTree(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelRuffleTree(), 1.0F);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityRuffleTree entity) {
		// TODO 自動生成されたメソッド・スタブ
		return tex;
	}

	@Override
    protected void preRenderCallback(EntityRuffleTree entitylivingbaseIn, float partialTickTime)
    {
        float f = 1.0F;
        float f1 = (8.0F + f) / 2.0F;
        float f2 = (8.0F + 1.0F / f) / 2.0F;
        GlStateManager.scale(f2, f1, f2);
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
