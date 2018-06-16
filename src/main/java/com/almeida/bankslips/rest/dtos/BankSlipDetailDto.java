package com.almeida.bankslips.rest.dtos;

import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;

public class BankSlipDetailDto extends BankSlipDto {
	private Optional<UUID> id = Optional.empty();
	private String dueDate;
	private String totalInCents;
	private String fine;
	private String customer;
	private String status;
	
	public BankSlipDetailDto() {
	}
	
	@NotEmpty(message = "Status Can't be empty")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getFine() {
		return fine;
	}
	public void setFine(String fine) {
		this.fine = fine;
	}
	
	
	@Override
	public String toString() {
		return "BankSlipDto [id=" + id + ", dueDate=" + dueDate + ", totalInCents=" + totalInCents
				+ ", customer=" + customer + ", status=" + status + "]";
	}
}
