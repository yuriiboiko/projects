package com.cogent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cogent.model.Answer;

@Repository
public interface AnswersRepository extends JpaRepository<Answer, Integer> {
	
    List<Answer> findByStatus(String status);

    List<Answer> findByQuestionId(int questionId);
    
    @Query(value="SELECT * FROM answer WHERE question_id=?1 AND status=?2",nativeQuery = true)
    List<Answer> findValidAnswersByQuestionId(int question_id, String status);
    
}
