package mods.hinasch.unsaga.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFireWall extends ModelBase{

    public ModelRenderer fireWall;

    public ModelFireWall() {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.fireWall = new ModelRenderer(this, 0, 0);
        this.fireWall.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.fireWall.addBox(-8.0F, -8F, -8F, 16, 16, 16);
//        this.setRotationAngles(this.fireWall, 3.141592653589793F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
    	super.render(entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);
//    	this.setRotationAngles(this.fireWall, 0, (float) Math.toRadians(-rotationYaw), 0);
        this.fireWall.render(scale);
    }

    public void setRotationAngles(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
