package com.wash.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseWashWorkDeviceRecord<M extends BaseWashWorkDeviceRecord<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.String getId() {
		return get("id");
	}

	public M setWashPersonId(java.lang.String washPersonId) {
		set("wash_person_id", washPersonId);
		return (M)this;
	}

	public java.lang.String getWashPersonId() {
		return get("wash_person_id");
	}

	public M setWashDeviceId(java.lang.String washDeviceId) {
		set("wash_device_id", washDeviceId);
		return (M)this;
	}

	public java.lang.String getWashDeviceId() {
		return get("wash_device_id");
	}

	public M setWashDeviceMac(java.lang.String washDeviceMac) {
		set("wash_device_mac", washDeviceMac);
		return (M)this;
	}

	public java.lang.String getWashDeviceMac() {
		return get("wash_device_mac");
	}

	public M setWorkOnTime(java.util.Date workOnTime) {
		set("work_on_time", workOnTime);
		return (M)this;
	}

	public java.util.Date getWorkOnTime() {
		return get("work_on_time");
	}

	public M setWorkOffTime(java.util.Date workOffTime) {
		set("work_off_time", workOffTime);
		return (M)this;
	}

	public java.util.Date getWorkOffTime() {
		return get("work_off_time");
	}

	public M setFlag(java.lang.String flag) {
		set("flag", flag);
		return (M)this;
	}

	public java.lang.String getFlag() {
		return get("flag");
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
