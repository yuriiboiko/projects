package com.cogent.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cogent.model.Question;
import com.cogent.model.QuestionDto;
import com.cogent.repository.QuestionRepository;

@Service
public class QuestionService {

	
	@Autowired
    private QuestionRepository questionRepository;

    public Question addQuestion(QuestionDto question) {
    	Question q=new Question();
    	q.setDatetime(LocalDateTime.now().toString());
    	q.setStatus("false");
    	q.setDescription_question(question.getDescription_question());
    	q.setImage_src(question.getImage_src());
    	q.setTopic(question.getTopic());
    	q.setTitle(question.getTitle());
    	q.setQapproved_by("");
    	q.setQcreated_by(question.getQcreated_by());
        return questionRepository.save(q);
    }

    public Question updateQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteQuestionById(int id) {
        questionRepository.deleteById(id);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getAllQuestionsFalse() {
        return questionRepository.findByStatusFalse();
    }

    public List<Question> getAllQuestionsTrue() {
        return questionRepository.findByStatusTrue();
    }

    public List<Question> getAllQuestionStatus(String status) {
        return questionRepository.findByStatus(status);
    }
    

    public Question getQuestionById(int id) {
        return questionRepository.findById(id).orElse(null);
    }
	
	
    public List<Question> getQuestionsByTopic(String topic) {
        return questionRepository.findAllByTopic(topic);
    }
	
    
    
	
	/*
	public List<Question> getAll(){
		return qr.findAll();
	}
	
	
	public Question createQuestion(QuestionDto question) {
		return qr.save(question);
	}
	
	public Optional<Question> update(Question q) {
		return qr.update(q.getDescription_question(),q.getImage_src(),q.getDatetime(),q.getStatus(),q.getTopic(),q.getTitle(),q.getId());
	}
	
	
	public Optional<Question> deleteById(int id) {
		return qr.deleteById(id);
	}
	
	public List<Optional<Question>> findByStatus(String status) {
		return qr.findByStatus(status);
	}
	
	public List<Optional<Question>> findByTopic(String topic) {
		return qr.findAllByTopic(topic);
	}
	*/
	
}
