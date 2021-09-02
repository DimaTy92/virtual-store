package com.example.demo.service.impl;

import com.example.demo.dto.OrderReportDto;
import com.example.demo.dto.type.StatusType;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.CustomerService;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_NOTE_FORMAT = "%s %s (%s): $%d";
    private final OrderRepository orderRepository;
    private final CustomerService customerService;

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public OrderReportDto getOrderReport() {
        List<Customer> allCustomer = customerService.findAllCustomer();
        Map<StatusType, List<Customer>> customerStatusTypeMap = allCustomer.stream()
                .collect(Collectors.groupingBy(this::getStatusType));
        List<Customer> activeCustomers = customerStatusTypeMap.getOrDefault(StatusType.ACTIVE, Collections.emptyList());
        List<Customer> inActiveCustomers = customerStatusTypeMap.getOrDefault(StatusType.INACTIVE, Collections.emptyList());
        Map<String, List<String>> activeOrderReports = getOrderReportData(activeCustomers);
        Map<String, List<String>> inActiveOrderReports = getOrderReportData(inActiveCustomers);
        return new OrderReportDto(activeOrderReports, inActiveOrderReports);
    }

    private Map<String, List<String>> getOrderReportData(List<Customer> customers) {
        return customers.stream()
                .map(Customer::getOrders)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Order::getDateOfOrder))
                .collect(Collectors.groupingBy(order -> order.getDateOfOrder().getMonth().toString(),
                        Collectors.mapping(this::getOrderDetailsNote, Collectors.toList())));
    }

    private String getOrderDetailsNote(Order order) {
        Customer customer = order.getCustomer();
        return String.format(ORDER_NOTE_FORMAT,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhoneNumber(),
                order.getOrderValue());
    }

    private StatusType getStatusType(Customer customer) {
        LocalDate currentDate = LocalDate.now();
        Optional<Order> lastOrder = customer.getOrders().stream()
                .max(Comparator.comparing(Order::getDateOfOrder));
        if (lastOrder.isEmpty()) {
            return StatusType.INACTIVE;
        }
        Order order = lastOrder.get();
        boolean active = isActive(currentDate, order.getDateOfOrder());
        if (active) {
            return StatusType.ACTIVE;
        }
        return StatusType.INACTIVE;
    }

    private boolean isActive(LocalDate currentDate, LocalDate orderDate) {
        return currentDate.getYear() == orderDate.getYear() &&
                currentDate.getMonth() == orderDate.getMonth();
    }
}
