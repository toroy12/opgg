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
	public static String API_KEY = "RGAPI-16d0cfa8-d28e-409f-977b-016ecbcbe478";

	@RequestMapping("/search")
	public String search(Model model, String summonerName) {

		String returnCheck = null;

		Summoner temp = summoner(summonerName);

		// 소환사명이 존재할때
		if (temp != null) {
			LeagueEntryDTO temp2 = soloQueue(temp.getId());
			//솔랭전적이 존재할때
			if(temp2 != null) {
				LeagueEntryDTO temp3 = freeQueue(temp.getId());
				//솔랭전적이 있으면서 자유랭전적이 있을때
				if(temp3 != null) {
					
					//List<String> temp4 = matchList(temp.getPuuid());
					
					model.addAttribute("title", summonerName + " - 게임 전적 - League of Legends");
					model.addAttribute("imgURL", "http://ddragon.leagueoflegends.com/cdn/9.16.1/img/profileicon/"
							+ temp.getProfileIconId() + ".png");
					model.addAttribute("tierImgURL1", "img/emblems/emblem-" + temp2.getTier() + ".png");
					model.addAttribute("tierImgURL2", "img/emblems/emblem-" + temp3.getTier() + ".png");
					model.addAttribute("temp", temp);
					model.addAttribute("temp2", temp2);
					model.addAttribute("temp3", temp3);
					//model.addAttribute("temp4", temp4);
					
					returnCheck = "user/result";
					// 솔랭전적이 있는데 자유랭 전적이 없을때
				} else {
					model.addAttribute("title", summonerName + " - 게임 전적 - League of Legends");
					model.addAttribute("imgURL", "http://ddragon.leagueoflegends.com/cdn/9.16.1/img/profileicon/"
							+ temp.getProfileIconId() + ".png");
					model.addAttribute("tierImgURL1", "img/emblems/emblem-" + temp2.getTier() + ".png");
					model.addAttribute("tierImgURL2", "img/emblems/emblem-unlanked.png");
					model.addAttribute("temp", temp);
					model.addAttribute("temp2", temp2);
					
					returnCheck = "user/result";
				}
				//솔랭전적이 없을때
			} else {
				LeagueEntryDTO temp3 = freeQueue(temp.getId());
				// 솔랭전적이 없는데 자유랭전적이 있을때
				if(temp3 != null) {
					model.addAttribute("title", summonerName + " - 게임 전적 - League of Legends");
					model.addAttribute("imgURL", "http://ddragon.leagueoflegends.com/cdn/9.16.1/img/profileicon/"
							+ temp.getProfileIconId() + ".png");
					model.addAttribute("tierImgURL1", "img/emblems/emblem-unlanked.png");
					model.addAttribute("tierImgURL2", "img/emblems/emblem-" + temp3.getTier() + ".png");
					model.addAttribute("temp", temp);
					model.addAttribute("temp3", temp3);
					
					returnCheck = "user/result";
					// 솔랭적적이 없는데 자유랭전적도 없을때
				} else {
					model.addAttribute("title", summonerName + " - 게임 전적 - League of Legends");
					model.addAttribute("imgURL", "http://ddragon.leagueoflegends.com/cdn/9.16.1/img/profileicon/"
							+ temp.getProfileIconId() + ".png");
					model.addAttribute("tierImgURL1", "img/emblems/emblem-unlanked.png");
					model.addAttribute("tierImgURL2", "img/emblems/emblem-unlanked.png");
					model.addAttribute("temp", temp);
					
					returnCheck = "user/result";
				}
			}
			// 소환사명이 존재하지 않을때
		} else {

			model.addAttribute("title", "롤 전적 검색 OP.GG - 전적 검색");

			returnCheck = "user/nonExistentName";
		}

		return returnCheck;
	}

	public Summoner summoner(String summonerName) {

		BufferedReader br = null;
		Summoner summoner = null;

		try {
			String SummonerName = summonerName.replaceAll(" ", "%20");
			String urlstr = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + SummonerName
					+ "?api_key=" + API_KEY;
			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("GET");
			br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
			String result = "";
			String line;
			while ((line = br.readLine()) != null) {
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

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return summoner;
	}

	public LeagueEntryDTO soloQueue(String id) {

		BufferedReader br = null;
		LeagueEntryDTO leagueInfo = null;

		try {
			String urlstr = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + id + "?api_key="
					+ API_KEY;
			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("GET");
			br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
			String result = "";
			String line;
			while ((line = br.readLine()) != null) {
				result = result + line;
			}
			JsonParser jsonParser = new JsonParser();
			JsonArray arr = (JsonArray) jsonParser.parse(result);
			JsonObject k = (JsonObject) arr.get(0);
			int wins = k.get("wins").getAsInt();
			int losses = k.get("losses").getAsInt();
			String rank = k.get("rank").getAsString();
			String tier = k.get("tier").getAsString();
			int leaguePoints = k.get("leaguePoints").getAsInt();

			int odds = Math.round(((float) wins / (wins + losses)) * 100);

			leagueInfo = new LeagueEntryDTO(wins, losses, rank, tier, leaguePoints, odds);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return leagueInfo;
	}

	public LeagueEntryDTO freeQueue(String id) {

		BufferedReader br = null;
		LeagueEntryDTO leagueInfo = null;

		try {
			String urlstr = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + id + "?api_key="
					+ API_KEY;
			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("GET");
			br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
			String result = "";
			String line;
			while ((line = br.readLine()) != null) {
				result = result + line;
			}
			JsonParser jsonParser = new JsonParser();
			JsonArray arr = (JsonArray) jsonParser.parse(result);
			JsonObject k = (JsonObject) arr.get(1);
			int wins = k.get("wins").getAsInt();
			int losses = k.get("losses").getAsInt();
			String rank = k.get("rank").getAsString();
			String tier = k.get("tier").getAsString();
			int leaguePoints = k.get("leaguePoints").getAsInt();

			int odds = Math.round(((float) wins / (wins + losses)) * 100);

			leagueInfo = new LeagueEntryDTO(wins, losses, rank, tier, leaguePoints, odds);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return leagueInfo;
	}

	/*
	public List<String> matchList(String puuid) {

		BufferedReader br = null;
		List<String> matchList = null;

		try {
			String urlstr = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid
					+ "/ids?start=0&count=20&api_key=" + API_KEY;
			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("GET");
			br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
			String result = "";
			String line;
			while ((line = br.readLine()) != null) {
				result = result + line;
			}
			String[] arr = new String[20];

			int a = 2;
			int b = 15;
			for (int i = 0; i < 20; i++) {

				arr[i] = result.substring(a, b);
				a += 16;
				b += 16;
			}

			matchList = Arrays.asList(arr);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return matchList;
	}
	*/
}