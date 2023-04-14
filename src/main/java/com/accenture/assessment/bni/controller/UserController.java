package com.accenture.assessment.bni.controller;

import com.accenture.assessment.bni.controller.request.UserRequest;
import com.accenture.assessment.bni.controller.response.UserPagedResponse;
import com.accenture.assessment.bni.controller.response.UserResponse;
import com.accenture.assessment.bni.service.UserService;
import com.accenture.assessment.bni.service.UserSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.accenture.assessment.bni.constants.ApiConstants.V1_USER;


@RestController
@RequestMapping(V1_USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserSettingService userSettingService;

    @GetMapping
    public ResponseEntity<UserPagedResponse> getActiveUserData(@RequestParam("max_records") Integer maxRecords,
                                                               @RequestParam("offset") Integer offset) {
        return new ResponseEntity(userService.getPagedActiveUser(maxRecords, offset), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> save(@PathVariable Long id) {
        return new ResponseEntity(userService.getUser(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@RequestBody @Valid UserRequest request, @PathVariable Long id) {
        return new ResponseEntity(userService.update(request, id), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<UserResponse> save(@RequestBody @Valid UserRequest request) {
        return new ResponseEntity(userService.save(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/settings")
    public ResponseEntity<UserResponse> updateSettings(@RequestBody List<Map<String, Object>> request,
                                                       @PathVariable("id") Long id) {
        return new ResponseEntity(userService.updateSettings(request,id), HttpStatus.OK);
    }

    @PutMapping("/{id}/refresh")
    public ResponseEntity<UserResponse> activateUser(@PathVariable("id") Long id) {
        return new ResponseEntity(userService.activateUser(id), HttpStatus.OK);
    }


}