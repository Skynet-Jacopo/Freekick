package com.football.freekick.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuqun on 1/13/2017.
 */

public class NumCheck {
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][34578]\\d{9}";
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。

        if (TextUtils.isEmpty(mobiles)){
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }
    public static boolean isFourNum(String mobiles){
        if (TextUtils.isEmpty(mobiles)){
            return false;
        }else if (mobiles.length()!=4){
            return false;
        }else {
            return true;
        }
    }
    public static boolean isSixNum(String mobiles){
        if (TextUtils.isEmpty(mobiles)){
            return false;
        }else if (mobiles.length()!=6){
            return false;
        }else {
            return true;
        }
    }
    /**
     * 长度在6~18之间，只能包含字符、数字和下划线
     *
     * @param password 密码
     */
    public static boolean verifyPassword(@NonNull String password) {

        int length = password.length();
        if (length < 6 || length > 20) {
            return false;
        }

        String  regex   = "^\\w+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) return false;
        return true;
    }

}
