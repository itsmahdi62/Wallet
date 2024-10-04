package com.example.wallet.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("*")
public class NotFoundRoutes {
    public ResponseEntity<String> nofFoundroutes(){
        return new ResponseEntity<>("This route does not exist" , HttpStatus.NOT_FOUND);
    }
}
