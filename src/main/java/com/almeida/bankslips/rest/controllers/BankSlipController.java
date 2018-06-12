package com.almeida.bankslips.rest.controllers;

import java.math.BigDecimal;
import java.rmi.server.UID;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<BankSlipDto>> getBankSlipById(@PathVariable("id") UID id){
		log.info("searching for BankSlip ID Number: {}", id);
		
		Response<BankSlipDto> response = new Response<BankSlipDto>();		
		Optional<BankSlip> bankSlip = this.bankSlipService.findById(id);
		
		if (!bankSlip.isPresent()) {
			log.info("bank Slip of ID {} not found in the database");
			response.getErrors().add("Bankslip not found with the specified id");
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.convertBankSliptoDto(bankSlip.get()));
		return ResponseEntity.ok(response);
	}
	
	@GetMapping
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
	}
	
	@PostMapping
	public ResponseEntity<Response<BankSlipDto>> addBankSlip(@Valid @RequestBody BankSlipDto bankSlipDto, 
			BindingResult result) throws ParseException{
		log.info("Adding BankSlip: ", bankSlipDto.toString());
		
		Response<BankSlipDto> response = new Response<BankSlipDto>();
		BankSlip bankSlip = this.convertDtotoBankSlip(bankSlipDto, result);
		
		if (result.hasErrors()) {
			log.error("Erroe validating BankSlip: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		bankSlip = this.bankSlipService.persist(bankSlip);
		response.setData(this.convertBankSliptoDto(bankSlip));
		return ResponseEntity.ok(response);
	}
	
	@PutMapping(value = "/{id}")
	ResponseEntity<Response<BankSlipDto>> updateBanSlipStatus(@PathVariable("id") UID id, 
			@Valid @RequestBody BankSlipDto bankSlipDto, BindingResult result) throws ParseException{
		log.info("updating BankSlip Status: {} ", bankSlipDto.toString());
		
		Response<BankSlipDto> response = new Response<BankSlipDto>();
		bankSlipDto.setId(Optional.of(id));
		BankSlip bankSlip = this.convertDtotoBankSlip(bankSlipDto, result);
		
		if (result.hasErrors()) {
			log.error("Error validating BankSlip: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		bankSlip = this.bankSlipService.persist(bankSlip);
		response.setData(this.convertBankSliptoDto(bankSlip));
		return ResponseEntity.ok(response);
	}
	
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> remove(@PathVariable("id") UID id){
		log.info("Removing bankSlip id: {}", id);
		Response<String> response = new Response<>();
		Optional<BankSlip> bankSlip = this.bankSlipService.findById(id);
		
		if (!bankSlip.isPresent()) {
			log.info("Error removing bankSlip, id: {} invalid", id);
			response.getErrors().add("Erroe removing bankSlip, regiser not fount for id " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		this.bankSlipService.remove(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	private BankSlipDto convertBankSliptoDto(BankSlip bankSlip) {
		BankSlipDto bankSlipDto = new BankSlipDto();
		
		bankSlipDto.setId(Optional.of(bankSlip.getId()));
		bankSlipDto.setDueDate(this.dateFormat.format(bankSlip.getDueDate()));
		bankSlipDto.setCustomer(bankSlip.getCustomer());
		bankSlipDto.setStatus(bankSlip.getStatus().toString());
		bankSlipDto.setTotalInCents(bankSlip.getTotalInCents().toString());
		
		return bankSlipDto;
	}
	
	private BankSlip convertDtotoBankSlip(BankSlipDto bankSlipDto, BindingResult result) throws ParseException {
		BankSlip bankSlip = new BankSlip();
		
		if(bankSlipDto.getId().isPresent()) {
			Optional<BankSlip> bs = this.bankSlipService.findById(bankSlipDto.getId().get());
			if(bs.isPresent()) {
				bankSlip = bs.get();
			} else {
				result.addError(new ObjectError("bankslip", "BankSlip not found"));
			}
		} else {
			bankSlip.setId(bankSlipDto.getId().get());
		}
		bankSlip.setDueDate(this.dateFormat.parse(bankSlipDto.getDueDate()));
		bankSlip.setCustomer(bankSlipDto.getCustomer());
		bankSlip.setTotalInCents(new BigDecimal(bankSlipDto.getTotalInCents()));
		
		if (EnumUtils.isValidEnum(StatusEnum.class, bankSlipDto.getStatus())) {
			bankSlip.setStatus(StatusEnum.valueOf(bankSlipDto.getStatus()));
		}else {
			result.addError(new ObjectError("sattus", "Status inváido"));
		}
		
		return bankSlip;	
	}
	
}
