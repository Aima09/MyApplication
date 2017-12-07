package com.bjbsh.linford.bookapp.util;

import android.content.Context;

import com.bjbsh.linford.greendao.gen.DaoMaster;
import com.bjbsh.linford.greendao.gen.DaoSession;

/**
 * Created by 艾特不出先生 on 8/6 0006.
 * 创建GreenDao数据库管理类
 */

public class GreenDaoUtil {
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private static GreenDaoUtil greenDaoUtil = null;

    /**
     * 使用单例模式
     */
    private GreenDaoUtil() {

    }

    public static GreenDaoUtil getGreenDaoInstance() {
        if (greenDaoUtil == null) {
            greenDaoUtil = new GreenDaoUtil();
        }
        return greenDaoUtil;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoSession getNewSession(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "book_linford.db");
        daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        return daoSession;
    }
}
