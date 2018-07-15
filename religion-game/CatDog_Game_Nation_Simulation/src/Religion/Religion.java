package Religion;

public class Religion {
	public static final int NUMBER_OF_RELIGION=4;
	public static final int ATHEISM=0;
	public static final int PLAYER=1;
	
	private final String name;
	private final int index;
	
	private double def;//방어력
	private double off;//전도력
	
	public Religion(String name, int index){
		this.name=name;
		this.index=index;
		
		def=1.5;
		off=1;
	}
	
	
	public String getName(){
		return name;
	}
	public int getIndex(){
		return index;
	}
	
	public void setDef(double def){
		this.def=def;
	}
	
	public void setOff(double off){
		this.off=off;
	}
	public double getDef(){
		return def;
	}
	public double getOff(){
		return off;
	}
}
