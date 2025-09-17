package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import com.example.orderservice.producer.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderProducer orderProducer;

    public void processOrder(Order order) {
        System.out.println("Processing order: " + order);

        orderProducer.sendOrderEvent(order);

        System.out.println("Order event sent to Kafka: " + order.getOrderId());
    }
}