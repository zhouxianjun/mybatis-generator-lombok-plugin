package com.alone.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.*;

/**
 * @author zhouxianjun(Alone)
 * @ClassName:
 * @Description:
 * @date 2018/6/13 13:05
 */
public class LombokPlugin extends PluginAdapter {
    private boolean data = true;
    private Map<String, String> map = new HashMap<String, String>(20);
    private Set<String> importPackage = new HashSet<String>();
    private static Map<String, String> packageMap = new HashMap<String, String>(20);

    static {
        packageMap.put("@Accessors", "lombok.experimental");
        packageMap.put("@Builder", "lombok.experimental");
        packageMap.put("@Delegate", "lombok.experimental");
        packageMap.put("@ExtensionMethod", "lombok.experimental");
        packageMap.put("@FieldDefaults", "lombok.experimental");
        packageMap.put("@Helper", "lombok.experimental");
        packageMap.put("@NonFinal", "lombok.experimental");
        packageMap.put("@PackagePrivate", "lombok.experimental");
        packageMap.put("@Tolerate", "lombok.experimental");
        packageMap.put("@UtilityClass", "lombok.experimental");
        packageMap.put("@Value", "lombok.experimental");
        packageMap.put("@var", "lombok.experimental");
        packageMap.put("@Wither", "lombok.experimental");
        packageMap.put("@CommonsLog", "lombok.extern.apachecommons");
        packageMap.put("@Log", "lombok.extern.java");
        packageMap.put("@Log4j", "lombok.extern.log4j");
        packageMap.put("@Log4j2", "lombok.extern.log4j");
        packageMap.put("@Slf4j", "lombok.extern.slf4j");
        packageMap.put("@XSlf4j", "lombok.extern.slf4j");
    }

    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    private void processEntityClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (importPackage != null && importPackage.size() > 0) {
            for (String p : importPackage) {
                topLevelClass.addImportedType(p);
            }
        }
        if (map != null && map.size() > 0) {
            for (String key : map.keySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append(key);
                String config = map.get(key);
                if (config != null && config.trim().length() > 0) {
                    sb.append("(").append(config).append(")");
                }
                String p = packageMap.get(key);
                topLevelClass.addImportedType(p == null ? "lombok." + key.substring(1) : p + "." + key.substring(1));
                topLevelClass.addAnnotation(sb.toString());
            }
        }
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith("@")) {
                map.put(key, (String) entry.getValue());
            }
            if (key.startsWith("import.")) {
                importPackage.add(key.substring(7));
            }
        }
        if (data && !map.containsKey("@Data")) {
            map.put("@Data", null);
        }
    }
}
