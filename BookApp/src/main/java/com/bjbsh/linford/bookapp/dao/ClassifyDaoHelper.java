package com.bjbsh.linford.bookapp.dao;

import android.content.Context;

import com.bjbsh.linford.bookapp.entity.ClassifyEnity;
import com.bjbsh.linford.bookapp.util.GreenDaoUtil;
import com.bjbsh.linford.greendao.gen.ClassifyEnityDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 艾特不出先生 on 8/14 0014.
 * 封装Classify的数据操作
 */

public class ClassifyDaoHelper {
    private Context context;
    private ClassifyEnityDao classifyDao;
    //分页没页显示的数量
    private static final int PAGESIZE=5;

    public ClassifyDaoHelper(Context context) {
        this.context = context;
        classifyDao = GreenDaoUtil.getGreenDaoInstance().getNewSession(context).getClassifyEnityDao();
    }

    /**
     * 根据ID查询单个ClassifyEntity
     *
     * @param classifyId
     * @return
     */
    public ClassifyEnity queryClassify(int classifyId) {
        Query query = classifyDao.queryBuilder().where(
                ClassifyEnityDao.Properties.Classify_delFlag.eq(1), ClassifyEnityDao.Properties.Classify_id.eq(classifyId)).build();
        return (ClassifyEnity) query.unique();
    }

    /**
     * 查询全部ClassifyEntity数据
     * @return
     */
    public List<ClassifyEnity> queryAll() {
        return classifyDao.queryBuilder().where(ClassifyEnityDao.Properties.Classify_delFlag.eq(1)).list();
    }

    /**
     * 分页查询数量
     *
     * @param offset   当前页数
     * @return
     */
    public List<ClassifyEnity> queryClassifyByPage(int offset) {
        List<ClassifyEnity> list = new ArrayList<ClassifyEnity>();
        QueryBuilder qb = classifyDao.queryBuilder();
        qb.where(ClassifyEnityDao.Properties.Classify_delFlag.eq(1));
        qb.offset(offset * PAGESIZE);
        qb.limit(PAGESIZE);
        list = qb.list();
//        //GreenDao的sql查询
//        classifyEnityList = classifyDao.queryBuilder().where(ClassifyEnityDao.Properties.Classify_delFlag.eq(1)).list();
        return list;
    }

    /**
     * 插入一条数据
     *
     * @param classifyEnity
     */
    public void insertClassify(ClassifyEnity classifyEnity) {
        classifyDao.insert(classifyEnity);
    }

    /**
     * 删除一个ClassifyEnity,把查询条件的del_flag改为0
     *
     * @param classifyEnity
     */
    public void deleteClassify(ClassifyEnity classifyEnity) {
        classifyDao.update(classifyEnity);
    }

    /**
     * 更新一个ClassifyEnity的内容
     *
     * @param classifyEnity
     */
    public void updateClassify(ClassifyEnity classifyEnity) {
        classifyDao.update(classifyEnity);
    }

    /**
     * 查询表中Classify的数量,del_flag为1的
     * @return
     */
    public Long classifyCount(){

        return classifyDao.queryBuilder().where(ClassifyEnityDao.Properties.Classify_delFlag.eq(1)).count();
    }
}
