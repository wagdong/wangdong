package jdk.LRU;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 操作一波 java 链表
 * @author 汪冬
 * @Date 2018/1/22
 */
public class CacheNode {

	private ReentrantLock lock=new ReentrantLock();
	transient int size = 0;
	transient Node first;
	transient Node last;

	public CacheNode() {

	}

	public void putCacheNode(Object o){
		lock.lock();
		try {
			if(o==null){
				throw new RuntimeException("object is null");
			}
			Node node = new Node(o);
			if(first==null){
				node.prev=node;
				node.next=node;
				first=node;
				if(last==null){
					last=node;
				}
			}else {
				node.next=first;
				node.prev=null;
			}
			size++;
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	public Node getCacheNode(){
		return null;
	}

	class  Node{
		Node prev;//前一节点
		Node next;//后一节点
		Object value;//值
		public Node(Object o) {
          this.value=o;
		}
	}
}
