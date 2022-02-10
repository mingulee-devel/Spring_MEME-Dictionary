package org.kh.meme.meme.service.logic;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.kh.meme.meme.domain.Meme;
import org.kh.meme.meme.domain.MemeFile;
import org.kh.meme.meme.domain.MemeRequest;
import org.kh.meme.meme.domain.PageInfo;
import org.kh.meme.meme.service.MemeService;
import org.kh.meme.meme.store.MemeStore;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemeServiceImpl implements MemeService{

	@Autowired
	private MemeStore mStore;
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public int getListCount() {
		int totalCount = mStore.selectListCount(sqlSession);
		return totalCount;
	}
	
	//게시물을 가져오기
	@Override
	public List<Meme> printAll(PageInfo pi) {
		List<Meme> mList = mStore.selectAll(sqlSession, pi);
		return mList;
	}
	
	@Override
	public int registerMeme(Meme meme, MemeFile memeFile) {
		int result = mStore.insertMeme(sqlSession, meme);
		if(result > 0) {
			mStore.insertMemeFile(sqlSession, memeFile);
		}
		return result;
	}


	@Override
	public Meme printOneByMeme(String memeName) {
		Meme meme = mStore.selectOneByMeme(sqlSession, memeName);
		return meme;
	}

	//조회수
	@Override
	public int memeCountUpdate(int memeNo) {
		int result = mStore.updateCount(sqlSession, memeNo);
		return result;
	}

	//사전 수정삭제 요청
	@Override
	public int modifyMeme(MemeRequest memeRequest) {
		int result = mStore.insertMemeRequest(sqlSession, memeRequest);
		return result;
	}




}
