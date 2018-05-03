package mods.hinasch.unsaga.ability.specialmove.action;

import java.util.function.Consumer;

import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.util.SoundAndSFX;
import mods.hinasch.lib.util.VecUtil;
import mods.hinasch.lib.world.ScannerNew;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.ability.specialmove.TechRegistry;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageHelper;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IntIdentityHashBiMap;
import net.minecraft.util.math.Vec3d;

public class MovingStates {

	public static Consumer<IMovingStateAdapter>  VELOCITY_ZERO = (ctx)->{
		ctx.getOwner().setVelocity(0, ctx.getOwner().motionY, 0);
	};
	public static Consumer<IMovingStateAdapter>  FLYING_KNEE = (ctx)->{
		Vec3d vec = ctx.getOwner().getLookVec();
		ctx.getOwner().addVelocity(vec.x, 1.0D, vec.z);

		if(ctx.getOwner().onGround){
			ctx.expire();
		}

		ctx.getCollisionEnemies().forEach(in ->{
			in.attackEntityFrom(DamageHelper.create(new AdditionalDamageData(ctx.getInvoker(),General.PUNCH)),ctx.getInvoker().getStrengthHP());
			ctx.expire();
		});

//		ctx.setCancelFall(10);
	};

	public static Consumer<IMovingStateAdapter>  HAWK_BLADE = (ctx)->{
		Vec3d vec = ctx.getOwner().getLookVec().scale(0.3D);



		ctx.getCollisionEnemies().forEach(in ->{
			in.attackEntityFrom(DamageHelper.create(new AdditionalDamageData(ctx.getInvoker(),General.SWORD)),ctx.getInvoker().getStrengthHP());
			in.addPotionEffect(new PotionEffect(UnsagaPotions.DOWN_DEX,ItemUtil.getPotionTime(15)));
		});
		ScannerNew.create().base(ctx.getOwner()).range(1).ready()
		.stream().forEach(pos ->{
			IBlockState state = ctx.getWorld().getBlockState(pos);
			if(state.getBlock() instanceof BlockLeaves || state.getBlock() instanceof BlockTallGrass || state.getBlock() instanceof BlockWeb){
				SoundAndSFX.playBlockBreakSFX(ctx.getWorld(), pos, state);
			}
		});


//		if(ctx.getOwner().onGround){
//
////			double motionX = ctx.getOwner().motionX - vec.x;
////			double motionZ = ctx.getOwner().motionZ - vec.z;
////			ctx.getOwner().setVelocity(motionX >0 ? motionX : 0, 0, motionZ >0 ? motionZ : 0);
//			ctx.getOwner().motionX += vec.x * 0.5D;
//			ctx.getOwner().motionZ += vec.z * 0.5D;
//			ctx.getOwner().motionY = 0;
//		}else{
//			ctx.getOwner().motionX += vec.x;
//			ctx.getOwner().motionZ += vec.z;
//			ctx.getOwner().motionY = -0.5D;
////			ctx.getOwner().setVelocity(ctx.getOwner().motionX + vec.x, -1.0D, ctx.getOwner().motionZ + vec.z);
//		}


		if(ctx.getOwner().onGround){
			ctx.getOwner().addVelocity(vec.x*0.5D, 0, vec.z*0.5D);
		}else{
			ctx.getOwner().addVelocity(vec.x, 0, vec.z);
		}

		if(ctx.getDuration()>ItemUtil.getPotionTime(1)){
			if(ctx.getOwner().motionY>0.0D){
				ctx.getOwner().motionY = 0.0D;
			}else{
				ctx.getOwner().motionY += 0.05D;
			}
		}

//		ctx.setCancelFall(0);
	};
	public static Consumer<IMovingStateAdapter>  BLITZ = (ctx)->{
		Vec3d vec = ctx.getOwner().getLookVec().scale(1.0D);
		ctx.getOwner().addVelocity(vec.x, 0, vec.z);
		ctx.getCollisionEnemies().forEach(in ->{
			in.attackEntityFrom(DamageHelper.create(new AdditionalDamageData(ctx.getInvoker(),General.SPEAR)),ctx.getInvoker().getStrengthHP());

		});
//		ctx.setCancelHurt();
	};
	public static Consumer<IMovingStateAdapter>  GUST = (ctx)->{
		Vec3d vec = ctx.getOwner().getLookVec().scale(0.8D);
		ctx.getOwner().addVelocity(vec.x, 0, vec.z);
		ctx.getCollisionEnemies().forEach(in ->{
			in.attackEntityFrom(DamageHelper.create(new AdditionalDamageData(ctx.getInvoker(),General.SWORD)),ctx.getInvoker().getStrengthHP());
			in.addPotionEffect(new PotionEffect(UnsagaPotions.DOWN_DEX,ItemUtil.getPotionTime(15)));
		});
		ScannerNew.create().base(ctx.getOwner()).range(1).ready()
		.stream().forEach(pos ->{
			IBlockState state = ctx.getWorld().getBlockState(pos);
			if(state.getBlock() instanceof BlockLeaves || state.getBlock() instanceof BlockTallGrass || state.getBlock() instanceof BlockWeb){
				SoundAndSFX.playBlockBreakSFX(ctx.getWorld(), pos, state);
			}
		});

//		ctx.setCancelHurt();
	};
	/**
	 * 稲妻キックと共用
	 */
	public static Consumer<IMovingStateAdapter>  SINKER = (ctx)->{
		Vec3d vec = ctx.getOwner().getLookVec();
		ctx.getOwner().addVelocity(vec.x, -1.0D, vec.z);
		if(ctx.getOwner().onGround){
			ctx.expire();
		}
		ctx.getCollisionEnemies().forEach(in ->{
			in.attackEntityFrom(DamageHelper.create(new AdditionalDamageData(ctx.getInvoker(),General.PUNCH)),ctx.getInvoker().getStrengthHP());
			ctx.expire();

			if(ctx.getInvoker().getActionProperty()==TechRegistry.instance().THUNDER_KICK){
			ctx.getInvoker().playSound(XYZPos.createFrom(ctx.getOwner()), SoundEvents.ENTITY_LIGHTNING_IMPACT, false);
			VecUtil.knockback(ctx.getOwner(), in, 1.0F, 0.2D);
		}else{
			ctx.getInvoker().playSound(XYZPos.createFrom(ctx.getOwner()), SoundEvents.ENTITY_IRONGOLEM_HURT, false);
			in.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,ItemUtil.getPotionTime(12)));
		}

			ctx.getOwner().addVelocity(-vec.x, 0.5D, -vec.z);
		});

		ctx.setCancelFall(0);
	};

	public static final IntIdentityHashBiMap<Consumer<IMovingStateAdapter>> REGISTRY = new IntIdentityHashBiMap(30);


	static{
		REGISTRY.put(BLITZ, 0);
		REGISTRY.put(FLYING_KNEE, 1);
		REGISTRY.put(GUST, 2);
		REGISTRY.put(HAWK_BLADE, 3);
		REGISTRY.put(SINKER, 4);
		REGISTRY.put(VELOCITY_ZERO, 5);
	}
}
