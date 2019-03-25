package demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author chao
 * @Date 2019/3/18 - 10:41
 */
public class AppMain {
    //定义字典
    static List<String> doc = null;


    public static void main(String[] args) {
        doc = new ArrayList<>();
        // 向字典中添加关键词
        doc.add("java");
        doc.add("编程");
        doc.add("思想");
        doc.add("是");
        doc.add("北京天安门");

        // 定义检索的字符串
        String str = "java是编程思想";
        List<String> result = segment(str);
        System.out.println("result = " + result);
    }

    private static List<String> segment(String str) {
        // 定义检索后的的候选词
        List<String> result = new ArrayList<>();

        while (str.length() >= 1) {

            int maxLength = 5;  // 检索关键词的最大长度为5
            if (str.length() < maxLength) {
                maxLength = str.length();
            }
            // 取出候选词和字典比较
            String tryWord = str.substring(0, maxLength);
            // 判断字典是否包含候选词
            while (! doc.contains(tryWord)) { //不在字典中
                // 判断是否是单字
                if (tryWord.length() == 1) {
                    break;  // 单字返回单字
                }
                // 不是单字，去掉右边的一个字符，继续比较
                tryWord = tryWord.substring(0, tryWord.length() - 1);
            }
            // 候选词在字典中
            // 将候选词添加到结果中
            result.add(tryWord);
            // 得到关键词后,去掉文本前面的关键词，继续找一下关键词
            str = str.substring(tryWord.length());
        }
        return result;
    }
}
