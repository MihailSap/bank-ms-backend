package ru.sapegin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sapegin.enums.KeyEnum;
import ru.sapegin.model.ClientProduct;
import ru.sapegin.repository.ClientProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientProductService {

    private final ClientProductRepository clientProductRepository;

//    public List<ClientProduct> getClientProducts(Long clientId) {
//        var clientProducts = clientProductRepository.findByClientId(clientId);
//        var sum = 0;
//        for(var clientProduct : clientProducts) {
//            var product = clientProduct.getProduct();
//            var productKey = product.getKey();
//            if(productKey != KeyEnum.AC && productKey != KeyEnum.PC && productKey != KeyEnum.IPO){
//                continue;
//            }
//            sum += product.get
//        }
//    }
}
