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

    public boolean decide(Long clientId){
        var clientDebt = getClientDebt(clientId);
        var currentCreditAmount = getCurrentCreditAmount();
        var abc = clientDebt.add(currentCreditAmount);
        if(abc.compareTo(N) > 0){
            return false;
        }

        var isClientHasExpires = isClientHasExpires(clientId);
        return !isClientHasExpires;
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

    public BigDecimal getCurrentCreditAmount(){
        return BigDecimal.ZERO;
    }

    public boolean isClientHasExpires(Long clientId){
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
}

