package org.kh.meme.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.kh.meme.board.domain.Board;
import org.kh.meme.board.domain.BoardFile;
import org.kh.meme.board.domain.Comment;
import org.kh.meme.board.domain.Recommend;
import org.kh.meme.board.service.BoardService;
import org.kh.meme.common.PageInfo;
import org.kh.meme.common.Pagination;
import org.kh.meme.member.domain.Member;
import org.kh.meme.rank.domain.BoardRank;
import org.kh.meme.rank.domain.MemeRank;
import org.kh.meme.rank.domain.QuizRank;
import org.kh.meme.rank.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
public class BoardController {

	@Autowired
	private BoardService bService;
	
	@Autowired
	private RankService rService;
	

	@RequestMapping(value="/boardtest", method=RequestMethod.GET)
	public String boardTestList() {
		return "/board/boardlist";
	}
	
	@RequestMapping(value="/boarderror", method=RequestMethod.GET)
	public String boarderror() {
		return "/board/error";
	}
	

	@RequestMapping(value="/boardwrite", method=RequestMethod.GET)
	public String boardwritetest() {
		return "/board/write";
	}
	
	@RequestMapping(value="/boarddetail", method=RequestMethod.GET)
	public String boarddetailtest() {
		return "/board/boardDetailView";
	}
	
	
	//??????
	@ResponseBody
	@RequestMapping(value="/board/commentAdd", method=RequestMethod.POST)
	public String boardCommentAdd(
			@ModelAttribute Comment comment) {
		System.out.println(comment);
//		String commentWriter = "????????????";
//		comment.setMemberNickname(commentWriter);
		int result = bService.registerComment(comment);
		
		//????????????
		if(result > 0 ) {
			return "success";
		} else {
			return "fail";
		}

	}
	
	
	@ResponseBody
	@RequestMapping(value="/board/commentList", method=RequestMethod.GET
								, produces="application/json;charset=utf-8")
	public String boardCommentList(
			Model model
			, @RequestParam("boardNo") int boardNo) {
		
		List<Comment> commentList = bService.printAllCommentList(boardNo);

		if(!commentList.isEmpty()) {
//			return new Gson().toJson(rList);
			//?????? ??????
//			Gson gson = new Gson();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			return gson.toJson(commentList);
		}
		return null;

	}
	

	@ResponseBody
	@RequestMapping(value="/board/commentModify", method=RequestMethod.POST)
	public String boardReplyModify(
			@ModelAttribute Comment comment) {
		int result = bService.modifyComment(comment);
		if(result > 0 ) {
			return "success";
		} else {
			return "fail";
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/board/commentDelete", method=RequestMethod.GET)
	public String boardCommentRemove(
			@RequestParam("commentNo") int commentNo) {
		int result = bService.removeComment(commentNo);
		if(result > 0) {
			return "success";
		} else {
			return "fail";
		}
	}
	
	
	
	
	//?????????
	@RequestMapping(value="/board/detail_updateView", method=RequestMethod.POST)
	public String boardDetailUpdateView(
			Model model
			, @RequestParam("boardNo") Integer boardNo) {
		
		//????????? ??????
		Board oneBoard = bService.printBoardOneByNo(boardNo);
		BoardFile boardFile = bService.printBoardFileOneByNo(oneBoard.getBoardNo());
		
		//??????
		model.addAttribute("rankmain", "board");
		List<MemeRank> memeRankList = rService.printMemeRank();
		List<BoardRank> boardPushRankList = rService.printBoardPushRank();
		List<BoardRank> boardFreeRankList = rService.printBoardFreeRank();
		List<QuizRank> quizRankList = rService.printQuizRank();
 
		
		if(oneBoard != null && !memeRankList.isEmpty() && !boardPushRankList.isEmpty() && !boardFreeRankList.isEmpty() && !quizRankList.isEmpty()) {
			//?????????
			model.addAttribute("oneBoard", oneBoard);
			model.addAttribute("boardFile", boardFile);
			
			model.addAttribute("memeRankList", memeRankList);
				model.addAttribute("boardPushRankList", boardPushRankList);
				model.addAttribute("boardFreeRankList", boardFreeRankList);
				model.addAttribute("quizRankList", quizRankList);
				return ".tiles/board/update";
			
		} else {
			//?????? error ???????????? ??? ??????, ???????????? ??????
			model.addAttribute("msg", "????????? ?????? ??????");
			return "error";
		}
		
//		return ".tiles/board/update";
	}
	
	
	
	@RequestMapping(value="/board/detail_update", method=RequestMethod.POST)
	public String boardDetailUpdate(
			Model model
			, @RequestParam("boardNo") Integer boardNo
			, @ModelAttribute BoardFile boardFile
			, @ModelAttribute Board board
			, HttpServletRequest request
			, @RequestParam(value="uploadFile", required = false) MultipartFile uploadFile
			) {
	
		//memberId session?????? ????????????
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("loginMember");
		System.out.println(member);
		
		//????????????
		if(!uploadFile.getOriginalFilename().contentEquals("")) {
			String fileRename = saveFile(uploadFile, request);
			if(fileRename != null) {
				boardFile.setBoardFilename(uploadFile.getOriginalFilename());
				boardFile.setBoardFilerename(fileRename);
			}
		}
		
		board.setBoardNo(boardNo);
		board.setMemberNickname(member.getMemberNickname());
		System.out.println(board);
		System.out.println(boardFile);


		int result = bService.updateBoard(board, boardFile);
		System.out.println(result);
		
		//??????
		model.addAttribute("rankmain", "board");
		List<MemeRank> memeRankList = rService.printMemeRank();
		List<BoardRank> boardPushRankList = rService.printBoardPushRank();
		List<BoardRank> boardFreeRankList = rService.printBoardFreeRank();
		List<QuizRank> quizRankList = rService.printQuizRank();
		
		
		if(result > 0 && !memeRankList.isEmpty() && !boardPushRankList.isEmpty() && !boardFreeRankList.isEmpty() && !quizRankList.isEmpty()) {
		
			//??????
			model.addAttribute("memeRankList", memeRankList);
			model.addAttribute("boardPushRankList", boardPushRankList);
			model.addAttribute("boardFreeRankList", boardFreeRankList);
			model.addAttribute("quizRankList", quizRankList);
			return "redirect:/board";
		} else {
			//?????? error ???????????? ??? ??????, ???????????? ??????
			model.addAttribute("msg", "?????? ?????? ??????");
			return "error";
		}
		
//		try {
//			
//		} catch (Exception e) {
//			System.out.println("????????? ?????? ??????");
//			return "error";
//		}
		
	}
	
	
	@RequestMapping(value="/board/detail_delete", method=RequestMethod.POST)
	public String boardDetailDelete(
			Model model
			, @RequestParam("boardNo") Integer boardNo
			, HttpServletRequest request) {
		

		int result = bService.deleteBoard(boardNo);
		System.out.println(result);
		
		//??????
		model.addAttribute("rankmain", "board");
		List<MemeRank> memeRankList = rService.printMemeRank();
		List<BoardRank> boardPushRankList = rService.printBoardPushRank();
		List<BoardRank> boardFreeRankList = rService.printBoardFreeRank();
		List<QuizRank> quizRankList = rService.printQuizRank();
		
		
		if(result > 0 && !memeRankList.isEmpty() && !boardPushRankList.isEmpty() && !boardFreeRankList.isEmpty() && !quizRankList.isEmpty()) {
		
			//??????
			model.addAttribute("memeRankList", memeRankList);
			model.addAttribute("boardPushRankList", boardPushRankList);
			model.addAttribute("boardFreeRankList", boardFreeRankList);
			model.addAttribute("quizRankList", quizRankList);
			return "redirect:/board";
		} else {
			//?????? error ???????????? ??? ??????, ???????????? ??????
			model.addAttribute("msg", "?????? ?????? ??????");
			return "error";
		}
		
//		try {
//			
//		} catch (Exception e) {
//			System.out.println("????????? ?????? ??????");
//			return "error";
//		}
		
	}
	
	@RequestMapping(value="/board/detail_delete_mypage", method=RequestMethod.POST)
	public String boardDetailDeleteMyPage(
			Model model
			, @RequestParam("boardNo") Integer boardNo
			, HttpServletRequest request) {
		

		int result = bService.deleteBoard(boardNo);
		
		return "redirect:/myPage.me";
	}
	
	@RequestMapping(value="/board/detail_delete_admin", method=RequestMethod.POST)
	public String boardDetailDeleteAdmin(
			Model model
			, @RequestParam("boardNo") Integer boardNo
			, HttpServletRequest request) {
		

		int result = bService.deleteBoard(boardNo);
		
		return "redirect:/admin/manageBoard.me";
	}

	@RequestMapping(value="/board/detail_report", method=RequestMethod.POST
			, produces="application/json;charset=utf-8")
	public String boardDetailReport(
			HttpServletRequest request
			, @RequestParam("boardNo") int boardNo) {
		System.out.println(boardNo);
		
		String referer = request.getHeader("Referer");
		
		
		int boardReportData = bService.addBoardReport(boardNo);
		if(boardReportData > 0) {
			//????????? ?????????
			//board_tbl boardLike update
			
			System.out.println("board??? ????????? ????????? ??????!");
			return "redirect:/board/detail?boardNo="+boardNo;
//			return "redirect:"+referer;
		} else {
			System.out.println("board??? ????????? ????????? ?????? ??????!");
			return "redirect:"+referer;
		}

	}
	
	@RequestMapping(value="/board/detail_reportAdminToN", method=RequestMethod.POST
			, produces="application/json;charset=utf-8")
	public String boardDetailReportManager1(
			HttpServletRequest request
			, @RequestParam("boardNo") int boardNo) {
		System.out.println(boardNo);

		String referer = request.getHeader("Referer");
		
		int boardReportManager = bService.boardReportManagerToN(boardNo);
		if(boardReportManager > 0) {
			//????????? ?????????
			//board_tbl boardLike update
			
			System.out.println("board ?????????");
			return "redirect:/admin/manageBoardReported.me";
//				return "redirect:"+referer;
		} else {
			System.out.println("board ????????? ??????");
			return "redirect:"+referer;
		}
		
	}
	
	@RequestMapping(value="/board/detail_reportAdminToY", method=RequestMethod.POST
			, produces="application/json;charset=utf-8")
	public String boardDetailReportManager2(
			HttpServletRequest request
			, @RequestParam("boardNo") int boardNo) {
		System.out.println(boardNo);

		String referer = request.getHeader("Referer");
		
		int boardReportManager = bService.boardReportManagerToY(boardNo);
		if(boardReportManager > 0) {
			//????????? ?????????
			//board_tbl boardLike update
			
			System.out.println("board ?????????");
			return "redirect:/admin/manageBoardReported.me";
//				return "redirect:"+referer;
		} else {
			System.out.println("board ????????? ??????");
			return "redirect:"+referer;
		}
		
	}
	
	@RequestMapping(value="/board/detail_like", method=RequestMethod.POST)
	public String boardDetailLike( HttpServletRequest request 
			, @RequestParam("boardNo") Integer boardNo) {
		
		//?????? ???
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("loginMember");
		System.out.println(member);
		
		Recommend recommend = new Recommend();
		recommend.setBoardNo(boardNo);
		recommend.setRecommendId(member.getMemberId());
		
		String referer = request.getHeader("Referer");
		
		//?????? ???
		//????????? recommend ??????
		int boardLikeData = bService.addBoardLike(recommend);
		
		if(boardLikeData > 0) {
			//????????? ?????????
			//board_tbl boardLike update
			int boardLike = bService.updateBoardLike(recommend);
			System.out.println("board??? ????????? ????????? ??????!");
			return "redirect:/board/detail?boardNo="+boardNo;
//			return "redirect:"+referer;
		} else {
			System.out.println("board??? ????????? ????????? ?????? ??????!");
			return "redirect:"+referer;
		}
		
	}

	@RequestMapping(value="/board/detail", method=RequestMethod.GET)
	public String boardDetail( HttpServletRequest request
			, Model model
			, @RequestParam("boardNo") Integer boardNo) {
		
		//memberId session?????? ????????????
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("loginMember");
		System.out.println(member);
		
		//????????? ??????
		Board oneBoard = bService.printBoardOneByNo(boardNo);
		BoardFile boardFile = bService.printBoardFileOneByNo(boardNo);
		
		//??????
		model.addAttribute("rankmain", "board");
		List<MemeRank> memeRankList = rService.printMemeRank();
		List<BoardRank> boardPushRankList = rService.printBoardPushRank();
		List<BoardRank> boardFreeRankList = rService.printBoardFreeRank();
		List<QuizRank> quizRankList = rService.printQuizRank();
 
		
		if(oneBoard != null && !memeRankList.isEmpty() && !boardPushRankList.isEmpty() && !boardFreeRankList.isEmpty() && !quizRankList.isEmpty()) {
			//?????????
			model.addAttribute("oneBoard", oneBoard);
			model.addAttribute("boardFile", boardFile);
			model.addAttribute("member", member);
			
			//????????? ????????? ++
//			bService.boardCount(oneBoard.getBoardNo());
			bService.boardCount(boardNo);
			
			model.addAttribute("memeRankList", memeRankList);
				model.addAttribute("boardPushRankList", boardPushRankList);
				model.addAttribute("boardFreeRankList", boardFreeRankList);
				model.addAttribute("quizRankList", quizRankList);
				return ".tiles/board/detail";
			
		} else {
			//?????? error ???????????? ??? ??????, ???????????? ??????
			model.addAttribute("msg", "????????? ?????? ??????");
			return "error";
		}
		
		
	}

	//????????? ?????????
	@RequestMapping(value="/board/write", method=RequestMethod.GET)
	public String boardwrite( HttpServletRequest request,
			Model model) {
		

		//memberId session?????? ????????????
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("loginMember");
		System.out.println(member);
				
				
		//??????
		model.addAttribute("rankmain", "board");
		List<MemeRank> memeRankList = rService.printMemeRank();
		List<BoardRank> boardPushRankList = rService.printBoardPushRank();
		List<BoardRank> boardFreeRankList = rService.printBoardFreeRank();
		List<QuizRank> quizRankList = rService.printQuizRank();
 
		
		if(!memeRankList.isEmpty() && !boardPushRankList.isEmpty() && !boardFreeRankList.isEmpty() && !quizRankList.isEmpty()) {
			//??????
			model.addAttribute("memeRankList", memeRankList);
			model.addAttribute("boardPushRankList", boardPushRankList);
			model.addAttribute("boardFreeRankList", boardFreeRankList);
			model.addAttribute("quizRankList", quizRankList);
			return ".tiles/board/write";
		} else {
			//?????? error ???????????? ??? ??????, ???????????? ??????
			model.addAttribute("msg", "?????? ?????? ??????");
			return "error";
		}
		
		
	}
	
	//????????? ??????
	@RequestMapping(value="/board/register", method=RequestMethod.POST)
	public String boardRegister( 
			Model model
			, @ModelAttribute BoardFile boardFile
			, @ModelAttribute Board board
			, HttpServletRequest request
			, @RequestParam(value="uploadFile", required = false) MultipartFile uploadFile
			) {
		
		//memberId session?????? ????????????
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("loginMember");
		System.out.println(member);
		
		try {
			//????????????
			if(!uploadFile.getOriginalFilename().contentEquals("")) {
				String fileRename = saveFile(uploadFile, request);
				if(fileRename != null) {
					boardFile.setBoardFilename(uploadFile.getOriginalFilename());
					boardFile.setBoardFilerename(fileRename);
				}
			}
			
			System.out.println(board);
			System.out.println(boardFile);
			board.setMemberNickname(member.getMemberNickname());
//			int result = bService.registerBoard(board);

			int result = bService.registerNewBoard(board, boardFile);
			System.out.println(result); //?????? ?????? ??? ?????????...
			
			//??????
			model.addAttribute("rankmain", "board");
			List<MemeRank> memeRankList = rService.printMemeRank();
			List<BoardRank> boardPushRankList = rService.printBoardPushRank();
			List<BoardRank> boardFreeRankList = rService.printBoardFreeRank();
			List<QuizRank> quizRankList = rService.printQuizRank();
			
			
			if(result > 0 && !memeRankList.isEmpty() && !boardPushRankList.isEmpty() && !boardFreeRankList.isEmpty() && !quizRankList.isEmpty()) {
			
				//??????
				model.addAttribute("memeRankList", memeRankList);
				model.addAttribute("boardPushRankList", boardPushRankList);
				model.addAttribute("boardFreeRankList", boardFreeRankList);
				model.addAttribute("quizRankList", quizRankList);
				return "redirect:/board";
			} else {
				//?????? error ???????????? ??? ??????, ???????????? ??????
				model.addAttribute("msg", "?????? ?????? ??????");
				return "error";
			}
		} catch (Exception e) {
			System.out.println("????????? ?????? ??????");
			return "error";
		}
			
		
	}
	

	// ??????????????????
	public String saveFile(MultipartFile uploadFile, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savePath = root + "\\boardUploadFiles";
		File folder = new File(savePath);
		if (!folder.exists()) {
			folder.mkdir();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileOriginalname = uploadFile.getOriginalFilename();
		String fileRename = sdf.format(new Date(System.currentTimeMillis())) + "."
				+ fileOriginalname.substring(fileOriginalname.lastIndexOf(".") + 1);
		String filePath = folder + "\\" + fileRename;
		try {
			uploadFile.transferTo(new File(filePath));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileRename;

	}

	
	
	
	
	@RequestMapping(value="/board", method = RequestMethod.GET, produces="application/text;charset=utf-8")
	public String boardranklist(
			Model model
			, @RequestParam(value="page", required=false) Integer page) {
		
		int currentPage = (page != null) ? page : 1;
		
		int totalCount = bService.getListCount();

		PageInfo pi = Pagination.getPageInfo(currentPage, totalCount);
		
		model.addAttribute("pi", pi);
		//???????????? ?????? -> DB?????? ?????? ????????? ?????? ?????????
		
		//?????????
		List<Board> boardAllList = bService.printAllBoard(pi);
		
		
		//??????
		model.addAttribute("rankmain", "board");
		List<MemeRank> memeRankList = rService.printMemeRank();
		List<BoardRank> boardPushRankList = rService.printBoardPushRank();
		List<BoardRank> boardFreeRankList = rService.printBoardFreeRank();
		List<QuizRank> quizRankList = rService.printQuizRank();
 
		
		if(!boardAllList.isEmpty() && !memeRankList.isEmpty() && !boardPushRankList.isEmpty() && !boardFreeRankList.isEmpty() && !quizRankList.isEmpty()) {
			//?????????
			model.addAttribute("boardAllList", boardAllList);
			
			//??????
			model.addAttribute("memeRankList", memeRankList);
			model.addAttribute("boardPushRankList", boardPushRankList);
			model.addAttribute("boardFreeRankList", boardFreeRankList);
			model.addAttribute("quizRankList", quizRankList);
			return ".tiles/board/list";
		} else {
			//?????? error ???????????? ??? ??????, ???????????? ??????
			model.addAttribute("msg", "?????? ?????? ??????");
			return "error";
		}
		
	}
}
