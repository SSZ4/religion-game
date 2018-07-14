package World;
import java.util.*;
import Buff.*;
import Religion.Religion;
public class World {
	public static final int NUMBER_OF_NATION=3;//전체 나라 개수
	
	private int population;//전체 인구수
	private int population_by_religion[]=new int[Religion.NUMBER_OF_RELIGION+1];//전체 종교별 인구수
	
	private int most_dominate_religion;
	
	private double delta[][]=new double[NUMBER_OF_NATION][NUMBER_OF_NATION];//접근성 delta값
	private static final double INIT_DELTA[][]={//***임의의 값을 붙임 밸런스 조정가능
			{10,10,1},
			{10,10,10},
			{1,10,10}
	};
	
	private Nation nations[]=new Nation[NUMBER_OF_NATION];//전체 나라 배열
	private Religion religions[]=new Religion[Religion.NUMBER_OF_RELIGION+1];//전체 종교 배열
	
	private ArrayList<BigBuff> big_buff_list;//BigBuff 리스트
	
	public World(){
		big_buff_list=new ArrayList<BigBuff>();
		
		religions[0]=new Religion("무교",0);//이부분은 외부에서 데이터 파일을 만들어 읽어들어오는 코드를 써야 한다.
		religions[1]=new Religion("한영",1);//모두 다 정의 되있어야 함(플레이어 종교 이름빼고)
		religions[2]=new Religion("대건",2);
		religions[3]=new Religion("지훈",3);
		religions[4]=new Religion("승진",4);
		nations[0]=new Nation(this,"Korea",10000,Religion.PLAYER);
		nations[1]=new Nation(this,"Japan",20000,Religion.ATHEISM);
		nations[2]=new Nation(this,"China",100000,Religion.ATHEISM);
		
		updatePopulation();
		updatePopulationByReligion();
		updateMostDominateReligion();
		updateDeltaValue();
	}
	
	
	public void updatePopulation(){//인구수 계산
		population=0;
		for(int i=0;i<NUMBER_OF_NATION;i++){
			population+=nations[i].getPopulation();
		}
	}
	
	public void updatePopulationByReligion(){//종교별 인구수 계산
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			population_by_religion[i]=0;
		}
		for(int i=0;i<NUMBER_OF_NATION;i++){
			for(int j=0;j<=Religion.NUMBER_OF_RELIGION;j++){
				population_by_religion[j]+=nations[i].getPopulationByReligionAt(j);
			}
		}
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
	
	private void resetDeltaValue(){//delta값 리셋 함수
		for(int i=0;i<NUMBER_OF_NATION;i++){
			for(int j=0;j<NUMBER_OF_NATION;j++){
				delta[i][j]=INIT_DELTA[i][j];
			}
		}
	}
	
	public void updateDeltaValue(){//theta값 업데이트 함수(재계산)
		resetDeltaValue();
		for(int a=0;a<big_buff_list.size();a++){
			BigBuff bb=big_buff_list.get(a);
			for(int i=0;i<NUMBER_OF_NATION;i++){
				for(int j=0;j<NUMBER_OF_NATION;j++){
					delta[i][j]*=bb.getBuffDeltaAt(i, j);
				}
			}
		}
	}
	
	public void addBigBuff(BigBuff bb){
		System.out.println("BigBuff:\""+bb.getBuffName()+"\" Added for "+bb.getDurationTime());
		big_buff_list.add(bb);
		updateDeltaValue();
	}
	
	public void update(){
		for(int i=0;i<big_buff_list.size();i++){
			try{
				big_buff_list.get(i).countTheClock();
			}catch(ExpiredBuffException ebe){
				System.out.println("BigBuff:\""+big_buff_list.get(i).getBuffName()+"\" Removed ");
				big_buff_list.remove(i);
				updateDeltaValue();
			}
		}
		
		for(int i=0;i<NUMBER_OF_NATION;i++){
			for(int j=0;j<NUMBER_OF_NATION;j++){
				if(delta[i][j]>7){//***임의의 값을 붙임 밸런스 조정가능
					nations[j].unlockReligion(nations[i].getMostDominateReligion());
					SmallBuff sb = new SmallBuff("고무",1);
					sb.modifyBuffTheta(nations[j].getMostDominateReligion(), nations[i].getMostDominateReligion(), religions[nations[i].getMostDominateReligion()].getOff());//***임의의 값을 붙임 밸런스 조정가능
					nations[j].addSmallBuff(sb);
				}
				if(delta[i][j]>17){//***임의의 값을 붙임 밸런스 조정가능
					SmallBuff sb = new SmallBuff("강력한 고무",1);//무교는 전도 버프를 주지 않으면 어떠할까 고민중...
					sb.modifyBuffTheta(nations[j].getMostDominateReligion(), nations[i].getMostDominateReligion(), religions[nations[i].getMostDominateReligion()].getOff()*2);//***임의의 값을 붙임 밸런스 조정가능
					nations[j].addSmallBuff(sb);
				}
			}
		}
		
		for(int i=0;i<NUMBER_OF_NATION;i++){
			nations[i].update();
		}
		
		updatePopulation();
		updatePopulationByReligion();
		updateMostDominateReligion();
	}
	
	
	public int getPopulation(){return population;}
	public int getMostDominateReligion(){return most_dominate_religion;}
	public int getPopulationByReligionAt(int i){return population_by_religion[i];}
	public Nation getNationsAt(int i){return nations[i];}
	public Religion getReligionsAt(int i){return religions[i];}
}
