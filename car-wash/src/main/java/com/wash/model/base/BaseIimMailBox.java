package com.wash.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseIimMailBox<M extends BaseIimMailBox<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.String getId() {
		return get("id");
	}

	public M setReadstatus(java.lang.String readstatus) {
		set("readstatus", readstatus);
		return (M)this;
	}

	public java.lang.String getReadstatus() {
		return get("readstatus");
	}

	public M setSenderId(java.lang.String senderId) {
		set("senderId", senderId);
		return (M)this;
	}

	public java.lang.String getSenderId() {
		return get("senderId");
	}

	public M setReceiverId(java.lang.String receiverId) {
		set("receiverId", receiverId);
		return (M)this;
	}

	public java.lang.String getReceiverId() {
		return get("receiverId");
	}

	public M setSendtime(java.util.Date sendtime) {
		set("sendtime", sendtime);
		return (M)this;
	}

	public java.util.Date getSendtime() {
		return get("sendtime");
	}

	public M setMailid(java.lang.String mailid) {
		set("mailid", mailid);
		return (M)this;
	}

	public java.lang.String getMailid() {
		return get("mailid");
	}

}
