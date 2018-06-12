package com.almeida.bankslips.rest.services.impl;

import java.rmi.server.UID;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.almeida.bankslips.rest.entities.BankSlip;
import com.almeida.bankslips.rest.repositories.BankSlipRepository;
import com.almeida.bankslips.rest.services.BankSlipService;

@Service
public class BankSlipServiceImpl implements BankSlipService {
	
	private static final Logger log = LoggerFactory.getLogger(BankSlipServiceImpl.class);
	
	@Autowired
	BankSlipRepository bankSlipRepository;
	
	public Page<BankSlip> findAll(PageRequest pageRequest){
		log.info("Searching for all bankSlips in the Database");
		return this.bankSlipRepository.findAll(pageRequest);
	}
	
	public Optional<BankSlip> findById(UID id){
		log.info("Searching bankSlips of id {}", id);
		return this.bankSlipRepository.findById(id);		
	}
	
	public BankSlip persist(BankSlip bankSlip) {
		log.info("Saving bakSlip: {}", bankSlip);
		return this.bankSlipRepository.save(bankSlip);
	}
	
	public void remove(UID id) {
		log.info("Removing bankSlip ID {}", id);
		this.bankSlipRepository.deleteById(id);
	}

}
