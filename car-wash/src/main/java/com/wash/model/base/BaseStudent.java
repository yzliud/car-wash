package com.wash.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseStudent<M extends BaseStudent<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.String getId() {
		return get("id");
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

	public M setRemarks(java.lang.String remarks) {
		set("remarks", remarks);
		return (M)this;
	}

	public java.lang.String getRemarks() {
		return get("remarks");
	}

	public M setDelFlag(java.lang.String delFlag) {
		set("del_flag", delFlag);
		return (M)this;
	}

	public java.lang.String getDelFlag() {
		return get("del_flag");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public java.lang.String getName() {
		return get("name");
	}

	public M setCId(java.lang.String cId) {
		set("c_id", cId);
		return (M)this;
	}

	public java.lang.String getCId() {
		return get("c_id");
	}

}
