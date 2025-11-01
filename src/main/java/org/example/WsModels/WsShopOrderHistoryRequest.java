package org.example.WsModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class WsShopOrderHistoryRequest {

    @JsonProperty("start_timestamp")
    private Instant startTimestamp = Instant.now() .minusSeconds(30 * 24 * 60 * 60); // Default to one month ago

    @JsonProperty("end_timestamp")
    private Instant endTimestamp = Instant.now();
}
