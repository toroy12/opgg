package gg.op.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ParticipantDto {
	
	public ParticipantDto(String championName, int champLevel, int kills, int deaths, int assists, int item0, int item1, int item2, int item3, int item4, int item5, int item6, boolean win) {
		this.championName = championName;
		this.champLevel = champLevel;
		this.kills = kills;
		this.deaths = deaths;
		this.assists = assists;
		this.item0 = item0;
		this.item1 = item1;
		this.item2 = item2;
		this.item3 = item3;
		this.item4 = item4;
		this.item5 = item5;
		this.item6 = item6;
		this.win = win;
	}
	
	//내가 한 챔피언 이름(초상화사용)
	private String championName;
	
	//내가 한 게임에서의 마지막 챔피언 레벨
	private int champLevel;
	
	//킬 데스 어시
	private int kills;
	private int deaths;
	private int assists;
	
	//아이템
	private	int	item0;
	private	int	item1;
	private int	item2;
	private	int	item3;
	private	int	item4;
	private	int	item5;
	private int item6;
	
	//승패
	private boolean win;

}
