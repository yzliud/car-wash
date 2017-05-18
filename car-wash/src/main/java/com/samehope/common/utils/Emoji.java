package com.samehope.common.utils;


public class Emoji {
	
	public static String filterEmoji(String source) {  
        if(StringUtils.isNotBlank(source)){  
            return source.replaceAll("[^\\u0000-\\uFFFF]", "*");  
        }else{  
            return source;  
        }  
    }  
    public static void main(String[] arg ){  
        try{  
            String text = "This is a smiley \uD83C\uDFA6 face\uD860\uDD5D \uD860\uDE07 \uD860\uDEE2 \uD863\uDCCA \uD863\uDCCD \uD863\uDCD2 \uD867\uDD98 ";  
            System.out.println(text);  
            System.out.println(text.length());  
            System.out.println(filterEmoji(text));  
        }catch (Exception ex){  
            ex.printStackTrace();  
        }  
    } 
}
