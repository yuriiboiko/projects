package com.cogent.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cogent.model.Answer;
import com.cogent.model.Question;
import com.cogent.repository.AnswersRepository;
import com.cogent.repository.QuestionRepository;

@Service
public class AnswerService {


    @Autowired
    private AnswersRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public List<Answer> getAllFalseAnswers() {
        return answerRepository.findByStatus("false");
    }
    
    public List<Answer> getAllApprovedAnswers() {
        return answerRepository.findByStatus("true");
    }

    public Answer addAnswer(Answer answer) {
        // Check if the associated question exists
        int questionId = answer.getQuestion().getId();
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (!optionalQuestion.isPresent()) {
            throw new IllegalArgumentException("Question with id " + questionId + " does not exist");
        }

        // Set the answer's created datetime to the current time
        answer.setDatetime(LocalDateTime.now().toString());
        answer.setStatus("false");
        return answerRepository.save(answer);
    }

    public Answer getAnswerById(int id) {
        Optional<Answer> optionalAnswer = answerRepository.findById(id);
        return optionalAnswer.orElse(null);
    }

    public Answer updateAnswer(Answer newAnswer) {
        Optional<Answer> optionalAnswer = answerRepository.findById(newAnswer.getId());
        if (!optionalAnswer.isPresent()) {
            return null;
        }

        Answer oldAnswer = optionalAnswer.get();
        oldAnswer.setDescription_answer(newAnswer.getDescription_answer());
        oldAnswer.setImg_src(newAnswer.getImg_src());
        oldAnswer.setStatus(newAnswer.getStatus());
        oldAnswer.setApproved_by(newAnswer.getApproved_by());

        return answerRepository.save(oldAnswer);
    }

    public boolean deleteAnswerById(int id) {
        Optional<Answer> optionalAnswer = answerRepository.findById(id);
        if (!optionalAnswer.isPresent()) {
            return false;
        }

        answerRepository.delete(optionalAnswer.get());
        return true;
    }

    public List<Answer> getAnswersByQuestionId(int questionId) {
        return answerRepository.findByQuestionId(questionId);
    }
    
    
    public List<Answer> getValidAnswersByQuestionId(int question_id) {
        return answerRepository.findValidAnswersByQuestionId(question_id,"true");
    }

	public int deleteByQuestionId(int id) {
		List<Answer> answersToDelete=getAnswersByQuestionId(id);
		int size=answersToDelete.size();
		if(size>0) {
			answerRepository.deleteAll(answersToDelete);

		}
		return size;
		
	}
}
