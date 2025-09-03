package org.example.DbModels;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
public class ItemPojo {
    private String itemName;
    private Double itemPrice;
    private Long itemQuantity;
    
    @Override
    public String toString() {
        return "{" +
                "itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemQuantity=" + itemQuantity +
                '}';
    }
}
