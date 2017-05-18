package com.wash.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseWashCarMember<M extends BaseWashCarMember<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.String getId() {
		return get("id");
	}

	public M setOpenId(java.lang.String openId) {
		set("open_id", openId);
		return (M)this;
	}

	public java.lang.String getOpenId() {
		return get("open_id");
	}

	public M setNickName(java.lang.String nickName) {
		set("nick_name", nickName);
		return (M)this;
	}

	public java.lang.String getNickName() {
		return get("nick_name");
	}

	public M setMobile(java.lang.String mobile) {
		set("mobile", mobile);
		return (M)this;
	}

	public java.lang.String getMobile() {
		return get("mobile");
	}

	public M setCardNum(java.lang.String cardNum) {
		set("card_num", cardNum);
		return (M)this;
	}

	public java.lang.String getCardNum() {
		return get("card_num");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public java.lang.String getName() {
		return get("name");
	}

	public M setCarModel(java.lang.String carModel) {
		set("car_model", carModel);
		return (M)this;
	}

	public java.lang.String getCarModel() {
		return get("car_model");
	}

	public M setCarNumber(java.lang.String carNumber) {
		set("car_number", carNumber);
		return (M)this;
	}

	public java.lang.String getCarNumber() {
		return get("car_number");
	}

	public M setSex(java.lang.String sex) {
		set("sex", sex);
		return (M)this;
	}

	public java.lang.String getSex() {
		return get("sex");
	}

	public M setCountry(java.lang.String country) {
		set("country", country);
		return (M)this;
	}

	public java.lang.String getCountry() {
		return get("country");
	}

	public M setProvince(java.lang.String province) {
		set("province", province);
		return (M)this;
	}

	public java.lang.String getProvince() {
		return get("province");
	}

	public M setCity(java.lang.String city) {
		set("city", city);
		return (M)this;
	}

	public java.lang.String getCity() {
		return get("city");
	}

	public M setImg(java.lang.String img) {
		set("img", img);
		return (M)this;
	}

	public java.lang.String getImg() {
		return get("img");
	}

	public M setLastTime(java.util.Date lastTime) {
		set("last_time", lastTime);
		return (M)this;
	}

	public java.util.Date getLastTime() {
		return get("last_time");
	}

	public M setRemarks(java.lang.String remarks) {
		set("remarks", remarks);
		return (M)this;
	}

	public java.lang.String getRemarks() {
		return get("remarks");
	}

	public M setCreateBy(java.lang.String createBy) {
		set("create_by", createBy);
		return (M)this;
	}

	public java.lang.String getCreateBy() {
		return get("create_by");
	}

	public M setCreateDate(java.util.Date createDate) {
		set("create_date", createDate);
		return (M)this;
	}

	public java.util.Date getCreateDate() {
		return get("create_date");
	}

	public M setUpdateBy(java.lang.String updateBy) {
		set("update_by", updateBy);
		return (M)this;
	}

	public java.lang.String getUpdateBy() {
		return get("update_by");
	}

	public M setUpdateDate(java.util.Date updateDate) {
		set("update_date", updateDate);
		return (M)this;
	}

	public java.util.Date getUpdateDate() {
		return get("update_date");
	}

	public M setDelFlag(java.lang.String delFlag) {
		set("del_flag", delFlag);
		return (M)this;
	}

	public java.lang.String getDelFlag() {
		return get("del_flag");
	}

}
