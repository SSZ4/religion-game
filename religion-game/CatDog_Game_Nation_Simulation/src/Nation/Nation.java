package Nation;
import java.util.*;
import Buff.*;

public class Nation {
	private final int number_of_religion=4;//���� ��
	
	private String name;//���� �̸�
	private int population;//���� �α���
	private int population_by_religion[]=new int[number_of_religion+1];//������ ���� �α���
	
	private double theta[][]=new double[number_of_religion+1][number_of_religion+1];//theta��
	private final double init_theta[][]={
			{10,1,1,1,1},
			{1,10,1,1,1},
			{1,1,10,1,1},
			{1,1,1,10,1},
			{1,1,1,1,10}
	};
	
	private ArrayList<SmallBuff> small_buff_list;//���� �ɷ��ִ� �������
	
	private int most_dominate_religion;//���� �켼�� ����
	
	private boolean religion_expressed[]=new boolean[number_of_religion+1];//���� ����
	
	public Nation(String name, int population, int religion){//������:�����̸�,�α���,�� ������ ���� ���� ����
		this.name=name;//���� �̸� �ʱ�ȭ
		this.population=population;//���� �α��� �ʱ�ȭ
		
		small_buff_list = new ArrayList<SmallBuff>();
		
		if(religion==0){//0:����
			for(int i=0;i<=number_of_religion;i++){
				religion_expressed[i]=false;
			}
			religion_expressed[0]=true;
			for(int i=1;i<=number_of_religion;i++){
				population_by_religion[i]=0;
			}
			population_by_religion[0]=population;
			//������ ��� �ٸ� ����=�α���, �ٸ�����=0
		}else{//������ �������
			for(int i=0;i<=number_of_religion;i++){
				religion_expressed[i]=false;
			}
			religion_expressed[0]=true;
			religion_expressed[religion]=true;
			
			for(int i=1;i<=number_of_religion;i++){
				population_by_religion[i]=0;
			}
			population_by_religion[religion]=population/10;
			population_by_religion[0]=population-population_by_religion[religion];
			//������ ������� �� ����=�α����� 10�ۼ�Ʈ, ������ ����, �ٸ�����=0
		}
		
		updateMostDominateReligion();
		updateThetaValue();
	}
	
	private void updateMostDominateReligion(){//���� �켼�� ������ ������Ʈ �Լ�
		int tmp_religion=0;
		for(int i=0;i<=number_of_religion;i++){
			if(population_by_religion[tmp_religion]<population_by_religion[i]){
				tmp_religion=i;
			}
		}
		most_dominate_religion=tmp_religion;
	}
	
	private void resetThetaValue(){//theta�� ���� �Լ�
		for(int i=0;i<=number_of_religion;i++){
			for(int j=0;j<=number_of_religion;j++){
				theta[i][j]=init_theta[i][j];
			}
		}
	}
	
	public void updateThetaValue(){//theta�� ������Ʈ �Լ�(����)
		System.out.println("updating theta value");
		resetThetaValue();
		for(int a=0;a<small_buff_list.size();a++){
			SmallBuff sb=small_buff_list.get(a);
			for(int i=0;i<=number_of_religion;i++){
				for(int j=0;j<=number_of_religion;j++){
					theta[i][j]*=sb.getBuffThetaAt(i, j);
				}
			}
		}
		for(int i=0;i<=number_of_religion;i++){
			if(religion_expressed[i]==false){
				for(int j=0;j<=number_of_religion;j++){
					theta[j][i]=0;
				}
			}
		}
		for(int i=0;i<=number_of_religion;i++){
			for(int j=0;j<=number_of_religion;j++){
				System.out.print(theta[i][j]+"\t");
			}
			System.out.println("");
		}
	}
	
	public void updatePopulation(){
		for(int i=0;i<small_buff_list.size();i++){
			try{
				small_buff_list.get(i).countTheClock();
			}catch(ExpiredBuffException ebe){
				System.out.println("Buff:\""+small_buff_list.get(i).getBuffName()+"\" Removed ");
				small_buff_list.remove(i);
				updateThetaValue();
			}
		}
		int new_population=0;
		int new_population_by_religion[]=new int[number_of_religion+1];
		for(int i=0;i<=number_of_religion;i++){
			new_population_by_religion[i]=0;
		}
		for(int i=0;i<=number_of_religion;i++){
			double sum_of_theta=0;
			int sum_of_religious_population=0;
			for(int j=0;j<=number_of_religion;j++){
				sum_of_theta+=theta[i][j];
			}
			for(int j=1;j<=number_of_religion;j++){
				new_population_by_religion[j]+=(int)(population_by_religion[i]*(theta[i][j]/sum_of_theta));
				sum_of_religious_population+=(int)(population_by_religion[i]*(theta[i][j]/sum_of_theta));
			}
			new_population_by_religion[0]+=population_by_religion[i]-sum_of_religious_population;
			if(new_population_by_religion[0]<0){
				new_population_by_religion[0]=0;
			}
		}
		for(int i=0;i<=number_of_religion;i++){
			new_population+=new_population_by_religion[i];
			population_by_religion[i]=(int)new_population_by_religion[i];
		}
		population=new_population;
		
		updateMostDominateReligion();
	}
	
	public void addSmallBuff(SmallBuff sb){
		System.out.println("Buff:\""+sb.getBuffName()+"\" Added for "+sb.getDurationTime());
		small_buff_list.add(sb);
		updateThetaValue();
	}
	
	public void unlockReligion(int i){
		religion_expressed[i]=true;
	}
	public void lockReligion(int i){
		religion_expressed[i]=false;
	}
	//
	public String getName(){return name;}
	public int getPopulation(){return population;}
	public int getMostDominateReligion(){return most_dominate_religion;}
	public int getPopulationByReligionAt(int i){return population_by_religion[i];}
	public boolean getReligionExpressedAt(int i){return religion_expressed[i];}
	
}