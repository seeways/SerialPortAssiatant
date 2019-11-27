package com.jiataoyuan.bean;

import java.io.Serializable;

/**
 *	用于保存界面数据
 *  @author TaoYuan
 */
public class AssistBean implements Serializable {
	private static final long serialVersionUID = -5620661009186692227L;
	private boolean isTxt=true;
	private String SendTxtA="COMA",SendTxtB="COMB";
	private String SendHexA="AA",SendHexB="BB";
	public String sTimeA="500";
	public String sTimeB="500";

	public boolean isTxt()
	{
		return isTxt;
	}
	public void setTxtMode(boolean isTxt)
	{
		this.isTxt = isTxt;
	}
	
	public String getSendA()
	{
		if (isTxt)
		{
			return SendTxtA;
		} else
		{
			return SendHexA;
		}
	}
	public String getSendB()
	{
		if (isTxt)
		{
			return SendTxtB;
		} else
		{
			return SendHexB;
		}
	}
	
	public void setSendA(String sendA)
	{
		if (isTxt)
		{
			SendTxtA = sendA;
		} else
		{
			SendHexA = sendA;
		}
	}
	
	public void setSendB(String sendB)
	{
		if (isTxt)
		{
			SendTxtB = sendB;
		} else
		{
			SendHexB = sendB;
		}
	}

}