package com.accenture.assessment.bni.controller;

import com.accenture.assessment.bni.controller.request.UserRequest;
import com.accenture.assessment.bni.controller.response.UserPagedResponse;
import com.accenture.assessment.bni.controller.response.UserResponse;
import com.accenture.assessment.bni.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.accenture.assessment.bni.constants.ApiConstants.V1_USER;


@RestController
@RequestMapping(V1_USER)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserPagedResponse> getActiveUserData(@RequestParam("max_records") Integer maxRecords,
                                                               @RequestParam("offset") Integer offset) {
        return new ResponseEntity(userService.getActiveUser(maxRecords, offset), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponse> save(@RequestBody @Valid UserRequest request) {
        return new ResponseEntity(userService.save(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> save(@PathVariable Long id) {
        return new ResponseEntity(userService.getUser(id), HttpStatus.OK);
    }
}