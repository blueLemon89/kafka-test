package com.example.orderservice.producer;

import com.example.orderservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class OrderProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.order-events}")
    private String orderTopic;

    public void sendOrderEvent(Order order) {
        System.out.println("🚀 Sending order event to Kafka topic: " + orderTopic);
        System.out.println("   Order details: " + order);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(orderTopic, order.getOrderId(), order);

        future.whenComplete((result, exception) -> {
            if (exception == null) {
                System.out.println("✅ Order event sent successfully!");
                System.out.println("   Topic: " + result.getRecordMetadata().topic());
                System.out.println("   Partition: " + result.getRecordMetadata().partition());
                System.out.println("   Offset: " + result.getRecordMetadata().offset());
            } else {
                System.err.println("❌ Failed to send order event: " + exception.getMessage());
            }
        });
    }
}