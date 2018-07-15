package Buff;
import Religion.Religion;

public class SmallBuff extends Buff{
	private double buff_theta[][]=new double[Religion.NUMBER_OF_RELIGION+1][Religion.NUMBER_OF_RELIGION+1];//buff_theta葵
	
	public SmallBuff(String buff_name, int duration_time){//持失切1
		super(buff_name,duration_time);
		
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			for(int j=0;j<=Religion.NUMBER_OF_RELIGION;j++){
				buff_theta[i][j]=1;
			}
		}
	}
	
	public SmallBuff(String buff_name, int duration_time, double buff_theta[][]){//持失切2
		super(buff_name,duration_time);
		this.buff_theta=buff_theta;
	}
	
	public void modifyBuffTheta(int i, int j, double modify_to){
		buff_theta[i][j]=modify_to;
	}
	
	public double getBuffThetaAt(int i, int j){
		return buff_theta[i][j];
	}
}
