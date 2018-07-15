package World;
import java.util.*;
import Buff.*;
import Religion.Religion;

public class Nation {
	private final String name;//나라 이름
	private int population;//나라 인구수
	private int population_by_religion[]=new int[Religion.NUMBER_OF_RELIGION+1];//종교별 나라 인구수
	
	private double theta[][]=new double[Religion.NUMBER_OF_RELIGION+1][Religion.NUMBER_OF_RELIGION+1];//theta값
	private static final double INIT_THETA[][]={//***임의의 값을 붙임 밸런스 조정가능
			{10,1,1,1,1},
			{1,10,1,1,1},
			{1,1,10,1,1},
			{1,1,1,10,1},
			{1,1,1,1,10}
	};
	
	private ArrayList<SmallBuff> small_buff_list;//나라에 걸려있는 버프목록
	
	private int most_dominate_religion;//가장 우세한 종교
	
	private boolean religion_expressed[]=new boolean[Religion.NUMBER_OF_RELIGION+1];//종교 발현
	
	private World world;
	
	public Nation(World world, String name, int population, int religion){//생성자:나라이름,인구수,그 나라의 종교 선택 가능
		this.world=world;
		this.name=name;//나라 이름 초기화
		this.population=population;//나라 인구수 초기화
		
		small_buff_list = new ArrayList<SmallBuff>();
		
		if(religion==Religion.ATHEISM){//0:무교
			for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
				religion_expressed[i]=false;
			}
			religion_expressed[Religion.ATHEISM]=true;
			for(int i=1;i<=Religion.NUMBER_OF_RELIGION;i++){
				population_by_religion[i]=0;
			}
			population_by_religion[0]=population;
			//무교일 경우 다른 무교=인구수, 다른종교=0
		}else{//종교가 있을경우
			for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
				religion_expressed[i]=false;
			}
			religion_expressed[Religion.ATHEISM]=true;
			religion_expressed[religion]=true;
			
			for(int i=1;i<=Religion.NUMBER_OF_RELIGION;i++){
				population_by_religion[i]=0;
			}
			population_by_religion[religion]=population/10;//***임의의 값을 붙임 밸런스 조정가능
			population_by_religion[Religion.ATHEISM]=population-population_by_religion[religion];
			//종교가 있을경우 그 종교=인구수의 10퍼센트, 나머지 무교, 다른종교=0
		}
		
		updateMostDominateReligion();
		updateThetaValue();
	}
	
	private void updateMostDominateReligion(){//가장 우세한 종교의 업데이트 함수
		int tmp_religion=0;
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			if(population_by_religion[tmp_religion]<population_by_religion[i]){
				tmp_religion=i;
			}
		}
		most_dominate_religion=tmp_religion;
	}
	
	private void resetThetaValue(){//theta값 리셋 함수
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			for(int j=0;j<=Religion.NUMBER_OF_RELIGION;j++){
				theta[i][j]=INIT_THETA[i][j];
			}
		}
	}
	
	public void updateThetaValue(){//theta값 업데이트 함수(재계산)
		resetThetaValue();
		for(int a=0;a<small_buff_list.size();a++){
			SmallBuff sb=small_buff_list.get(a);
			for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
				for(int j=0;j<=Religion.NUMBER_OF_RELIGION;j++){
					theta[i][j]*=sb.getBuffThetaAt(i, j);
				}
			}
		}
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			if(religion_expressed[i]==false){
				for(int j=0;j<=Religion.NUMBER_OF_RELIGION;j++){
					theta[j][i]=0;
				}
			}
		}
	}
	
	public void update(){
		//가장 우세한 종교 버프
		SmallBuff dominate_sb = new SmallBuff("가장 우세한 종교",1);
		dominate_sb.modifyBuffTheta(getMostDominateReligion(), getMostDominateReligion(), world.getReligionsAt(getMostDominateReligion()).getDef());//***임의의 값을 붙임 밸런스 조정가능
		addSmallBuff(dominate_sb);
		
		
		for(int i=0;i<small_buff_list.size();i++){
			try{
				small_buff_list.get(i).countTheClock();
			}catch(ExpiredBuffException ebe){
				System.out.println("SmallBuff:\""+small_buff_list.get(i).getBuffName()+"\" Removed ");
				small_buff_list.remove(i);
				updateThetaValue();
			}
		}
		int new_population=0;
		int new_population_by_religion[]=new int[Religion.NUMBER_OF_RELIGION+1];
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			new_population_by_religion[i]=0;
		}
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			double sum_of_theta=0;
			int sum_of_religious_population=0;
			for(int j=0;j<=Religion.NUMBER_OF_RELIGION;j++){
				sum_of_theta+=theta[i][j];
			}
			for(int j=1;j<=Religion.NUMBER_OF_RELIGION;j++){
				new_population_by_religion[j]+=(int)(population_by_religion[i]*(theta[i][j]/sum_of_theta));
				sum_of_religious_population+=(int)(population_by_religion[i]*(theta[i][j]/sum_of_theta));
			}
			new_population_by_religion[Religion.ATHEISM]+=population_by_religion[i]-sum_of_religious_population;
			if(new_population_by_religion[Religion.ATHEISM]<0){
				new_population_by_religion[Religion.ATHEISM]=0;
			}
		}
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			new_population+=new_population_by_religion[i];
			population_by_religion[i]=(int)new_population_by_religion[i];
		}
		population=new_population;
		
		updateMostDominateReligion();
	}
	
	public void addSmallBuff(SmallBuff sb){
		System.out.println("SmallBuff:\""+sb.getBuffName()+"\" Added for "+sb.getDurationTime());
		small_buff_list.add(sb);
		updateThetaValue();
	}
	
	public void unlockReligion(int i){
		religion_expressed[i]=true;
	}
	public void lockReligion(int i){
		if(i!=Religion.ATHEISM){
			religion_expressed[i]=false;
		}
	}
	//
	public String getName(){return name;}
	public int getPopulation(){return population;}
	public int getMostDominateReligion(){return most_dominate_religion;}
	public int getPopulationByReligionAt(int i){return population_by_religion[i];}
	public boolean getReligionExpressedAt(int i){return religion_expressed[i];}
	
}
