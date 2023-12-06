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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class BasicController {

    List<Object> mainData = new ArrayList<>();

    // http://127.0.0.1:8080/hello?name=lisi
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }

    // functions related to display
    @GetMapping("/getlist")
    @ResponseBody
    public List<Object> getList() {
        return mainData;
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

    @PostMapping("/addItems")
    @ResponseBody
    public Map<String, Object> addItems(@RequestBody Map<String, Object> request) throws NoSuchAlgorithmException {
        Map<String, Object> response = new HashMap<>();
        // 基本的参数检查
        // ... 参数检查和处理逻辑 ...
//        if (!request.containsKey("key1")) {
//            String value1 = (String) myMap.get("key1");
//            // 处理value1
//        }
        System.out.println("Enter function");
        System.out.println(request.toString());

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
            response.put("status", 1);
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
            response.put("status", 1);
            response.put("message", "star is necessary");
            return response;
        }

        // username
        if (request.containsKey("username")) {
            child.put("username", request.get("username"));
        } else {
            response.put("status", 1);
            response.put("message", "username is necessary");
            return response;
        }

        // desc
        if (request.containsKey("desc")) {
            child.put("desc", request.get("desc"));
        } else {
            response.put("status", 1);
            response.put("message", "desc is necessary");
            return response;
        }

        // longitude and latitude
        if (request.containsKey("lng")) {
            child.put("lng", request.get("lng"));
        } else {
            response.put("status", 1);
            response.put("message", "lng is necessary");
            return response;
        }
        if (request.containsKey("lat")) {
            child.put("lat", request.get("lat"));
        } else {
            response.put("status", 1);
            response.put("message", "lat is necessary");
            return response;
        }

        // address
        if (request.containsKey("address")) {
            child.put("address", request.get("address"));
        } else {
            response.put("status", 1);
            response.put("message", "address is necessary");
            return response;
        }

        // images
        if (request.containsKey("imgs")) {
            // TODO: check type
            child.put("imgs", request.get("imgs"));
        } else {
            response.put("status", 1);
            response.put("message", "imgs is necessary");
            return response;
        }

        // 添加到列表
        mainData.add(child);

        response.put("status", 0);
        response.put("message", "ok");
        response.put("data", child);
        return response;
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

    @ModelAttribute
    public void parseUser(@RequestParam(name = "name", defaultValue = "unknown user") String name
            , @RequestParam(name = "age", defaultValue = "12") Integer age, User user) {
        user.setName("zhangsan");
        user.setAge(18);
    }
}
