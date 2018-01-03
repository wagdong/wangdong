package com.wangdong.lucene;

import com.wangdong.lucene.entity.Article;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;

/**
 * @author 汪冬
 * @Date 2018/1/2
 */
public class lucwneIndex {

	public static String indexPath = "C:\\Users\\SY\\Desktop\\indexDir\\";

	public void makeIndex() {

		try {
			// 模拟一条数据库中的记录
			Article artical = new Article(1, "Lucene全文检索框架", "Lucene现货黄金", "田守枝");
			File file=new File("C:\\Users\\SY\\Desktop\\codeList.txt");
			//获取读取流
			FileReader reader = new FileReader("C:\\Users\\SY\\Desktop\\codeList.txt");
			BufferedReader br = new BufferedReader(reader);
			// 建立索引
			// 1、把Article转换为Doucement对象
			Document doc = new Document();
			//根据实际情况，使用不同的Field来对原始内容建立索引， Store.YES表示是否存储字段原始内容
			doc.add(new LongField("id", artical.getId(), Field.Store.YES));
			doc.add(new TextField("author", artical.getAuthor(), Field.Store.YES));
			doc.add(new TextField("title", artical.getTitle(), Field.Store.YES));
			doc.add(new TextField("content", artical.getContent(), Field.Store.YES));

			// 2、建立索引
			// 指定索引库的位置，本例为项目根目录下indexDir
			Directory directory = FSDirectory.open(new File(indexPath));
			// 分词器，不同的分词器有不同的规则
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
			IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
			indexWriter.addDocument(doc);
			indexWriter.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


/*	@Test
	public void test() {
		try {
			Analyzer analyzer = new StandardAnalyzer();
			String text = "我爱北京天安门";
			TokenStream tokenStream = analyzer.tokenStream("", text);
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
				System.out.println(charTermAttribute);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new lucwneIndex().makeIndex();
	}


	@Test
	public void testChineseAnalyzer() throws Exception {
		//中文分词
		String text = "传智播客：Lucene是全文检索的框架";

*//*        //单字分词StandardAnalyzer、ChineseAnalyzer
        Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_30);//也是单字分词
        Analyzer analyzer2=new ChineseAnalyzer();//也是单字分词

        //相连的两个字组合在一起
        Analyzer analyzer3=new CJKAnalyzer(Version.LUCENE_30);*//*

		//词库分词IKAnalyzer
		Analyzer analyzer = new IKAnalyzer();

		testAnalyzer(analyzer, text);
	}

	*//**
	 * 使用指定的分词器对指定的文本进行分词，并打印结果--不需要掌握
	 *
	 * @param analyzer
	 * @param text
	 * @throws Exception
	 *//*
	public void testAnalyzer(Analyzer analyzer, String text) throws Exception {
		System.out.println("当前使用的分词器：" + analyzer.getClass());

		TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
		tokenStream.addAttribute(CharTermAttribute.class);

		while (tokenStream.incrementToken()) {
			CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
			System.out.println(termAttribute);
		}


	}*/
}
