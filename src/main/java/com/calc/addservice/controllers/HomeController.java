package com.calc.addservice.controllers;


import com.calc.addservice.responseObjects.GenericResponseObject;
import com.calc.addservice.services.AddService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@RestController(value = "/")
public class HomeController {

    @Autowired
    private AddService addService;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<GenericResponseObject> index() {
        
        return new ResponseEntity<>(new GenericResponseObject(){ { setSuccess(true); setMessage("Hello!! This is add service. My other endpoints are: [GET]/healthcheck and [POST]/sum. Use Wisely. Test!!"); } }, 
            HttpStatus.OK);     
    }

    @RequestMapping(value="healthcheck", method=RequestMethod.GET)
    public ResponseEntity<GenericResponseObject> healthCheck() {
        
        return new ResponseEntity<>(new GenericResponseObject(){ { setSuccess(true); setMessage("Hello!! Addservice is running. Thanks"); } }, 
            HttpStatus.OK);     
    }


    @RequestMapping(value="sum", method=RequestMethod.POST)
    public ResponseEntity<GenericResponseObject> sum(@RequestParam String a, @RequestParam String b) throws NumberFormatException, InterruptedException {
        final String sum;
        // System.out.println(a);
        double x = this.addService.add(Double.parseDouble(a), Double.parseDouble(b));
        if(x % 1 == 0) {
            sum = "" + (int)x;
        } else {
            sum = "" + x;
        }
        Thread.sleep(5000);
        return new ResponseEntity<>(new GenericResponseObject(){ { setSuccess(true); setMessage(sum); } }, 
            HttpStatus.OK);     
    }
    


}
