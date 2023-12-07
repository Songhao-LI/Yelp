/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.server.yelp.demos.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin(origins = "http://localhost:5000")
public class BasicController {

    List<Object> mainData = new ArrayList<>();
    Map<String, Map<String, Object>> storedFiles = new HashMap<>();

    // functions related to display
    @GetMapping("api/getList")
    @ResponseBody
    public Map<String, Object> getList() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "ok");
        response.put("data", mainData);
        return response;
    }

    @PostMapping("api/addItems")
    @ResponseBody
    public Map<String, Object> addItems(@RequestBody Map<String, Object> request) throws NoSuchAlgorithmException {
        Map<String, Object> response = new HashMap<>();
        // check if parameter exist
        if (request == null || request.isEmpty()) {
            response.put("code", 1);
            response.put("message", "parameter is None");
            return response;
        }

        // init
        Map<String, Object> child = new HashMap<>();
        child.put("id", "");
        child.put("title", "");
        child.put("star", 0);
        child.put("lat", 0.0);
        child.put("lng", 0.0);
        child.put("address", "");
        child.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        child.put("comments", 0);
        child.put("desc", "");
        child.put("imgs", new ArrayList<>());

        // title
        if (request.containsKey("title")) {
            child.put("title", request.get("title"));
        } else {
            response.put("code", 1);
            response.put("message", "title is necessary");
            return response;
        }

        // generate ID
        String titleEncode = child.get("title") + String.valueOf(System.currentTimeMillis());
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hash = digest.digest(titleEncode.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        child.put("id", sb.toString());

        // star
        if (request.containsKey("star")) {
            child.put("star", request.get("star"));
        } else {
            response.put("code", 1);
            response.put("message", "star is necessary");
            return response;
        }

        // username
        if (request.containsKey("username")) {
            child.put("username", request.get("username"));
        } else {
            response.put("code", 1);
            response.put("message", "username is necessary");
            return response;
        }

        // desc
        if (request.containsKey("desc")) {
            child.put("desc", request.get("desc"));
        } else {
            response.put("code", 1);
            response.put("message", "desc is necessary");
            return response;
        }

        // longitude and latitude
        if (request.containsKey("lng")) {
            child.put("lng", request.get("lng"));
        } else {
            response.put("code", 1);
            response.put("message", "lng is necessary");
            return response;
        }
        if (request.containsKey("lat")) {
            child.put("lat", request.get("lat"));
        } else {
            response.put("code", 1);
            response.put("message", "lat is necessary");
            return response;
        }

        // address
        if (request.containsKey("address")) {
            child.put("address", request.get("address"));
        } else {
            response.put("code", 1);
            response.put("message", "address is necessary");
            return response;
        }

        // images
        if (request.containsKey("imgs")) {
            // TODO: check type
            child.put("imgs", request.get("imgs"));
        } else {
            response.put("code", 1);
            response.put("message", "imgs is necessary");
            return response;
        }

        // store
        mainData.add(child);

        response.put("code", 0);
        response.put("message", "ok");
        response.put("data", child);
        return response;
    }

    @PostMapping("api/upload")
    @ResponseBody
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) throws NoSuchAlgorithmException {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("code", 1);
            response.put("message", "File is empty");
            return response;
        }

        String fileName = file.getOriginalFilename() + System.currentTimeMillis();
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hash = digest.digest(fileName.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        String fileId = sb.toString();

        Map<String, Object> fileInfo = new HashMap<>();
        try {
            fileInfo.put("type", file.getContentType());
            // for small files only
            fileInfo.put("body", file.getBytes());
            storedFiles.put(fileId, fileInfo);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("message", "Error processing file");
            return response;
        }

        response.put("code", 0);
        response.put("message", "ok");
        response.put("id", fileId);
        return response;
    }

    @GetMapping("/api/getImage")
    public ResponseEntity<?> getImage(@RequestParam String id) {
        if (!storedFiles.containsKey(id)) {
            return ResponseEntity.status(404).body("Picture does not exist");
        }

        Map<String, Object> imageInfo = storedFiles.get(id);
        byte[] imageData = (byte[]) imageInfo.get("body");
        String imageType = (String) imageInfo.get("type");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageType))
                .body(imageData);
    }





    // http://127.0.0.1:8080/save_user?name=newName&age=11
    @RequestMapping("/save_user")
    @ResponseBody
    public String saveUser(User u) {
        return "user will save: name=" + u.getName() + ", age=" + u.getAge();
    }

    // http://127.0.0.1:8080/html
    @RequestMapping("/html")
    public String html(){
        return "index.html";
    }

    // http://127.0.0.1:8080/hello?name=lisi
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }

    // http://127.0.0.1:8080/user
    @RequestMapping("/user")
    @ResponseBody
    public User user() {
        User user = new User();
        user.setName("theonefx");
        user.setAge(666);
        return user;
    }

    @ModelAttribute
    public void parseUser(@RequestParam(name = "name", defaultValue = "unknown user") String name
            , @RequestParam(name = "age", defaultValue = "12") Integer age, User user) {
        user.setName("zhangsan");
        user.setAge(18);
    }
}
