package com.example.util;

import java.lang.reflect.Field;

/**
 * 入参：旧PO，新PO
 * 出参：新旧PO发生变化的字段，拼接成的字符串
 */
public class POComparator {
    public static <T> String getChanges(T oldPo, T newPo) {
        StringBuilder changes = new StringBuilder();

        try {
            Class<?> poClass = oldPo.getClass();
            Field[] fields = poClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                Object oldValue = field.get(oldPo);
                Object newValue = field.get(newPo);

                if (!isEqual(oldValue, newValue)) {
                    String fieldName = field.getName();
                    String oldValueStr = (oldValue != null) ? oldValue.toString() : "空";
                    String newValueStr = (newValue != null) ? newValue.toString() : "空";

                    changes.append("将 ").append(fieldName).append(" 从：[ ").append(oldValueStr).append(" ] 变更为了 [ ").append(newValueStr).append(" ]\n");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return changes.toString();
    }

    private static boolean isEqual(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }

        if (obj1 == null || obj2 == null) {
            return false;
        }

        return obj1.equals(obj2);
    }
}
