package mods.hinasch.unsaga.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * beam by Unknown
 */
@SideOnly(Side.CLIENT)
public class ModelBeam extends ModelBase {
    public ModelRenderer beamAll;
    public ModelRenderer glow;
    public ModelRenderer beam;

    public ModelBeam() {
        this.textureWidth = 32;
        this.textureHeight = 32;

        this.glow = new ModelRenderer(this, 0, 0);
        this.glow.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.glow.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 32);
        this.beam = new ModelRenderer(this, 0, 0);
        this.beam.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.beam.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 32);
    }

    public void setTextureOffset(int offset){
    	this.glow.setTextureOffset(offset,0);
    	this.beam.setTextureOffset(offset, 0);
    }

    public void setCoreRotate(float rotate){
    	this.beam.rotateAngleZ = rotate;
    }
    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {

        GlStateManager.disableLighting();

//        UnsagaMod.logger.trace("yaw", rotationYaw);
//	    int light = entity.getBrightnessForRender();
//        int i = 61680;
//        int j = i % 65536;
//        int k = i / 65536;
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
//    	super.render(scale);

//        UnsagaMod.logger.trace("brithgt", entity.getBrightnessForRender());
        float lastx = OpenGlHelper.lastBrightnessX;
        float lasty = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        GlStateManager.color(1.0F, 1.0F, 1.0F,1.0F);
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
//        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.0F);
//        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale,entity);
        this.beam.render(scale);
//        this.setRotationAngles(beam, 0, rotationYaw, 0);


        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastx, lasty);
//        i = light;
//        j = i % 65536;
//        k = i / 65536;
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);



        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F,0.125F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//        GlStateManager.depthMask(false);
        this.glow.render(scale);
//        this.setRotationAngles(glow, 0, rotationYaw, 0);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
//        GlStateManager.enableCull();
//        GlStateManager.enableTexture2D()


        ;
        GlStateManager.disableAlpha();
//        GlStateManager.depthMask(true);
    }

    public void setRotationAngles(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
