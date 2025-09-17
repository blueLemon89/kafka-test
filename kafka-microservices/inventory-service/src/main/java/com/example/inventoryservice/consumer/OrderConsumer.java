package com.example.inventoryservice.consumer;

import com.example.inventoryservice.model.Order;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    @Autowired
    private InventoryService inventoryService;

    @KafkaListener(topics = "${kafka.topic.order-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(
            @Payload Order order,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {

        System.out.println("📨 Received order event from Kafka:");
        System.out.println("   Topic: " + topic);
        System.out.println("   Partition: " + partition);
        System.out.println("   Key: " + key);
        System.out.println("   Offset: " + offset);
        System.out.println("   Order: " + order);

        try {
            inventoryService.processOrder(order);
            acknowledgment.acknowledge();
            System.out.println("✅ Message acknowledged successfully");
        } catch (Exception e) {
            System.err.println("❌ Error processing order: " + e.getMessage());
        }
    }
}