package com.tsgz.monitor.common.entity;

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
    // 丢失的心跳数
    protected int loseHeartbeatNum;

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

    public int getLoseHeartbeatNum() {
        return loseHeartbeatNum;
    }

    public void setLoseHeartbeatNum(int loseHeartbeatNum) {
        this.loseHeartbeatNum = loseHeartbeatNum;
    }

    @Override
    public String toString() {
        return "AbstractAppInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", updateTime=" + updateTime +
                ", loseHeartbeatNum=" + loseHeartbeatNum +
                '}';
    }
}
