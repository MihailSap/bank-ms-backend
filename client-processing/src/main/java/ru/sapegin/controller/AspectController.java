package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.aspect.annotation.HttpIncomeRequestLog;
import ru.sapegin.aspect.annotation.HttpOutcomeRequestLog;
import ru.sapegin.aspect.annotation.LogDatasourceError;
import ru.sapegin.dto.CardDTO;

@RestController
@RequestMapping("/api/aspect")
@RequiredArgsConstructor
public class AspectController {

    @GetMapping("/first")
    @LogDatasourceError
    public void testFirst(){
        throw new RuntimeException("Вы вызвали ошибку");
    }

    @PostMapping("/post")
    @HttpIncomeRequestLog
    public void doSomething(@RequestBody CardDTO cardDTO){
        System.out.println("Hello World" + cardDTO.toString());
    }

    @GetMapping("/get")
    @HttpOutcomeRequestLog
    public CardDTO getCardDTO(){
        return new CardDTO(1L, "MIR");
    }
}
