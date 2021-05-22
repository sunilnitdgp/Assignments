package com.assignment.model;

import lombok.Data;

@Data
public class Order {
    private Long unique_order_id;
    private Long c_unique_id;
    private Integer quantity;
    private String c_name;
}
