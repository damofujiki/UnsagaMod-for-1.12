package mods.hinasch.unsaga.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelSignalTree extends ModelBase{
    public ModelRenderer leg1;
    public ModelRenderer leg1_1;
    public ModelRenderer leg2;
    public ModelRenderer leg2_1;
    public ModelRenderer body;
    public ModelRenderer branch1;
    public ModelRenderLight fruit1;
    public ModelRenderer branch2;
    public ModelRenderer branch2_1;
    public ModelRenderLight fruit2;
    public ModelRenderer branch3;
    public ModelRenderLight fruit3;
    public ModelRenderer leg3;
    public ModelRenderer leg3_1;
    public ModelRenderer leg4;
    public ModelRenderer leg4_1;
    public ModelRenderer branch4;
    public ModelRenderLight fruit4;

    public ModelSignalTree() {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.leg1 = new ModelRenderer(this, 0, 0);
        this.leg1.setRotationPoint(1.0F, 6.2F, 0.5F);
        this.leg1.addBox(-0.6F, 0.0F, -0.5F, 1, 7, 1);
        this.setRotationAngles(this.leg1, -0.7641051252178287F, -0.42446406875869874F, 0.0F);
        this.leg1_1 = new ModelRenderer(this, 0, 0);
        this.leg1_1.setRotationPoint(0.0F, 6.5F, 0.0F);
        this.leg1_1.addBox(-0.5F, 0.0F, -0.5F, 1, 4, 1);
        this.setRotationAngles(this.leg1_1, -1.6556193390944651F, 0.0F, 0.0F);
        this.leg1.addChild(this.leg1_1);
        this.leg2 = new ModelRenderer(this, 0, 0);
        this.leg2.setRotationPoint(-0.2F, 6.2F, 0.5F);
        this.leg2.addBox(-0.3F, 0.0F, -0.5F, 1, 7, 1);
        this.setRotationAngles(this.leg2, -0.7641051252178287F, 0.42446406875869874F, 0.0F);
        this.leg2_1 = new ModelRenderer(this, 0, 0);
        this.leg2_1.setRotationPoint(0.0F, 6.5F, 0.0F);
        this.leg2_1.addBox(-0.5F, 0.0F, -0.5F, 1, 4, 1);
        this.setRotationAngles(this.leg2_1, -1.6556193390944651F, 0.0F, 0.0F);
        this.leg2.addChild(this.leg2_1);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(0.0F, -0.5F, 0.0F, 1, 7, 1);
        this.branch1 = new ModelRenderer(this, 0, 0);
        this.branch1.setRotationPoint(0.4F, 0.3F, 0.6F);
        this.branch1.addBox(-0.5F, 0.0F, -0.5F, 1, 7, 1);
        this.setRotationAngles(this.branch1, 2.2924900790209954F, 2.4198989013636942F, 0.0F);
        this.fruit1 = new ModelRenderLight(this, 0, 17);
        this.fruit1.setRotationPoint(0.0F, 7.1F, 0.0F);
        this.fruit1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2);
        this.setRotationAngles(this.fruit1, -0.9339256534473936F, -0.08482300397719036F, 0.0F);
        this.branch1.addChild(this.fruit1);
        this.branch2 = new ModelRenderer(this, 0, 0);
        this.branch2.setRotationPoint(0.4F, 0.3F, 0.6F);
        this.branch2.addBox(-0.5F, 0.0F, -0.5F, 1, 4, 1);
        this.setRotationAngles(this.branch2, 2.080257978062619F, -1.1887437641859822F, 0.0F);
        this.branch2_1 = new ModelRenderer(this, 0, 0);
        this.branch2_1.setRotationPoint(0.0F, 3.5F, -0.1F);
        this.branch2_1.addBox(-0.5F, 0.0F, -0.5F, 1, 4, 1);
        this.setRotationAngles(this.branch2_1, 1.4433872381360888F, -0.04241150198859518F, 0.0F);
        this.branch2.addChild(this.branch2_1);
        this.fruit2 = new ModelRenderLight(this, 8, 17);
        this.fruit2.setRotationPoint(-0.8F, 4.7F, -0.7F);
        this.fruit2.addBox(-1.0F, -1.0F, -1.0F, 3, 3, 3);
        this.setRotationAngles(this.fruit2, -0.21223203437934937F, 0.0F, 0.12740903872453743F);
        this.branch2_1.addChild(this.fruit2);
        this.branch3 = new ModelRenderer(this, 0, 0);
        this.branch3.setRotationPoint(0.4F, 0.3F, 0.6F);
        this.branch3.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
        this.setRotationAngles(this.branch3, 2.1650809903621875F, -0.2546435405291338F, 0.0F);
        this.fruit3 = new ModelRenderLight(this, 0, 17);
        this.fruit3.setRotationPoint(0.0F, 5.7F, 0.0F);
        this.fruit3.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2);
        this.setRotationAngles(this.fruit3, -0.169820528229565F, -0.169820528229565F, 0.0F);
        this.branch3.addChild(this.fruit3);
        this.leg3 = new ModelRenderer(this, 0, 0);
        this.leg3.setRotationPoint(-0.1F, 6.2F, 0.5F);
        this.leg3.addBox(-0.5F, 0.0F, -0.5F, 1, 7, 1);
        this.setRotationAngles(this.leg3, 0.6368706733475028F, -0.46705011182842415F, 0.0F);
        this.leg3_1 = new ModelRenderer(this, 0, 0);
        this.leg3_1.setRotationPoint(0.0F, 6.5F, 0.0F);
        this.leg3_1.addBox(-0.5F, 0.0F, -0.5F, 1, 4, 1);
        this.setRotationAngles(this.leg3_1, 1.6982054154537043F, 0.0F, 0.0F);
        this.leg3.addChild(this.leg3_1);
        this.leg4 = new ModelRenderer(this, 0, 0);
        this.leg4.setRotationPoint(1.0F, 6.2F, 0.5F);
        this.leg4.addBox(-0.5F, 0.0F, -0.5F, 1, 7, 1);
        this.setRotationAngles(this.leg4, 0.67928211291826F, 0.46705011182842415F, 0.0F);
        this.leg4_1 = new ModelRenderer(this, 0, 0);
        this.leg4_1.setRotationPoint(0.0F, 6.5F, 0.0F);
        this.leg4_1.addBox(-0.5F, 0.0F, -0.5F, 1, 4, 1);
        this.setRotationAngles(this.leg4_1, 1.6132078329446808F, 0.04241150198859518F, 0.0F);
        this.leg4.addChild(this.leg4_1);
        this.branch4 = new ModelRenderer(this, 0, 0);
        this.branch4.setRotationPoint(0.4F, 0.3F, 0.6F);
        this.branch4.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
        this.setRotationAngles(this.branch4, 2.080257978062619F, 0.8915142138766367F, 0.0F);
        this.fruit4 = new ModelRenderLight(this, 0, 21);
        this.fruit4.setRotationPoint(0.0F, 5.8F, 0.0F);
        this.fruit4.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2);
        this.setRotationAngles(this.fruit4, 0.0F, 0.46705011182842415F, 0.0F);
        this.branch4.addChild(this.fruit4);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	int light = entityIn.getBrightnessForRender();
    	fruit1.setLight(light);
    	fruit2.setLight(light);
    	fruit3.setLight(light);
    	fruit4.setLight(light);
//    	BlockPos pos = UnsagaMod.proxy.getDebugPos();
//    	GlStateManager.translate(0,pos.getY(), 0);
    	GlStateManager.scale(2,2,2);
    	this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    	this.leg1.render(scale);
    	this.leg2.render(scale);
    	this.leg3.render(scale);
    	this.leg4.render(scale);
    	this.branch1.render(scale);
    	this.branch2.render(scale);
    	this.branch3.render(scale);
    	this.branch4.render(scale);
    	this.body.render(scale);
    }

    public void setRotationAngles(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;

    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        this.leg1.rotateAngleX = -0.1F + MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg3.rotateAngleX = 0.1F + MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.rotateAngleX = 0.1F + MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg2.rotateAngleX = -0.1F + MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    public static class ModelRenderLight extends ModelRenderer{

    	int light = 0;
		public ModelRenderLight(ModelBase model, int texOffX, int texOffY) {
			super(model, texOffX, texOffY);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		public void setLight(int light){
			this.light = light;
		}

		public boolean isEnableAlpha(){
			return false;
		}
		@Override
	    @SideOnly(Side.CLIENT)
	    public void render(float scale)
	    {
	        GlStateManager.enableBlend();
	        GlStateManager.disableAlpha();
	        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
//	        GlStateManager.disableLighting();
	        int i = 61680;
	        int j = i % 65536;
	        int k = i / 65536;
	        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
	    	super.render(scale);
	        i = this.light;
	        j = i % 65536;
	        k = i / 65536;
	        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
//	        this.spiderRenderer.setLightmap(entitylivingbaseIn, partialTicks);
	        GlStateManager.enableLighting();
	        GlStateManager.disableBlend();
	        GlStateManager.enableAlpha();
	    }
    }
}
