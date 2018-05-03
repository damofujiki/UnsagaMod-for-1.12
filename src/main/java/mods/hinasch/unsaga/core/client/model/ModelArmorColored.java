package mods.hinasch.unsaga.core.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModelArmorColored extends ModelBiped{

	protected ItemStack itemStackArmor;

	protected ModelBiped parent;


	public ModelArmorColored(float f) {
		super(f);
	}

	public void setStack(ItemStack is){
		this.itemStackArmor = is;
	}

	public void setParent(ModelBiped parent){
		this.parent = parent;

	}

	public ModelBiped getParent(){
		return this.parent;
	}
	@Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {

//		UnsagaMod.logger.trace("armoar", "called");
        this.setLivingAnimations((EntityLivingBase) par1Entity, par2, par3, par4);

        Item item = this.itemStackArmor.getItem();
        if(item instanceof IItemColor){
        	IItemColor colorItem = (IItemColor) item;
            int k = colorItem.colorMultiplier(itemStackArmor, 0);
            //Unsaga.debug("armor:"+k,this.getClass());
            float f31 = (float)(k >> 16 & 255) / 255.0F;
            float f32 = (float)(k >> 8 & 255) / 255.0F;
            float f33 = (float)(k & 255) / 255.0F;
            GlStateManager.color(f31, f32, f33, 1.0F);
        }


        super.render(par1Entity, par2, par3, par4, par5, par6, par7);
//        if(this.parent!=null){
////    		UnsagaMod.logger.trace("armoar", "called");
//        	parent.render(par1Entity, par2, par3, par4, par5, par6, par7);
//        }

//        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
//        GlStateManager.pushMatrix();
//        if (this.isChild)
//        {
//            float f6 = 2.0F;
//
//
//            int k = this.itemStackArmor.getItem().getColorFromItemStack(itemStackArmor, 0);
//            //Unsaga.debug("armor:"+k,this.getClass());
//            float f31 = (float)(k >> 16 & 255) / 255.0F;
//            float f32 = (float)(k >> 8 & 255) / 255.0F;
//            float f33 = (float)(k & 255) / 255.0F;
//            GlStateManager.color(f31, f32, f33, 1.0F);
//
//            GlStateManager.scale(1.5F / f6, 1.5F / f6, 1.5F / f6);
//            GlStateManager.translate(0.0F, 16.0F * par7, 0.0F);
//            this.bipedHead.render(par7);
//            GlStateManager.popMatrix();
//            GlStateManager.pushMatrix();
//            GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
//            GlStateManager.translate(0.0F, 24.0F * par7, 0.0F);
//            this.bipedBody.render(par7);
//            this.bipedRightArm.render(par7);
//            this.bipedLeftArm.render(par7);
//            this.bipedRightLeg.render(par7);
//            this.bipedLeftLeg.render(par7);
//            this.bipedHeadwear.render(par7);
//
//        }
//        else
//        {
//
//            if (par1Entity.isSneaking())
//            {
//                GlStateManager.translate(0.0F, 0.2F, 0.0F);
//            }
//            int k = this.itemStackArmor.getItem().getColorFromItemStack(itemStackArmor, 0);
//
//            float f31 = (float)(k >> 16 & 255) / 255.0F;
//            float f32 = (float)(k >> 8 & 255) / 255.0F;
//            float f33 = (float)(k & 255) / 255.0F;
//            GlStateManager.color(f31, f32, f33, 1.0F);
//            this.bipedHead.render(par7);
//            this.bipedBody.render(par7);
//            this.bipedRightArm.render(par7);
//            this.bipedLeftArm.render(par7);
//            this.bipedRightLeg.render(par7);
//            this.bipedLeftLeg.render(par7);
//            this.bipedHeadwear.render(par7);
//        }
//        GlStateManager.popMatrix();
    }
}
