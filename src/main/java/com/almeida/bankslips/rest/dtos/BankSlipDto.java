package com.almeida.bankslips.rest.dtos;

import java.rmi.server.UID;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;

public class BankSlipDto {
	
	private Optional<UID> id = Optional.empty();
	private String dueDate;
	private String totalInCents;
	private String customer;
	private String status;
	
	public BankSlipDto() {
	}
	
	public Optional<UID> getId() {
		return id;
	}
	public void setId(Optional<UID> id) {
		this.id = id;
	}
	
	@NotEmpty(message = "Due Date Can't be empty")
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
	@NotEmpty(message = "Bank Slip value can't be empty")
	public String getTotalInCents() {
		return totalInCents;
	}
	public void setTotalInCents(String totalInCents) {
		this.totalInCents = totalInCents;
	}
	
	@NotEmpty(message = "Customer Can't be empty")
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	@NotEmpty(message = "Status Can't be empty")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	@Override
	public String toString() {
		return "BankSlipDto [id=" + id + ", dueDate=" + dueDate + ", totalInCents=" + totalInCents
				+ ", customer=" + customer + ", status=" + status + "]";
	}

}
