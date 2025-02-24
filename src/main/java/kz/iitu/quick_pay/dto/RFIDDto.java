package kz.iitu.quick_pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RFIDDto {
    @JsonProperty("rfid_tag")
    String rfidTag;

    @JsonProperty("cashbox_id")
    String cashbox_id;
}
