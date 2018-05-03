package mods.hinasch.unsaga.core.client.render.entity;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.entity.mob.EntityTreasureSlimeNew;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



@SideOnly(Side.CLIENT)
public class RenderTreasureSlime extends RenderLiving<EntityTreasureSlimeNew>
{
    private static final ResourceLocation slimeTextures = new ResourceLocation(UnsagaMod.MODID+":textures/entity/mob/slime.png");

    public RenderTreasureSlime(RenderManager p_i46141_1_, ModelBase p_i46141_2_, float p_i46141_3_)
    {
        super(p_i46141_1_, p_i46141_2_, p_i46141_3_);
        this.addLayer(new LayerTreasureSlimeGel(this));
    }

    @Override
    public void doRender(EntityTreasureSlimeNew p_177124_1_, double p_177124_2_, double p_177124_4_, double p_177124_6_, float p_177124_8_, float p_177124_9_)
    {
        this.shadowSize = 0.25F * (float)p_177124_1_.getSlimeSize();
        super.doRender(p_177124_1_, p_177124_2_, p_177124_4_, p_177124_6_, p_177124_8_, p_177124_9_);
    }

    @Override
    protected void preRenderCallback(EntityTreasureSlimeNew slime, float p_77041_2_)
    {
        float f1 = (float)slime.getSlimeSize();
        float f2 = (slime.prevSquishFactor + (slime.squishFactor - slime.prevSquishFactor) * p_77041_2_) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        GlStateManager.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
//        GlStateManager.scale(1.0F, 1.0F, 1.0F);
//        EntityTreasureSlimeNew.Type type = slime.getSlimeType();
//        type.setRenderColor();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTreasureSlimeNew entity)
    {
        return slimeTextures;
    }

//    public void doRender(EntityLiving entity, double x, double y, double z, float p_76986_8_, float partialTicks)
//    {
//        this.doRender((EntityTreasureSlime)entity, x, y, z, p_76986_8_, partialTicks);
//    }

//    @Override
//    protected void preRenderCallback(EntityTreasureSlime p_77041_1_, float p_77041_2_)
//    {
//        this.preRenderCallback(p_77041_1_, p_77041_2_);
//    }

//    public void doRender(EntityTreasureSlime entity, double x, double y, double z, float p_76986_8_, float partialTicks)
//    {
//        this.doRender((EntityTreasureSlime)entity, x, y, z, p_76986_8_, partialTicks);
//    }

//    @Override
//    protected ResourceLocation getEntityTexture(EntityTreasureSlime entity)
//    {
//        return this.getEntityTexture((EntityTreasureSlime)entity);
//    }

//    @Override
//    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
//    {
//        this.doRender((EntityTreasureSlime)entity, x, y, z, p_76986_8_, partialTicks);
//    }

    @SideOnly(Side.CLIENT)
    public class LayerTreasureSlimeGel implements LayerRenderer
    {
        private final RenderTreasureSlime slimeRenderer;
        private final ModelBase slimeModel = new ModelSlime(0);

        public LayerTreasureSlimeGel(RenderTreasureSlime p_i46111_1_)
        {
            this.slimeRenderer = p_i46111_1_;
        }

        public void doRenderLayer(EntityTreasureSlimeNew slime, float p_177159_2_, float p_177159_3_, float p_177159_4_, float p_177159_5_, float p_177159_6_, float p_177159_7_, float p_177159_8_)
        {
            if (!slime.isInvisible())
            {
                EntityTreasureSlimeNew.Type type = slime.getSlimeType();
                type.setRenderColor();
//                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableNormalize();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);

                this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
                this.slimeModel.render(slime, p_177159_2_, p_177159_3_, p_177159_5_, p_177159_6_, p_177159_7_, p_177159_8_);
                GlStateManager.disableBlend();
                GlStateManager.disableNormalize();
            }
        }

        public boolean shouldCombineTextures()
        {
            return true;
        }

        @Override
        public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
        {
            this.doRenderLayer((EntityTreasureSlimeNew)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
        }
    }
}