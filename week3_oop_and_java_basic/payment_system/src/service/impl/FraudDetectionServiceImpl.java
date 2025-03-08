package service.impl;

import service.FraudDetectionService;

import java.math.BigDecimal;
import java.util.Random;

public class FraudDetectionServiceImpl implements FraudDetectionService {
    private static final Random random = new Random();

    @Override
    public boolean isFraudulentTransaction(String user, BigDecimal amount) {
        return random.nextDouble() < 0.1;
    }
}

