package com.wash.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseTestInterface<M extends BaseTestInterface<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.String getId() {
		return get("id");
	}

	public M setType(java.lang.String type) {
		set("type", type);
		return (M)this;
	}

	public java.lang.String getType() {
		return get("type");
	}

	public M setUrl(java.lang.String url) {
		set("url", url);
		return (M)this;
	}

	public java.lang.String getUrl() {
		return get("url");
	}

	public M setBody(java.lang.String body) {
		set("body", body);
		return (M)this;
	}

	public java.lang.String getBody() {
		return get("body");
	}

	public M setSuccessmsg(java.lang.String successmsg) {
		set("successmsg", successmsg);
		return (M)this;
	}

	public java.lang.String getSuccessmsg() {
		return get("successmsg");
	}

	public M setErrormsg(java.lang.String errormsg) {
		set("errormsg", errormsg);
		return (M)this;
	}

	public java.lang.String getErrormsg() {
		return get("errormsg");
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

}
