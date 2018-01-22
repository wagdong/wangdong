package com.wangdong.lucene;

import com.wangdong.lucene.concurrent.Simulation;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 控制写的并发
 * @author 汪冬
 * @Date 2018/1/2
 */
public class lucwneIndex {

	public static String indexPath = "C:\\Users\\SY\\Desktop\\indexDir\\";
	static  IndexWriter indexWriter=null;//help gc
	public  void  makeIndex() throws IOException {

		try {
			//获取读取流
		/*	FileReader reader = new FileReader("C:\\Users\\SY\\Desktop\\codeList.txt");
			BufferedReader br = new BufferedReader(reader);*/
			// 建立索引
			//Doucement对象
			Document doc = new Document();
			//根据实际情况，使用不同的Field来对原始内容建立索引， Store.YES表示是否存储字段原始内容
			doc.add(new StringField("id", "123456789", Field.Store.YES));
			doc.add(new TextField("content", "Store.YES表示是否存储字段原始内容", Field.Store.YES));
			Term term = new Term("id","123456789");
			indexWriter.deleteDocuments(term);
			indexWriter.updateDocument(term,doc);
			indexWriter.commit();
		} catch (Exception e) {
			e.printStackTrace();
			/*indexWriter.close();*/
		}

	}


	public static void main(String[] args) throws IOException, InterruptedException {
		// 2、建立索引
		// 指定索引库的位置，本例为项目根目录下indexDir
		Directory directory = FSDirectory.open(new File(indexPath));
		// 分词器，不同的分词器有不同的规则
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
		//索引写入 并发问题字全局控制一个写索引  IndexSearcher,它是从索引库中获取数据的，不涉及对索引库中内容的增删改，所以IndexSearcher没有并发问题
		indexWriter = new IndexWriter(directory, indexWriterConfig);

		//多线程并发问题模拟下面程序开启了多个IndexWriter，都没有关闭，执行结果抛出异常：
		List<Runnable> runnableList=new ArrayList<Runnable>();
		for (int i = 0; i < 9; i++) {
			final int NO = i + 1;
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						Simulation.getBegin().await();
						lucwneIndex lucwneIndex = new lucwneIndex();
						lucwneIndex.makeIndex();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			runnableList.add(runnable);
		}
		Simulation.simulationLoad(runnableList);
	}

}
