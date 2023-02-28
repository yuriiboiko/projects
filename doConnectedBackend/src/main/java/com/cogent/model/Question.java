package com.cogent.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	private String description_question;
	private String image_src;
	private String datetime;
	private String status;
	private String topic;
	private String title;
	
	//@OneToMany(mappedBy = "question", fetch = FetchType.EAGER, orphanRemoval = true)
	@OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Answer> answers;
	
	private String qcreated_by;
	private String qapproved_by;
	
	public Question(int id, String description_question, String image_src, String datetime, String status,
			String topic, String title, List<Answer> answers, String qcreated_by,String qapproved_by) {
		super();
		this.id = id;
		this.description_question = description_question;
		this.image_src = image_src;
		this.datetime = datetime;
		this.status = status;
		this.topic = topic;
		this.title = title;
		this.answers = answers;
		this.setQcreated_by(qcreated_by);
		this.setQapproved_by(qapproved_by);
		
	}
	
	public Question() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription_question() {
		return description_question;
	}
	public void setDescription_question(String description_question) {
		this.description_question = description_question;
	}
	public String getImage_src() {
		return image_src;
	}
	public void setImage_src(String image_src) {
		this.image_src = image_src;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	public String getQcreated_by() {
		return qcreated_by;
	}
	public void setQcreated_by(String qcreated_by) {
		this.qcreated_by = qcreated_by;
	}
	public String getQapproved_by() {
		return qapproved_by;
	}
	public void setQapproved_by(String qapproved_by) {
		this.qapproved_by = qapproved_by;
	}

	
}
