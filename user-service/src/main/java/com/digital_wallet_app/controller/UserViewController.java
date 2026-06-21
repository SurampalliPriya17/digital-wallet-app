package com.digital_wallet_app.controller;

import java.util.concurrent.ExecutionException;

import com.digital_wallet_app.client.TransactionServiceClient;
import com.digital_wallet_app.client.WalletServiceClient;
import com.digital_wallet_app.dto.AddMoneyRequest;
import com.digital_wallet_app.dto.AddMoneyResponse;
import com.digital_wallet_app.dto.LoginRequestDto;
import com.digital_wallet_app.dto.TxnRequestDto;
import com.digital_wallet_app.dto.UserDto;
import com.digital_wallet_app.dto.UserProfileDto;
import com.digital_wallet_app.dto.WalletBalanceDto;
import com.digital_wallet_app.entity.User;
import com.digital_wallet_app.exception.UserNotFoundException;
import com.digital_wallet_app.repo.UserRepo;
import com.digital_wallet_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import jakarta.servlet.http.HttpSession;

@Controller
public class UserViewController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    private WalletServiceClient walletServiceClient;

    @Autowired
    private TransactionServiceClient transactionServiceClient;

    @GetMapping("/welcome")
    public String dashboard(Model model) {

        return "welcome";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(HttpSession session, @ModelAttribute UserDto userDto)
            throws ExecutionException, InterruptedException {

        Long id = userService.createUser(userDto);

        session.setAttribute("userName", userDto.getName());
        session.setAttribute("userId", id);

        return "redirect:/home";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequestDto loginRequest, Model model, HttpSession session) {

        User user = userRepo.findByEmail(loginRequest.getEmail());

        if (user == null) {
            model.addAttribute("error", "User not found. Please register first.");
            return "login";
        }

        session.setAttribute("userName", user.getName());
        session.setAttribute("userId", user.getId());

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        String name = (String) session.getAttribute("userName");
        Long id=(Long)session.getAttribute("userId");

        model.addAttribute("name", name);

        WalletBalanceDto walletBalanceDto = walletServiceClient.getBalance(id).getBody();
        Double balance = walletBalanceDto.getBalance();
        model.addAttribute("balance", balance);


        return "home";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) throws UserNotFoundException {

        Long id = (Long) session.getAttribute("userId");

        UserProfileDto userprofileDto = userService.getUserProfile(id);

        model.addAttribute("user", userprofileDto);

        return "getProfile";
    }

    @GetMapping("/update")
    public String showUpdatePage(HttpSession session, Model model)
            throws UserNotFoundException {

        Long id = (Long) session.getAttribute("userId");

        UserProfileDto existingUser = userService.getUserProfile(id);

        UserDto updateDto = new UserDto();

        updateDto.setName(existingUser.getUserDetail().getName());
        updateDto.setEmail(existingUser.getUserDetail().getEmail());
        updateDto.setPhone(existingUser.getUserDetail().getPhone());
        updateDto.setKycNumber(existingUser.getUserDetail().getKycNumber());

        model.addAttribute("user", updateDto);

        return "update";
    }
    @PostMapping("/update")
    public String updateUser(HttpSession session, @ModelAttribute UserDto userDto) throws UserNotFoundException {
        Long id = (Long) session.getAttribute("userId");
        UserProfileDto userprofileDto = userService.updateUser(id, userDto);

        return "redirect:/profile";
    }

    @GetMapping("/delete")
    public String deleteUser(HttpSession session) {
        Long id = (Long) session.getAttribute("userId");
        System.out.println("Delete User ID = " + id);

        userService.deleteUser(id);

        session.invalidate();

        return "redirect:/welcome";
    }

    @GetMapping("/sendMoney")
    public String showDeletePage(Model model) {
        model.addAttribute("transaction", new TxnRequestDto());
        return "transfer";
    }

    @PostMapping("/sendMoney")
    public String send(@ModelAttribute TxnRequestDto txnRequestDto,HttpSession session) throws ExecutionException, InterruptedException {
        Long id = (Long) session.getAttribute("userId");
        txnRequestDto.setFromUserId(id);

        ResponseEntity<String> result =  transactionServiceClient.initTransaction(txnRequestDto);

        String txnId = result.hasBody() ?  result.getBody() : "";

        return "success";
    }

    @GetMapping("/addMoney")
    public String addMoneyPage(Model model) {
        model.addAttribute("wallet", new AddMoneyRequest());
        return "addMoney";
    }

    @PostMapping("/addMoney")
    public String addMoney(@ModelAttribute AddMoneyRequest addMoneyRequest, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");


        addMoneyRequest.setUserId(userId);
        ResponseEntity responseEntity=walletServiceClient.addMoney(addMoneyRequest);

        AddMoneyResponse response = (AddMoneyResponse) responseEntity.getBody();

        if (response == null || response.getUrl() == null) {
            throw new RuntimeException("Payment initialization failed");
        }
        return "redirect:" + response.getUrl();


    }

}

