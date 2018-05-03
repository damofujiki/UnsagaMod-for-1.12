//package mods.hinasch.unsaga.core.client.render.tileentity;
//
//import com.google.common.base.Supplier;
//
//import mods.hinasch.lib.client.ClientHelper;
//import mods.hinasch.unsaga.core.client.render.projectile.IChestModel;
//import mods.hinasch.unsaga.core.tileentity.TileEntityUnsagaChest;
//import mods.hinasch.unsaga.skillpanel.SkillPanels;
//import mods.hinasch.unsaga.skillpanel.SkillPanels.SkillPanel;
//import net.minecraft.client.model.ModelChest;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ResourceLocation;
//
//public class RenderBlockChest extends TileEntitySpecialRenderer{
//
//	SkillPanel roadAdviser = SkillPanels.instance().roadAdviser;
//	SkillPanel cavernExprorer = SkillPanels.instance().cavernExprorer;
//	private static final ResourceLocation chestTex = new ResourceLocation("textures/entity/chest/normal.png");
//	private ModelChest modelChest;
//	private ModelChest modelChestOpened = new Supplier<ModelChest>(){
//
//		@Override
//		public ModelChest get() {
//			float ff1 = 1.0F;
//			ModelChest model = new ModelChest();
//			model.chestLid.rotateAngleX = -(ff1 * ((float)Math.PI / 2F));
//			return model;
//		}
//	}.get();
//	public RenderBlockChest(){
//		this.modelChest = new ModelChest();
//	}
//	@Override
//	public void renderTileEntityAt(TileEntity te, double x,
//			double y, double z, float partialTicks, int p_180535_9_) {
//		TileEntityUnsagaChest.VisibleType type = null;
//		boolean visible = false;
//		if(te instanceof TileEntityUnsagaChest){
//			TileEntityUnsagaChest chest = (TileEntityUnsagaChest) te;
//			type = chest.getType();
//		}
//
//		if(type!=null){
//			switch(type){
//			case CAVE:
//				if(SkillPanels.hasPanel(getWorld(), ClientHelper.getPlayer(), cavernExprorer)){
//					visible = true;
//				}
//				break;
//			case OVERWORLD:
//				if(SkillPanels.hasPanel(getWorld(), ClientHelper.getPlayer(), roadAdviser)){
//					visible = true;
//				}
//				break;
//			case VISIBLE:
//				visible = true;
//				break;
//			default:
//				break;
//
//			}
//		}
//		GlStateManager.pushMatrix();
//		this.bindTexture(chestTex);
//		GlStateManager.translate(x, y + 1.0D, z+1.0D);
//		GlStateManager.rotate(180F, 1.0F, 0, 0);
//		float ff1 = 1.0F;
//
//		if(visible){
//			if(te instanceof IChestModel){
//				if(((IChestModel)te).isOpened()){
//					modelChestOpened.renderAll();
//				}else{
//					modelChest.renderAll();
//				}
//			}
//		}
//
//
//		GlStateManager.popMatrix();
//	}
//
//}
