package Buff;

public class SmallBuff {
	private final int number_of_religion=4;//종교 수
	
	private String buff_name;//버프이름
	private int duration_time;//지속시간
	
	private double buff_theta[][]=new double[number_of_religion+1][number_of_religion+1];//buff_theta값
	
	public SmallBuff(String buff_name, int duration_time){//생성자1
		this.buff_name=buff_name;
		this.duration_time=duration_time;
		
		for(int i=0;i<=number_of_religion;i++){
			for(int j=0;j<=number_of_religion;j++){
				buff_theta[i][j]=1;
			}
		}
	}
	
	public SmallBuff(String buff_name, int duration_time, double buff_theta[][]){//생성자2
		this.buff_name=buff_name;
		this.duration_time=duration_time;
		this.buff_theta=buff_theta;
	}
	
	public void modifyBuffTheta(int i, int j, double modify_to){
		buff_theta[i][j]=modify_to;
	}
	
	public double getBuffThetaAt(int i, int j){
		return buff_theta[i][j];
	}
	
	public void countTheClock() throws ExpiredBuffException{
		if(duration_time==0){
			throw new ExpiredBuffException();
		}else{
			duration_time--;
		}
	}
	
	public String getBuffName(){return buff_name;}
	public int getDurationTime(){return duration_time;}
}
