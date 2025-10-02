package project.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class FulfillmentOrdersResponse {
    private List<FulfillmentOrder> fulfillment_orders;
}
