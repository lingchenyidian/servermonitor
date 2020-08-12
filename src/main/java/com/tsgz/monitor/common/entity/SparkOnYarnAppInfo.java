package com.tsgz.monitor.common.entity;

import java.util.Date;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 10:51 2020/8/5
 * @Modified By:
 */
public class SparkOnYarnAppInfo extends AbstractAppInfo {

    private long completedBatch;
    private long activeBatch;

    public SparkOnYarnAppInfo() {
    }

    public SparkOnYarnAppInfo(String name, String id) {
        this.name = name;
        this.id = id;
        this.updateTime = new Date();
    }


    public long getCompletedBatch() {
        return completedBatch;
    }

    public void setCompletedBatch(long completedBatch) {
        this.completedBatch = completedBatch;
    }

    public long getActiveBatch() {
        return activeBatch;
    }

    public void setActiveBatch(long activeBatch) {
        this.activeBatch = activeBatch;
    }

    @Override
    public String toString() {
        return this.id + "\t" + this.name + "\t" + this.updateTime + "\t" + this.activeBatch + "\t" + this.completedBatch;
    }
}
