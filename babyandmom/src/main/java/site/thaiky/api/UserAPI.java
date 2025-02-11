package site.thaiky.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.thaiky.entity.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserAPI {
    List<User> listUsers = new ArrayList<>();

    //Create user (CREATE)
    @PostMapping
    public ResponseEntity createUser(@RequestBody User newUser){
        listUsers.add(newUser);
        return ResponseEntity.ok(newUser);
    }

    //List user (READ)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){

        return ResponseEntity.ok(listUsers);
    }




}
