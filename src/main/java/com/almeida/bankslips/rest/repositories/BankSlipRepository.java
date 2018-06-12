package com.almeida.bankslips.rest.repositories;

import java.rmi.server.UID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.almeida.bankslips.rest.entities.BankSlip;

@Transactional(readOnly = true)
public interface BankSlipRepository extends JpaRepository<BankSlip, UID> {
	
	Optional<BankSlip> findById(UID id);
	
	Page<BankSlip> findAll(Pageable pageable);
}
