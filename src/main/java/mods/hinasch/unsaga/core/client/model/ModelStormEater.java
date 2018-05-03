package mods.hinasch.unsaga.core.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;

public class ModelStormEater extends ModelBase{

	public static float ALPHA = 0.7F;
    public ModelRenderer cloud1;
    public ModelRenderer cloud2;
    public ModelRenderer cloud3;
    public ModelRenderer cloud4;
    public ModelRenderer electric;

    public ModelStormEater() {
        this.textureWidth = 64;
        this.textureHeight = 48;

        this.cloud1 = new ModelRenderer(this, 0, 0);
        this.cloud1.setRotationPoint(-1.1F, 0.0F, 0.0F);
        this.cloud1.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8);
        this.cloud2 = new ModelRenderer(this, 0, 0);
        this.cloud2.setRotationPoint(2.0F, 11.1F, 2.8F);
        this.cloud2.addBox(0.0F, 0.0F, 0.0F, 6, 6, 6);
        this.cloud3 = new ModelRenderer(this, 3, 6);
        this.cloud3.setRotationPoint(0.0F, 16.5F, 5.0F);
        this.cloud3.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
        this.cloud4 = new ModelRenderer(this, 0, 0);
        this.cloud4.setRotationPoint(0.0F, 4.6F, 5.0F);
        this.cloud4.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8);
        this.electric = new ModelRenderer(this, 0, 16);
        this.electric.setRotationPoint(-4.0F, 2.0F, -0.3F);
        this.electric.addBox(0.0F, 0.0F, 0.0F, 16, 16, 16);

        this.textureWidth = 64;
        this.textureHeight = 48;


    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, ALPHA);
        this.cloud1.render(scale);
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, ALPHA);
        this.cloud2.render(scale);
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, ALPHA);
        this.cloud3.render(scale);
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, ALPHA);
        this.cloud4.render(scale);
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
//        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE,GlStateManager.DestFactor.ONE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        this.electric.render(scale);
        i = entity.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.disableBlend();
//        GlStateManager.disableAlpha();
//        GlStateManager.enableLighting();

    }


    public void setRotationAngles(ModelRenderer modelRenderer, float x, float y, float z) {
//        modelRenderer.rotateAngleX = x;
//        modelRenderer.rotateAngleY = y;
//        modelRenderer.rotateAngleZ = z;
    }
}
