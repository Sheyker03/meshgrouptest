package t.meshgroup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import t.meshgroup.exceptions.IncorrectDataByClassException;
import t.meshgroup.service.PhoneService;

import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api/phones")
@RequiredArgsConstructor
@Tag(name = "Phone controller function by user")
@Validated
public class PhoneController {
    private final PhoneService phoneService;

    @Operation(summary = "Change phone")
    @PostMapping("/change")
    public ResponseEntity<String> changePhone(
            @RequestParam String deprecatePhone,
            @RequestParam
            @Size(min = 11, max = 11, message = "Phone may be just 11 long symbols")
            String newPhone
    ) throws IncorrectDataByClassException {
        phoneService.changePhone(deprecatePhone, newPhone);
        return new ResponseEntity<>(
                String.format("Phone changed - %s.", newPhone),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Delete Phone")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePhone(
            @RequestParam
            @Size(min = 11, max = 11, message = "Phone may be just 11 long symbols")
            String phone
    ) throws IncorrectDataByClassException {
        phoneService.deletePhone(phone);
        return new ResponseEntity<>(
                String.format("Phone deleted - %s.", phone),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Add phone")
    @PutMapping("/add")
    public ResponseEntity<String> addPhone(
            @RequestParam
            @Size(min = 11, max = 11, message = "Phone may be just 11 long symbols")
            String phone
    ) throws IncorrectDataByClassException {
        phoneService.addPhone(phone);
        return new ResponseEntity<>(
                String.format("Phone added - %s.", phone),
                HttpStatus.OK
        );
    }
}
