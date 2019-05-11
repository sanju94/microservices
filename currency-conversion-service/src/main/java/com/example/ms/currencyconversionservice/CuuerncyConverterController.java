package com.example.ms.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.ms.currencyconversionservice.bean.CurrencyConverterBean;

@RestController
public class CuuerncyConverterController {
	
	@Autowired
	private CurrencyExchangeServiceProxy proxy;

	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConverterBean convertCurrency(@PathVariable String from, @PathVariable String to
			,@PathVariable BigDecimal quantity) {
		
		Map<String,String> uriVariables = new HashMap<>();
		uriVariables.put("to", to);
		uriVariables.put("from", from);
		ResponseEntity<CurrencyConverterBean> resp= new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConverterBean.class, uriVariables);
		
		CurrencyConverterBean body =resp.getBody();
		
		
		return new CurrencyConverterBean(body.getId(), from, to, body.getConversionMultiple(), quantity,
				quantity.multiply(body.getConversionMultiple()), body.getPort());
	}
	
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConverterBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to
			,@PathVariable BigDecimal quantity) {
		
		CurrencyConverterBean body =proxy.retreive(from, to);
		
		
		return new CurrencyConverterBean(body.getId(), from, to, body.getConversionMultiple(), quantity,
				quantity.multiply(body.getConversionMultiple()), body.getPort());
	}
}
