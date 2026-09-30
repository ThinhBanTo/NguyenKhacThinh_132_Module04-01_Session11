package com.example.pharmacyservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseExportRequest {

    private Integer quantityStock;
    private Integer numberTrucks;
    private String destination;
}