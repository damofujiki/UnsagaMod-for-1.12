package mods.hinasch.unsaga.core.client.render.projectile;

import mods.hinasch.unsaga.core.entity.projectile.EntityCustomArrow;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCustomArrow extends RenderArrow<EntityCustomArrow>{


    public static final ResourceLocation RES_ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");
    public static final ResourceLocation RES_TIPPED_ARROW = new ResourceLocation("textures/entity/projectiles/tipped_arrow.png");
	public RenderCustomArrow(RenderManager renderManagerIn) {
		super(renderManagerIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
    protected ResourceLocation getEntityTexture(EntityCustomArrow entity)
    {
        return RES_ARROW;
    }

}
