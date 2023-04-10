package gg.op.controller;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import gg.op.dto.Community;
import gg.op.mapper.BoardMapper;
import gg.op.dto.Search;
import gg.op.dto.Pagination;
import lombok.AllArgsConstructor;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class BoardController {
	
	private final BoardMapper boardMapper;
	
	@GetMapping("/community")
	public String board(Model model,
						@RequestParam(defaultValue="1", required=false) int curPage,
						@RequestParam(value="searchValue", required = false, defaultValue = "") String searchValue) {
		
		int listCnt = boardMapper.boardListCnt(searchValue);
		Pagination pagination = new Pagination(listCnt, curPage);
		Search search = new Search(searchValue);
		List<Community> community = boardMapper.boardList(pagination.getStartIndex(), pagination.getPageSize(), searchValue);
		
		model.addAttribute("title", "커뮤니티");
		model.addAttribute("community", community);
		model.addAttribute("pagination", pagination);
		model.addAttribute("search", search);
		
		return "user/community";
	}
	
	@GetMapping("/addCommunity")
	public String addBoard(Model model) {
		
		model.addAttribute("title", "게시판작성");
		
		return "user/addBoard";
	}
	
	@PostMapping("/addCommunity")
	public String addBoard(@RequestParam(value="title") String title,
							  @RequestParam(value="content") String content) {
		boardMapper.addBoard(title, content);
		
		return "redirect:/community";
	}
	
	@GetMapping("/detailCommunity")
	public String detailBoard(Model model, 
							  @RequestParam(value="boardCode") int boardCode) {
		
		Community detailBoard = boardMapper.detailBoard(boardCode);
		
		model.addAttribute("title", "커뮤니티");
		model.addAttribute("detailBoard", detailBoard);
		
		return "user/detailBoard";
	}
	
	@GetMapping("/removeCommunity")
	public String removeBoard(@RequestParam(value="boardCode") int boardCode) {
		
		boardMapper.removeBoard(boardCode);
		
		return "redirect:/community";
	}
	
	@GetMapping("/modifyCommunity")
	public String modifyBoard(Model model, 
							  @RequestParam(value="boardCode") int boardCode) {
		
		Community modifyboard = boardMapper.detailBoard(boardCode);
		
		model.addAttribute("title", "커뮤니티");
		model.addAttribute("modifyboard", modifyboard);
		
		return "user/modifyBoard";
	}
	
	@PostMapping("/modifyCommunity")
	public String modifyBoard(@RequestParam(value="boardCode") int boardCode,
							  @RequestParam(value="title") String title,
							  @RequestParam(value="content") String content)
							{
		
		boardMapper.modifyBoard(boardCode, title, content);
		
		return "redirect:/detailCommunity?boardCode=" + boardCode;
	}
}
