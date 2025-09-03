package org.example.WsModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WsShop {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;
}
