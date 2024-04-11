package t.meshgroup.controller;

import io.swagger.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import t.meshgroup.exceptions.IncorrectDataByClassException;
import t.meshgroup.models.User;
import t.meshgroup.service.UserService;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User controller")
@Validated
public class UserController {
    private static final String dateOfBirthPattern = "(^(((0[1-9]|1[0-9]|2[0-8])[\\.](0[1-9]|1[012]))|((29|30|31)[\\.](0[13578]|1[02]))|((29|30)[\\.](0[4,6,9]|11)))[\\.](19|[2-9][0-9])\\d\\d$)|(^29[\\.]02[\\.](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)";
    private final UserService userService;


    @Operation(summary = "Find users")
    @GetMapping
    @ResponseBody
    ResponseEntity<List<User>> findUsers(
            @RequestParam(required = false) @Pattern(regexp = dateOfBirthPattern) String dateOfBirth,
            @RequestParam(required = false) @Size(min = 11, max = 11, message = "Phone need be 11 long symbols") String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) throws IncorrectDataByClassException {
        return new ResponseEntity(userService.getUsersCommon(dateOfBirth, phone, email, name, limit, offset), HttpStatus.OK);
    }
}
