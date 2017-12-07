package com.bjbsh.linford.bookapp.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by 艾特不出先生 on 8/6 0006.
 */
@Entity
public class BookEntity {
    @Id(autoincrement = true)
    private Long book_id;//书籍Id(自动生成)
    @NotNull
    private String book_imgUrl;//书籍图片地址
    @NotNull
    private String book_name;//书籍名称
    @NotNull
    private String book_type;//书籍类型
    @NotNull
    private Integer book_delFlag=0;//是否删除
    @NotNull
    private String book_classifyName;//分类名
    //自定义classify_id,对应Classify实体类id
    //private Long classifyId;
    public String getBook_classifyName() {
        return this.book_classifyName;
    }
    public void setBook_classifyName(String book_classifyName) {
        this.book_classifyName = book_classifyName;
    }
    public Integer getBook_delFlag() {
        return this.book_delFlag;
    }
    public void setBook_delFlag(Integer book_delFlag) {
        this.book_delFlag = book_delFlag;
    }
    public String getBook_type() {
        return this.book_type;
    }
    public void setBook_type(String book_type) {
        this.book_type = book_type;
    }
    public String getBook_name() {
        return this.book_name;
    }
    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }
    public String getBook_imgUrl() {
        return this.book_imgUrl;
    }
    public void setBook_imgUrl(String book_imgUrl) {
        this.book_imgUrl = book_imgUrl;
    }
    public Long getBook_id() {
        return this.book_id;
    }
    public void setBook_id(Long book_id) {
        this.book_id = book_id;
    }
    @Generated(hash = 1300962377)
    public BookEntity(Long book_id, @NotNull String book_imgUrl,
            @NotNull String book_name, @NotNull String book_type,
            @NotNull Integer book_delFlag, @NotNull String book_classifyName) {
        this.book_id = book_id;
        this.book_imgUrl = book_imgUrl;
        this.book_name = book_name;
        this.book_type = book_type;
        this.book_delFlag = book_delFlag;
        this.book_classifyName = book_classifyName;
    }
    @Generated(hash = 1373651409)
    public BookEntity() {
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "book_id=" + book_id +
                ", book_imgUrl='" + book_imgUrl + '\'' +
                ", book_name='" + book_name + '\'' +
                ", book_type='" + book_type + '\'' +
                ", book_delFlag=" + book_delFlag +
                ", book_classifyName='" + book_classifyName + '\'' +
                '}';
    }
}
