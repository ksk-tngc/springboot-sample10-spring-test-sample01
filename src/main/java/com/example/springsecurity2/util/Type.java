package com.example.springsecurity2.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 区分・コードを管理するクラス。
 */
public class Type {

    /**
     * 性別
     */
    public static final Map<Integer, String> GENDERS;

    static {
        Map<Integer, String> genders = new HashMap<>();
        genders.put(0, "選択しない");
        genders.put(1, "男性");
        genders.put(2, "女性");
        genders.put(3, "その他");
        // 変更不可なMapを返す（定数）
        GENDERS = Collections.unmodifiableMap(genders);
    }

}
