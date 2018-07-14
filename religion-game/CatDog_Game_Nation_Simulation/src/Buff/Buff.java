package Buff;

public class Buff {
	protected String buff_name;
	protected int duration_time;
	
	public Buff(String buff_name, int duration_time){
		this.buff_name=buff_name;
		this.duration_time=duration_time;
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
