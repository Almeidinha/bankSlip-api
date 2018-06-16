package com.almeida.bankslips.rest.entities;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.almeida.bankslips.rest.utils.BankSlipUtils;

import com.almeida.bankslips.rest.enums.StatusEnum;

@Entity
@Table(name  = "bank_slip")
public class BankSlip implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private Date dueDate;
	private BigDecimal totalInCents;
	private String customer;
	private StatusEnum status;
	
	public BankSlip() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "due_date", nullable =  false)
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	@Column(name = "total_in_cents", nullable =  false)
	public BigDecimal getTotalInCents() {
		return totalInCents;
	}
	public void setTotalInCents(BigDecimal totalInCents) {
		this.totalInCents = totalInCents;
	}
	
	@Column(name = "customer", nullable =  false)
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable =  false)
	public StatusEnum getStatus() {
		return status;
	}
	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	
	@Transient
	public Optional<BigDecimal> getFine() {
		return Optional.ofNullable(BankSlipUtils.getFineValue(totalInCents, dueDate));
	}

	@Override
	public String toString() {
		return "BankSlip [id=" + id + ", dueDate=" + dueDate + ", totalInCents=" + totalInCents + ", customer="
				+ customer + ", status=" + status + "]";
	}
	
}
