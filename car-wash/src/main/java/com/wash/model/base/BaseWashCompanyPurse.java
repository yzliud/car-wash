package com.wash.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseWashCompanyPurse<M extends BaseWashCompanyPurse<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.String getId() {
		return get("id");
	}

	public M setUid(java.lang.String uid) {
		set("uid", uid);
		return (M)this;
	}

	public java.lang.String getUid() {
		return get("uid");
	}

	public M setTFlowNo(java.lang.String tFlowNo) {
		set("t_flow_no", tFlowNo);
		return (M)this;
	}

	public java.lang.String getTFlowNo() {
		return get("t_flow_no");
	}

	public M setTType(java.lang.String tType) {
		set("t_type", tType);
		return (M)this;
	}

	public java.lang.String getTType() {
		return get("t_type");
	}

	public M setTDatetime(java.util.Date tDatetime) {
		set("t_datetime", tDatetime);
		return (M)this;
	}

	public java.util.Date getTDatetime() {
		return get("t_datetime");
	}

	public M setComment(java.lang.String comment) {
		set("comment", comment);
		return (M)this;
	}

	public java.lang.String getComment() {
		return get("comment");
	}

	public M setIncome(java.math.BigDecimal income) {
		set("income", income);
		return (M)this;
	}

	public java.math.BigDecimal getIncome() {
		return get("income");
	}

	public M setPay(java.math.BigDecimal pay) {
		set("pay", pay);
		return (M)this;
	}

	public java.math.BigDecimal getPay() {
		return get("pay");
	}

	public M setBalance(java.math.BigDecimal balance) {
		set("balance", balance);
		return (M)this;
	}

	public java.math.BigDecimal getBalance() {
		return get("balance");
	}

}
