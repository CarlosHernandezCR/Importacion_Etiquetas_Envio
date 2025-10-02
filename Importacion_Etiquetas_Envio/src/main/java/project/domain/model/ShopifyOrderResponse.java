package project.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class ShopifyOrderResponse { private List<ShopifyOrder> orders; }
