package com.samehope.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtils {
	
    /**金额为分的格式 */    
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";    
    
	public static BigDecimal getByString(String str){
		BigDecimal bd  = new BigDecimal(str);   
		bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);  
		return bd;
	}
	
	 /**  
     * 将分为单位的转换为元 （除100）  
     *   
     * @param amount  
     * @return  
     * @throws Exception   
     */    
    public static String changeF2Y(String amount) throws Exception{    
        if(!amount.matches(CURRENCY_FEN_REGEX)) {    
            throw new Exception("金额格式有误");    
        }    
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();    
    }    
        
    /**   
     * 将元为单位的转换为分 （乘100）  
     *   
     * @param amount  
     * @return  
     */    
    public static String changeY2F(Long amount){    
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).toString();    
    }    

    /**
     * 将元为单位的转换为分 （乘100）  
     * @param amtY
     * @return
     */
	public static String formatAmtY2F(String amtY) {
		if (amtY == null || "".equals(amtY.trim()) || "0".equals(amtY))
			return "0";
		if (amtY.indexOf(",") != -1) {
			amtY = amtY.replace(",", "");
		}
		amtY = new DecimalFormat("0.00").format(new BigDecimal(amtY));
		int index = amtY.indexOf(".");
		int len = amtY.length();
		StringBuffer amtF = new StringBuffer();
		if (index == -1) {
			amtF.append(amtY).append("00");
		} else if ((len - index) == 1) {
			amtF.append(Long.parseLong(amtY.replace(".", ""))).append("00");
		} else if ((len - index) == 2) {
			amtF.append(Long.parseLong(amtY.replace(".", ""))).append("0");
		} else {
			amtF.append(Long.parseLong(amtY.replace(".", "")));
		}
		return amtF.toString();
	}
}
