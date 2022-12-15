package com.project.web4.controllers;

import com.project.web4.DTO.DataDTO;
import com.project.web4.config.jwt.JWTProvider;
import com.project.web4.model.Data;
import com.project.web4.model.User;
import com.project.web4.services.DataService;
import com.project.web4.services.UserService;
import com.project.web4.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Log
@RestController
@RequestMapping(path = "/api/data")
@AllArgsConstructor
public class DataController {
    @Autowired
    private final DataService dataService;
    @Autowired
    private final DataUtils dataUtils;
    @Autowired
    private final JWTProvider jwtProvider;
    @Autowired
    private final UserService userService;

    @PostMapping("/add-data")
    private ResponseEntity<String> addData(@Valid @RequestBody DataDTO dataDTO, HttpServletRequest request) {
        try {
            double x = dataDTO.getX();
            double y = dataDTO.getY();
            double r = dataDTO.getR();
            Data data = new Data(x, y, r);
            data.setAnswer(dataUtils.checkAll(data));
            data.setTime(dataUtils.getTime());
            String username = jwtProvider.getLoginFromToken(request.getHeader("Authorization").substring(7));
            User user = userService.getUserByUsername(username);
            data.setUser(user);
            dataService.saveData(data);
            log.info("Add new point");
            return new ResponseEntity<>("Точка успешно добавлена", HttpStatus.OK);
        } catch (NumberFormatException e) {
            log.severe("Wrong value of x, y or r: " + e.getMessage());
            return new ResponseEntity<>("Значения x, y, r должны быть числами", HttpStatus.OK);
        }
    }

    @GetMapping("/get-data")
    private ResponseEntity<List<Data>> getData(HttpServletRequest request) {
        String username = jwtProvider.getLoginFromToken(request.getHeader("Authorization").substring(7));
        User user = userService.getUserByUsername(username);
        List<Data> mass = dataService.getData(user);
        log.info("Get all user data");
        return new ResponseEntity<>(mass, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    private ResponseEntity<String> deleteData(HttpServletRequest request) {
        String username = jwtProvider.getLoginFromToken(request.getHeader("Authorization").substring(7));
        User user = userService.getUserByUsername(username);
        dataService.deleteData(user);
        log.info("Delete all user data");
        return new ResponseEntity<>("Точки пользователя удалены", HttpStatus.OK);
    }
}
