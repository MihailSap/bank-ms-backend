package ru.sapegin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sapegin.model.PaymentRegistry;
import ru.sapegin.repository.PaymentRegistryRepository;
import ru.sapegin.repository.ProductRegistryRepository;

import java.math.BigDecimal;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class CreditService {

    private BigDecimal N;
    private final ProductRegistryRepository productRegistryRepository;
    private final PaymentRegistryRepository paymentRegistryRepository;

    public boolean canClientOpenCredit(Long clientId){
        if(hasExpiredPayment(clientId)){
            return false;
        }

        var clientDebt = getClientDebt(clientId);
        var currentCreditAmount = getCurrentCreditAmount();
        var allCreditsAmount = clientDebt.add(currentCreditAmount);
        return allCreditsAmount.compareTo(N) > 0;
    }

    public BigDecimal getClientDebt(Long clientId) {
        var clientProducts = productRegistryRepository.findByClientId(clientId);
        var totalDebt = BigDecimal.ZERO;
        for (var pr : clientProducts) {
            var paymentRegistries = paymentRegistryRepository.findByProductRegistryId(pr.getId());
            if (!paymentRegistries.isEmpty()) {
                var lastPayment = paymentRegistries.stream()
                        .max(Comparator.comparing(PaymentRegistry::getPaymentDate))
                        .orElseThrow();
                totalDebt = totalDebt
                        .add(lastPayment.getDebtAmount())
                        .add(lastPayment.getInterestRateAmount());
            }
        }

        return totalDebt;
    }

    public boolean hasExpiredPayment(Long clientId){
        var clientProductRegistries = productRegistryRepository.findByClientId(clientId);
        for (var clientProductRegistry : clientProductRegistries) {
            var clientPaymentRegistries = paymentRegistryRepository.findByProductRegistryId(clientProductRegistry.getId());
            for(var clientPaymentRegistry : clientPaymentRegistries) {
                if(clientPaymentRegistry.isExpired()){
                    return true;
                }
            }
        }
        return false;
    }

    public BigDecimal getCurrentCreditAmount(){
        return BigDecimal.ZERO;
    }
}

