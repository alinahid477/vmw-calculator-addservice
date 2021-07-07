package com.calc.addservice.services;

import org.springframework.stereotype.Service;

@Service
public class AddServiceImpl implements AddService {

    @Override
    public double add(double x, double y) {
        return x + y;
    }
    
}
