using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;

public class SmallBuff : MonoBehaviour
{
	private readonly int number_of_religion = 4;//종교 수

	private string buff_name;//버프이름
	private int duration_time;//지속시간

	private float[,] buff_theta;//=new float[number_of_religion + 1, number_of_religion + 1];//buff_theta값

	// Use this for initialization
	void Start()
	{
		buff_theta = new float[number_of_religion + 1, number_of_religion + 1];
	}

	// Update is called once per frame
	void Update()
	{

	}

	public float getBuffThetaAt(int i, int j)
	{
		return buff_theta[i, j];
	}

	public void CountTheClock()
	{
		if(duration_time == 0){
			throw new ExpiredBuffException();
		}
		else{
			duration_time--;
		}
	}
}