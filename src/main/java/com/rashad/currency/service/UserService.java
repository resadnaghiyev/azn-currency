package com.rashad.currency.service;

import com.rashad.currency.model.JwtResponse;
import com.rashad.currency.model.RegisterRequest;

import java.util.Map;

public interface UserService {

    String register(RegisterRequest request);

    JwtResponse login(RegisterRequest request);
}
