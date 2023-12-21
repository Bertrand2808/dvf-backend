package com.bezkoder.spring.jpa.h2.service;

import com.bezkoder.spring.jpa.h2.repository.ParcelleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParcelleService {

    private final ParcelleRepository parcelleRepository;

    @Autowired
    public ParcelleService(ParcelleRepository parcelleRepository) {
        this.parcelleRepository = parcelleRepository;
    }

    public boolean isParcelleEmpty() {
        long count = parcelleRepository.count();
        return count == 0;
    }
}


