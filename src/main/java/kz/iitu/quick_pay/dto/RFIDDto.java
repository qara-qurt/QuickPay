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

    @JsonProperty("manufacturer")
    String manufacturer;

    @JsonProperty("model")
    String model;

    @JsonProperty("xtid")
    String xtid;

    @JsonProperty("security")
    String security;

    @JsonProperty("file_open")
    boolean fileOpen;

    @JsonProperty("serial_number")
    String serialNumber;
}
