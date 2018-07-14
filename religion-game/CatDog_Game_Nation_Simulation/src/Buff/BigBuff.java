package Buff;
import Nation.Nation;

public class BigBuff extends Buff{
	private double buff_delta[][]=new double[Nation.NUMBER_OF_NATION][Nation.NUMBER_OF_NATION];//buff_delta葵
	
	public BigBuff(String buff_name, int duration_time){//持失切1
		super(buff_name,duration_time);
		
		for(int i=0;i<Nation.NUMBER_OF_NATION;i++){
			for(int j=0;j<Nation.NUMBER_OF_NATION;j++){
				buff_delta[i][j]=1;
			}
		}
	}
	
	public BigBuff(String buff_name, int duration_time, double buff_delta[][]){//持失切2
		super(buff_name,duration_time);
		this.buff_delta=buff_delta;
	}
	
	public void modifyBuffDelta(int i, int j, double modify_to){
		buff_delta[i][j]=modify_to;
	}
	
	public double getBuffDeltaAt(int i, int j){
		return buff_delta[i][j];
	}
}
