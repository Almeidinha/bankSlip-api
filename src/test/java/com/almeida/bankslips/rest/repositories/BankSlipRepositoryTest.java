package com.almeida.bankslips.rest.repositories;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.almeida.bankslips.rest.entities.BankSlip;
import com.almeida.bankslips.rest.enums.StatusEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BankSlipRepositoryTest {
	
	@Autowired
	private BankSlipRepository bankSlipRepository;

	private UUID bankSlipId;
	
	@Before
	public void setUp(){
		BankSlip bankSlip = bankSlipRepository.save(getBankSlipData());	
		bankSlipRepository.save(getBankSlipData());
		bankSlipRepository.save(getBankSlipData());
		this.bankSlipId = bankSlip.getId();
	}
	
	@After
	public void tearDown() throws Exception {
		this.bankSlipRepository.deleteAll();
	}
	
	@Test
	public void searchBankSlipById() {
		Optional<BankSlip> bankSlip = this.bankSlipRepository.findById(this.bankSlipId);
		
		assertEquals("Almeida TI teste", bankSlip.get().getCustomer());
	}
	
	@Test
	public void searchBankSlipList() {
		List<BankSlip> bankSlips = this.bankSlipRepository.findAll();
		
		assertEquals(3, bankSlips.size());
	}
	
	private BankSlip getBankSlipData() {
		BankSlip bankSlip = new BankSlip();
		bankSlip.setCustomer("Almeida TI teste");
		bankSlip.setDueDate(new Date());
		bankSlip.setStatus(StatusEnum.PENDING);
		bankSlip.setTotalInCents(new BigDecimal(50000));
		
		return bankSlip;
	}
	
}
