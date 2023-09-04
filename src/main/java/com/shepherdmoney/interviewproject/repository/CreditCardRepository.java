package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Crud repository to store credit cards
 */
@Repository("CreditCardRepo")
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    //Finds the credit card given the user ID
    @Query(value = "SELECT * FROM CREDIT_CARD WHERE USER_ID=?",nativeQuery = true)
    public List<CreditCard> findByUserId(int userId);
    //Finds the user by given card number
    @Query(value = "SELECT * FROM CREDIT_CARD WHERE NUMBER=?",nativeQuery = true)
    public List<CreditCard> findByCardNumber(String cardNumber);
}
