package cn.weiyinfu.gs;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class ResultSetGs implements GetterAndSetter {
ResultSet resultSet;
Map<String, Integer> column2Index;
boolean ignoreCase = true;

public ResultSetGs(ResultSet resultSet, boolean ignoreCase) {
    this.ignoreCase = ignoreCase;
    column2Index = new TreeMap<>();
    this.resultSet = resultSet;
    try {
        ResultSetMetaData meta = resultSet.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            column2Index.put(meta.getColumnName(i).toLowerCase(), i);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


/**
 * 将ResultSet中的某一列的值根据类型转换为Java中的对象
 */
public static Object getObjectOfType(ResultSet res, int index, String columnType) {
    try {
        if (columnType.equalsIgnoreCase("varchar") ||
                columnType.equalsIgnoreCase("text")) {
            return res.getString(index);
        } else if (columnType.equalsIgnoreCase("int")) {
            return res.getInt(index);
        } else if (columnType.equalsIgnoreCase("float")) {
            return res.getFloat(index);
        } else if (columnType.equalsIgnoreCase("blob")) {
            return res.getBytes(index);
        } else {
            throw new RuntimeException("无法处理的数据类型" + columnType);
        }
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

@Override
public Object get(String attr) {
    try {
        if (this.ignoreCase) attr = attr.toLowerCase();
        Integer columnIndex = column2Index.get(attr);
        if (columnIndex == null) return null;
        String columnType = resultSet.getMetaData().getColumnTypeName(columnIndex);
        Object value = getObjectOfType(resultSet, columnIndex, columnType);
        return value;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

@Override
public void set(String attr, Object valueObj) {
    throw new RuntimeException("cannot assign to ResultSet object");
}
}