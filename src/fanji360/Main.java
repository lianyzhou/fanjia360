package fanji360;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;


public class Main {
	
	//当前的考试
	private static Exam currentExam = null;
	//当前的问题
	private static Question currentQuestion = null;
	//上一行是什么类型   number desp options answer
	private static String lastLineType = "";
	
	public static void analysisQuestion(String line) {
		Pattern typePattern = Pattern.compile("^(\\d+)\\.((单|多)选题)");
		Matcher typeMatcher = typePattern.matcher(line);
		
		Pattern optionPattern = Pattern.compile("^[a-z]\\..*$");
		
		//判断是否是 1.单选题
		if(line.equals("the end")) {
			currentExam.getQuestionList().add(currentQuestion);
		} else if(typeMatcher.find()) {
			if(currentQuestion != null) {
				currentExam.getQuestionList().add(currentQuestion);
			}
			currentQuestion = new Question();
			String number = typeMatcher.group(1);
			String type = typeMatcher.group(2);
			currentQuestion.setNumber(number);
			currentQuestion.setType(type);
			lastLineType = "number";
			//判断是否是number的下一行，就是desp，也就是问题
		} else if (lastLineType.equals("number")){
			lastLineType = "desp";
			line = line.replaceAll("[\\s 　\\u3000]{2,100}", "______");
			currentQuestion.setDesp(line);
		} else if(lastLineType.equals("desp") || lastLineType.equals("options")) {
			lastLineType = "options";
			Matcher optionMatcher = optionPattern.matcher(line);
			if(optionMatcher.find()) {
				currentQuestion.getOptions().add(line);	
			} else if(line.startsWith("正确答案")) {
				lastLineType = "answer";
			}
		} else if(lastLineType.equals("answer")) {
			currentQuestion.setAnswer(line);
		}
	}
	
	//一个文档
	public static void ProcessOneDoc(File currentFile) throws Exception {
		List<String> readLines = IOUtils.readLines(new FileInputStream(currentFile), "UTF-8");
		currentExam = new Exam();
		currentQuestion = new Question();
		String title = readLines.remove(0);
		System.out.println();
		currentExam.setTitle(title);
		for (String currentLine : readLines) {
			analysisQuestion(currentLine);
		}
		currentExam.getQuestionList().add(currentQuestion);
		WritePdf();
	}
	//生成pdf
	public static void WritePdf() throws Exception {
		//页面大小
		Rectangle rect = new Rectangle(PageSize.A5);
		//页面背景色
		rect.setBackgroundColor(BaseColor.WHITE);
		//Step 1—Create a Document.
		Document document = new Document();
		//Step 2—Get a PdfWriter instance.
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("output/" + currentExam.getTitle() + ".pdf"));
		document.addTitle(currentExam.getTitle());
		document.addAuthor("fanjia360");
		document.addSubject(currentExam.getTitle());
		document.addKeywords("反假币 银行 考试 济南 从业 资格");
		document.addCreator("周连毅");
		
		
		
		//Step 3—Open the Document.
		document.open();
		//页边空白
		document.setMargins(10, 10, 10, 10);
		
		List<Question> questionList = currentExam.getQuestionList();
		for (Iterator iterator = questionList.iterator(); iterator.hasNext();) {
			Question question = (Question) iterator.next();
			document.newPage();
			Paragraph typeAndNumber = new Paragraph(question.getNumber() + "." + question.getType() , setCommonFont());
			if(question.getNumber() == null) {
				continue;
			}
			document.add(typeAndNumber);
			document.add(new Paragraph(" "));
			Paragraph desp = new Paragraph(question.getDesp() , setCommonFont());
			document.add(desp);
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			List<String> options = question.getOptions();
			for (Iterator iterator2 = options.iterator(); iterator2.hasNext();) {
				String string = (String) iterator2.next();
				Paragraph option = new Paragraph(string , setCommonFont());
				document.add(option);
			}
			
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			
			
			Paragraph answer = new Paragraph("正确答案：" + question.getAnswer() , setAnswerFont());
			document.add(answer);
		}
		
		//Step 5—Close the Document.
		document.close();
		
	}
	
	public static Font setCommonFont() {
        BaseFont bf = null;
        Font fontChinese = null;
        try {
            bf = BaseFont.createFont("微软正黑体.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            fontChinese = new Font(bf, 24, Font.NORMAL);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }
	
	public static Font setAnswerFont() {
        BaseFont bf = null;
        Font fontChinese = null;
        try {
            bf = BaseFont.createFont("微软正黑体.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            fontChinese = new Font(bf, 12, Font.NORMAL);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }
	
	public static void main(String[] args) {
		Collection<File> listFiles = FileUtils.listFiles(new File("input"), null, true);
		for (File file : listFiles) {
			try {
				ProcessOneDoc(file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
