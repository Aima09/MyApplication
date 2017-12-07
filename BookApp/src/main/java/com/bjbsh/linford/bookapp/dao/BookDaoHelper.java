package com.bjbsh.linford.bookapp.dao;

import android.content.Context;

import com.bjbsh.linford.bookapp.entity.BookEntity;
import com.bjbsh.linford.bookapp.util.GreenDaoUtil;
import com.bjbsh.linford.greendao.gen.BookEntityDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 艾特不出先生 on 8/14 0014.
 * 添加书籍BookEntity数据库管理类
 */

public class BookDaoHelper {
    //传个Activity的上下文过来
    private Context context;
    //定义BookEntityDao
    private BookEntityDao bookDao;

    /**
     * 构造方法
     *
     * @param context
     */
    public BookDaoHelper(Context context) {
        this.context = context;
        bookDao = GreenDaoUtil.getGreenDaoInstance().getNewSession(context).getBookEntityDao();
    }

    /**
     * 根据ID查询单个BookEntity
     *
     * @param BookId
     * @return
     */
    public BookEntity queryBookById(int BookId) {
        Query query = bookDao.queryBuilder().where(
                BookEntityDao.Properties.Book_delFlag.eq(1), BookEntityDao.Properties.Book_id.eq(BookId)).build();
        return (BookEntity) query.unique();
    }

    /**
     * 根据书名查询书籍信息
     *
     * @param bookName
     * @return
     */
    public BookEntity queryBookByName(String bookName) {
        Query query = bookDao.queryBuilder().where(BookEntityDao.Properties.Book_delFlag.eq(1), BookEntityDao.Properties.Book_name.eq(bookName)).build();
        return (BookEntity) query.unique();
    }

    /**
     * 查询全部BookEntity数据
     *
     * @return
     */
    public List<BookEntity> queryBookAll() {
        return bookDao.queryBuilder().where(BookEntityDao.Properties.Book_delFlag.eq(1)).list();
    }

    /**
     * 不加过滤的查询全部书籍
     */
    public List<BookEntity> loadAllBook() {

        return bookDao.loadAll();
    }

    /**
     * 分页查询数量
     *
     * @param offset   当前页数
     * @param pageSize 每页显示的数量
     * @return
     */
    public List<BookEntity> queryBookByPage(int offset, int pageSize) {
        List<BookEntity> list = new ArrayList<BookEntity>();
        QueryBuilder qb = bookDao.queryBuilder();
        qb.where(BookEntityDao.Properties.Book_delFlag.eq(1));
        qb.offset(offset * pageSize);
        qb.limit(pageSize);
        list = qb.list();
//        //GreenDao的sql查询
//        BookEntityList = bookDao.queryBuilder().where(BookEntityDao.Properties.Book_delFlag.eq(1)).list();
        return list;
    }

    /**
     * 插入一条数据
     *
     * @param BookEntity
     */
    public void insertBook(BookEntity BookEntity) {
        bookDao.insert(BookEntity);
    }

    /**
     * 删除一个BookEntity,把查询条件的del_flag改为0
     *
     * @param BookEntity
     */
    public void deleteBook(BookEntity BookEntity) {
        bookDao.update(BookEntity);
    }

    /**
     * 更新一个BookEntity的内容
     *
     * @param BookEntity
     */
    public void updateBook(BookEntity BookEntity) {
        bookDao.update(BookEntity);
    }

    /**
     * 查询表中Book的数量,del_flag为1的
     *
     * @return
     */
    public Long BookCount() {

        return bookDao.queryBuilder().where(BookEntityDao.Properties.Book_delFlag.eq(1)).count();
    }

    /**
     * 删除所有数据
     */
    public void deleteBookAll() {
        bookDao.deleteAll();
    }
}
