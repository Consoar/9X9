package bos.consoar.ninebynine.support;

import bos.consoar.ninebynine.support.entity.Block;

public class Common {
	public static final int width = 9;
	public static final int height = 9;
	
	public static Block[] BlocksCopy(Block[]src ,int length){
		Block[] dst=new Block[length];
		for (int i=0;i<length;++i){
			dst[i]=src[i];
		}
		return dst;
	}
}
