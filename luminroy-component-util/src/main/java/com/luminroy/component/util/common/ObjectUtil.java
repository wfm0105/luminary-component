package com.luminroy.component.util.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对象工具类
 * 
 * @author huangtf
 * @date 20161025
 *
 */
public class ObjectUtil {

	private final static Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

	/**
	 * 对象序列化为字节数组
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] serialize(Object obj) throws Exception {
		byte[] byt = null;
		ObjectOutputStream obi = null;
		ByteArrayOutputStream bai = null;
		try {
			bai = new ByteArrayOutputStream();
			obi = new ObjectOutputStream(bai);
			obi.writeObject(obj);
			byt = bai.toByteArray();
		} catch (Exception ex) {
			logger.info("ObjectUtil.serialize.exception,exception=" + ex.getMessage());
		} finally {
			if (bai != null) {
				bai.close();
			}
			if (obi != null) {
				obi.close();
			}
		}
		return byt;
	}

	/**
	 * 字节数组反序列化为对象
	 * 
	 * @param byt
	 * @return
	 */
	public static Object unserizlize(byte[] byt) throws Exception {
		Object obj = null;
		ObjectInputStream oii = null;
		ByteArrayInputStream bis = null;
		try {
			bis = new ByteArrayInputStream(byt);
			oii = new ObjectInputStream(bis);
			obj = oii.readObject();
		} catch (Exception ex) {
			logger.info("ObjectUtil.unserizlize.exception,exception=" + ex.getMessage());
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (oii != null) {
				oii.close();
			}
		}
		return obj;
	} 
	
	/**
     * javaBean 转 Map
     * @param object 需要转换的javabean
     * @return  转换结果map
     * @throws Exception
     */
    public static Map<String, Object> beanToMap(Object object) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
 
        Class<? extends Object> cls = object.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(object));
        }
        return map;
    }

}
