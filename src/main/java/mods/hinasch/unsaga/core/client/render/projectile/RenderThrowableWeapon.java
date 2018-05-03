package mods.hinasch.unsaga.core.client.render.projectile;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.unsaga.core.entity.projectile.EntityThrowableWeapon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

public class RenderThrowableWeapon extends Render<EntityThrowableWeapon>{

	final ItemStack defaultStack;
	public RenderThrowableWeapon(RenderManager renderManager,ItemStack def) {
		super(renderManager);
		this.defaultStack = def;
		// TODO 自動生成されたコンストラクター・スタブ
	}


	@Override
	public void doRender(EntityThrowableWeapon entity, double x, double y, double z, float entityYaw, float partialTicks)
	{

		ItemStack stack = !entity.getThrowStack().isEmpty() ? entity.getThrowStack() : this.defaultStack.copy();
		IBakedModel model = ClientHelper.getItemRenderer().getItemModelWithOverrides(stack, entity.world, entity.getThrower());

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();

		model = ForgeHooksClient.handleCameraTransforms(model, TransformType.GROUND, false);
//        float offset = model.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
		this.bindEntityTexture(entity);
		GlStateManager.translate((float)x, (float)y, (float)z);
		entity.fixRenderPosition();
//		XYZPos pos = UnsagaMod.proxy.getDebugPos(0);
//		GlStateManager.translate(pos.dx,pos.dy,pos.dz);
//		XYZPos pos1 = UnsagaMod.proxy.getDebugPos(1);
//		XYZPos pos2 = UnsagaMod.proxy.getDebugPos(2);
		GlStateManager.rotate(entity.rotationYaw, 0f, 1f, 0f);
//		GlStateManager.rotate((float)pos2.dx, (float)pos1.dx,(float)pos1.dy,(float)pos1.dz);
		GlStateManager.disableLighting();
		double f2 = 1.0F;

		float speed = 90.0F;
		//		if(!entity.isInGround()){
		//			if(entity.ticksExisted %  7 >3){



		if(entity.hasRotationInRender()){
			Axis axis = entity.getRotationAxis();
			GlStateManager.rotate(entity.ticksExisted % (360/speed) * speed + (speed * partialTicks), axis == Axis.X ? 1.0F : 0, axis == Axis.Y ? 1.0F : 0, axis == Axis.Z ? 1.0F : 0);
		}

		entity.preRenderFix(entity);

		//回転も順番がおかしいと描画が変に？
		//進行方向へ向ける→回転アニメ→縦に倒したりのちょっとしたfix
		//の順にやるとうまくいった、、

		//			}
		//			if(entity.ticksExisted % 7 == 3){
		//				GlStateManager.rotate(180F, 0, 0, 1.0F);
		//			}
		//			if(entity.ticksExisted % 7 == 4){
		//				GlStateManager.rotate(180F, 0,1.0F, 0);
		//			}
		//		}else{
		//			GlStateManager.rotate(90F, 1.0F,0,0);
		//		}


		//			UnsagaMod.logger.trace("yaw", entity.ticksExisted % 360);


		GlStateManager.scale(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);
		ClientHelper.getItemRenderer().renderItem(stack,model);

		GlStateManager.enableLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	@Override
	protected ResourceLocation getEntityTexture(EntityThrowableWeapon entity) {
		// TODO 自動生成されたメソッド・スタブ
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
