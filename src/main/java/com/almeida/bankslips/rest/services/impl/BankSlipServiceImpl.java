package com.almeida.bankslips.rest.services.impl;

import java.util.UUID;
import java.util.List;
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
	
	public Optional<BankSlip> findById(UUID id){
		log.info("Searching bankSlips of id {}", id);
		return this.bankSlipRepository.findById(id);		
	}
	
	public Optional<BankSlip> findByCustomer(String customer){
		log.info("Searching bankSlips of Customer Name {}", customer);
		return this.bankSlipRepository.findByCustomer(customer);		
	}
	
	public BankSlip persist(BankSlip bankSlip) {
		log.info("Saving bakSlip: {}", bankSlip);
		return this.bankSlipRepository.save(bankSlip);
	}
	
	public void remove(UUID id) {
		log.info("Removing bankSlip ID {}", id);
		this.bankSlipRepository.deleteById(id);
	}

	public List<BankSlip> findAll() {
		log.info("Searching for all bankSlips in the Database");
		return this.bankSlipRepository.findAll();
	}

}
