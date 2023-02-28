package com.cogent.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cogent.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
/*
	@Query("UPDATE question SET description_question = ?1, image_src = ?2, datetime = ?3, status = ?4, topic = ?5 title = ?6 WHERE id = ?7;")
	Optional<Question> update(String description_question, String image_src, String datetime, String status, String topic, String title, int id);
	
	@Query("DELETE FROM question WHERE id = ?1")
	Optional<Question> deleteById(int id);
	
	@Query("SELECT * FROM question WHERE id = ?1")
	Optional<Question> findById(int id);
	
	@Query("SELECT * FROM question WHERE status = ?1")
	List<Optional<Question>> findByStatus(String status);
	
	@Query("SELECT * FROM question WHERE topic = ?1")
	List<Optional<Question>> findAllByTopic(String topic);

	Question save(QuestionDto question);
	*/
	
    List<Question> findByStatusFalse();
    
    List<Question> findByStatusTrue();
    
    List<Question> findByStatus(String status);

    List<Question> findAllByTopic(String topic);
    
	
    //@Query("SELECT q FROM question q WHERE q.topic = ?1")
	//List<Question> findAllByTopic(String topic);

}
