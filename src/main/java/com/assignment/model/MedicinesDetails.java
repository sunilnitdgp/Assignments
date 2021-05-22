package com.assignment.model;

import lombok.Data;

import java.util.Date;

@Data
public class MedicinesDetails extends Order{
    private String c_batch_no;
    private Date d_expiry_date;
    private Integer n_balance_qty;
    private String c_packaging;
    private String c_unique_code;
    private String c_schemes;
    private Float n_mrp;
    private String c_manufacturer;
    private long hsn_code;
}
