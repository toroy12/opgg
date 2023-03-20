package gg.op.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gg.op.dto.LeagueEntryDTO;
import gg.op.dto.Summoner;

@org.springframework.stereotype.Controller
public class SearchSummoner {
	public static String API_KEY = "RGAPI-3f390f90-c7eb-4940-a9fa-4de78b070018";

	@RequestMapping("/search")
	public String search(Model model, String summonerName) {
		
		String returnCheck = null;
		
		Summoner temp = summoner(summonerName);
		
		    if(temp != null) {
		    	LeagueEntryDTO temp2 = league(temp.getId());
		    	
		    	model.addAttribute("title", summonerName + " - 게임 전적 - League of Legends");
		    	model.addAttribute("imgURL", "http://ddragon.leagueoflegends.com/cdn/9.16.1/img/profileicon/"+temp.getProfileIconId()+".png");
		    	model.addAttribute("tierImgURL", "img/emblems/emblem-" + temp2.getTier() + ".png");
		    	model.addAttribute("temp", temp);
		    	model.addAttribute("temp2", temp2);
		    	
		    	returnCheck = "user/result";
		    }else {
		    	
		    	model.addAttribute("title", "롤 전적 검색 OP.GG - 전적 검색");
		    	
		    	returnCheck = "user/nonExistentName";
		    }
			
		return returnCheck;
	}
	
	public Summoner summoner(String summonerName) {
		
		BufferedReader br = null;
		Summoner summoner = null;
		
		try{            
			String SummonerName = summonerName.replaceAll(" ", "%20");
			String urlstr = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + SummonerName + "?api_key=" + API_KEY;
			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("GET");
			br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8")); 
			String result = "";
			String line;
			while((line = br.readLine()) != null) {
				result = result + line;
			}	
			JsonParser jsonParser = new JsonParser();
			JsonObject k = (JsonObject) jsonParser.parse(result);
			int profileIconId = k.get("profileIconId").getAsInt();
			String name = k.get("name").getAsString();
			String puuid = k.get("puuid").getAsString();
			long summonerLevel = k.get("summonerLevel").getAsLong();
			long revisionDate = k.get("revisionDate").getAsLong();
			String id = k.get("id").getAsString();
			String accountId = k.get("accountId").getAsString();
			
			summoner = new Summoner(profileIconId, name, puuid, summonerLevel, revisionDate, id, accountId);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return summoner;
	}
		
		
	
	public LeagueEntryDTO league(String id) {
		
		BufferedReader br = null;
		LeagueEntryDTO leagueInfo = null;
		
		try{            
			String urlstr = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/"+ id + "?api_key=" + API_KEY;
			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("GET");
			br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8")); 
			String result = "";
			String line;
			while((line = br.readLine()) != null) { 
				result = result + line;
			}
			JsonParser jsonParser = new JsonParser();
			JsonArray arr = (JsonArray) jsonParser.parse(result);
			JsonObject k =  (JsonObject) arr.get(0);
			int wins = k.get("wins").getAsInt();
			int losses = k.get("losses").getAsInt();
			String rank = k.get("rank").getAsString();
			String tier = k.get("tier").getAsString();
			int leaguePoints = k.get("leaguePoints").getAsInt();
			
			int odds = Math.round(((float) wins / (wins + losses))*100);
			
			leagueInfo = new LeagueEntryDTO(wins, losses, rank,tier, leaguePoints, odds);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return leagueInfo;
	}
}