package com.server.yelp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.server.yelp.demos.dto.MangoDocDto;
import com.server.yelp.demos.dto.OrderDto;
import com.server.yelp.demos.dto.OrderItemDto;

@SpringBootTest
public class SpringbootApplicationTests {

    @Test
    void contextLoads() {
    }

    
    @Autowired
    MongoTemplate mongoTemplate;
    
    
    @Test
    public void save(){
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
    	
		
		mongoTemplate.insert(doc);
		
    	
    	
    }
    
    
    
    
}
