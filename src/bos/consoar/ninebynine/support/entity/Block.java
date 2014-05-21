package bos.consoar.ninebynine.support.entity;

import java.io.Serializable;

public class Block implements Serializable{
	private static final long serialVersionUID = 7777076814987835487L;
	private int state;
	private int num;
	public Block() {
		super();
		this.state = 0;
		this.num = 0;
	}
	public Block(int state, int num) {
		super();
		this.state = state;
		this.num = num;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "Block [state=" + state + ", num=" + num + "]";
	}
	
}
