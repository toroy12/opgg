package gg.op.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Search {
	
	 public Search(String searchValue) { 
		 this.searchValue = searchValue; 
		 }

	private String searchValue;
}
