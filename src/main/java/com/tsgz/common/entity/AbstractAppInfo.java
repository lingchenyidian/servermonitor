package com.tsgz.common.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 10:48 2020/8/5
 * @Modified By:
 */
public abstract class AbstractAppInfo implements Serializable {

    protected String id;
    protected String name;
    protected Date updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
