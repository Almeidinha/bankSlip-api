package com.almeida.bankslips.rest.services;

import java.rmi.server.UID;
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
	 * Return a BankSlip by ID
	 * 
	 * @param id
	 * @return
	 */
	Optional<BankSlip> findById(UID id);
	
	
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
	void remove(UID id);
}
