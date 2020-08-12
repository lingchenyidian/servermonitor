package com.tsgz.monitor.server;

import com.tsgz.monitor.common.entity.AbstractAppInfo;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 13:05 2020/8/5
 * @Modified By:
 */
public class MonitorManager {
    private MonitorManager() {}

    private ConcurrentHashMap<String, AbstractAppInfo> apps = new ConcurrentHashMap<>();

    private volatile static MonitorManager instance = null;

    public static MonitorManager getInstance() {
        if (instance == null) {
            synchronized (MonitorManager.class) {
                if (instance == null) {
                    instance = new MonitorManager();
                }
            }
        }
        return instance;
    }

    public void updateAppInfo(AbstractAppInfo appInfo) {
        apps.put(appInfo.getId(), appInfo);
    }

    public ConcurrentHashMap<String, AbstractAppInfo> getApps() {
        return apps;
    }

    public void print() {
        System.out.println(Thread.currentThread().getName());
        apps.values().stream().collect(Collectors.groupingBy(AbstractAppInfo::getClass))
                .forEach(
                        (GKey, V) -> {
                            System.out.println("----------------------------------------------------------------------------------------------------");
                            System.out.println(GKey);
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                            alignPrint(V);
                            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                        }
                );
    }

    /**
     * 对齐打印
     * @param list
     */
    private void alignPrint(List<AbstractAppInfo> list) {
        List<List<String>> lines = new ArrayList<>();
        List<String> fieldNames = new ArrayList<>();
        List<StringBuilder> sbs = new ArrayList<>();
        for (AbstractAppInfo appInfo : list) {
            if (fieldNames.size() == 0) {
                getAllFieldName(appInfo.getClass(), fieldNames);
                lines.add(fieldNames);
                sbs.add(new StringBuilder());
            }

            List<String> fieldValues = new ArrayList<>();
            getAllFieldValue(appInfo, appInfo.getClass(), fieldValues);
            lines.add(fieldValues);
            sbs.add(new StringBuilder());

        }


        for (int j = 0; j < fieldNames.size(); j++) {
            int col = j;
            // 得到每列最长的字段值
            int max = lines.stream().mapToInt(line -> line.get(col).length()).max().getAsInt();
            for (int i = 0; i < sbs.size(); i++) {
                String unit = lines.get(i).get(j);
                sbs.get(i).append(unit);

                // 小于max的部分用空格补齐
                int whiteSize = max - unit.length();
                for (int k = 0; k < whiteSize; k++) {
                    sbs.get(i).append(" ");
                }
                sbs.get(i).append("\t");
            }
        }

        sbs.forEach(s -> System.out.println(s.toString()));

    }


    /**
     * 获取类的所有字段包括父类的字段
     * @param clazz
     * @param fieldNames
     */
    private void getAllFieldName(Class clazz, List<String> fieldNames) {
        if (clazz != null) {
            getAllFieldName(clazz.getSuperclass(), fieldNames);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if ("instance".equals(field.getName()))
                    continue;
                fieldNames.add(field.getName());
            }
        }
    }


    /**
     * 获取对象的所有字段值包括父类的字段
     * @param appInfo
     * @param clazz
     * @param fieldValues
     */
    private void getAllFieldValue(AbstractAppInfo appInfo, Class clazz, List<String> fieldValues) {
        if (clazz != null) {
            getAllFieldValue(appInfo, clazz.getSuperclass(), fieldValues);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if ("instance".equals(field.getName()))
                    continue;
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                    Method readMethod = pd.getReadMethod();

                    Object invoke = readMethod.invoke(appInfo);
                    fieldValues.add(invoke.toString());
//                    fieldNames.add(field.getName());
                } catch (NullPointerException e) {
                    System.out.println(appInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
