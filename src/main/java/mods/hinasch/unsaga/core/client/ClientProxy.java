package mods.hinasch.unsaga.core.client;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.core.client.event.EventMouseClicked;
import mods.hinasch.unsaga.core.client.event.EventRenderGameOverlay;
import mods.hinasch.unsaga.core.client.event.EventRenderLivingEffect;
import mods.hinasch.unsaga.core.client.render.entity.RenderEntityChest;
import mods.hinasch.unsaga.core.client.render.entity.RenderEntityEffect;
import mods.hinasch.unsaga.core.client.render.entity.RenderFireWall;
import mods.hinasch.unsaga.core.client.render.entity.RenderRuffleTree;
import mods.hinasch.unsaga.core.client.render.entity.RenderShadowServant;
import mods.hinasch.unsaga.core.client.render.entity.RenderSignalTree;
import mods.hinasch.unsaga.core.client.render.entity.RenderStormEater;
import mods.hinasch.unsaga.core.client.render.entity.RenderTreasureSlime;
import mods.hinasch.unsaga.core.client.render.projectile.RenderBeam;
import mods.hinasch.unsaga.core.client.render.projectile.RenderBullet;
import mods.hinasch.unsaga.core.client.render.projectile.RenderCustomArrow;
import mods.hinasch.unsaga.core.client.render.projectile.RenderIceNeedle;
import mods.hinasch.unsaga.core.client.render.projectile.RenderMagicBall;
import mods.hinasch.unsaga.core.client.render.projectile.RenderThrowableWeapon;
import mods.hinasch.unsaga.core.entity.mob.EntityPoisonEater;
import mods.hinasch.unsaga.core.entity.mob.EntityRuffleTree;
import mods.hinasch.unsaga.core.entity.mob.EntityShadowServant;
import mods.hinasch.unsaga.core.entity.mob.EntitySignalTree;
import mods.hinasch.unsaga.core.entity.mob.EntityStormEater;
import mods.hinasch.unsaga.core.entity.mob.EntityTreasureSlimeNew;
import mods.hinasch.unsaga.core.entity.passive.EntityBeam;
import mods.hinasch.unsaga.core.entity.passive.EntityEffectSpawner;
import mods.hinasch.unsaga.core.entity.passive.EntityFireWall;
import mods.hinasch.unsaga.core.entity.passive.EntityUnsagaChest;
import mods.hinasch.unsaga.core.entity.projectile.EntityBullet;
import mods.hinasch.unsaga.core.entity.projectile.EntityCustomArrow;
import mods.hinasch.unsaga.core.entity.projectile.EntityFlyingAxe;
import mods.hinasch.unsaga.core.entity.projectile.EntityIceNeedle;
import mods.hinasch.unsaga.core.entity.projectile.EntityJavelin;
import mods.hinasch.unsaga.core.entity.projectile.EntityMagicBall;
import mods.hinasch.unsaga.core.entity.projectile.EntityThrowingKnife;
import mods.hinasch.unsaga.core.net.CommonProxy;
import mods.hinasch.unsaga.element.EventRenderText;
import mods.hinasch.unsaga.init.UnsagaItemRegistry;
import mods.hinasch.unsaga.init.UnsagaItems;
import mods.hinasch.unsaga.material.UnsagaMaterials;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{

	ModelAttacher modelAttacher;
	mods.hinasch.unsagamagic.client.ModelAttacher modelAttacherMagic;
	KeyBindingUnsaga keyBindings;
	NonNullList<XYZPos> debugPosBank = NonNullList.withSize(6, XYZPos.ZERO);

	@Override
	public void registerRenderers() {
		// TODO 自動生成されたメソッド・スタブ
		this.modelAttacher = new ModelAttacher();
		this.modelAttacher.register();
		this.modelAttacherMagic = new mods.hinasch.unsagamagic.client.ModelAttacher();
		this.modelAttacherMagic.register();






//		this.keyBindings = new KeyBindingUnsaga();
//		HSLibs.registerEvent(this.keyBindings);

//		HSLibs.registerEvent(new EvnetMouseClicked());

	}

	@Override
	public XYZPos getDebugPos(int bank){
		return this.debugPosBank.get(bank);
	}
	@Override
	public void setDebugPos(int bank,XYZPos pos){
		this.debugPosBank.set(bank, pos);
	}
	@Override
	public void registerKeyHandlers() {
		this.keyBindings = new KeyBindingUnsaga();
		HSLibs.registerEvent(this.keyBindings);

	}

	@Override
	public void registerEvents(){
		HSLibs.registerEvent(new EventRenderText());
		HSLibs.registerEvent(EventRenderGameOverlay.RenderEnemyStatus.getEvent());
		HSLibs.registerEvent(EventRenderGameOverlay.RenderPlayerStatus.getEvent());
		HSLibs.registerEvent(new EventRenderLivingEffect());
		HSLibs.registerEvent(new EventMouseClicked());
	}
	@Override
	public KeyBindingUnsaga getKeyBindings(){
		return this.keyBindings;
	}

	@Override
	public void registerEntityRenderers(){
		ItemStack axe = new ItemStack(Items.IRON_AXE);
//		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingAxe.class, manager ->new RenderThrowableItemNew(manager,axe));
		RenderingRegistry.registerEntityRenderingHandler(EntityCustomArrow.class, manager ->new RenderCustomArrow(manager));
//		RenderingRegistry.registerEntityRenderingHandler(EntityDamageableItem.class, manager ->new RenderItemAttackable(manager,Minecraft.getMinecraft().getRenderItem()));
//		RenderingRegistry.registerEntityRenderingHandler(EntityBoulder.class, manager -> new RenderThrowable("textures/entity/projectiles/boulder.png",manager));
//		RenderingRegistry.registerEntityRenderingHandler(EntityFireArrow.class, manager -> new RenderThrowableItem<EntityFireArrow>(manager,1.0F,Items.FIRE_CHARGE,0));
//		RenderingRegistry.registerEntityRenderingHandler(EntitySolutionLiquid.class, manager -> new RenderThrowable("textures/entity/projectiles/slimeball.png",manager));
//		RenderingRegistry.registerEntityRenderingHandler(EntityBlaster.class, manager -> new RenderThrowable("textures/entity/projectiles/blaster.png",manager));
//		RenderingRegistry.registerEntityRenderingHandler(EntityBubbleBlow.class, manager -> new RenderThrowable("textures/entity/projectiles/bubble.png",manager));
//		RenderingRegistry.registerEntityRenderingHandler(EntityShadow.class, manager -> new RenderShadow(manager,1.0F));
		RenderingRegistry.registerEntityRenderingHandler(EntityShadowServant.class, RenderShadowServant::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTreasureSlimeNew.class, manager -> new RenderTreasureSlime(manager, new ModelSlime(16), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityUnsagaChest.class, RenderEntityChest::new );
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, manager -> new RenderBullet(manager, 1.0F));
		RenderingRegistry.registerEntityRenderingHandler(EntityRuffleTree.class, RenderRuffleTree::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBeam.class, RenderBeam::new);
		ItemStack knife = UnsagaItemRegistry.instance().createStack(UnsagaItems.KNIFE, UnsagaMaterials.IRON, 0);
//		RenderingRegistry.registerEntityRenderingHandler(EntityThrowingKnife.class, manager -> new RenderThrowableItemNew(manager,knife));
		RenderingRegistry.registerEntityRenderingHandler(EntityFireWall.class, RenderFireWall::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySignalTree.class, RenderSignalTree::new);
//		RenderingRegistry.registerEntityRenderingHandler(EntityIceNeedle.class, manager -> new RenderThrowable("textures/entity/projectiles/needle.png",manager));
//		RenderingRegistry.registerEntityRenderingHandler(EntityFireArrow.class, manager -> new RenderThrowable("textures/entity/projectiles/fireball.png",manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityStormEater.class, manager -> new RenderStormEater(manager,EntityStormEater.class));
		RenderingRegistry.registerEntityRenderingHandler(EntityPoisonEater.class, manager -> new RenderStormEater(manager,EntityPoisonEater.class));

		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingAxe.class, manager -> new RenderThrowableWeapon(manager,axe));
		RenderingRegistry.registerEntityRenderingHandler(EntityThrowingKnife.class, manager -> new RenderThrowableWeapon(manager,knife));
		RenderingRegistry.registerEntityRenderingHandler(EntityMagicBall.class, RenderMagicBall::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityIceNeedle.class, RenderIceNeedle::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityJavelin.class, manager -> new RenderThrowableWeapon(manager,knife));
		RenderingRegistry.registerEntityRenderingHandler(EntityEffectSpawner.class, RenderEntityEffect::new);
	}
}
