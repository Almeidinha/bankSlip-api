package com.almeida.bankslips.rest.controllers;

import java.math.BigDecimal;
import java.util.UUID;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.almeida.bankslips.rest.dtos.BankSlipDetailDto;
import com.almeida.bankslips.rest.dtos.BankSlipDto;
import com.almeida.bankslips.rest.entities.BankSlip;
import com.almeida.bankslips.rest.enums.StatusEnum;
import com.almeida.bankslips.rest.response.Response;
import com.almeida.bankslips.rest.services.BankSlipService;

@RestController
@RequestMapping("/rest/bankslips")
@CrossOrigin(origins = "*")
public class BankSlipController {
	
	private static final Logger log = LoggerFactory.getLogger(BankSlipController.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private BankSlipService bankSlipService;
	
	@Value("${pagination.amount_per_page}")
	private int amountPerPage;
	
	public BankSlipController() {
	}
	
	/**
	 * Return a Bank Slip of given ID
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<BankSlipDetailDto>> getBankSlipById(@PathVariable("id") UUID id){
		log.info("searching for BankSlip ID Number: {}", id);
		
		Response<BankSlipDetailDto> response = new Response<BankSlipDetailDto>();		
		Optional<BankSlip> bankSlip = this.bankSlipService.findById(id);
		
		if (!bankSlip.isPresent()) {
			log.info("bank Slip of ID {} not found in the database");
			response.getErrors().add("Bankslip not found with the specified id");
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.convertBankSlipDetailtoDto(bankSlip.get()));
		return ResponseEntity.ok(response);
	}
	
	/*@GetMapping
	public ResponseEntity<Response<Page<BankSlipDto>>> listBankSlipsPaged(
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		log.info("Lisint BankSlips, página: {}", pag);
		
		Response<Page<BankSlipDto>> response = new Response<Page<BankSlipDto>>();
		
		PageRequest pageRequest = PageRequest.of(pag, this.amountPerPage, Direction.valueOf(dir), ord);
		Page<BankSlip> bankSlip = this.bankSlipService.findAll(pageRequest);
		Page<BankSlipDto> bankSlipDto = bankSlip.map(bankslip -> this.convertBankSliptoDto(bankslip));

		response.setData(bankSlipDto);
		return ResponseEntity.ok(response);
	}*/
	
	 /**
	  * Return a List of Bank Slips
	  * 
	  * @return
	  */
	@GetMapping
	public ResponseEntity<Response<List<BankSlipDto>>> listBankSlips() {
		log.info("Listing all Bank Slips:");
		
		Response<List<BankSlipDto>> response = new Response<List<BankSlipDto>>();
		
		List<BankSlip> bankSlip = this.bankSlipService.findAll();
		List<BankSlipDto> bankSlipDto = new ArrayList<BankSlipDto>(); 
		
		bankSlip.forEach(bs -> bankSlipDto.add(this.convertBankSliptoDto(bs)));		
		response.setData(bankSlipDto);

		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Add a Bank Slip to the Database
	 * 
	 * @param bankSlipDetailDto
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	@PostMapping
	public ResponseEntity<Response<BankSlipDetailDto>> addBankSlip(@Valid @RequestBody BankSlipDetailDto bankSlipDetailDto, 
			BindingResult result) throws ParseException{
		log.info("Adding BankSlip: ", bankSlipDetailDto.toString());
		
		Response<BankSlipDetailDto> response = new Response<BankSlipDetailDto>();
		BankSlip bankSlip = this.convertDtotoBankSlip(bankSlipDetailDto, result);
		
		if (result.hasErrors()) {
			log.error("Erroe validating BankSlip: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}else {
			response.getMessages().add("Bankslip created");
		}
		
		bankSlip = this.bankSlipService.persist(bankSlip);
		response.setData(this.convertBankSlipDetailtoDto(bankSlip));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Update a bank Slip Status of give ID
	 * 
	 * @param id
	 * @param bankSlipDetailDto
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	@PutMapping(value = "/{id}")
	ResponseEntity<Response<BankSlipDetailDto>> updateBanSlipStatus(@PathVariable("id") UUID id, 
			@Valid @RequestBody BankSlipDetailDto bankSlipDetailDto, BindingResult result) throws ParseException{
		log.info("updating BankSlip Status: {} ", bankSlipDetailDto.toString());
		
		Response<BankSlipDetailDto> response = new Response<BankSlipDetailDto>();
		bankSlipDetailDto.setId(Optional.of(id));
		BankSlip bankSlip = this.convertDtotoBankSlip(bankSlipDetailDto, result);
		
		if (result.hasErrors()) {
			log.error("Error validating BankSlip: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}else {
			response.getMessages().add("Bankslip " + bankSlip.getStatus().toString().toLowerCase());
		}
		
		bankSlip = this.bankSlipService.persist(bankSlip);
		response.setData(this.convertBankSlipDetailtoDto(bankSlip));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * 
	 * Remove a Bank Slip of given ID from the database 
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> removeBankSlip(@PathVariable("id") UUID id){
		log.info("Removing bankSlip id: {}", id);
		Response<String> response = new Response<>();
		Optional<BankSlip> bankSlip = this.bankSlipService.findById(id);
		
		if (!bankSlip.isPresent()) {
			log.info("Error removing bankSlip, id: {} invalid", id);
			response.getErrors().add("Erroe removing bankSlip, regiser not fount for id " + id);
			return ResponseEntity.badRequest().body(response);
		}else {
			response.getMessages().add("Bankslip removed");
		}
		
		this.bankSlipService.remove(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	
	/**
	 * Converts a BankSlip Entity to its respective DTO
	 * 
	 * @param bankSlip
	 * @return
	 */
	private BankSlipDto convertBankSliptoDto(BankSlip bankSlip) {
		BankSlipDto bankSlipDto = new BankSlipDto();
		
		bankSlipDto.setId(Optional.of(bankSlip.getId()));
		bankSlipDto.setDueDate(this.dateFormat.format(bankSlip.getDueDate()));
		bankSlipDto.setCustomer(bankSlip.getCustomer());
		bankSlipDto.setTotalInCents(bankSlip.getTotalInCents().setScale(0).toString());
		
		return bankSlipDto;
	}
	
	/**
	 * 
	 * Converts a BankSlipDetail Entity to its respective DTO
	 * 
	 */
	private BankSlipDetailDto convertBankSlipDetailtoDto(BankSlip bankSlip) {
		BankSlipDetailDto bankSlipDetailDto = new BankSlipDetailDto();
		
		bankSlipDetailDto.setId(Optional.of(bankSlip.getId()));
		bankSlipDetailDto.setDueDate(this.dateFormat.format(bankSlip.getDueDate()));
		bankSlipDetailDto.setCustomer(bankSlip.getCustomer());
		bankSlipDetailDto.setTotalInCents(bankSlip.getTotalInCents().setScale(0).toString());
		bankSlipDetailDto.setStatus(bankSlip.getStatus().toString());
		
		bankSlip.getFine().ifPresent(
				fine -> bankSlipDetailDto.setFine(fine.setScale(0).toString()));
		
		return bankSlipDetailDto;
	}
	
	/**
	 * 
	 * Converts a BankSlipDetailDtoDto to an Entity BankSlipDetailDto
	 * 
	 * @param bankSlipDto
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	private BankSlip convertDtotoBankSlip(BankSlipDetailDto bankSlipDto, BindingResult result) throws ParseException {
		BankSlip bankSlip = new BankSlip();
		
		if(bankSlipDto.getId().isPresent()) {
			Optional<BankSlip> bs = this.bankSlipService.findById(bankSlipDto.getId().get());
			if(bs.isPresent()) {
				bankSlip = bs.get();
				if (EnumUtils.isValidEnum(StatusEnum.class, bankSlipDto.getStatus())) {
					bankSlip.setStatus(StatusEnum.valueOf(bankSlipDto.getStatus()));
				}else {
					result.addError(new ObjectError("sattus", "Status inválido"));
				}
			} else {
				result.addError(new ObjectError("bankslip", "BankSlip not found"));
			}
		} else {
			bankSlip.setDueDate(this.dateFormat.parse(bankSlipDto.getDueDate()));
			bankSlip.setCustomer(bankSlipDto.getCustomer());
			bankSlip.setTotalInCents(new BigDecimal(bankSlipDto.getTotalInCents()));
			
			if (EnumUtils.isValidEnum(StatusEnum.class, bankSlipDto.getStatus())) {
				bankSlip.setStatus(StatusEnum.valueOf(bankSlipDto.getStatus()));
			}else {
				result.addError(new ObjectError("sattus", "Status inválido"));
			}
		}
		
		return bankSlip;	
	}
	
}
