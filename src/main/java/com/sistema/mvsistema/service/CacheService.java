package com.sistema.mvsistema.service;

import com.sistema.mvsistema.model.Estado;
import com.sistema.mvsistema.model.Municipio;
import com.sistema.mvsistema.repository.EstadoRepository;
import com.sistema.mvsistema.repository.MunicipioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CacheService {
    @Autowired
    MunicipioRepository municipioRepository;

    @Autowired
    EstadoRepository estadoRepository;

    private List<Municipio> municipiosCache;
    private List<Estado> estadosCache;

    @PostConstruct
    public void carregarDados(){
        try{
            municipiosCache = municipioRepository.findAll();
            System.out.println("✔ [CacheGlobal] Municipios carregados: " + municipiosCache.size());
        } catch (Exception e) {
            System.err.println("✔ [CacheGlobal] Falha ao carregar municipios: " + e);
            municipiosCache = Collections.emptyList();
        }

        try{
            estadosCache = estadoRepository.findAll();
            System.out.println("✔ [CacheGlobal] Estados carregados: " + estadosCache.size());
        } catch (Exception e) {
            System.err.println("✔ [CacheGlobal] Falha ao carregar estados: " + e);
            estadosCache = Collections.emptyList();
        }
    }

    public List<Municipio> getMunicipiosCache() {
        return municipiosCache;
    }

    public List<Estado> getEstadosCache() {
        return estadosCache;
    }

}
