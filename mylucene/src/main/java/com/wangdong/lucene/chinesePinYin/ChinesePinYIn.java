package com.wangdong.lucene.chinesePinYin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;
import java.util.HashSet;

/**
 * pinyin4j Chinese处理成头部拼音缩写  递归处理多音字组合{@see recursionArrays}
 * @author 汪冬
 * @Date 2018/1/3
 */
public class ChinesePinYIn {

	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 *
	 * @param chines 汉字
	 * @return 拼音
	 */
	private static String[] converterToFirstSpell(String chines) {
		String[][] allPinyins = new String[chines.length()][];//存放整个字符串的各个字符所有可能的拼音情况
		chines = qj2bj(chines);
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					String[] stringArray = PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat);
					if (stringArray != null) {
						for (int j = 0; j < stringArray.length; j++) {
							stringArray[j] = String.valueOf(stringArray[j].charAt(0));
						}
						allPinyins[i] = stringArray;
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				String[] formatPinyin = new String[1];
				formatPinyin[0] = String.valueOf(nameChar[i]);//返回读音,如果多音字自返回一个
				allPinyins[i] = formatPinyin;
			}
		}
		String[] strings = recursionArrays(allPinyins, allPinyins.length, 0);
		HashSet<String> set = new HashSet<String>();
		for (String var : strings) {
			set.add(var);
		}
		return set.toArray(new String[set.size()]);
	}


	/**
	 *
	 * 用递归方法，求出这个二维数组每行取一个数据组合起来的所有情况，返回一个字符串数组
	 *
	 * @param s      求组合数的2维数组
	 * @param len    此二维数组的长度，省去每一次递归长度计算的时间和空间消耗，提高效率
	 * @param cursor 类似JDBC、数据库、迭代器里的游标，指明当前从第几行开始计算求组合数，此处从0开始（数组第一行）
	 *               避免在递归中不断复制剩余数组作为新参数，提高时间和空间的效率
	 * @return String[] 以数组形式返回所有的组合情况
	 */
	public static String[] recursionArrays(String[][] s, int len, int cursor) {
		if (cursor <= len - 2) {//递归条件,直至计算到还剩2行
			int len1 = s[cursor].length;
			int len2 = s[cursor + 1].length;
			int newLen = len1 * len2;//上下2行的总共的组合情况
			String[] temp = new String[newLen];//存上下2行中所有的组合情况
			int index = 0;
			for (int i = 0; i < len1; i++) {//嵌套循环遍历，求出上下2行中，分别取一个数据组合起来的所有情况
				for (int j = 0; j < len2; j++) {
					temp[index] = s[cursor][i] + s[cursor + 1][j];
					index++;
				}
			}
			s[cursor + 1] = temp;//把当前计算到此行的所有组合结果放在cursor+1行
			cursor++;//游标指向下一行，即上边的数据结果
			return recursionArrays(s, len, cursor);
		} else {
			return s[len - 1];//返回最终的所有组合结果
		}
	}

	public static void main(String[] args) {
		String s = "重庆银行长沙";
		String[] s1 = converterToFirstSpell(s);
		System.out.println(Arrays.toString(s1));

	}

	/**
	 * 全角对应于ASCII表的可见字符从！开始，偏移值为65281
	 */
	static final char SBC_CHAR_START = 65281; // 全角！

	/**
	 * 全角对应于ASCII表的可见字符到～结束，偏移值为65374
	 */
	static final char SBC_CHAR_END = 65374; // 全角～

	/**
	 * ASCII表中除空格外的可见字符与对应的全角字符的相对偏移
	 */
	static final int CONVERT_STEP = 65248; // 全角半角转换间隔

	/**
	 * 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理
	 */
	static final char SBC_SPACE = 12288; // 全角空格 12288

	/**
	 * 半角空格的值，在ASCII中为32(Decimal)
	 */
	static final char DBC_SPACE = ' '; // 半角空格

	/**
	 * <PRE>
	 * 全角字符->半角字符转换
	 * 只处理全角的空格，全角！到全角～之间的字符，忽略其他
	 * </PRE>
	 */
	public static String qj2bj(String src) {
		if (src == null) {
			return src;
		}
		StringBuilder buf = new StringBuilder(src.length());
		char[] ca = src.toCharArray();
		for (int i = 0; i < src.length(); i++) {
			if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内
				buf.append((char) (ca[i] - CONVERT_STEP));
			} else if (ca[i] == SBC_SPACE) { // 如果是全角空格
				buf.append(DBC_SPACE);
			} else { // 不处理全角空格，全角！到全角～区间外的字符
				buf.append(ca[i]);
			}
		}
		return buf.toString();
	}

}
