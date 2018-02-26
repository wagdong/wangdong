package jdk.sunzn;

/**
 * 算法：当数据量很大适宜采用该方法。
 * 采用二分法查找时，数据需是有序不重复的。
 * 基本思想：假设数据是按升序排序的，对于给定值 x，从序列的中间位置开始比较，
 * 如果当前位置值等于 x，则查找成功；若 x 小于当前位置值，则在数列的前半段中查找；
 * 若 x 大于当前位置值则在数列的后半段中继续查找，直到找到为止。
 * 复杂度 logN
 * @author 汪冬
 * @Date 2018/2/23
 */
public class Dichotomy {

	public static void main(String[] args) {
		int[] arr = new int[] { 12, 23, 34, 45, 56, 67, 77, 89, 90,100 };
		System.out.println(search(arr,45));
		System.out.println(search(arr,67));
		System.out.println(search(arr,10));
		System.out.println(search(arr,100));
	}

	public static int search(int[] arr, int key) {
		if(arr==null||arr.length<1){
			return -1;
		}
		int start=0;
		int end=arr.length-1;
		while (start<=end){
			int middle=(start+end)/2;
			if(key>arr[middle]){
				start=middle+1;
			}else if(key<arr[middle]){
				end=middle-1;
			}else {
				return middle;
			}
		}
		return -1;
	}
}
