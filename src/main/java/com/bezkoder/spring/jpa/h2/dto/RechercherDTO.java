package com.bezkoder.spring.jpa.h2.dto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RechercherDTO {
    private double latitude;
    private double longitude;
    private double rayon;
}
