package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CUSTOMER_ORDER")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_VALUE", nullable = false)
    private Integer orderValue;

    @Column(name = "DATE_OF_ORDER", nullable = false)
    private LocalDate dateOfOrder;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;
}
