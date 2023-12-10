package com.server.yelp.demos.dto;

public class OrderReqDto {
	
	private String id;
	
	private String day;
	
	private OrderItemDto itemDto;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public OrderItemDto getItemDto() {
		return itemDto;
	}

	public void setItemDto(OrderItemDto itemDto) {
		this.itemDto = itemDto;
	}
	
	
}
