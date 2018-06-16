package com.almeida.bankslips.rest.dtos;

import java.util.UUID;
import java.util.Optional;

public class BankSlipDto {
	
	private Optional<UUID> id = Optional.empty();
	
	private String dueDate;
	private String totalInCents;
	private String customer;
	
	public BankSlipDto() {
	}
	
	public Optional<UUID> getId() {
		return id;
	}
	public void setId(Optional<UUID> id) {
		this.id = id;
	}
	
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
	public String getTotalInCents() {
		return totalInCents;
	}
	public void setTotalInCents(String totalInCents) {
		this.totalInCents = totalInCents;
	}
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "BankSlipDto [id=" + id + ", dueDate=" + dueDate + ", totalInCents=" + totalInCents + ", customer="
				+ customer + "]";
	}


}
