package mods.hinasch.unsaga.core.client.gui;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.client.GuiMapBase;
import mods.hinasch.lib.misc.XY;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.chest.FieldChestType;
import mods.hinasch.unsaga.core.entity.passive.EntityUnsagaChest;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class GuiUnsagaMap extends GuiMapBase{


	/** マップ上のアイコンの種類。SPAWN=スポーンポイント*/
	public static enum IconType{
		SPAWN,PLAYER,CHEST,PLAIN;

		public XY getOffset(){
			switch(this){
			case CHEST:
				return new XY(16,168);
			case PLAIN:
				return new XY(0,168);
			case PLAYER:
				return new XY(8,168);
			case SPAWN:
				return new XY(24,168);
			default:
				break;

			}
			return new XY(0,168);
		}
	}

	public static class EntityIcon extends Icon{

		IconType type;
		Entity entity;
		BlockPos corner;
		int resolution;
		public EntityIcon(int id, int x, int y,IconType type, Entity entity,BlockPos corner,int reso) {
			super(id, x, y, type.getOffset().getX(), type.getOffset().getY(), true);
			this.type = type;
			this.entity = entity;
			this.corner = corner;
			this.resolution = reso;
		}

		public int getX(){
			return (entity.getPosition().getX() - corner.getX())*resolution;
		}

		public int getY(){
			return (entity.getPosition().getZ() - corner.getZ())*resolution;
		}
		@Override
		public int getSize(){
			return 8;
		}

		public Entity getEntity(){
			return this.entity;
		}
	}

	public static class ChestIcon extends Icon{

		IconType type;
		BlockPos pos;
		FieldChestType chestType;
		public ChestIcon(int id, int x, int y,IconType type,BlockPos pos,FieldChestType t) {
			super(id, x, y, type.getOffset().getX(), type.getOffset().getY(), true);
			this.type = type;
			this.pos = pos;
			this.chestType = t;
		}

		@Override
		public int getSize(){
			return 8;
		}

		public FieldChestType getChestType(){
			return this.chestType;
		}
		public BlockPos getPos(){
			return this.pos;
		}
	}
	public static final int RESOLUTION_MAP = 3;
	public static final Set<Potion> DETECT_POTIONS = Sets.newHashSet(UnsagaPotions.DETECT_GOLD,UnsagaPotions.DETECT_TREASURE);


	public GuiUnsagaMap(EntityPlayer ep,Type maptype) {
		super(ep,maptype,RESOLUTION_MAP);


		this.world.getEntitiesWithinAABB(EntityLivingBase.class, HSLibs.getBounding(ep.getPosition(), this.getMaxPos(), this.getMaxPos()))
		.forEach(in ->{
			//			checkPotionExpire(in, UnsagaPotions.instance().DETECTED);
			if(in.isPotionActive(MobEffects.GLOWING) || in==this.ep){
				BlockPos enp = in.getPosition().subtract(getMapCorner());
				this.drawMapEntity(in, enp);

				IconType icon = IconType.PLAIN;
				if(in instanceof EntityUnsagaChest){
					icon = IconType.CHEST;
				}
				if(in==ep){
					icon = IconType.PLAYER;
				}
				this.addIcon(new EntityIcon(0, enp.getX() * resolution, enp.getZ() * resolution, icon,in,this.getMapCorner(),resolution));
			}

		});
		ChestPosCache.getChestPosCache(0).forEach(in ->{
			FieldChestType type = in.getFieldChestType();
			if(in.getChestPos().isPresent() && SkillPanelAPI.hasPanel(ep, type.getSuitableSkill())){
				BlockPos p1 = in.getChestPos().get().subtract(this.getMapCorner());
				BlockPos enp = this.toMapPos(p1, 0, this.getMaxPos());
				this.addIcon(new ChestIcon(0, enp.getX() * resolution, enp.getZ() * resolution, IconType.CHEST,in.getChestPos().get(),in.getFieldChestType()));

			}
		});

		ChestPosCache.getChestPosCache(1).forEach(in ->{
			FieldChestType type = in.getFieldChestType();
			if(in.getChestPos().isPresent() && ep.isPotionActive(UnsagaPotions.DETECT_TREASURE)){
				BlockPos p1 = in.getChestPos().get().subtract(this.getMapCorner());
				BlockPos enp = this.toMapPos(p1, 0, this.getMaxPos());
				this.addIcon(new ChestIcon(0, enp.getX() * resolution, enp.getZ() * resolution, IconType.CHEST,in.getChestPos().get(),FieldChestType.BLOCK));

			}
		});
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		this.drawEntity();


		if(this.type==GuiMapBase.Type.FIELD){
			this.drawSpawnPoint();
		}
	}

	@Override
	public String getGuiTextureName(){
		return UnsagaMod.MODID+":textures/gui/container/black.png";
	}


	public void drawSpawnPoint(){
		int max = MAP_MAX_SIZE/resolution;
		BlockPos spawnPoint = ep.getEntityWorld().getSpawnPoint();
		BlockPos mapOrigin = ep.getPosition().add(-max/2, 0, -max/2);
		int dx = spawnPoint.getX() - mapOrigin.getX();
		int dz = spawnPoint.getZ() - mapOrigin.getZ();
		dx = dx<0 ? 0 : dx; //枠越えたら欄外
		dz = dz<0 ? 0 : dz;
		dx = dx>max ? max : dx;
		dz = dz>max ? max : dz;
		this.drawTexturedModalRect(dx*resolution, dz*resolution, 24, 168, 8, 8);
	}

	private void drawMapEntity(EntityLivingBase in,BlockPos enp){
		GlStateManager.disableBlend();
		ClientHelper.bindTextureToTextureManager(new ResourceLocation(this.getGuiTextureName()));
		if(in!=this.ep){
			if(in instanceof EntityUnsagaChest){
				this.drawTexturedModalRect(enp.getX()*resolution, enp.getZ()*resolution, 16, 168, 8, 8);
			}else{
				this.drawTexturedModalRect(enp.getX()*resolution, enp.getZ()*resolution, 0, 168, 8, 8);
			}

		}else{
			this.drawTexturedModalRect(enp.getX()*resolution, enp.getZ()*resolution, 8, 168, 8, 8);
		}

		GlStateManager.enableBlend();
	}

	public BlockPos getMapCorner(){
		return calcMapCornerPos(ep.getPosition(),getMaxPos());
	}

	public int getMaxPos(){
		return MAP_MAX_SIZE/resolution;
	}
	public static BlockPos calcMapCornerPos(BlockPos center,int maxsize){
		return center.add(-maxsize/2,0,-maxsize/2);
	}
	public void drawEntity(){
//		int max_pos = MAP_MAX_SIZE/resolution;
//		BlockPos mapCorner = getMapCorner(ep.getPosition(),max_pos); //マップの基点
//		this.world.getEntitiesWithinAABB(EntityLivingBase.class, HSLibs.getBounding(ep.getPosition(), 30, 4))
//		.forEach(in ->{
//			//			checkPotionExpire(in, UnsagaPotions.instance().DETECTED);
//			if(in.isPotionActive(MobEffects.GLOWING) || in==this.ep){
//				BlockPos enp = in.getPosition().subtract(getMapCorner());
//				this.drawMapEntity(in, enp);
//			}
//
//		});
		//
		//		int chunkMax = MAP_MAX_SIZE / 16 + 1;
		//		Set<Chunk> chunks = Sets.newHashSet();
		//		for(int i=0;i<chunkMax;i++){
		//			Chunk chunk = world.getChunkFromBlockCoords(mapOrigin.add(i*16, 0, i*16));
		////			chunks.add(chunk);
		//			if(UnsagaChunkCapability.ADAPTER.hasCapability(chunk)){
		//				IUnsagaChunkCapability instance = UnsagaChunkCapability.ADAPTER.getCapability(chunk);
		//				FieldChestType type = instance.getChunkChestInfo().getFieldChestType();
		//				if(instance.getChunkChestInfo().getBlockPos().isPresent() && SkillPanelAPI.hasPanel(ep, type.getSuitableSkill())){
		//					BlockPos p1 = instance.getChunkChestInfo().getBlockPos().get().subtract(mapOrigin);
		//					BlockPos enp = this.cut(p1, 0, max_pos);
		//					GlStateManager.disableBlend();
		//					GlStateManager.disableAlpha();
		//					ClientHelper.bindTextureToTextureManager(new ResourceLocation(this.getGuiTextureName()));
		//					this.drawTexturedModalRect(enp.getX()*resolution, enp.getZ()*resolution, 16, 168, 8, 8);
		//					GlStateManager.enableBlend();
		//					GlStateManager.disableAlpha();
		//				}
		//			}
		//		}
//
//		ChestPosCache.getChestPosCache().forEach(in ->{
//			FieldChestType type = in.getFieldChestType();
//			if(in.getChestPos().isPresent() && SkillPanelAPI.hasPanel(ep, type.getSuitableSkill())){
//				BlockPos p1 = in.getChestPos().get().subtract(this.getMapCorner());
//
//				BlockPos enp = this.toMapPos(p1, 0, this.getMaxPos());
//				GlStateManager.disableBlend();
//				GlStateManager.disableAlpha();
//				ClientHelper.bindTextureToTextureManager(new ResourceLocation(this.getGuiTextureName()));
//				this.drawTexturedModalRect(enp.getX()*resolution, enp.getZ()*resolution, 16, 168, 8, 8);
//				GlStateManager.enableBlend();
//				GlStateManager.disableAlpha();
//
//
//			}
//		});
		//		this.checkPotionExpire(ep, UnsagaPotions.instance().detectTreasure);
		//		UnsagaMod.logger.trace("pos", ChestPosCache.instance().getCache().asMap().values());
		//		ChestPosCache.instance().values().stream().filter(in -> in.getType()!=FieldChestInfo.Type.EXPIRED).forEach(in ->{
		//			BlockPos pos = in.getPos().subtract(mapOrigin);
		//			pos = this.cut(pos, 0, max_pos);
		////			if(pos.getX()>0 && pos.getX()<max_pos && pos.getZ()>0 && pos.getZ()<max_pos){
		//				GlStateManager.disableBlend();
		//				ClientHelper.bindTextureToTextureManager(new ResourceLocation(this.getGuiTextureName()));
		//				this.drawTexturedModalRect(pos.getX()*resolution, pos.getZ()*resolution, 16, 168, 8, 8);
		//				GlStateManager.enableBlend();
		////			}
		//
		//		});
//		world.getEntitiesWithinAABB(EntityUnsagaChest.class, HSLibs.getBounding(this.ep.getPosition(), 16*3, 256))
//		.forEach(in ->{
//			if(SkillPanelAPI.hasPanel(ep, in.getVisibilityType().getSuitableSkill())){
//				BlockPos p1 = in.getPosition().subtract(this.getMapCorner());
//				BlockPos enp = this.toMapPos(p1, 0, this.getMaxPos());
//				GlStateManager.disableBlend();
//				GlStateManager.disableAlpha();
//				ClientHelper.bindTextureToTextureManager(new ResourceLocation(this.getGuiTextureName()));
//				this.drawTexturedModalRect(enp.getX()*resolution, enp.getZ()*resolution, 16, 168, 8, 8);
//				GlStateManager.enableBlend();
//				GlStateManager.disableAlpha();
//			}
//
//		});
//		DETECT_POTIONS.forEach(potionIn ->{
//			if(LivingHelper.isStateActive(ep, potionIn)){
//				if(LivingHelper.getCapability(ep).getState(potionIn) instanceof PotionOreDetector.Effect){
//					PotionOreDetector.Effect effect = (Effect) LivingHelper.getCapability(ep).getState(potionIn);
//					effect.getOrePosList().forEach(in ->{
//						BlockPos enp = in.subtract(this.getMapCorner());
//						GlStateManager.disableBlend();
//						ClientHelper.bindTextureToTextureManager(new ResourceLocation(this.getGuiTextureName()));
//						this.drawTexturedModalRect(enp.getX()*resolution, enp.getZ()*resolution, 16, 168, 8, 8);
//						GlStateManager.enableBlend();
//					});
//				}
//				//				if(EntityStateCapability.adapter.hasCapability(ep)){
//				//					StateOreDetecter state = (StateOreDetecter) EntityStateCapability.adapter.getCapability(ep).getState(StateRegistry.instance().stateOreDetecter);
//				//					state.getOrePosList().forEach(in ->{
//				//						BlockPos enp = in.subtract(mapOrigin);
//				//						GlStateManager.disableBlend();
//				//						ClientHelper.bindTextureToTextureManager(new ResourceLocation(this.getGuiTextureName()));
//				//						this.drawTexturedModalRect(enp.getX()*resolution, enp.getZ()*resolution, 16, 168, 8, 8);
//				//						GlStateManager.enableBlend();
//				//					});
//				//				}
//			}
//		});
		//		if(this.ep.isPotionActive(UnsagaPotions.instance().DETECT_TREASURE)){
		//			if(EntityStateCapability.adapter.hasCapability(ep)){
		//				StateOreDetecter state = (StateOreDetecter) EntityStateCapability.adapter.getCapability(ep).getState(StateRegistry.instance().stateOreDetecter);
		//				state.getOrePosList().forEach(in ->{
		//					BlockPos enp = in.subtract(mapOrigin);
		//					GlStateManager.disableBlend();
		//					ClientHelper.bindTextureToTextureManager(new ResourceLocation(this.getGuiTextureName()));
		//					this.drawTexturedModalRect(enp.getX()*resolution, enp.getZ()*resolution, 16, 168, 8, 8);
		//					GlStateManager.enableBlend();
		//				});
		//			}
		//		}
	}

	public BlockPos toMapPos(BlockPos pos,int min,int max){
		int x = pos.getX();
		int z = pos.getZ();
		x = MathHelper.clamp(x, min, max);
		z = MathHelper.clamp(z, min, max);
		return new BlockPos(x,0,z);
	}

	//	private void checkPotionExpire(EntityLivingBase el,Potion potion){
	//		if(el.getActivePotionEffect(potion)!=null){
	//			if(el.getActivePotionEffect(potion).getDuration()<=0){
	//				el.removePotionEffect(potion);
	//			}
	//		}
	//	}
	//

//	@Override
//	public void drawScreen(int mouseX, int mouseY, float partialTicks)
//	{
////        this.drawDefaultBackground();
//		super.drawScreen(mouseX, mouseY, partialTicks);
////		this.renderHoveredToolTip(mouseX, mouseY);
////		int max_pos = MAP_MAX_SIZE/resolution;
//		ChestPosCache.getChestPosCache().forEach(in ->{
//			FieldChestType type = in.getFieldChestType();
////			BlockPos mapOrigin = getMapCorner(ep.getPosition(),MAP_MAX_SIZE);
//			if(in.getChestPos().isPresent() && SkillPanelAPI.hasPanel(ep, type.getSuitableSkill())){
//				BlockPos p1 = in.getChestPos().get().subtract(this.getMapCorner());
//
//				BlockPos point = this.toMapPos(p1, 0, this.getMaxPos());
////				GlStateManager.disableBlend();
////				GlStateManager.disableAlpha();
////				ClientHelper.bindTextureToTextureManager(new ResourceLocation(this.getGuiTextureName()));
////				this.drawTexturedModalRect(enp.getX()*resolution, enp.getZ()*resolution, 16, 168, 8, 8);
////				GlStateManager.enableBlend();
////				GlStateManager.disableAlpha();
//
//				if(this.isMouseInBox(mouseX - this.getWindowStartX(), mouseY - this.getWindowStartY(), point.getX() * resolution, point.getZ() * resolution,point.getX() * resolution+8, point.getZ() * resolution+8)){
////					if(ic.isVisible(this)){
//						this.drawHoveringText(in.getChestPos().get().toString(), mouseX, mouseY);
////					}
//
//				}
//			}
//		});
////		for(Icon ic:this.iconList){
////			if(this.isMouseInBox(mouseX - this.getWindowStartX(), mouseY - this.getWindowStartY(), ic.x, ic.y, ic.x+16, ic.y+16)){
////				if(ic.isVisible(this)){
////					this.drawHoveringText(this.getIconHoverText(ic), mouseX, mouseY);
////				}
////
////			}
////		}
//	}

	@Override
	public List<String> getIconHoverText(Icon id){
		if(id instanceof ChestIcon){
			ChestIcon chestIcon = (ChestIcon) id;
			List<String> list = Lists.newArrayList();
			list.add("Type:"+chestIcon.getChestType().getName());
			list.add(chestIcon.getPos().toString());
			return list;
		}
		if(id instanceof EntityIcon){
			EntityIcon chestIcon = (EntityIcon) id;
			List<String> list = Lists.newArrayList();

			if(chestIcon.getEntity()==this.ep){
				EntityPlayer ep = (EntityPlayer) chestIcon.getEntity();
				list.add("Player");
				list.add(ep.getPosition().toString());
				list.add(ep.getHorizontalFacing().toString());
			}else{
				list.add(chestIcon.getEntity().getName());
			}
			return list;
		}
		return Lists.newArrayList();
	}
}
