package com.example.inventoryservice.service;

import com.example.inventoryservice.model.Order;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {

    private final Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("product-1", 100);
        inventory.put("product-2", 50);
        inventory.put("product-3", 75);
    }

    public void processOrder(Order order) {
        System.out.println("Processing inventory for order: " + order);

        String productId = order.getProductId();
        int requestedQuantity = order.getQuantity();

        if (inventory.containsKey(productId)) {
            int currentStock = inventory.get(productId);
            if (currentStock >= requestedQuantity) {
                inventory.put(productId, currentStock - requestedQuantity);
                System.out.println("✅ Order " + order.getOrderId() + " processed successfully!");
                System.out.println("   Product: " + productId + ", Quantity: " + requestedQuantity);
                System.out.println("   Remaining stock: " + inventory.get(productId));
            } else {
                System.out.println("❌ Insufficient stock for order " + order.getOrderId());
                System.out.println("   Product: " + productId + ", Requested: " + requestedQuantity + ", Available: " + currentStock);
            }
        } else {
            System.out.println("❌ Product not found: " + productId + " for order " + order.getOrderId());
        }

        System.out.println("Current inventory: " + inventory);
        System.out.println("---");
    }
}