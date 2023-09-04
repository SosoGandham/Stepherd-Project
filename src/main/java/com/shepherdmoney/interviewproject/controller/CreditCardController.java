package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import jakarta.persistence.EntityNotFoundException;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class CreditCardController {

    // TODO: wire in CreditCard repository here (~1 line)

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        // TODO: Create a credit card entity, and then associate that credit card with user with given userId
        //       Return 200 OK with the credit card id if the user exists and credit card is successfully associated with the user
        //       Return other appropriate response code for other exception cases
        //       Do not worry about validating the card number, assume card number could be any arbitrary format and length

        //Validating if card is part of the request and if bank is part of the request
        //If any of this information is missing, we will return as a bad request(400)
        if(payload.getUserId() < 1 || StringUtils.isNullOrEmpty(payload.getCardNumber()) || StringUtils.isNullOrEmpty(payload.getCardIssuanceBank())){
            return new ResponseEntity<Integer>(0, null, HttpStatus.BAD_REQUEST);
        }
        
        try {
            System.out.println("userId-" + payload.getUserId());
            // Gets user record from the database, based on the given ID
            User u = userRepository.getReferenceById(payload.getUserId());
            System.out.println(u.getId() + "-" + u.getName() + "-" + u.getEmail());
            
            //Creates the credit card object to store it in the table
            CreditCard cc = new CreditCard();
            cc.setUserId(u.getId());
            cc.setNumber(payload.getCardNumber());
            cc.setIssuanceBank(payload.getCardIssuanceBank());
            //saves the credit card record to the table with linking to the user 
            creditCardRepository.save(cc);
            
            return new ResponseEntity<Integer>(cc.getId(), null, HttpStatus.CREATED);
        }catch(EntityNotFoundException enfe){
            //If user does not exist in the database then return "Bad Request"
            return new ResponseEntity<Integer>(0, null, HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            //Anything else that may result in error then return "Bad Request"
            return new ResponseEntity<Integer>(0, null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        // TODO: return a list of all credit card associated with the given userId, using CreditCardView class
        //       if the user has no credit card, return empty list, never return null
        //Finds the credit card number given the user ID
        List<CreditCard> list = creditCardRepository.findByUserId(userId);
        //Creates empty array of credit card view 
        List<CreditCardView> creditCardList = new ArrayList<>();
        //Converts list of credit cards to list of credit cards view 
        for(CreditCard c: list){
            creditCardList.add(new CreditCardView(c.getIssuanceBank(), c.getNumber()));
        }
        //returns the list of credit cards(0 or more)
        return new ResponseEntity<List<CreditCardView>>(creditCardList, null, HttpStatus.OK);
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        // TODO: Given a credit card number, efficiently find whether there is a user associated with the credit card
        //       If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request
        //Getting the matching records of the given credit card number
        List<CreditCard> list = creditCardRepository.findByCardNumber(creditCardNumber);
        //If there are no matching records then we will return "Bad Request"
        if(list == null || list.size() == 0){
            return new ResponseEntity<Integer>(0, null, HttpStatus.BAD_REQUEST);
        }
        //Returns matching user Id of the credit card number
        return new ResponseEntity<Integer>(list.get(0).getUserId(), null, HttpStatus.OK);
    }

    /*@PostMapping("/credit-card:update-balance")
    public SomeEnityData postMethodName(@RequestBody UpdateBalancePayload[] payload) {
        //TODO: Given a list of transactions, update credit cards' balance history.
        //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        //      Given a transaction of {date: 4/10, amount: 10}, the new balanceHistory is
        //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 110}]
        //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //        is not associated with a card.
        
        return null;
    }*/
    
}
