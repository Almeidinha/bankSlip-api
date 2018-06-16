package com.almeida.bankslips.rest.repositories;

import java.util.UUID;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.almeida.bankslips.rest.entities.BankSlip;

@Transactional(readOnly = true)
@NamedQueries({
		@NamedQuery (name = "BankSlipRepository.findAll", 
		query="SELECT bs.dueDate, bs.totalInCents, bs.customer FROM BankSlip bs") })

public interface BankSlipRepository extends JpaRepository<BankSlip, UUID> {
	
	Optional<BankSlip> findById(UUID id);
	
	List<BankSlip> findAll();
	
	Page<BankSlip> findAll(Pageable pageable);
	
	Optional<BankSlip> findByCustomer(String Customer);
}
