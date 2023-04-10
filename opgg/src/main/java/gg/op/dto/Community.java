package gg.op.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Community {
	
	private int boardCode;
	private String title;
	private String content;
	private String author;
	private String createDate;
}
