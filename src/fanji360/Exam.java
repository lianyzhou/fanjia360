package fanji360;

import java.util.ArrayList;
import java.util.List;

public class Exam {
	
	//考试标题
	private String title;
	//题目列表
	private List<Question> questionList = new ArrayList<Question>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Question> getQuestionList() {
		return questionList;
	}
	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}
	
	
	
}
