package gg.op.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Summoner {
	
	public Summoner(int profileIconId, String name, String puuid, long summonerLevel, long revisionDate,
			String id, String accountId) {
		this.profileIconId = profileIconId;
		this.name = name;
		this.puuid = puuid;
		this.summonerLevel = summonerLevel;
		this.revisionDate = revisionDate;
		this.id = id;
		this.accountId = accountId;
	}
	
	private String id;
	private String accountId;
	private String puuid;
	private String name;
	private int profileIconId;
	private long revisionDate;
	private long summonerLevel;

}
