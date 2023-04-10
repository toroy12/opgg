package gg.op.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import gg.op.dto.Community;

@Mapper
public interface BoardMapper {

	public List<Community> boardList(int startIndex, int pageSize, String searchValue);
	
	public int addBoard(String title, String content);
	
	public Community detailBoard(int boardCode);
	
	public int removeBoard(int boardCode);
	
	public int modifyBoard(int boardCode, String title, String content);
	
	public int boardListCnt(String searchValue);
}
