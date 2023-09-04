package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import jakarta.persistence.EntityNotFoundException;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    // TODO: wire in the user repository (~ 1 line)

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        // TODO: Create an user entity with information given in the payload, store it in the database
        //       and return the id of the user in 200 OK response
        //Validates the email and the name 
        //If an information is missing then we will return "Bad Requeset"
        if(payload == null || StringUtils.isNullOrEmpty(payload.getEmail()) || StringUtils.isNullOrEmpty(payload.getName())){
            return new ResponseEntity<Integer>(0, null, HttpStatus.BAD_REQUEST);
        }
        //Creates user object from the request input
        User u = new User();
        u.setEmail(payload.getEmail());
        u.setName(payload.getName());
        //Saves the user record to the database
        userRepository.save(u);
        //Returns the response with created user id 
        return new ResponseEntity<Integer>(u.getId(), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        // TODO: Return 200 OK if a user with the given ID exists, and the deletion is successful
        //       Return 400 Bad Request if a user with the ID does not exist
        //       The response body could be anything you consider appropriate
        //User u = new User();
        //u.setId(userId);
        try {
            //Checking if user exists 
            User u = userRepository.getReferenceById(userId);
        //Deleting the user if they exist
            userRepository.delete(u);
            //Returning sucess response (successfully deleted)
            return new ResponseEntity<String>("User " + u.getName() + " successfully deleted", null, HttpStatus.OK);
        }catch(EntityNotFoundException enfe){
            //Returning "Bad Request" if no record exists 
            return new ResponseEntity<String>("User Id " + userId + " does not exist", null, HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            //Returning "Bad Request" for any other possible errors
            return new ResponseEntity<String>(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }
}
