package com.almeida.bankslips.rest.services;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.almeida.bankslips.rest.entities.BankSlip;


public interface BankSlipService {
	
	/**
	 * 
	 * Return a list of BankSlips
	 * 
	 * @param id
	 * @param pageRequest
	 * @return
	 */
	Page<BankSlip> findAll(PageRequest pageRequest);
	
	/**
	 * 
	 * 
	 * @return
	 */
	List<BankSlip> findAll();
	
	/**
	 * 
	 * Return a BankSlip by ID
	 * 
	 * @param id
	 * @return
	 */
	Optional<BankSlip> findById(UUID id);
	
	/**
	 * 
	 * @param customer
	 * @return
	 */
	Optional<BankSlip> findByCustomer(String customer);
	
	/**
	 * 
	 * Persist a BankSlipin the database
	 * 
	 * @param bankSlip
	 * @return
	 */
	BankSlip persist(BankSlip bankSlip);
	
	/**
	 * 
	 * Remove a BankSlip from the database
	 * 
	 * @param id
	 */
	void remove(UUID id);
}
