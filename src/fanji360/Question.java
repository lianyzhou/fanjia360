package fanji360;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class Question {
	
	//单选题/多选题
	private String type;
	//问题描述
	private String desp;
	//第几道题
	private String number;
	//选项
	private List<String> options = new ArrayList<String>();
	//答案
	private String answer;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesp() {
		return desp;
	}
	public void setDesp(String desp) {
		this.desp = desp;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String toString() {
		return number + "." + type + "\n" + desp + "\n" +  StringUtils.join(options, "\n") + "\n正确答案：" + answer;
	}
	
}
