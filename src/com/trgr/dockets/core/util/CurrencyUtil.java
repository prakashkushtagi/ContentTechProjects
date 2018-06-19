package com.trgr.dockets.core.util;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;


public class CurrencyUtil {
	
	static final Logger LOG = Logger.getLogger(CurrencyUtil.class);
	
	public static String returnWithTwoDecimals(String paymentAmount) {
		Double amount = 0.00;
		
		try{
			amount = Double.parseDouble(paymentAmount);
		}catch(NumberFormatException e){
			LOG.warn("PaymentAmount contains non-number characters. paymentAmount = " + paymentAmount);
			return paymentAmount;
		}
		
		DecimalFormat df = new DecimalFormat("0.00##");
		paymentAmount = df.format(amount);
		
		return paymentAmount;
	}

}
