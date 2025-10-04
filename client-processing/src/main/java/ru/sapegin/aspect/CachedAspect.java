package ru.sapegin.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.sapegin.aspect.annotation.Cached;
import ru.sapegin.aspect.cache.CacheEntry;
import ru.sapegin.aspect.cache.CacheStorage;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CachedAspect {

    private final CacheStorage<String, Object> cacheStorage;

    @Pointcut("@annotation(ru.sapegin.aspect.annotation.Cached)")
    public void cachedMethods(){
    }

    @Around("cachedMethods()")
    public Object processCache(ProceedingJoinPoint joinPoint) throws Throwable {
        var signature = (MethodSignature) joinPoint.getSignature();
        var annotation = signature.getMethod().getAnnotation(Cached.class);

        String key;
        var args = joinPoint.getArgs();
        if (annotation.cacheByPrimaryKey()) {
            key = args[0].toString();
        } else {
            key = String.valueOf(Arrays.hashCode(args));
        }

        var result = cacheStorage.get(key);
        if(result == null){
            log.info("Объект с ключом {} отсутствует в кэше", key);
            result = joinPoint.proceed();
            cacheStorage.put(key, new CacheEntry<>(result, System.currentTimeMillis()));
        } else {
            log.info("Объект с ключом {} найден в кэше", key);
        }
        return result;
    }
}
