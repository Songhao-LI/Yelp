package com.server.yelp.demos.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.assertj.core.util.Arrays;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.server.yelp.demos.dto.MangoDocDto;
import com.server.yelp.demos.dto.OrderDto;
import com.server.yelp.demos.dto.OrderItemDto;
import com.server.yelp.demos.dto.OrderReqDto;
import com.server.yelp.demos.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService  {

	@Autowired
	MongoTemplate mongoTemplate;
	//mangodb 中集合名称
	static final String DOC_NAME="dataList";
	
	@Override
	public MangoDocDto findById(String id) {
		return mongoTemplate.findById(id,MangoDocDto.class,DOC_NAME);
	}

	@Override
	public String update(OrderReqDto reqDto) {
		//1. 根据文档ID ,获取到对应的文档数据
		MangoDocDto doc = this.findById(reqDto.getId());
		if(doc ==null){
			return String.format("ID为:{} 的数据为空，请重新检查", reqDto.getId());
		}
		
		List<OrderDto> orderList = doc.getOrder();
		if(orderList ==null ){
			orderList = new ArrayList(1);
			doc.setOrder(orderList);
		}
		
		Map<String,OrderDto> ordMap =  orderList.stream()
		.collect(Collectors.toMap(
				OrderDto::getDay, each->each,(value1,value2)->value1));
		//2. 判断 文档对象下的  order 集合下的 day 于 入参中的day
		if(!ordMap.containsKey(reqDto.getDay())){
			//  2.1 如果不从在，则 将入参对象添加到 order集合下
			OrderDto orde = new OrderDto();
			orde.setDay(reqDto.getDay());

			List<OrderItemDto> odrsList = new ArrayList(1);
			odrsList.add(reqDto.getItemDto());
			orde.setOrders(odrsList);
			
			orderList.add(orde);
		}else{ //  2.2 从在，则继续判断 day 属性于 入参是否匹配，
			
			OrderDto orderDto = ordMap.get(reqDto.getDay());
			List<OrderItemDto> orders = orderDto.getOrders();
			
			Map<String,OrderItemDto> ordItemMap =  orders.stream()
					.collect(Collectors.toMap(
							OrderItemDto::getValue, eacoh->eacoh,(valueo1,valueo2)->valueo1));
		    //2.2.1 匹配 则继续判断  orders 下的 Value 值与入参是否一致
			if(ordItemMap.containsKey(reqDto.getItemDto().getValue())){
				return "抱歉，你预定晚了";  //2.2.1.1 一致 则 返回’抱歉，你预定晚了’
			}else{
				orders.add(reqDto.getItemDto()); //	2.2.12 不一致 ，将数据插入到 orders 集合下
			}
			
		}
		Criteria idCriteria = Criteria.where("id").is(reqDto.getId());
		Update update = new Update();
		update.set("order", doc.getOrder());
		mongoTemplate.updateFirst(new Query(idCriteria), update, MangoDocDto.class ,DOC_NAME);
		return "操作成功";
	}

	@Override
	public List<OrderItemDto> findByIdAndDay(OrderReqDto reqDto) {
		MangoDocDto doc = this.findById(reqDto.getId());
		if(doc ==null){
			return new ArrayList<OrderItemDto>(1);
		}
		
		List<OrderDto> orderList = doc.getOrder();
		if(orderList ==null ){
			orderList = new ArrayList(1);
		}
		for (OrderDto d : orderList) {
			if(d.getDay().equalsIgnoreCase(reqDto.getDay())){
				return d.getOrders();
			}
		}
		return new ArrayList<OrderItemDto>(1);
	}

}
