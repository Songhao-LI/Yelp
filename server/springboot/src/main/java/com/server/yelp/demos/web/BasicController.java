package com.server.yelp.demos.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.server.yelp.demos.dto.MangoDocDto;
import com.server.yelp.demos.dto.OrderDto;
import com.server.yelp.demos.dto.OrderItemDto;
import com.server.yelp.demos.dto.OrderReqDto;
import com.server.yelp.demos.service.OrderService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin(origins = "http://localhost:5000")
public class BasicController {

    List<Map<String, Object>> mainData = new ArrayList<>();
    List<Map<String, Object>> allComments = new ArrayList<>();
    Map<String, Map<String, Object>> storedFiles = new HashMap<>();

    // GET
    @GetMapping("api/getList")
    @ResponseBody
    public Map<String, Object> getList() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "ok");
        response.put("data", mainData);
        return response;
    }

    @GetMapping("api/getDetails")
    @ResponseBody
    public Map<String, Object> getDetails(@RequestParam String id) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> matchingData = null;
        for (Map<String, Object> item : mainData) {
            if (id.equals(item.get("id"))) {
                matchingData = item;
                break;
            }
        }

        if (matchingData == null) {
            response.put("code", 1);
            response.put("message", "not found");
            response.put("data", null);
            return response;
        }

        response.put("code", 0);
        response.put("message", "ok");
        response.put("data", matchingData);
        return response;
    }

    @GetMapping("api/getCommentList")
    @ResponseBody
    public Map<String, Object> getCommentList(@RequestParam String id) {
        Map<String, Object> response = new HashMap<>();
        ArrayList<Map<String, Object>> comments = new ArrayList<>();
        for (Map<String, Object> item : allComments) {
            if (id.equals(item.get("sourceId"))) {
                comments.add(item);
            }
        }

        response.put("code", 0);
        response.put("message", "ok");
        response.put("data", comments);
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

    // POST
    @PostMapping("api/addToList")
    @ResponseBody
    public Map<String, Object> addItems(@RequestBody Map<String, Object> request) throws NoSuchAlgorithmException {
        Map<String, Object> response = new HashMap<>();
        // CSRF protection
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

    @PostMapping("api/addComment")
    @ResponseBody
    public Map<String, Object> addComment(@RequestBody Map<String, Object> request) throws NoSuchAlgorithmException {
        Map<String, Object> response = new HashMap<>();
        // CSRF protection
        if (request == null || request.isEmpty()) {
            response.put("code", 1);
            response.put("message", "parameter is None");
            return response;
        }

        // init
        Map<String, Object> child = new HashMap<>();
        child.put("sourceId", "");
        child.put("username", "");
        child.put("star", 0);
        child.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        child.put("desc", "");

        // sourceId
        if (request.containsKey("sourceId")) {
            child.put("sourceId", request.get("sourceId"));
        } else {
            response.put("code", 1);
            response.put("message", "sourceId is necessary");
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

        // star
        if (request.containsKey("star")) {
            child.put("star", request.get("star"));
        } else {
            response.put("code", 1);
            response.put("message", "star is necessary");
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

        // store
        allComments.add(child);

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
        Map<String, String> returnData = new HashMap<>();
        returnData.put("id", fileId);
        response.put("data", returnData);
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
    
    @Deprecated
    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping("/test/saveMg")
    @ResponseBody
    @Deprecated
    public String testSave(){
    	
    	
    	MangoDocDto doc = new MangoDocDto();
    	doc.setAddress("北京");
    	doc.setComments(1);
    	doc.setStar(10);
    	doc.setLng(1);
    	doc.setId("ab9990990");
    	doc.setTime(new Date());
    	doc.setTitle("测试数据");
    	List<OrderDto> order = new ArrayList();
    	OrderDto od = new OrderDto();
    	od.setDay("2023-12-10");

    	List<OrderItemDto> orders = new ArrayList(1);
    	OrderItemDto it = new OrderItemDto();
    	it.setPeopleNumber("10");
    	it.setTime("2023-12-10");
    	it.setValue("vt");
    	orders.add(it);
    	
    	 
    	od.setOrders(orders);;
    	order.add(od);
		doc.setOrder(order );
    	
		
		mongoTemplate.insert(doc,"dataList");
    	
    	
    	
    	
    	
    	return "success";
    }

    @RequestMapping("/api/findById/{id}")
    @ResponseBody
    public MangoDocDto findById(@PathVariable(name="id")  String id){
    	//return mongoTemplate.findById("ab9990990", MangoDocDto.class,"dataList");
    	return orderService.findById(id);
    }
    
    @Autowired
    OrderService orderService;
    
    @RequestMapping("/test/updateMg")
    @ResponseBody
    @Deprecated
    public MangoDocDto testUpdateMg(){
    	OrderReqDto t = new OrderReqDto();
    	t.setDay("2023-12-10");
    	t.setId("ab9990960");
    	OrderItemDto it = new OrderItemDto();
    	it.setPeopleNumber("10");
    	it.setTime("2023-12-09");
    	it.setValue("va");  //   day = 2023-12-10  数据已经在 表中存在 ， value = va 数据不存在，测试 
		t.setItemDto(it );
		
		orderService.update(t);
		
		return this.findById(t.getId());
    	
    }
    
    @RequestMapping("/api/updateMg")
    @ResponseBody
    public String updateMg(@RequestBody OrderReqDto reqDto ){
    	return orderService.update(reqDto);
    }
    //复制放到这下，重新服务
    @RequestMapping("/api/findByIdAndDay/{id}")
    @ResponseBody
    public List<OrderItemDto> findByIdAndDay(@PathVariable(name="id")  String id ){
        OrderReqDto t = new OrderReqDto();
        t.setId(id);
        return orderService.findByIdAndDay(t);
    }
}
