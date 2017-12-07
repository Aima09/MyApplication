package com.example.greendaodemo;

import android.app.Application;
import android.content.Context;

import com.linford.greendao.gen.DaoMaster;
import com.linford.greendao.gen.DaoSession;

/**
 * Created by 艾特不出先生 on 7/31 0031.
 * 快捷打出Sytem.out.println:输入sout然后按tab键
 */

public class GreenDaoManager {
    private DaoMaster daoMaster;
    private DaoSession daoSession;


    public GreenDaoManager(Context context) {

            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "user.db");
            daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
            daoSession = daoMaster.newSession();

    }


    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoSession getNewSession() {
        daoSession = daoMaster.newSession();
        return daoSession;
    }
}
