package com.server.yelp.demos.service;

import java.util.List;

import com.server.yelp.demos.dto.MangoDocDto;
import com.server.yelp.demos.dto.OrderItemDto;
import com.server.yelp.demos.dto.OrderReqDto;

public interface OrderService {

	public MangoDocDto findById(String id);
	
	public String update(OrderReqDto reqDto);
	
	public List<OrderItemDto> findByIdAndDay(OrderReqDto reqDto);
}
