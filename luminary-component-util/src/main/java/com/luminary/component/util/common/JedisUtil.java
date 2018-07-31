/**  
* <p>Title: JedisUtil.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:05:49  
*/  
package com.luminary.component.util.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**  
* <p>Title: JedisUtil</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月31日下午4:05:49
*/
public class JedisUtil {
	
	private final static Logger logger =  LoggerFactory.getLogger(JedisUtil.class);
	
	public final static String LOCK = "local retValue = redis.call(\"get\", KEYS[1])" ;
	
	private String host;
	
	private int port;
	
	private String password;
	
	public JedisUtil() {
		
	}
	
	public JedisUtil(String host, int port, String password) {
		this.host = host;
		this.port = port;
		this.password = password;
	}
	
	public JedisUtil(String host, int port, String password, JedisPoolConfig jedisPoolConfig) {
		this.host = host;
		this.port = port;
		this.password = password;
		config = jedisPoolConfig;
	}
	
	private static JedisPoolConfig config = new JedisPoolConfig();
	static{
	     config.setMaxIdle(200);  //最大空闲连接数 
	     config.setMaxTotal(300); //最大连接数 
	     config.setTestOnBorrow(false);
	     config.setTestOnReturn(false);
	}
	
	public Jedis init() {
		JedisPool jedisPool = new JedisPool(config, host, port);
		return  jedisPool.getResource();
	}
	
	/**
	 * 查询String
	 * 
	 * @param key
	 * @return
	 */
	public  String get(String key){
		logger.info("JedisUtil.get.start,key="+key);
        Jedis jedis = init();
        //鉴权信息由用户名:密码拼接而成
        logger.info("JedisUtil.get.auth.start ");
        jedis.auth(password);
        logger.info("JedisUtil.get.auth.end");
        
        String value = jedis.get(key);
        logger.info("JedisUtil.get.end");
        
        jedis.quit();
        jedis.close();
        
        return value;
	}
	
	/**
	 * 保存String(有设置过期时间，过期失效)
	 * 
	 * @param key
	 * @param value
	 * @param expiredSeconds 经过多长时间过期(单位秒)
	 * @return
	 */
	public  String set(String key, String value,int expiredSeconds){
		logger.info("JedisUtil.set.start,key="+key+",value="+value+",expiredSeconds="+expiredSeconds);
        Jedis jedis = init();
        //鉴权信息由用户名:密码拼接而成
        logger.info("JedisUtil.set.auth.start");
        jedis.auth(password);
        logger.info("JedisUtil.set.auth.end");
        
        String retValue = jedis.set(key,value);
        if(0!=expiredSeconds){
        	logger.info("JedisUtil.set.expire.start");
        	jedis.expire(key, expiredSeconds);
        	logger.info("JedisUtil.set.expire.end");
        }
        logger.info("JedisUtil.set.end");
        
        jedis.quit();
        jedis.close();
        
        return retValue;
	}
	
	/**
	 * 保存String(有设置过期时间，过期失效)
	 * 
	 * @param key
	 * @param value
	 * @param expiredSeconds 经过多长时间过期(单位秒)
	 * @return
	 */
	public  Long setnx(String key, String value,int expiredSeconds){
		logger.info("JedisUtil.setnx.start,key="+key+",value="+value+",expiredSeconds="+expiredSeconds);
        Jedis jedis = init();
        //鉴权信息由用户名:密码拼接而成
        logger.info("JedisUtil.setnx.auth.start");
        jedis.auth(password);
        logger.info("JedisUtil.setnx.auth.end");
        
        Long retValue = jedis.setnx(key,value);
        if(1==retValue) {
	        if(0!=expiredSeconds){
	        	logger.info("JedisUtil.setnx.expire.start");
	        	jedis.expire(key, expiredSeconds);
	        	logger.info("JedisUtil.setnx.expire.end");
	        }
        }
        logger.info("JedisUtil.set.end");
        
        jedis.quit();
        jedis.close();
        
        return retValue;
	}
	
	/**
	 * 保存String(没有设置过期时间，永久有效)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public  String set(String key, String value){
        String retValue = set(key,value,0);//没有设置过期时间，永久有效
        return retValue;
	}
	
	/**
	 * blpop(从queue取出并移除头部元素)
	 * 
	 * @param key
	 * @return
	 */
	public  List<String> blpop(String key){
		logger.info("JedisUtil.blpop.start,key="+key);
        Jedis jedis = init();
        //鉴权信息由用户名:密码拼接而成
        logger.info("JedisUtil.blpop.auth.start");
        jedis.auth(password);
        logger.info("JedisUtil.blpop.auth.end");

        logger.info("JedisUtil.blpop.start,key="+key);
        List<String> retValue=jedis.blpop(30, key);
        logger.info("JedisUtil.blpop.end,value="+retValue);
        
        jedis.quit();
        jedis.close();
        
        return retValue;
	}
	
	/**
	 * brpop(从queue取出并移除尾部元素)
	 * 
	 * @param key
	 * @return
	 */
	public  List<String> brpop(String key){
		logger.info("JedisUtil.brpop.start,key="+key);
        Jedis jedis = init();
        //鉴权信息由用户名:密码拼接而成
        logger.info("JedisUtil.brpop.auth.start");
        jedis.auth(password);
        logger.info("JedisUtil.brpop.auth.end");
        
        logger.info("JedisUtil.brpop.start,key="+key);
        List<String> retValue=jedis.brpop(30, key);
        logger.info("JedisUtil.brpop.end,value="+retValue);
        
        jedis.quit();
        jedis.close();
        
        return retValue;
	}
	
	/**
	 * brpop( 把元素插入到queue头部)
	 * 
	 * @param key
	 * @return
	 */
	public  long lpush(String key,String value){
		logger.info("JedisUtil.lpush.start,key="+key);
        Jedis jedis = init();
        //鉴权信息由用户名:密码拼接而成
        logger.info("JedisUtil.lpush.auth.start");
        jedis.auth(password);
        logger.info("JedisUtil.lpush.auth.end");
        
        logger.info("JedisUtil.lpush.start,key="+key+",value="+value);
        long retValue=jedis.lpush(key,value);
        logger.info("JedisUtil.lpush.end");
        
        jedis.quit();
        jedis.close();
        
        return retValue;
	}
	
	/**
	 * brpop( 从queue尾部出队元素)
	 * 
	 * @param key
	 * @return
	 */
	public  String rpop(String key){
		logger.info("JedisUtil.rpop.start,key="+key);
        Jedis jedis = init();
        //鉴权信息由用户名:密码拼接而成
        logger.info("JedisUtil.rpop.auth.start");
        jedis.auth(password);
        logger.info("JedisUtil.rpop.auth.end");
        
        logger.info("JedisUtil.rpop.start,key="+key);
        String retValue=jedis.rpop(key);
        logger.info("JedisUtil.rpop.end");
        
        jedis.quit();
        jedis.close();
        
        return retValue;
	}
	
	/**
	 * llen(获取队列queue的长度)
	 * 
	 * @param key
	 * @return
	 */
	public  long llen(String key){
		logger.info("JedisUtil.llen.start,key="+key);
        Jedis jedis = init();
        //鉴权信息由用户名:密码拼接而成
        logger.info("JedisUtil.llen.auth.start");
        jedis.auth(password);
        logger.info("JedisUtil.llen.auth.end");
        
        logger.info("JedisUtil.llen.start,key="+key);
        long retValue=jedis.llen(key);
        logger.info("JedisUtil.llen.end,len="+retValue);
        
        jedis.quit();
        jedis.close();
        
        return retValue;
	}
	
	/**
	 * 删除
	 * 
	 * @param key redis的key
	 * @return
	 */
	public  long del(String key){
		logger.info("JedisUtil.del.start,key="+key);
        Jedis jedis = init();
        //鉴权信息由用户名:密码拼接而成
        logger.info("JedisUtil.del.auth.start");
        jedis.auth(password);
        logger.info("JedisUtil.del.auth.end");
        
        logger.info("JedisUtil.del.start,key="+key);
        long retValue=jedis.del(key);
        logger.info("JedisUtil.del.end,len="+retValue);
        
        jedis.quit();
        jedis.close(); 
        
        return retValue;
	}
	
	/**
	 * hash是否存在
	 * 
	 * @param key redis的key
	 * @return
	 */
	public  boolean hexists(String key,String field){
		logger.info("JedisUtil.hexists.start,key="+key+",field="+field);
        Jedis jedis = init();
        //鉴权信息由用户名:密码拼接而成
        logger.info("JedisUtil.del.auth.start");
        jedis.auth(password);
        logger.info("JedisUtil.hexists.auth.end");
        
        logger.info("JedisUtil.hexists.start,key="+key+",field="+field);
        boolean retValue=jedis.hexists(key, field);
        logger.info("JedisUtil.hexists.end,len="+retValue);
        
        jedis.quit();
        jedis.close(); 
        
        return retValue;
	}
	
	public boolean setBit(String key, int offset, boolean value) {
		logger.info("JedisUtil.setBit.start,key="+key);
		Jedis jedis = init();
		logger.info("JedisUtil.setBit.auth.start");
	    jedis.auth(password);
	    logger.info("JedisUtil.setBit.auth.end");
	    
	    logger.info("JedisUtil.setBit.start,key="+key);
	    boolean retValue = jedis.setbit(key, offset, value);
	    logger.info("JedisUtil.setBit.end,len="+retValue);
	    
	    jedis.quit();
        jedis.close();
        
        return retValue;
	}
	
	public boolean getBit(String key, int offset) {
		logger.info("JedisUtil.getBit.start,key="+key);
		Jedis jedis = init();
		logger.info("JedisUtil.getBit.auth.start");
	    jedis.auth(password);
	    logger.info("JedisUtil.getBit.auth.end");
	    
	    logger.info("JedisUtil.getBit.start,key="+key);
	    boolean retValue = jedis.getbit(key, offset);
	    logger.info("JedisUtil.getBit.end,len="+retValue);
	    
	    jedis.quit();
        jedis.close();
        
        return retValue;
	}
	
	/**
	 * bitcount
	 * 
	 * @param key
	 * @return
	 */
	public  long bitcount(String key){
		logger.info("JedisUtil.bitcount.start,key="+key);
        Jedis jedis = init();
        //鉴权信息由用户名:密码拼接而成
        logger.info("JedisUtil.bitcount.auth.start");
        jedis.auth(password);
        logger.info("JedisUtil.bitcount.auth.end");
        
        logger.info("JedisUtil.bitcount.start,key="+key);
        long retValue=jedis.bitcount(key);
        logger.info("JedisUtil.bitcount.end,len="+retValue);
        
        jedis.quit();
        jedis.close();
        
        return retValue;
	}
	
	public  Object evalScript(String scriptName,String script,int keyCount,String ... params) {
		Jedis jedis = init();
		jedis.auth(password);
		String sha = jedis.get("script-sha-"+scriptName);
		if(sha==null || "".equals(sha) || !jedis.scriptExists(sha)) {
			sha = jedis.scriptLoad(script);
			jedis.set("script-sha-"+scriptName, sha);
		}
		Object retValue = jedis.evalsha(sha, keyCount, params);
		return retValue;
	}
	
	public  boolean flushScript() {
		logger.info("JedisUtil.flushScript.start");
		Jedis jedis = init();
		jedis.auth(password);
		String retValue = jedis.scriptFlush();
		logger.info("JedisUtil.flushScript.end,retValue="+retValue);
		if("OK".equals(retValue.toUpperCase())) {
			return true;
		} else {
			return false;
		}
	}

}
