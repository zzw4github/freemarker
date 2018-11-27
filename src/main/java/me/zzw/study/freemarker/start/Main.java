package me.zzw.study.freemarker.start;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Main {

	public static void main(String[] args) {
		try {
			Configuration config = MyConfiguration.config( "D:\\sts-workspace\\freemarker\\src\\main\\resources\\" );
			Template temp = config.getTemplate( "test.ftlh" ); //  1
			@SuppressWarnings("unchecked")
			Map<String, Object> root = MyDataModel.getData();  //  2
			Writer out = new OutputStreamWriter(System.out);
			temp.process(root, out);                           //  3
		}
		catch (IOException e) {
			System.out.println( "directory is not exists" );
		}
		catch (TemplateException e) {
			System.err.println( " deal template error" );
		}
		

	}

}
