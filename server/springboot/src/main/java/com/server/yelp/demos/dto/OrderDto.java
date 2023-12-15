package com.server.yelp.demos.dto;

import java.util.List;

public class OrderDto {

	private String day;
	
	private List<OrderItemDto> orders;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public List<OrderItemDto> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderItemDto> orders) {
		this.orders = orders;
	}
	
	
	
	
}
