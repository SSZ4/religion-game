using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;

public class Nation : MonoBehaviour {
	private readonly int number_of_religion = 4;//종교 수 readonly = final
	public string nationname;//나라 이름
	public int population;//나라 인구수
	public int religion;//종교 설정
	private int most_dominate_religion;//가장 우세한 종교

	private float[,] theta; //=new double[number_of_religion + 1][number_of_religion + 1];//theta값
	private readonly float[,] init_theta={
			{10.0f,3.0f,3.0f,3.0f,3.0f},
			{3.0f,10.0f,3.0f,3.0f,3.0f},
			{3.0f,3.0f,10.0f,3.0f,3.0f},
			{3.0f,3.0f,3.0f,10.0f,3.0f},
			{3.0f,3.0f,3.0f,3.0f,10.0f}
	};

	//private ArrayList<SmallBuff> small_buffs = new ArrayList<SmallBuff>();//나라에 걸려있는 버프목록
	private List<SmallBuff> small_buffs;
	public int[] population_by_religion; // = new int[number_of_religion + 1];//종교별 나라 인구수	
	private bool[] religion_expressed;// = new bool[number_of_religion + 1];//종교 발현

	// Use this for initialization
	void Start () {
		small_buffs = new List<SmallBuff>();
		theta = new float[number_of_religion + 1, number_of_religion + 1];
		population_by_religion = new int[number_of_religion + 1];		
		religion_expressed = new bool[number_of_religion + 1];
		if (religion == 0)
		{//0:무교
			for (int i = 0; i <= number_of_religion; i++)
			{
				religion_expressed[i] = false;
			}
			religion_expressed[0] = true;
			for (int i = 1; i <= number_of_religion; i++)
			{
				population_by_religion[i] = 0;
			}
			population_by_religion[0] = population;
			//무교일 경우 다른 무교=인구수, 다른종교=0
		}
		else
		{//종교가 있을경우
			for (int i = 0; i <= number_of_religion; i++)
			{
				religion_expressed[i] = false;
			}
			religion_expressed[0] = true;
			religion_expressed[religion] = true;

			for (int i = 1; i <= number_of_religion; i++)
			{
				population_by_religion[i] = 0;
			}
			population_by_religion[religion] = population / 10;
			population_by_religion[0] = population - population_by_religion[religion];
			//종교가 있을경우 그 종교=인구수의 10퍼센트, 나머지 무교, 다른종교=0
		}

		updateMostDominateReligion();
		resetThetaValue();

		InvokeRepeating("reguler_update", 1, 1);
	}

	// Update is called once per frame
	void Update () {
		
	}

	public void reguler_update()
	{
		for (int i = 0; i < small_buffs.Count; i++)
		{
			try
			{
				small_buffs.GetRange(i,1)[0].CountTheClock();
			}
			catch (ExpiredBuffException ebe)
			{
				small_buffs.RemoveAt(i);
				updateThetaValue();
			}
		}
		int new_population = 0;
		int [] new_population_by_religion = new int[number_of_religion + 1];
		for (int i = 0; i <= number_of_religion; i++)
		{
			new_population_by_religion[i] = 0;
		}
		for (int i = 0; i <= number_of_religion; i++)
		{
			float sum_of_theta = 0;
			int sum_of_religious_population = 0;

			for (int j = 0; j <= number_of_religion; j++)
			{
				sum_of_theta += theta[i, j];
			}
			/*
			for (int j = 0; j <= number_of_religion; j++)
			{
				new_population_by_religion[i] += (int)(population_by_religion[i] * (theta[i, j] / sum_of_theta));//문제점
			}
			*/
			for (int j = 1; j <= number_of_religion; j++)
			{
				new_population_by_religion[j] += (int)(population_by_religion[i] * (theta[i, j] / sum_of_theta));
				sum_of_religious_population += (int)(population_by_religion[i] * (theta[i, j] / sum_of_theta));
			}
			new_population_by_religion[0] += population_by_religion[i] - sum_of_religious_population;
			if (new_population_by_religion[0] < 0)
			{
				new_population_by_religion[0] = 0;
			}
		}

		for (int i = 0; i <= number_of_religion; i++)
		{
			new_population += new_population_by_religion[i];
			population_by_religion[i] = (int)new_population_by_religion[i];
		}
		population = new_population;

		updateMostDominateReligion();
	}


	private void updateMostDominateReligion()
	{//가장 우세한 종교의 업데이트 함수
		int tmp_religion = 0;
		for (int i = 0; i <= number_of_religion; i++)
		{
			if (population_by_religion[tmp_religion] < population_by_religion[i])
			{
				tmp_religion = i;
			}
		}
		most_dominate_religion = tmp_religion;
	}

	private void resetThetaValue()
	{//theta값 리셋 함수
		for (int i = 0; i <= number_of_religion; i++)
		{
			for (int j = 0; j <= number_of_religion; j++)
			{
				theta[i, j] = init_theta[i, j];
			}
		}
	}

	public void updateThetaValue()
	{//theta값 업데이트 함수(재계산)
		resetThetaValue();
		for (int a = 0; a < small_buffs.Count; a++)
		{	
			SmallBuff sb = small_buffs.GetRange(a, 1)[0];
			for (int i = 0; i <= number_of_religion; i++)
			{
				for (int j = 0; j <= number_of_religion; j++)
				{
					theta[i, j] *= sb.getBuffThetaAt(i, j);
				}
			}
		}
		for (int i = 0; i <= number_of_religion; i++)
		{
			if (religion_expressed[i] == false)
			{
				for (int j = 0; j <= number_of_religion; j++)
				{
					theta[j, i] = 0;
				}
			}
		}
	}
}

class ExpiredBuffException : Exception
{
	private static readonly long serialVersionUID = 1L;
}

