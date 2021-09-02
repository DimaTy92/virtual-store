package com.example.demo.service;

import com.example.demo.dto.OrderReportDto;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void saveOrderTest() {
        Order order = new Order(1L, 100, LocalDate.of(2021, 1,1), null);
        orderService.saveOrder(order);

        Mockito.verify(orderRepository, Mockito.times(1)).save(order);
    }

    @Test
    public void getOrderReport_WhenDataIsPresent_ThenReturnValidData() {
        LocalDate currentDate = LocalDate.now();
        LocalDate inActiveDate = LocalDate.now().minusMonths(1);
        Customer activeCustomer = Customer.builder()
                .setId(1L)
                .setFirstName("Serhii")
                .setLastName("T")
                .setPhoneNumber("777")
                .build();
        Order activeOrder = new Order(1L, 100, currentDate, activeCustomer);
        activeCustomer.setOrders(List.of(activeOrder));
        Customer inActiveCustomer = Customer.builder()
                .setFirstName("Dmytro")
                .setLastName("T")
                .setPhoneNumber("228-228")
                .build();
        Order inActiveOrder = new Order(2L, 200, inActiveDate, inActiveCustomer);
        inActiveCustomer.setOrders(List.of(inActiveOrder));
        List<Customer> customers = List.of(activeCustomer, inActiveCustomer);
        when(customerService.findAllCustomer()).thenReturn(customers);

        OrderReportDto orderReport = orderService.getOrderReport();

        assertThat(orderReport.getActive()).hasSize(1);
        assertThat(orderReport.getInActive()).hasSize(1);

        Map<String, List<String>> active = orderReport.getActive();
        assertThat(active.get(currentDate.getMonth().toString())).hasSize(1);
        assertThat(active.get(currentDate.getMonth().toString()))
                .containsOnly(String.format("%s %s (%s): $%d", "Serhii", "T", "777", 100));

        Map<String, List<String>> inActive = orderReport.getInActive();
        assertThat(inActive.get(inActiveDate.getMonth().toString())).hasSize(1);
        assertThat(inActive.get(inActiveDate.getMonth().toString()))
                .containsOnly(String.format("%s %s (%s): $%d", "Dmytro", "T", "228-228", 200));
    }
}
