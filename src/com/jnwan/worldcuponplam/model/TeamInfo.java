package com.jnwan.worldcuponplam.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamInfo{

    public String briefIntr;

    public String country;

    public String group;

    public Integer logo;

    public Map<String, ArrayList<PlayerDetail>> players;

    public TeamInfo(String country, Integer logo, String group) {

        this.players = new HashMap<String, ArrayList<PlayerDetail>>();

        this.country = country;

        this.logo = logo;

        this.group = group;

    }
  
  public String toString(){
	  switch (country) {
	     case "Brazil":
          return "巴西";
	     case "Croatia":
	          return "克罗地亚";
	     case "Mexico":
	          return "墨西哥";
	     case "Cameroon":
	          return "喀麦隆";
	     case "Spain":
	          return "西班牙";
	     case "Netherlands":
	          return "荷兰";
	     case "Chile":
	          return "智利";
	     case "Australia":
	          return "澳大利亚";
	     case "Columbia":
	          return "哥伦比亚";
	     case "Greece":
	          return "希腊";
	     case "Ivory Coast":
	          return "科特迪瓦";
	     case "Japan":
	          return "日本";
	     case "Uruguay":
	          return "乌拉圭";
	     case "Costa Rica":
	          return "哥斯达黎加";
	     case "England":
	          return "英格兰";
	     case "Italy":
	          return "意大利";
	     case "Switzerland":
	          return "瑞士";
	     case "Ecuador":
	          return "厄瓜多尔";
	     case "France":
	          return "法国";
	     case "Honduras":
	          return "洪都拉斯";
	     case "Argentina":
	          return "阿根廷";
	     case "Bosnia":
	          return "波黑";
	     case "Iran":
	          return "伊朗";
	     case "Nigeria":
	          return "尼日利亚";
	     case "Portugal":
	          return "葡萄牙";
	     case "Ghana":
	          return "加纳";
	     case "USA":
	          return "美国";
	     case "Belgium":
	          return "比利时";
	     case "Algeria":
	          return "阿尔及利亚";
	     case "Russia":
	          return "俄罗斯";
	     case "Germany":
	          return "德国";
	     case "Korea":
	          return "韩国";
	default:
		 return null;
	}
  }
}