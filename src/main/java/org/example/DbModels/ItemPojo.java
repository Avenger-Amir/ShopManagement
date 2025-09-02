package org.example.DbModels;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemPojo {
    private String itemName;
    private Double itemPrice;
    private Long itemQuantity;
}
