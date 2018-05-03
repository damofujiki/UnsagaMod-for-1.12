package mods.hinasch.unsaga.core.client.render.projectile;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.unsaga.core.client.particle.ParticleItems;
import mods.hinasch.unsaga.core.entity.projectile.EntityIceNeedle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderIceNeedle extends Render<EntityIceNeedle>{

	public static final ItemStack DUMMY = new ItemStack(ParticleItems.ICE_NEEDLE);
	public RenderIceNeedle(RenderManager renderManager) {
		super(renderManager);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIceNeedle entity) {
		// TODO 自動生成されたメソッド・スタブ
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	@Override
	public void doRender(EntityIceNeedle entity, double x, double y, double z, float entityYaw, float partialTicks)
	{

		IBakedModel model = ClientHelper.getItemRenderer().getItemModelWithOverrides(DUMMY, entity.world, entity.getThrower());

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();

//		model = ForgeHooksClient.handleCameraTransforms(model, TransformType.GROUND, false);
//        float offset = model.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
		this.bindEntityTexture(entity);
		GlStateManager.translate((float)x, (float)y, (float)z);
		GlStateManager.translate(0,0 , 0);

//		GlStateManager.rotate(entity.rotationYaw, 0f, 1f, 0f);
//		GlStateManager.rotate((float)pos2.dx, (float)pos1.dx,(float)pos1.dy,(float)pos1.dz);
		GlStateManager.disableLighting();

		float f2 = 1.0F;

		GlStateManager.scale(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);
		ClientHelper.getItemRenderer().renderItem(DUMMY,model);

		GlStateManager.enableLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}
