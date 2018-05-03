package mods.hinasch.unsaga.core.inventory.container;

import java.util.List;

import com.google.common.collect.Lists;

import mods.hinasch.lib.misc.XY;

public abstract class HexMatrixAdapter<T> {


	static final XY CENTER = new XY(1,1);
	static final List<XY> AROUND = Lists.newArrayList(new XY(0,0),new XY(1,0),new XY(0,1),new XY(2,1),new XY(0,2),new XY(1,2),new XY(2,2));

	public abstract T getMatrix(int x,int y);

	public T getMatrix(XY xy){
		return this.getMatrix(xy.getX(),xy.getY());
	}


	public List<T> getAllElements(){
		List<T> list = Lists.newArrayList();
		for(XY xy:this.getAll()){
			if(this.getMatrix(xy)!=null){
				list.add(this.getMatrix(xy));
			}
		}
		return list;
	}
	public List<XY> getAll(){
		List<XY> list = Lists.newArrayList();
		for(int y=0;y<3;y++){
			int size = y==1 ? 3 : 2;
			for(int x=0;x<size;x++){
				list.add(new XY(x,y));
			}
		}
		return list;
	}
	public List<T> getAroundElements(int x,int y){
		List<XY> xys = this.getAround(x, y);
		List<T> list = Lists.newArrayList();
		if(!xys.isEmpty()){
			for(XY xy:xys){
				if(this.getMatrix(xy)!=null){
					list.add(this.getMatrix(xy));
				}
			}
		}
		return list;
	}
	public List<XY> getAround(int x,int y){
		List<XY> list = Lists.newArrayList();
		for(int i=-1;i<2;i++){
			for(int j=-1;j<2;j++){
				if(this.chckValidMatrix(x+i, y+j)){
					if(!(x==x+i && y==y+j)){
						list.add(new XY(x+i,y+j));
					}

				}
			}
		}

		return list;
	}
	public boolean chckValidMatrix(int x,int y){
		if(y==0 || y==2){
			if(x>=0 && x<2){
				return true;
			}
		}
		if(y==1){
			if(x>=0 && x<3){
				return true;
			}
		}
		return false;
	}

	public abstract boolean isSame(T o1,T o2);

	public List<XY> getJointed(){
		List<XY> xys = Lists.newArrayList();
		for(XY xy:this.getAll()){
			T t = this.getMatrix(xy);
			if(t!=null){
				for(T around:this.getAroundElements(xy.getX(),xy.getY())){
					if(around!=null){
						if(this.isSame(t, around)){
							xys.add(xy);
						}
					}
				}
			}
		}
		return xys;
	}
	public List<XY> checkLine(){
//		List<XY> xys = Lists.newArrayList();
//		int index =0;
//		int index2 = 5- index;

		if(this.getMatrix(CENTER)!=null){
			T center = this.getMatrix(CENTER);
			if(this.isSame(center, this.getMatrix(0,1)) && this.isSame(center, this.getMatrix(2,1))){
				return Lists.newArrayList(CENTER,new XY(0,1),new XY(2,1));
			}
			if(this.isSame(center, this.getMatrix(0,0)) && this.isSame(center, this.getMatrix(1,2))){
				return Lists.newArrayList(CENTER,new XY(0,0),new XY(1,2));
			}
			if(this.isSame(center, this.getMatrix(1,0)) && this.isSame(center, this.getMatrix(0,2))){
				return Lists.newArrayList(CENTER,new XY(1,0),new XY(0,2));
			}
//			for(int i=0;i<3;i++){
//
//				if(ListHelper.stream(this.getMatrix(AROUND.get(index)),this.getMatrix(CENTER),this.getMatrix(AROUND.get(index2)))
//						.filter(new Predicate<T>(){
//
//							@Override
//							public boolean apply(T input) {
//								// TODO 自動生成されたメソッド・スタブ
//								return isSame(getMatrix(CENTER),input);
//							}}).count()==3
//						)
//				{
//					xys.addAll(Lists.newArrayList(CENTER,AROUND.get(index),AROUND.get(index2)));
//				}
//			}
		}


		return Lists.newArrayList();
	}


}
