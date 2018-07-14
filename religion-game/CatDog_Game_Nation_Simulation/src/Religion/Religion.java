package Religion;

public class Religion {
	public static final int NUMBER_OF_RELIGION=4;
	public static final int ATHEISM=0;
	public static final int PLAYER=1;
	
	private final String name;
	private final int index;
	
	public Religion(String name, int index){
		this.name=name;
		this.index=index;
	}
	
	public String getName(){
		return name;
	}
	public int getIndex(){
		return index;
	}
}
