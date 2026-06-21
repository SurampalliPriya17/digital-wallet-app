package com.digital_wallet_app.controller;

import com.digital_wallet_app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wallet-service")
public class WalletViewController {

	@Autowired
    private WalletService walletService;
	
	  @GetMapping("/add-money-status/{pgTxnId}")
	    public String addMoneyStatus(@PathVariable String pgTxnId) {
	       String status = walletService.processPgTxnId(pgTxnId);
	       if(status.equalsIgnoreCase("success")) {
	    	   return "walletUpdated";
	       }
	       return "failed";
	    }

}
