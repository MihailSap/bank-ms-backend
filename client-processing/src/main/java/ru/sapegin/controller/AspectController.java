package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.aspect.annotation.HttpIncomeRequestLog;
import ru.sapegin.aspect.annotation.LogDatasourceError;
import ru.sapegin.dto.CardDTO;
import ru.sapegin.dto.UserDTO;

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
}
