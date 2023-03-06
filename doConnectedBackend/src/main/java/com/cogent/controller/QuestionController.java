package com.cogent.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.model.Question;
import com.cogent.model.QuestionDto;
import com.cogent.service.QuestionService;

@RestController
@CrossOrigin
//@CrossOrigin(origins ="http://localhost:4200")
@RequestMapping("/question")
public class QuestionController {
	
	@Autowired
    private QuestionService questionService;

    // Add a new question
    @PostMapping("/addquestion")
    public ResponseEntity<?> addQuestion(@RequestBody QuestionDto question) {
        Question newQuestion = questionService.addQuestion(question);
        return ResponseEntity.ok(newQuestion);
    }

    // Update an existing question
    @PutMapping("/updatequestion")
    public ResponseEntity<?> updateQuestion(@RequestBody Question question) {
        Question updatedQuestion = questionService.updateQuestion(question);
        return ResponseEntity.ok(updatedQuestion);
    }

    // Delete a question by its ID
    @DeleteMapping("/deleteQuestionById/{id}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable int id) {
    	System.out.println("deleting question with id: " +id);
        questionService.deleteQuestionById(id);
        return ResponseEntity.ok().build();
    }

    // Get all questions
    @GetMapping("/getallquestion")
    public ResponseEntity<?> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    // Get all questions with status = false
    @GetMapping("/getallquestionFalse")
    public ResponseEntity<?> getAllQuestionsFalse() {
        List<Question> questions = questionService.getAllQuestionStatus("false");
        return ResponseEntity.ok(questions);
    }
    
    @GetMapping("/getallquestionTrue")
    public ResponseEntity<?> getAllQuestionsTrue() {
        List<Question> questions = questionService.getAllQuestionStatus("true");
        return ResponseEntity.ok(questions);
    }

    // Get questions by topic
    @GetMapping("/getQuestionbytopic/{topic}")
    public ResponseEntity<?> getQuestionsByTopic(@PathVariable String topic) {
    	System.out.println("Get question by topic executing");
        List<Question> questions = questionService.getQuestionsByTopic(topic);
        return ResponseEntity.ok(questions);
    }
    
    
    // Get a question by its ID
    @GetMapping("/getquestionbyid/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable int id) {
        Question question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }
    

}
