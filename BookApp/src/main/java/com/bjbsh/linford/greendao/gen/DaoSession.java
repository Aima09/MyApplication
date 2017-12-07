package com.bjbsh.linford.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.bjbsh.linford.bookapp.entity.BookEntity;
import com.bjbsh.linford.bookapp.entity.ClassifyEnity;

import com.bjbsh.linford.greendao.gen.BookEntityDao;
import com.bjbsh.linford.greendao.gen.ClassifyEnityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bookEntityDaoConfig;
    private final DaoConfig classifyEnityDaoConfig;

    private final BookEntityDao bookEntityDao;
    private final ClassifyEnityDao classifyEnityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bookEntityDaoConfig = daoConfigMap.get(BookEntityDao.class).clone();
        bookEntityDaoConfig.initIdentityScope(type);

        classifyEnityDaoConfig = daoConfigMap.get(ClassifyEnityDao.class).clone();
        classifyEnityDaoConfig.initIdentityScope(type);

        bookEntityDao = new BookEntityDao(bookEntityDaoConfig, this);
        classifyEnityDao = new ClassifyEnityDao(classifyEnityDaoConfig, this);

        registerDao(BookEntity.class, bookEntityDao);
        registerDao(ClassifyEnity.class, classifyEnityDao);
    }
    
    public void clear() {
        bookEntityDaoConfig.clearIdentityScope();
        classifyEnityDaoConfig.clearIdentityScope();
    }

    public BookEntityDao getBookEntityDao() {
        return bookEntityDao;
    }

    public ClassifyEnityDao getClassifyEnityDao() {
        return classifyEnityDao;
    }

}
