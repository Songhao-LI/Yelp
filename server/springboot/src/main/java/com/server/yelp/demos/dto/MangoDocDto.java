package com.server.yelp.demos.dto;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("Order")
public class MangoDocDto {
	@Field
	private String imgs [];
	@Field
	private String address;
	@Field
	private Integer comments;
	@Field
	private Integer star;
	@Field
	private Integer lng;
	@Field
	private String id;
	@Field
	private Date time;
	@Field
	private String title;
	@Field
	private Integer lat;
	@Field
	private String desc ;
	@Field
	private String username;
	@Field
	private List<OrderDto> order;
	public String[] getImgs() {
		return imgs;
	}
	public void setImgs(String[] imgs) {
		this.imgs = imgs;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getComments() {
		return comments;
	}
	public void setComments(Integer comments) {
		this.comments = comments;
	}
	public Integer getStar() {
		return star;
	}
	public void setStar(Integer star) {
		this.star = star;
	}
	public Integer getLng() {
		return lng;
	}
	public void setLng(Integer lng) {
		this.lng = lng;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getLat() {
		return lat;
	}
	public void setLat(Integer lat) {
		this.lat = lat;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<OrderDto> getOrder() {
		return order;
	}
	public void setOrder(List<OrderDto> order) {
		this.order = order;
	}
	
	
	
}
