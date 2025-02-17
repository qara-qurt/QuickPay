package kz.iitu.quick_pay.controller;

import kz.iitu.quick_pay.dto.RFIDDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/rfid")
public class RFIDController {

    @PostMapping
    public void createRFID(@RequestBody RFIDDto rfid) {
        if (rfid == null) {
            System.out.println("[ERROR] Received null RFID object");
        } else {
            System.out.println("[INFO] RFID Created:");
            System.out.println("RFID Tag: " + rfid.getRfidTag());
            System.out.println("Manufacturer: " + rfid.getManufacturer());
            System.out.println("Model: " + rfid.getModel());
            System.out.println("XTID: " + rfid.getXtid());
            System.out.println("Security: " + rfid.getSecurity());
            System.out.println("File Open: " + rfid.isFileOpen());
            System.out.println("Serial Number: " + rfid.getSerialNumber());
        }
    }
}
