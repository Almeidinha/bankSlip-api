package com.almeida.bankslips.rest.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.almeida.bankslips.rest.dtos.BankSlipDetailDto;
import com.almeida.bankslips.rest.entities.BankSlip;
import com.almeida.bankslips.rest.enums.StatusEnum;
import com.almeida.bankslips.rest.services.BankSlipService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BankSlipControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private BankSlipService bankSlipService;
	
	private static final String URL_BASE = "/rest/bankslips/";
	private static final UUID ID = UUID.fromString("4a56f8fd-9659-4249-a9de-23033451a42d");
	private static final String STATUS = StatusEnum.PENDING.name();
	private static final Date DUE_DATE = new Date();
	private static final BigDecimal TOTAL_IN_CENTS = new BigDecimal(1000);
	private static final String CUSTOMER = "Almeida Test Company";
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	
	@Test
	public void testcreateBankSlip() throws Exception {
		BankSlip bankSlip = getBankSlip();
		
		BDDMockito.given(this.bankSlipService.findById(Mockito.any(UUID.class))).willReturn(Optional.of(new BankSlip()));
		BDDMockito.given(this.bankSlipService.persist(Mockito.any(BankSlip.class))).willReturn(bankSlip);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPostRequest(null, DUE_DATE))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID.toString()))
				.andExpect(jsonPath("$.data.status").value(STATUS))
				.andExpect(jsonPath("$.data.dueDate").value(this.dateFormat.format(DUE_DATE)))
				.andExpect(jsonPath("$.data.customer").value(CUSTOMER))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	
	@Test
	public void testCreateBankSlipInvalidStatus() throws Exception {
		BDDMockito.given(this.bankSlipService.findById(Mockito.any(UUID.class))).willReturn(Optional.empty());

		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPostRequest("WAITING", DUE_DATE))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Status inválido"))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	
	@Test
	public void testRemoveBankSlip() throws Exception {
		BDDMockito.given(this.bankSlipService.findById(Mockito.any(UUID.class))).willReturn(Optional.of(new BankSlip()));

		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void tesBankSlipFine() throws Exception{
		BankSlip bankSlip = getBankSlip();
		Date date = Date.from(LocalDate.now().minusDays(7).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		bankSlip.setDueDate(date);
		
		BDDMockito.given(this.bankSlipService.persist(Mockito.any(BankSlip.class))).willReturn(bankSlip);
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPostRequest(null, DUE_DATE))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.fine").value("50"))
				.andExpect(jsonPath("$.errors").isEmpty());
		
		
		date = Date.from(LocalDate.now().minusDays(11).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		bankSlip.setDueDate(date);
		BDDMockito.given(this.bankSlipService.persist(Mockito.any(BankSlip.class))).willReturn(bankSlip);
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPostRequest(null, DUE_DATE))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.fine").value("100"))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	

	@Test
	public void tesBankSlipStatus() throws Exception{
		BankSlip bankSlip = getBankSlip();
		BDDMockito.given(this.bankSlipService.persist(Mockito.any(BankSlip.class))).willReturn(bankSlip);
		BDDMockito.given(this.bankSlipService.findById(Mockito.any(UUID.class))).willReturn(Optional.of(new BankSlip()));
		
		mvc.perform(MockMvcRequestBuilders.put(URL_BASE + ID)
				.content(this.getJsonPostRequest("PAID", DUE_DATE))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.errors").isEmpty())
				.andExpect(jsonPath("$.messages").value("Bankslip paid"));
	}
	
	
	
	private String getJsonPostRequest(String Status, Date date) throws JsonProcessingException {
		BankSlipDetailDto bankSlipDetailDto = new BankSlipDetailDto();
		bankSlipDetailDto.setId(null);
		bankSlipDetailDto.setTotalInCents(TOTAL_IN_CENTS.toString());
		bankSlipDetailDto.setDueDate(this.dateFormat.format(date));
		bankSlipDetailDto.setStatus(Status != null ? Status : STATUS.toString());
		bankSlipDetailDto.setCustomer(CUSTOMER);
		
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(bankSlipDetailDto);
	}
	
	private BankSlip getBankSlip() {
		BankSlip bankSlip = new BankSlip();
		bankSlip.setId(ID);
		bankSlip.setDueDate(DUE_DATE);
		bankSlip.setStatus(StatusEnum.valueOf(STATUS));
		bankSlip.setTotalInCents(TOTAL_IN_CENTS);
		bankSlip.setCustomer(CUSTOMER);
		
		return bankSlip;
	}	
	

}
