package project.domain.model;

import lombok.Data;

@Data
public class FulfillmentOrder {
    private long id;
    private String status;
    private Destination destination;
}
