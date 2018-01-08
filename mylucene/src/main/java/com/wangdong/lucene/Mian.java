package com.wangdong.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;


/**
 * 基于文件的持久化索引  内存 数据库索引等
 * @author 汪冬
 * @Date 2018/1/2
 */
public class Mian {

	public static void main(String[] args) {
		try {
			// 搜索条件(不区分大小写)
			String queryString = "表示";
			// 进行搜索得到结果
			Directory directory = FSDirectory.open(new File(lucwneIndex.indexPath));// 索引库目录
			Analyzer analyzer = new StandardAnalyzer();

			// 1、把查询字符串转为查询对象(存储的都是二进制文件，普通的String肯定无法查询，因此需要转换)
			QueryParser queryParser = new QueryParser("content", analyzer);// 只在标题里面查询
			Query query = queryParser.parse(queryString);

			// 2、查询，得到中间结果
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			TopDocs topDocs = indexSearcher.search(query, 100);// 根据指定查询条件查询，只返回前n条结果
			int count = topDocs.totalHits;// 总结果数
			System.out.println(count+"count");
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;// 按照得分进行排序后的前n条结果的信息
			// 3、处理中间结果
			for (ScoreDoc scoreDoc : scoreDocs) {
				float score = scoreDoc.score;// 相关度得分
				int docId = scoreDoc.doc; // Document在数据库的内部编号(是唯一的，由lucene自动生成)
				// 根据编号取出真正的Document数据
				Document doc = indexSearcher.doc(docId);
				System.out.println(docId);
				System.out.println(score);

				System.out.println(doc.getField("content").stringValue());
				Integer.parseInt(doc.getField("id").stringValue());
			}
			indexReader.close();
			// 显示结果
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
