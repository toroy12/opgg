package gg.op.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LeagueEntryDTO {
	
	
		public LeagueEntryDTO(int wins, int losses, String rank, String tier, int leaguePoints, int odds) {
			this.wins = wins;
			this.losses = losses;
			this.rank = rank;
			this.tier = tier;
			this.leaguePoints = leaguePoints;
			this.odds = odds;
	}
		private String leagueId;
		private String summonerId;
		private String summonerName;
		private String queueType;
		private String tier;
		private String rank;
		private int	leaguePoints;
		private int wins;
		private int	losses;
		private int odds;
		private boolean	hotStreak;
		private boolean	veteran;
		private boolean	freshBlood;
		private boolean inactive;
}
