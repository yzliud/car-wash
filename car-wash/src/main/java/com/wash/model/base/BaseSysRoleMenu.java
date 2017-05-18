package com.wash.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysRoleMenu<M extends BaseSysRoleMenu<M>> extends Model<M> implements IBean {

	public M setRoleId(java.lang.String roleId) {
		set("role_id", roleId);
		return (M)this;
	}

	public java.lang.String getRoleId() {
		return get("role_id");
	}

	public M setMenuId(java.lang.String menuId) {
		set("menu_id", menuId);
		return (M)this;
	}

	public java.lang.String getMenuId() {
		return get("menu_id");
	}

}
