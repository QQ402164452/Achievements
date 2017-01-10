package utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by Jason on 2017/1/2.
 */

public class PinyinUtil {

    public static String getPinyin(String src){//将汉字全部转换为拼音
        StringBuilder builder=new StringBuilder();
        char[] srcCharArray=src.toCharArray();
        String[] temp;
        HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        try {
            for(int i=0;i<srcCharArray.length;i++){
                if(Character.toString(srcCharArray[i]).matches("[\\u4E00-\\u9FA5]+")){//正则判断是否是汉字字符
                    temp= PinyinHelper.toHanyuPinyinStringArray(srcCharArray[i],format);
                    builder.append(temp[0]);
                }else{
                    builder.append(Character.toString(srcCharArray[i]));
                }
            }
        }catch (BadHanyuPinyinOutputFormatCombination e){
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static char getPinyinHeadChar(String src){//返回中文单词或英文单词的首字母
        char headChar = 0;
        char word=src.charAt(0);
        if(Character.toString(word).matches("[\\u4E00-\\u9FA5]+")){
            String[] pinyinArray=PinyinHelper.toHanyuPinyinStringArray(word);
            if(pinyinArray!=null&&pinyinArray.length>0){
                headChar=pinyinArray[0].charAt(0);
            }
        }else {
            headChar=Character.toLowerCase(word);
        }
        return headChar;
    }

    public static String getASCII(String src){// 将字符串转移为ASCII码
        StringBuilder builder=new StringBuilder();
        byte[] gbk=src.getBytes();
        for (byte b:gbk) {
            builder.append(Integer.toHexString(b&0xff));
        }
        return builder.toString();
    }
}
