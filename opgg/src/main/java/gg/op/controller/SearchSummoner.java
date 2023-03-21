package gg.op.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gg.op.dto.LeagueEntryDTO;
import gg.op.dto.ParticipantDto;
import gg.op.dto.Summoner;

@org.springframework.stereotype.Controller
public class SearchSummoner {
	public static String API_KEY = "RGAPI-57050e89-3c74-4326-a627-db119f915d31";

	@RequestMapping("/search")
	public String search(Model model, String summonerName) {
		
		String returnCheck = null;
		
		Summoner temp = summoner(summonerName);
		
		    if(temp != null) {
		    	LeagueEntryDTO temp2 = soloQueue(temp.getId());
		    	LeagueEntryDTO temp3 = freeQueue(temp.getId());
		    	List<String> temp4 = matchList(temp.getPuuid());
				/* ParticipantDto temp5 = matchGame(temp4.get(0)); */
		    	
		    	model.addAttribute("title", summonerName + " - 게임 전적 - League of Legends");
		    	model.addAttribute("imgURL", "http://ddragon.leagueoflegends.com/cdn/9.16.1/img/profileicon/"+temp.getProfileIconId()+".png");
		    	model.addAttribute("tierImgURL1", "img/emblems/emblem-" + temp2.getTier() + ".png");
		    	model.addAttribute("tierImgURL2", "img/emblems/emblem-" + temp3.getTier() + ".png");
		    	model.addAttribute("temp", temp);
		    	model.addAttribute("temp2", temp2);
		    	model.addAttribute("temp3", temp3);
		    	model.addAttribute("temp4", temp4);
		    	
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
		
		
	
	public LeagueEntryDTO soloQueue(String id) {
		
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
	
	
	public LeagueEntryDTO freeQueue(String id) {
		
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
			JsonObject k =  (JsonObject) arr.get(1);
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
	
	
	public List<String> matchList(String puuid) {
		
		BufferedReader br = null;
		List<String> matchList = null;
		
		try{            
			String urlstr = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=0&count=20&api_key=" + API_KEY;
			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("GET");
			br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8")); 
			String result = "";
			String line;
			while((line = br.readLine()) != null) { 
				result = result + line;
			}
			String[] arr = new String[20];
			
			int a = 2;
			int b = 15;
			for(int i = 0; i < 20; i++) {
				
				arr[i] = result.substring(a, b);
				a += 16;
				b += 16;
			}
			
			matchList = Arrays.asList(arr);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return matchList;
	}



	/*
	 * public ParticipantDto matchGame(String matchList) {
	 * 
	 * BufferedReader br = null; ParticipantDto gameInfo = null;
	 * 
	 * try{ String urlstr = "https://asia.api.riotgames.com/lol/match/v5/matches/" +
	 * matchList + "?api_key=" + API_KEY; URL url = new URL(urlstr);
	 * HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
	 * urlconnection.setRequestMethod("GET"); br = new BufferedReader(new
	 * InputStreamReader(urlconnection.getInputStream(),"UTF-8")); String result =
	 * ""; String line; while((line = br.readLine()) != null) { result = result +
	 * line; } System.out.println(result);
	 * 
	 * JsonParser jsonParser = new JsonParser(); JsonArray arr = (JsonArray)
	 * jsonParser.parse(result); JsonObject k = (JsonObject) arr.get(1);
	 * 
	 * String championName = k.get("championName").getAsString(); int champLevel =
	 * k.get("participants.champLevel").getAsInt(); int kills =
	 * k.get("kills").getAsInt(); int deaths = k.get("deaths").getAsInt(); int
	 * assists = k.get("assists").getAsInt(); int item0 = k.get("item0").getAsInt();
	 * int item1 = k.get("item1").getAsInt(); int item2 = k.get("item2").getAsInt();
	 * int item3 = k.get("item3").getAsInt(); int item4 = k.get("item4").getAsInt();
	 * int item5 = k.get("item5").getAsInt(); int item6 = k.get("item6").getAsInt();
	 * boolean win = k.get("win").getAsBoolean();
	 * 
	 * gameInfo = new ParticipantDto(championName, champLevel, kills, deaths,
	 * assists, item0, item1, item2, item3, item4, item5, item6, win);
	 * 
	 * }catch(Exception e){ System.out.println(e.getMessage()); }
	 * 
	 * return gameInfo; }
	 */
}