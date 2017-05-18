package com.wash.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseOaNotifyRecord<M extends BaseOaNotifyRecord<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.String getId() {
		return get("id");
	}

	public M setOaNotifyId(java.lang.String oaNotifyId) {
		set("oa_notify_id", oaNotifyId);
		return (M)this;
	}

	public java.lang.String getOaNotifyId() {
		return get("oa_notify_id");
	}

	public M setUserId(java.lang.String userId) {
		set("user_id", userId);
		return (M)this;
	}

	public java.lang.String getUserId() {
		return get("user_id");
	}

	public M setReadFlag(java.lang.String readFlag) {
		set("read_flag", readFlag);
		return (M)this;
	}

	public java.lang.String getReadFlag() {
		return get("read_flag");
	}

	public M setReadDate(java.util.Date readDate) {
		set("read_date", readDate);
		return (M)this;
	}

	public java.util.Date getReadDate() {
		return get("read_date");
	}

}
