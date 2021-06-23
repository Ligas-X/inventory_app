package ru.sg.inventory_server_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sg.inventory_server_app.models.UserInfo;
import ru.sg.inventory_server_app.services.UserInfoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user_info")
public class UserInfoController {
    private final UserInfoService userInfoService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping
    @ResponseBody
    public List<UserInfo> getUsersInfo() {
        return userInfoService.getUsers();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Optional<UserInfo> getUserInfo(@PathVariable("id") Long id) {
        return userInfoService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<UserInfo> addUserInfo(@RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(userInfoService.createUser(userInfo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserInfo> updateUserInfo(@RequestBody UserInfo userInfo,
                                                   @PathVariable Long id) {
        return ResponseEntity.ok(userInfoService.updateUser(userInfo, id));
    }

    @DeleteMapping("/{id}")
    public void deleteUserInfo(@PathVariable("id") Long id) {
        userInfoService.deleteUser(id);
    }
}
