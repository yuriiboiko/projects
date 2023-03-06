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
import org.springframework.web.bind.annotation.RestController;

import com.cogent.model.Answer;
import com.cogent.service.AnswerService;

@RestController
//@CrossOrigin(origins ="http://localhost:4200")
@CrossOrigin
@RequestMapping("/answer")
public class AnswerController {
	
	@Autowired
    private AnswerService answerService;

    @GetMapping("/getallanswers")
    public List<Answer> getAllAnswers() {
        return answerService.getAllAnswers();
    }

    @GetMapping("/getAllAnswerFalse")
    public List<Answer> getAllFalseAnswers() {
        return answerService.getAllFalseAnswers();
    }
    
    @GetMapping("/getAllAprovedAnswers")
    public List<Answer> getAllAprovedAnswers() {
        return answerService.getAllApprovedAnswers();
    }

    @PostMapping("/addanswer")
    public ResponseEntity<Answer> addAnswer(@RequestBody Answer answer) {
        Answer addedAnswer = answerService.addAnswer(answer);
        return ResponseEntity.ok(addedAnswer);
    }

    @GetMapping("/getanswerbyid/{id}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable int id) {
        Answer answer = answerService.getAnswerById(id);
        if (answer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(answer);
    }

    @PutMapping("/updateanswer")
    public ResponseEntity<Answer> updateAnswer( @RequestBody Answer answer) {
        Answer updatedAnswer = answerService.updateAnswer(answer);
        if (updatedAnswer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAnswer);
    }

    @DeleteMapping("/deleteAnswerById/{id}")
    public ResponseEntity<Void> deleteAnswerById(@PathVariable int id) {
        boolean deleted = answerService.deleteAnswerById(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAnswersbyQuestionID/{questionId}")
    public List<Answer> getAnswersByQuestionId(@PathVariable int questionId) {
        return answerService.getAnswersByQuestionId(questionId);
    }
    
    @GetMapping("/getValidAnswerById/{questionId}")
    public List<Answer> getValidAnswersByQuestionId(@PathVariable int questionId) {
        return answerService.getValidAnswersByQuestionId(questionId);
    }
    
    
    @DeleteMapping("/deleteAnswerByQuestionId/{id}")
    public ResponseEntity<?> deleteAnswerByQuestionId(@PathVariable int id) {
    	int deleted=answerService.deleteByQuestionId(id);
    	System.out.println("deleted "+ deleted+" rows using this question id:" +id);
    	  if (deleted<0) {
              return ResponseEntity.notFound().build();
          }
          return ResponseEntity.noContent().build();
    }
   
    
}
