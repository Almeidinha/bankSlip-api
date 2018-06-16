package com.almeida.bankslips.rest.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.almeida.bankslips.rest.entities.BankSlip;
import com.almeida.bankslips.rest.repositories.BankSlipRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BankSlipServiceTest {

	@MockBean
	private BankSlipRepository bankSlipRepository;
	
	@Autowired
	private BankSlipService bankSlipService;
	
	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.bankSlipRepository.findAll()).willReturn(new ArrayList<BankSlip>());
		BDDMockito.given(this.bankSlipRepository.findById(Mockito.any(UUID.class))).willReturn(Optional.ofNullable(new BankSlip()));
		BDDMockito.given(this.bankSlipRepository.findByCustomer(Mockito.anyString())).willReturn(Optional.ofNullable(new BankSlip()));
		
		BDDMockito.given(this.bankSlipRepository.save(Mockito.any(BankSlip.class))).willReturn(new BankSlip());
	}
	
	@Test
	public void testGetAllBankSlips() {
		List<BankSlip> bankSlips = this.bankSlipService.findAll();
		assertNotNull(bankSlips);
	}
	
	@Test
	public void testgetBankSlipById() {
		Optional<BankSlip> bankslip = this.bankSlipService.findById(UUID.randomUUID());

		assertTrue(bankslip.isPresent());
	}
	
	@Test
	public void testSaveBankSlip() {
		BankSlip bankSlip = this.bankSlipService.persist(new BankSlip());

		assertNotNull(bankSlip);
	}
	

	
	
	
}
