package org.kh.meme.quiz.store;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.kh.meme.quiz.domain.Quiz;
import org.kh.meme.quiz.domain.QuizCh;
import org.mybatis.spring.SqlSessionTemplate;

public interface QuizStore {

	public List<Quiz> selectRandom(SqlSession sqlSession);
	public List<Quiz> selectAll(SqlSession sqlSession, int quizNo);
	public int insertQuiz(SqlSession session, Quiz quiz);
	
	public int insertQuizM(SqlSession session, QuizCh quizCh);
	public int insertQuizFile(SqlSession sqlSession, Quiz quiz);

}
