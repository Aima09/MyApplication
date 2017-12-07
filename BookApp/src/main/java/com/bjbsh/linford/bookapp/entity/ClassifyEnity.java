package com.bjbsh.linford.bookapp.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 艾特不出先生 on 8/6 0006.
 */
@Entity
public class ClassifyEnity {
    @Id(autoincrement = true)
    private Long classify_id;
    private String classifyName;
    private Integer sort;
    private Integer classify_delFlag;
    //  @ToMany(referencedJoinProperty ="classifyId")
    // private List<BookEntity> bookEntityList;
    public Integer getClassify_delFlag() {
        return this.classify_delFlag;
    }
    public void setClassify_delFlag(Integer classify_delFlag) {
        this.classify_delFlag = classify_delFlag;
    }
    public Integer getSort() {
        return this.sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getClassifyName() {
        return this.classifyName;
    }
    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }
    public Long getClassify_id() {
        return this.classify_id;
    }
    public void setClassify_id(Long classify_id) {
        this.classify_id = classify_id;
    }
    @Generated(hash = 1627001379)
    public ClassifyEnity(Long classify_id, String classifyName, Integer sort,
            Integer classify_delFlag) {
        this.classify_id = classify_id;
        this.classifyName = classifyName;
        this.sort = sort;
        this.classify_delFlag = classify_delFlag;
    }
    @Generated(hash = 1124298861)
    public ClassifyEnity() {
    }

    @Override
    public String toString() {
        return "ClassifyEnity{" +
                "classify_id=" + classify_id +
                ", classifyName='" + classifyName + '\'' +
                ", sort=" + sort +
                ", classify_delFlag=" + classify_delFlag +
                '}';
    }
}
