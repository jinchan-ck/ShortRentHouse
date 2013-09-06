package tk.sweetvvck.utils;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**使用前先init_SP_Instance(context, "fileName")*/
public class MySharedPreferences{
	
	private static SharedPreferences sharedPreferences;
	private static Editor editor;
	
	static {
		
	}
	
	/**
	 * 初始化SharedPreferences实例
	 * @param c
	 * @param SPFileName
	 * */
	public static void init_SP_Instance(Context c, String SPFileName){
		sharedPreferences = c.getSharedPreferences(SPFileName, Context.MODE_PRIVATE);  
	}
	/**
	 * 返回String类型数据, 如果preference中不存在该key，将返回默认值
	 * @param Key
	 * @param defaultValue  
	 * @return
	 */
    public static String get_String(String Key, String defaultValue){
        //getString()第二个参数为默认值，如果preference中不存在该key，将返回默认值  
        String value = sharedPreferences.getString(Key, defaultValue); 
        return value;
    }
    /**
	 * 返回int类型数据, 如果preference中不存在该key，将返回默认值
	 * @param Key
	 * @param defaultValue  
	 * @return
	 */
    public static int get_Int(String Key, int defaultValue){
        //getString()第二个参数为默认值，如果preference中不存在该key，将返回默认值  
    	int value = sharedPreferences.getInt(Key, defaultValue); 
        return value;
    }
    /**
	 * 返回boolean类型数据, 如果preference中不存在该key，将返回默认值
	 * @param Key
	 * @param defaultValue  
	 * @return
	 */
    public static boolean get_Boolean(String Key, boolean defaultValue){
        //getString()第二个参数为默认值，如果preference中不存在该key，将返回默认值  
    	boolean value = sharedPreferences.getBoolean(Key, defaultValue); 
        return value;
    }
    /**
	 * 返回float类型数据, 如果preference中不存在该key，将返回默认值
	 * @param Key
	 * @param defaultValue  
	 * @return
	 */
    public static float get_Float(String Key, float defaultValue){
        //getString()第二个参数为默认值，如果preference中不存在该key，将返回默认值  
    	float value = sharedPreferences.getFloat(Key, defaultValue); 
        return value;
    }
    /**
	 * 返回long类型数据, 如果preference中不存在该key，将返回默认值
	 * @param Key
	 * @param defaultValue  
	 * @return
	 */
    public static long get_Long(String Key, long defaultValue){
        //getString()第二个参数为默认值，如果preference中不存在该key，将返回默认值  
    	long value = sharedPreferences.getLong(Key, defaultValue); 
        return value;
    }
    /**
	 * 返回所有类型数据
	 * @return Map<String, ?>
	 */
    public static Map<String, ?> get_All(){
        //getString()第二个参数为默认值，如果preference中不存在该key，将返回默认值  
        Map<String, ?> value = sharedPreferences.getAll(); 
        return value;
    }
    
    /**
     * 存储String类型数据
     * @param Key 键
     * @param Value 值
     */
    public static void put_String(String Key, String Value){
		editor = sharedPreferences.edit();//获取编辑器  
	    editor.putString(Key, Value);  //存储要存储的值
	    editor.commit();//提交修改  
    }
    /**
     * 存储boolean类型数据
     * @param Key 键
     * @param Value 值
     */
    public static void put_Boolean(String Key, boolean Value){
		editor = sharedPreferences.edit();//获取编辑器  
	    editor.putBoolean(Key, Value);  //存储要存储的值
	    editor.commit();//提交修改  
    }
    /**
     * 存储float类型数据
     * @param Key 键
     * @param Value 值
     */
    public static void put_Float(String Key, float Value){
		editor = sharedPreferences.edit();//获取编辑器  
	    editor.putFloat(Key, Value);  //存储要存储的值
	    editor.commit();//提交修改  
    }
    /**
     * 存储int类型数据
     * @param Key 键
     * @param Value 值
     */
    public static void put_Int(String Key, int Value){
		editor = sharedPreferences.edit();//获取编辑器  
	    editor.putInt(Key, Value);  //存储要存储的值
	    editor.commit();//提交修改  
    }
    /**
     * 存储long类型数据
     * @param Key 键
     * @param Value 值
     */
    public static void put_Long(String Key, long Value){
		editor = sharedPreferences.edit();//获取编辑器  
	    editor.putLong(Key, Value);  //存储要存储的值
	    editor.commit();//提交修改  
    }
    /**
     * 清除里边所有的信息
     * 
     */
    public static void clear(Context c, String SPFileName){
    	SharedPreferences sharedPreferences = c.getSharedPreferences(SPFileName, Context.MODE_PRIVATE);
    	editor = sharedPreferences.edit();
    	editor.clear();
    	editor.commit();//提交修改  
    }
}