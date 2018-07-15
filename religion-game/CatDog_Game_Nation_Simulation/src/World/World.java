package World;
import java.util.*;
import Buff.*;
import Religion.Religion;
public class World {
	public static final int NUMBER_OF_NATION=3;//��ü ���� ����
	
	private int population;//��ü �α���
	private int population_by_religion[]=new int[Religion.NUMBER_OF_RELIGION+1];//��ü ������ �α���
	
	private int most_dominate_religion;
	
	private double delta[][]=new double[NUMBER_OF_NATION][NUMBER_OF_NATION];//���ټ� delta��
	private static final double INIT_DELTA[][]={//***������ ���� ���� �뷱�� ��������
			{10,10,1},
			{10,10,10},
			{1,10,10}
	};
	
	private Nation nations[]=new Nation[NUMBER_OF_NATION];//��ü ���� �迭
	private Religion religions[]=new Religion[Religion.NUMBER_OF_RELIGION+1];//��ü ���� �迭
	
	private ArrayList<BigBuff> big_buff_list;//BigBuff ����Ʈ
	
	public World(){
		big_buff_list=new ArrayList<BigBuff>();
		
		religions[0]=new Religion("����",0);//�̺κ��� �ܺο��� ������ ������ ����� �о������ �ڵ带 ��� �Ѵ�.
		religions[1]=new Religion("�ѿ�",1);//��� �� ���� ���־�� ��(�÷��̾� ���� �̸�����)
		religions[2]=new Religion("���",2);
		religions[3]=new Religion("����",3);
		religions[4]=new Religion("����",4);
		nations[0]=new Nation(this,"Korea",10000,Religion.PLAYER);
		nations[1]=new Nation(this,"Japan",20000,Religion.ATHEISM);
		nations[2]=new Nation(this,"China",100000,Religion.ATHEISM);
		
		updatePopulation();
		updatePopulationByReligion();
		updateMostDominateReligion();
		updateDeltaValue();
	}
	
	
	public void updatePopulation(){//�α��� ���
		population=0;
		for(int i=0;i<NUMBER_OF_NATION;i++){
			population+=nations[i].getPopulation();
		}
	}
	
	public void updatePopulationByReligion(){//������ �α��� ���
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			population_by_religion[i]=0;
		}
		for(int i=0;i<NUMBER_OF_NATION;i++){
			for(int j=0;j<=Religion.NUMBER_OF_RELIGION;j++){
				population_by_religion[j]+=nations[i].getPopulationByReligionAt(j);
			}
		}
	}
	
	private void updateMostDominateReligion(){//���� �켼�� ������ ������Ʈ �Լ�
		int tmp_religion=0;
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			if(population_by_religion[tmp_religion]<population_by_religion[i]){
				tmp_religion=i;
			}
		}
		most_dominate_religion=tmp_religion;
	}
	
	private void resetDeltaValue(){//delta�� ���� �Լ�
		for(int i=0;i<NUMBER_OF_NATION;i++){
			for(int j=0;j<NUMBER_OF_NATION;j++){
				delta[i][j]=INIT_DELTA[i][j];
			}
		}
	}
	
	public void updateDeltaValue(){//theta�� ������Ʈ �Լ�(����)
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
				if(delta[i][j]>7){//***������ ���� ���� �뷱�� ��������
					nations[j].unlockReligion(nations[i].getMostDominateReligion());
					SmallBuff sb = new SmallBuff("��",1);
					sb.modifyBuffTheta(nations[j].getMostDominateReligion(), nations[i].getMostDominateReligion(), religions[nations[i].getMostDominateReligion()].getOff());//***������ ���� ���� �뷱�� ��������
					nations[j].addSmallBuff(sb);
				}
				if(delta[i][j]>17){//***������ ���� ���� �뷱�� ��������
					SmallBuff sb = new SmallBuff("������ ��",1);//������ ���� ������ ���� ������ ��ұ� �����...
					sb.modifyBuffTheta(nations[j].getMostDominateReligion(), nations[i].getMostDominateReligion(), religions[nations[i].getMostDominateReligion()].getOff()*2);//***������ ���� ���� �뷱�� ��������
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
