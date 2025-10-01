package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.aspect.annotation.HttpIncomeRequestLog;
import ru.sapegin.aspect.annotation.HttpOutcomeRequestLog;
import ru.sapegin.aspect.annotation.LogDatasourceError;
import ru.sapegin.dto.CardDTO;

@Slf4j
@RestController
@RequestMapping("/api/aspect")
@RequiredArgsConstructor
public class AspectExampleController {

    @GetMapping("/ex")
    @LogDatasourceError
    public void testLogDatasourceError(){
        throw new RuntimeException("Вы вызвали ошибку");
    }

    @PostMapping("/post")
    @HttpIncomeRequestLog
    public void testHttpIncomeRequestLog(@RequestBody CardDTO cardDTO){
        log.info("Income request: {}", cardDTO);
    }

    @GetMapping("/get")
    @HttpOutcomeRequestLog
    public CardDTO testHttpOutcomeRequestLog(){
        return new CardDTO(1L, "-");
    }
}
