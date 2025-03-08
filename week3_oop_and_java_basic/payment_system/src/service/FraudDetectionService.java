package service;

import java.math.BigDecimal;

public interface FraudDetectionService {
    boolean isFraudulentTransaction(String user, BigDecimal amount);
}
