package project.domain.model;

import lombok.Data;

@Data
public class ShopifyOrder {
    private long id;
    private int order_number;
    private Address shipping_address;
}
