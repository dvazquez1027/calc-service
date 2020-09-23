package com.ritmosoft.calculator.calcservice.generator;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UUIDGenerator implements IDGenerator {
    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }
}
