//package mods.hinasch.unsaga.core.client.event;
//
//import java.util.List;
//
//import javax.vecmath.Matrix4f;
//
//import org.apache.commons.lang3.tuple.Pair;
//
//import com.google.common.collect.Lists;
//
//import mods.hinasch.unsaga.UnsagaMod;
//import mods.hinasch.unsaga.core.client.render.projectile.RenderThrowableItemNew;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.renderer.block.model.BakedQuad;
//import net.minecraft.client.renderer.block.model.IBakedModel;
//import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
//import net.minecraft.client.renderer.block.model.ItemOverrideList;
//import net.minecraft.client.renderer.block.model.ModelResourceLocation;
//import net.minecraft.client.renderer.block.model.ModelRotation;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.util.EnumFacing;
//import net.minecraftforge.client.event.ModelBakeEvent;
//import net.minecraftforge.client.model.BakedItemModel;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//public class EventModelBake {
//
//
//	@SubscribeEvent
//	public void onModelBake(ModelBakeEvent e){
////		UnsagaMod.logger.trace(this.getClass().getName(), ");
//		List<ModelResourceLocation> list = Lists.newArrayList();
//		e.getModelRegistry().getKeys().forEach(in ->{
//			if(in.getResourceDomain().equals(UnsagaMod.MODID)){
//				IBakedModel model = e.getModelRegistry().getObject(in);
//				UnsagaMod.logger.trace(this.getClass().getName(), in.getResourcePath().toString(),model);
//				if(model instanceof BakedItemModel){
//					list.add(in);
//				}
//			}
//		});
//
//		list.forEach(in ->{
//			IBakedModel base = e.getModelRegistry().getObject(in);
//			e.getModelRegistry().putObject(in, new Baked((BakedItemModel) base));
//		});
//	}
//
//
//
//	public static class Baked implements IBakedModel{
//
//		public IBakedModel base;
//
//		public Baked(BakedItemModel base){
//			this.base = base;
//		}
//		@Override
//		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
//			// TODO 自動生成されたメソッド・スタブ
//			return base.getQuads(state, side, rand);
//		}
//
//		@Override
//		public boolean isAmbientOcclusion() {
//			// TODO 自動生成されたメソッド・スタブ
//			return base.isAmbientOcclusion();
//		}
//
//		@Override
//		public boolean isGui3d() {
//			// TODO 自動生成されたメソッド・スタブ
//			return base.isGui3d();
//		}
//
//		@Override
//		public boolean isBuiltInRenderer() {
//			// TODO 自動生成されたメソッド・スタブ
//			return base.isBuiltInRenderer();
//		}
//
//		@Override
//		public TextureAtlasSprite getParticleTexture() {
//			// TODO 自動生成されたメソッド・スタブ
//			return base.getParticleTexture();
//		}
//
//		@Override
//		public ItemOverrideList getOverrides() {
//			// TODO 自動生成されたメソッド・スタブ
//			return base.getOverrides();
//		}
//
//
//        @Override
//        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type)
//        {
////        	UnsagaMod.logger.trace("camera", type);
//        	if(type==RenderThrowableItemNew.THROW){
//        		Pair<? extends IBakedModel,Matrix4f> pair = base.handlePerspective(TransformType.NONE);
//        		Matrix4f mat = pair.getRight();
////        		TRSRTransformation.rotate(mat, EnumFacing.NORTH);
//        		return pair.of(pair.getLeft(), ModelRotation.X0_Y180.getMatrix());
//        	}
//        	if(type==RenderThrowableItemNew.THROW2){
//        		Pair<? extends IBakedModel,Matrix4f> pair = base.handlePerspective(TransformType.NONE);
//        		Matrix4f mat = pair.getRight();
////        		TRSRTransformation.rotate(mat, EnumFacing.NORTH);
//        		return pair.of(pair.getLeft(), ModelRotation.X0_Y270.getMatrix());
//        	}
//            return base.handlePerspective(type);
//        }
//	}
//}
//
