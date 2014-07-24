package com.jnwan.worldcuponplam.model;

import java.util.ArrayList;

public class SinaItem {
     public ArrayList<LinkInfo> item;
     public String type;
     public SinaItem(){
    	 item = new ArrayList<>();
     }
     public void setType(String type){
    	 this.type = type;
     }
}
