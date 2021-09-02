package com.example.demo.service;

import com.example.demo.dto.OrderReportDto;
import com.example.demo.entity.Order;

public interface OrderService {
    Order saveOrder(Order order);

    OrderReportDto getOrderReport();
}
