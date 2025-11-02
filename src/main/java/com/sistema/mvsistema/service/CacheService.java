package com.sistema.mvsistema.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    public void carregarDados(){
        System.out.println("✅ [CacheGlobal] Estados carregados: ");
    }
}
