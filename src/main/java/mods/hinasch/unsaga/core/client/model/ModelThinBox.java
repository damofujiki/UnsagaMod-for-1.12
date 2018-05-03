package mods.hinasch.unsaga.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 *
 * 薄い板
 *
 */
public class ModelThinBox extends ModelBase{

	ModelRenderer modelBox;

	public ModelThinBox(){
	    textureWidth = 32;
	    textureHeight = 32;

	    this.modelBox = new ModelRenderer(this,0,0);
	    this.modelBox.addBox(-8,0,-8,16,0,16);
	}

	  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	  {
	    super.render(entity, f, f1, f2, f3, f4, f5);
	    setRotationAngles(f, f1, f2, f3, f4, f5,entity);
	    this.modelBox.render(f5);
	  }

	  private void setRotation(ModelRenderer model, float x, float y, float z)
	  {
	    model.rotateAngleX = x;
	    model.rotateAngleY = y;
	    model.rotateAngleZ = z;
	  }

	  public void rotate(float rotate){
		  this.modelBox.rotateAngleY = rotate;
	  }
}
