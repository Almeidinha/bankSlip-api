package com.almeida.bankslips.rest.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class BankSlipUtils {
	
	public static BigDecimal getFineValue(BigDecimal totalInCents, Date dueDate) {
	    
		BigDecimal fine = null; 
		LocalDate today =  LocalDate.now();
		LocalDate due = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	
		Long daysBetwwen = java.time.temporal.ChronoUnit.DAYS.between(today, due);
		
		if (daysBetwwen < 0 && daysBetwwen >= -10) {
			fine =  totalInCents.setScale(0).multiply(BigDecimal.valueOf(0.05)).setScale(0);
		}else if (daysBetwwen < -10) {
			fine =  totalInCents.setScale(0).multiply(BigDecimal.valueOf(0.10)).setScale(0);
		}
		
		return fine;
	    
	}
}
