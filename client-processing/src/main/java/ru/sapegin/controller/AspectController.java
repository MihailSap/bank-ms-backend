package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sapegin.aspect.annotation.LogDatasourceError;

@RestController
@RequestMapping("/api/aspect")
@RequiredArgsConstructor
public class AspectController {

    @GetMapping("/first")
    @LogDatasourceError
    public void testFirst(){
        throw new RuntimeException("Вы вызвали ошибку");
    }
}
