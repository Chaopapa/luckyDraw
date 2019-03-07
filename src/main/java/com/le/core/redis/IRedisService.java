package com.le.core.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName IRedisService
 * @Author lz
 * @Description redis配置類
 * @Date 2018/10/8 13:50
 * @Version V1.0
 **/
@Slf4j
@Service
@Deprecated
public class IRedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //=============================common============================

    /**
     * @param key
     * @return Object
     * @Description 读取redis缓存
     * @author lz
     */
    public Object get(final String key) {
        Object result = null;
        try {
            result = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("读取redis缓存失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key, value
     * @return boolean
     * @Description 写入redis缓存（String, Object）
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            result = true;
        } catch (Exception e) {
            log.error("写入redis缓存（String, Object）失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key, value, expire, timeUnit
     * @return boolean
     * @Description 写入redis缓存(设置expire存活时间)
     * @author lz
     */
    public boolean set(final String key, Object value, Long expire, TimeUnit timeUnit) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value, expire, timeUnit);
            result = true;
        } catch (Exception e) {
            log.error("写入redis缓存（设置expire存活时间）失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key, value, expire
     * @return boolean
     * @Description 写入redis缓存(设置expire存活时间 ( 秒单位))
     * @author lz
     */
    public boolean setSeconds(final String key, Object value, Long expire) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("写入redis缓存（设置expire存活时间(秒单位)）失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key, value, expire
     * @return boolean
     * @Description 写入redis缓存(设置expire存活时间 ( 分钟单位))
     * @author lz
     */
    public boolean setMinutes(final String key, Object value, Long expire) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value, expire, TimeUnit.MINUTES);
            result = true;
        } catch (Exception e) {
            log.error("写入redis缓存（设置expire存活时间(分钟单位)）失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return boolean
     * @Description 判断redis缓存中是否有对应的key
     * @author lz
     */
    public boolean exists(final String key) {
        boolean result = false;
        try {
            result = redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("判断redis缓存中是否有对应的key失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return boolean
     * @Description redis根据key删除对应的value
     * @author lz
     */
    public boolean remove(final String key) {
        boolean result = false;
        try {
            if (exists(key)) {
                redisTemplate.delete(key);
            }
            result = true;
        } catch (Exception e) {
            log.error("redis根据key删除对应的value失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param keys
     * @return boolean
     * @Description redis根据keys批量删除对应的value
     * @author lz
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * @param key
     * @return boolean
     * @Description 如果 key 存在则覆盖,并返回旧值 如果不存在,返回null 并添加
     * @author lz
     */
    public Object getAndSet(final String key, Object value) {
        Object result = null;
        try {
            result = redisTemplate.opsForValue().getAndSet(key, value);
        } catch (Exception e) {
            log.error("key覆盖并返回旧值失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param keyAndValue
     * @return boolean
     * @Description 批量添加 key-value (重复的键会覆盖)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean batchSet(final Map<String, String> keyAndValue) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().multiSet(keyAndValue);
            result = true;
        } catch (Exception e) {
            log.error("批量添加 key-value(重复的键会覆盖)失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param keyAndValue
     * @return boolean
     * @Description 批量添加 key-value 只有在键不存在时,才添加
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean batchSetIfAbsent(final Map<String, String> keyAndValue) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
            result = true;
        } catch (Exception e) {
            log.error("批量添加 key-value (只有在键不存在时,才添加)失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param incrValue
     * @return boolean
     * @Description 递增
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long incr(final String key, long incrValue) {
        if (incrValue < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        Long result = null;
        try {
            result = redisTemplate.opsForValue().increment(key, incrValue);
        } catch (Exception e) {
            log.error("递增失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param decrValue
     * @return boolean
     * @Description 递减
     * @author lz
     * @date 2018/10/8 13:52
     */
    public long decr(final String key, long decrValue) {
        if (decrValue < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        Long result = null;
        try {
            result = redisTemplate.opsForValue().increment(key, decrValue);
        } catch (Exception e) {
            log.error("递减失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param number
     * @return boolean
     * @Description 对一个 key-value 的值进行加减操作
     * 如果该 key 不存在 将创建一个key 并赋值该 number 如果 key 存在,但 value 不是 纯数字 ,将报错
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Double incrDouble(final String key, double number) {
        if (number < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        Double result = null;
        try {
            result = redisTemplate.opsForValue().increment(key, number);
        } catch (Exception e) {
            log.error("递增失败！错误信息为：" + e.getMessage());
        }
        return result;

    }

    /**
     * @param key time timeUnit
     * @return boolean
     * @Description 指定缓存失效时间
     * @author lz
     * @DATE 2018/10/8 13:52
     */
    public boolean expire(final String key, long time, TimeUnit timeUnit) {
        boolean result = false;
        try {
            result = redisTemplate.boundValueOps(key).expire(time, timeUnit);
            result = true;
        } catch (Exception e) {
            log.error("指定缓存失效时间 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return boolean
     * @Description 移除指定key 的过期时间
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean persist(final String key) {
        boolean result = false;
        try {
            result = redisTemplate.boundValueOps(key).persist();
            result = true;
        } catch (Exception e) {
            log.error("移除指定 key 的过期时间 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return boolean
     * @Description 获取指定key 的过期时间
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long getExpire(final String key) {
        Long result = null;
        try {
            result = redisTemplate.boundValueOps(key).getExpire();
        } catch (Exception e) {
            log.error("获取指定key 的过期时间！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key newKey
     * @return boolean
     * @Description 修改key的名称为newKey
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean rename(final String key, String newKey) {
        boolean result = false;
        try {
            redisTemplate.boundValueOps(key).rename(newKey);
            result = true;
        } catch (Exception e) {
            log.error("修改key的名称为newKey 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    //=============================hash操作============================

    /**
     * @param key hashKey value
     * @return boolean
     * @Description 添加 Hash 键值对
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean put(final String key, String hashKey, Object value) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            result = true;
        } catch (Exception e) {
            log.error("添加 Hash 键值对 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key map
     * @return boolean
     * @Description 批量添加 hash 的 键值对
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean putAll(final String key, Map<String, Object> map) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().putAll(key, map);
            result = true;
        } catch (Exception e) {
            log.error("批量添加 hash 的键值对 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key hashKey value
     * @return boolean
     * @Description 添加 hash 键值对. 不存在的时候才添加
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean putIfAbsent(final String key, String hashKey, Object value) {
        boolean result = false;
        try {
            result = redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
        } catch (Exception e) {
            log.error("批量添加 hash 的键值对 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key hashKeys
     * @return Long 删除成功的 数量
     * @Description 删除指定 hash 的 HashKey
     * @author lz
     * @date 2018/10/8 13:52
     */
    @SuppressWarnings("all")
    public Long delete(final String key, Object... hashKeys) {
        Long result = null;
        try {
            result = redisTemplate.opsForHash().delete(key, hashKeys);
        } catch (Exception e) {
            log.error("删除指定 hash 的 HashKey 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key hashKey number
     * @return Long 返回增长后的值
     * @Description 给指定 hash 的 hashkey 做增减操作
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long increment(final String key, final String hashKey, long number) {
        Long result = null;
        try {
            result = redisTemplate.opsForHash().increment(key, hashKey, number);
        } catch (Exception e) {
            log.error("给指定 hash 的 hashkey 做增操作 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key hashKey number
     * @return Double 返回增长后的值
     * @Description 给指定 hash 的 hashkey(Double) 做增减操作
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Double increment(final String key, final String hashKey, Double number) {
        Double result = null;
        try {
            result = redisTemplate.opsForHash().increment(key, hashKey, number);
        } catch (Exception e) {
            log.error("给指定 hash 的 hashkey(Double) 做增减操作 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key hashKey
     * @return Object
     * @Description 获取指定 key 下的 hashkey
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Object getHashKey(final String key, final String hashKey) {
        Object result = null;
        try {
            result = redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            log.error("获取指定 key 下的 hashkey 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return Map<Object ,   Object>
     * @Description 获取 key 下的 所有  hashkey 和 value
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Map<Object, Object> getHashEntries(final String key) {
        Map<Object, Object> result = null;
        try {
            result = redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error("获取 key 下的 所有  hashkey 和 value 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key hashKey
     * @return boolean
     * @Description 验证指定 key 下 有没有指定的 hashkey
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean hashKey(final String key, final String hashKey) {
        boolean result = false;
        try {
            result = redisTemplate.opsForHash().hasKey(key, hashKey);
        } catch (Exception e) {
            log.error("验证指定 key 下 有没有指定的 hashkey 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return Set<Object>
     * @Description 获取 key 下的 所有 hashkey 字段名
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> hashKeys(final String key) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForHash().keys(key);
        } catch (Exception e) {
            log.error("获取 key 下的 所有 hashkey 字段名 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return Set<Object>
     * @Description 获取指定 hash 下面的 键值对 数量
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long hashSize(final String key) {
        Long result = null;
        try {
            result = redisTemplate.opsForHash().size(key);
        } catch (Exception e) {
            log.error("获取指定 hash 下面的键值对数量 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    //=============================List 操作============================


    /**
     * @param key
     * @return Long 当前队列的长度
     * @Description 获指定 list 从左入栈
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long leftPush(String key, Object value) {
        Long result = null;
        try {
            result = redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            log.error("获指定 list 从左入栈 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return Object 左出栈的值
     * @Description 指定 list 从左出栈  如果列表没有元素,会堵塞到列表一直有元素或者超时为止
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Object leftPop(String key) {
        Object result = null;
        try {
            result = redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("获指定 list 从左出栈 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key values
     * @return 左出栈的数量
     * @Description 从左边依次入栈 导入顺序按照 Collection 顺序 如: a b c => c b a
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long leftPushAll(String key, Collection<Object> values) {
        Long result = null;
        try {
            result = redisTemplate.opsForList().leftPushAll(key, values);
        } catch (Exception e) {
            log.error("获指定 list 从左入栈 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return Long 当前队列的长度
     * @Description 指定 list 从右入栈
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long rightPush(String key, Object value) {
        Long result = null;
        try {
            result = redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            log.error("指定 list 从右入栈 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return Object 出栈的值
     * @Description 指定 list 从右出栈 如果列表没有元素,会堵塞到列表一直有元素或者超时为止
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Object rightPop(String key) {
        Object result = null;
        try {
            result = redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error("指定 list 从右出栈 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key values
     * @return Long
     * @Description 从右边依次入栈 导入顺序按照 Collection 顺序 如: a b c =< a b c
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long rightPushAll(String key, Collection<Object> values) {
        Long result = null;
        try {
            result = redisTemplate.opsForList().rightPushAll(key, values);
        } catch (Exception e) {
            log.error("从右边依次入栈 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key index
     * @return Long
     * @Description 根据下标获取值
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Object popIndex(String key, long index) {
        Object result = null;
        try {
            result = redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("根据下标获取值 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key index
     * @return Long
     * @Description 获取列表指定长度
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long listSize(String key, long index) {
        Long result = null;
        try {
            result = redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("获取列表指定长度 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key start end
     * @return Long
     * @Description 获取列表 指定范围内的所有值
     * @author lz
     * @date 2018/10/8 13:52
     */
    public List<Object> listSize(String key, long start, long end) {
        List<Object> result = null;
        try {
            result = redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("获取列表 指定范围内的所有值 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key start end
     * @return Long 成功删除的个数
     * @Description 删除 key 中 值为 value 的 count 个数.
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long listRemove(String key, long count, Object value) {
        Long result = null;
        try {
            result = redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error("删除 key 中 值为 value 的 count 个数 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key start end
     * @return boolean
     * @Description 删除 列表 [start,end] 以外的所有元素
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean listTrim(String key, long start, long end) {
        boolean result = false;
        try {
            redisTemplate.opsForList().trim(key, start, end);
            result = true;
        } catch (Exception e) {
            log.error("删除 列表 [start,end] 以外的所有元素 失败！错误信息为：" + e.getMessage());
        }
        return result;

    }

    /**
     * @param key 右出栈的列表 key2 左入栈的列表
     * @return Object 操作的值
     * @Description 将 key 右出栈,并左入栈到 key2
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Object rightPopAndLeftPush(String key, String key2) {
        Object result = null;
        try {
            result = redisTemplate.opsForList().rightPopAndLeftPush(key, key2);
        } catch (Exception e) {
            log.error("将 key 右出栈,并左入栈到 key2 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    //=============================set 操作  无序不重复集合============================

    /**
     * @param key values
     * @return Long
     * @Description 添加 set 元素
     * @author lz
     * @date 2018/10/8 13:52
     */
    @SuppressWarnings("all")
    public Long add(String key, Object values) {
        Long result = null;
        try {
            result = redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("添加 set 元素 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key 集合1  otherkey 集合2
     * @return Set<Object> 新的集合
     * @Description 获取两个集合的差集
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> difference(String key, String otherkey) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForSet().difference(key, otherkey);
        } catch (Exception e) {
            log.error("获取两个集合的差集 ！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key 集合1  otherkey 集合2
     * @return Set<Object> 新的集合
     * @Description 获取 key 和 集合  collections 中的 key 集合的差集
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> difference(String key, Collection<String> otherKeys) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForSet().difference(key, otherKeys);
        } catch (Exception e) {
            log.error("获取两个集合的差集 ！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key 集合1  otherkey 集合2 newKey 新的集合
     * @return Long 返回差集的数量
     * @Description 将  key 与 otherkey 的差集 ,添加到新的 newKey 集合中
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long differenceAndStore(String key, String otherkey, String newKey) {
        Long result = null;
        try {
            result = redisTemplate.opsForSet().differenceAndStore(key, otherkey, newKey);
        } catch (Exception e) {
            log.error("添加 set 元素 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key 集合1  otherkey 集合2 newKey 新的集合
     * @return Long 返回差集的数量
     * @Description 将 key 和 集合  collections 中的 key 集合的差集 添加到  newkey 集合中
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long differenceAndStore(String key, Collection<String> otherKeys, String newKey) {
        Long result = null;
        try {
            result = redisTemplate.opsForSet().differenceAndStore(newKey, otherKeys, newKey);
        } catch (Exception e) {
            log.error("将 key和集合collections中的 key 集合的差集 添加到 newkey 集合中 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key 集合1  otherkey 集合2 newKey 新的集合
     * @return Long 成功删除数量
     * @Description 删除一个或多个集合中的指定值
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long removeSet(String key, Object... values) {
        Long result = null;
        try {
            result = redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error("删除一个或多个集合中的指定值 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return Object 随机移除元素
     * @Description 随机移除一个元素, 并返回出来
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Object randomSetPop(String key) {
        Object result = null;
        try {
            result = redisTemplate.opsForSet().pop(key);
        } catch (Exception e) {
            log.error("随机移除一个元素,并返回出来 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return Object 随机元素
     * @Description 随机获取一个元素
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Object randomSet(String key) {
        Object result = null;
        try {
            result = redisTemplate.opsForSet().randomMember(key);
        } catch (Exception e) {
            log.error("随机获取一个元素 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key count
     * @return Object 随机元素集合
     * @Description 随机获取指定数量的元素, 同一个元素可能会选中两次
     * @author lz
     * @date 2018/10/8 13:52
     */
    public List<Object> randomSet(String key, long count) {
        List<Object> result = null;
        try {
            result = redisTemplate.opsForSet().randomMembers(key, count);
        } catch (Exception e) {
            log.error("随机获取指定数量的元素,同一个元素可能会选中两次 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key count
     * @return Object 随机元素集合
     * @Description 随机获取指定数量的元素, 去重(同一个元素只能选择一次)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> randomSetDistinct(String key, long count) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForSet().distinctRandomMembers(key, count);
        } catch (Exception e) {
            log.error("随机获取指定数量的元素,去重(同一个元素只能选择一次) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key value destKey
     * @return boolean 返回成功与否
     * @Description 将 key 中的 value 转入到 destKey 中
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean moveSet(String key, Object value, String destKey) {
        boolean result = false;
        try {
            result = redisTemplate.opsForSet().move(key, value, destKey);
        } catch (Exception e) {
            log.error("将 key 中的 value 转入到 destKey 中 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return Long 无序集合的大小
     * @Description 无序集合的大小
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long setSize(String key) {
        Long result = null;
        try {
            result = redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("获取无序集合的大小 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return boolean 有
     * @Description 判断 set 集合中 是否有 value
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean isMember(String key, Object value) {
        boolean result = false;
        try {
            result = redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("判断 set 集合中 是否有 value 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key otherKey
     * @return Set<Object> 新集合
     * @Description 返回 key 和 othere 的并集
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> unionSet(String key, String otherKey) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForSet().union(key, otherKey);
        } catch (Exception e) {
            log.error("返回 key 和 othere 的并集 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }


    /**
     * @param otherKeys key 的集合
     * @return Set<Object> 新集合
     * @Description 返回 key 和 otherKeys 的并集
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> unionSet(String key, Collection<String> otherKeys) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForSet().union(key, otherKeys);
        } catch (Exception e) {
            log.error("返回 key 和 otherKeys 的并集 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param otherKey key destKey
     * @return Long destKey 数量
     * @Description 将 key 与 otherKey 的并集,保存到 destKey 中
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long unionAndStoreSet(String key, String otherKey, String destKey) {
        Long result = null;
        try {
            result = redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
        } catch (Exception e) {
            log.error("将 key 与 otherKey 的并集,保存到 destKey 中 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param otherKeys key destKey
     * @return Long destKey 数量
     * @Description 将 key 与 otherKey 的并集,保存到 destKey 中
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long unionAndStoreSet(String key, Collection<String> otherKeys, String destKey) {
        Long result = null;
        try {
            result = redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
        } catch (Exception e) {
            log.error("将 key 与 otherKey 的并集,保存到 destKey 中 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key
     * @return Set<Object> 返回集合
     * @Description 返回集合中所有元素
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> members(String key) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("返回集合中所有元素 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }


    //==Zset 根据 socre 排序   不重复 每个元素附加一个 socre  double类型的属性(double 可以重复)==

    /**
     * @param key value score
     * @return boolean
     * @Description 添加 ZSet 元素
     * @author lz
     * @date 2018/10/8 13:52
     */
    public boolean add(String key, Object value, double score) {
        boolean result = false;
        try {
            result = redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            log.error("添加 ZSet 元素 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key value score
     * @return Long 返回集合
     * @Description Zset  批量添加 
     * Set<TypedTuple<Object>> tuples = new HashSet<>();
     * TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>("zset-5",9.6);
     * tuples.add(objectTypedTuple1);
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long batchAddZset(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().add(key, tuples);
        } catch (Exception e) {
            log.error("Zset  批量添加 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key value
     * @return Long
     * @Description 删除一个或多个元素
     * @author lz
     * @date 2018/10/8 13:52
     */
    @SuppressWarnings("all")
    public Long removeZset(String key, Object... values) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().remove(key, values);
        } catch (Exception e) {
            log.error("Zset 删除一个或多个元素 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key value score
     * @return Long
     * @Description 对指定的 zset 的 value 值 , socre 属性做增减操作
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Double incrementScore(String key, Object value, double score) {
        Double result = null;
        try {
            result = redisTemplate.opsForZSet().incrementScore(key, value, score);
        } catch (Exception e) {
            log.error("对指定的 zset 的 value 值 , socre 属性做增减操作 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key value
     * @return Long
     * @Description 获取 key 中指定 value 的排名(从0开始,从小到大排序)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long rank(String key, Object value) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().rank(key, value);
        } catch (Exception e) {
            log.error("获取 key 中指定 value 的排名(从0开始,从小到大排序) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key value
     * @return Long
     * @Description 获取 key 中指定 value 的排名(从0开始,从大到小排序)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long reverseRank(String key, Object value) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().reverseRank(key, value);
        } catch (Exception e) {
            log.error("获取 key 中指定 value 的排名(从0开始,从大到小排序) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key value
     * @return Long
     * @Description 获取索引区间内的排序结果集合(从0开始, 从小到大, 带上分数)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long end) {
        Set<ZSetOperations.TypedTuple<Object>> result = null;
        try {
            result = redisTemplate.opsForZSet().rangeWithScores(key, start, end);
        } catch (Exception e) {
            log.error("获取索引区间内的排序结果集合(从0开始,从小到大,带上分数) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key start end
     * @return Set<Object>
     * @Description 获取索引区间内的排序结果集合(从0开始, 从小到大, 只有列名)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> range(String key, long start, long end) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error("获取索引区间内的排序结果集合(从0开始,从小到大,带上分数) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key min max
     * @return Set<Object>
     * @Description 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,只有列名)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> rangeByScore(String key, double min, double max) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("获取分数范围内的 [min,max] 的排序结果集合 (从小到大,只有列名) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key min max
     * @return Set<ZSetOperations.TypedTuple   <   Object>>
     * @Description 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,集合带分数)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max) {
        Set<ZSetOperations.TypedTuple<Object>> result = null;
        try {
            result = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            log.error("获取分数范围内的 [min,max] 的排序结果集合 (从小到大,集合带分数) 失败！错误信息为：" + e.getMessage());
        }
        return result;

    }

    /**
     * @param key min max offset 从指定下标开始 count 输出指定元素数量
     * @return Set<Object>
     * @Description 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,不带分数的集合)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> rangeByScore(String key, double min, double max, long offset, long count) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            log.error("返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,不带分数的集合) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key min max offset 从指定下标开始 count 输出指定元素数量
     * @return Set<ZSetOperations.TypedTuple   <   Object>>
     * @Description 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,带分数的集合)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        Set<ZSetOperations.TypedTuple<Object>> result = null;
        try {
            result = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
        } catch (Exception e) {
            log.error("返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,带分数的集合) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key start end
     * @return Set<Object>
     * @Description 获取索引区间内的排序结果集合(从0开始, 从大到小, 只有列名)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> reverseRange(String key, long start, long end) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForZSet().reverseRange(key, start, end);
        } catch (Exception e) {
            log.error("获取索引区间内的排序结果集合(从0开始,从大到小,只有列名) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key min max
     * @return Set<ZSetOperations.TypedTuple   <   Object>>
     * @Description 获获取索引区间内的排序结果集合(从0开始, 从大到小, 带上分数)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end) {
        Set<ZSetOperations.TypedTuple<Object>> result = null;
        try {
            result = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        } catch (Exception e) {
            log.error("获取索引区间内的排序结果集合(从0开始,从大到小,带上分数) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key min max
     * @return Set<Object>
     * @Description 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合不带分数)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> reverseRangeByScore(String key, double min, double max) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("获取索引区间内的排序结果集合(从0开始,从大到小,只有列名) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key min max
     * @return Set<ZSetOperations.TypedTuple   <   Object>>
     * @Description 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合带分数)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max) {
        Set<ZSetOperations.TypedTuple<Object>> result = null;
        try {
            result = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            log.error("获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合带分数) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key min max offset 从指定下标开始 count 输出指定元素数量
     * @return Set<Object>
     * @Description 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,不带分数的集合)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<Object> reverseRangeByScore(String key, double min, double max, long offset, long count) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            log.error("返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,不带分数的集合) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key min max offset 从指定下标开始 count 输出指定元素数量
     * @return Set<ZSetOperations.TypedTuple   <   Object>>
     * @Description 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,带分数的集合)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        Set<ZSetOperations.TypedTuple<Object>> result = null;
        try {
            result = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
        } catch (Exception e) {
            log.error("获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合带分数) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key min max
     * @return Long 指定分数区间元素个数
     * @Description 返回指定分数区间 [min,max] 的元素个数
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long countZSet(String key, double min, double max) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().count(key, min, max);
        } catch (Exception e) {
            log.error("返回指定分数区间 [min,max] 的元素个数 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key min max
     * @return Long zset 集合数量
     * @Description 返回 zset 集合数量
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long sizeZset(String key) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            log.error("返回 zset 集合数量 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key value
     * @return Double zset 指定成员的 score 值
     * @Description 获取指定成员的 score 值
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Double score(String key, Object value) {
        Double result = null;
        try {
            result = redisTemplate.opsForZSet().score(key, value);
        } catch (Exception e) {
            log.error("取指定成员的 score 值 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key start end
     * @return Long
     * @Description 删除指定索引位置的成员, 其中成员分数按(从小到大)
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long removeRange(String key, long start, long end) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().removeRange(key, start, end);
        } catch (Exception e) {
            log.error("删除指定索引位置的成员,其中成员分数按( 从小到大 ) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key start end
     * @return Long
     * @Description 删除指定 分数范围 内的成员 [main,max],其中成员分数按( 从小到大 )
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long removeRangeByScore(String key, double min, double max) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("删除指定 分数范围 内的成员 [main,max],其中成员分数按( 从小到大 ) 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key otherKey destKey
     * @return Long
     * @Description key 和 other 两个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long unionAndStoreZset(String key, String otherKey, String destKey) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
        } catch (Exception e) {
            log.error("key 和 other 两个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key otherKey destKey
     * @return Long
     * @Description key 和 otherKeys 多个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long unionAndStoreZset(String key, Collection<String> otherKeys, String destKey) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
        } catch (Exception e) {
            log.error("key 和 otherKeys 多个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key otherKey destKey
     * @return Long
     * @Description key 和 otherKey 两个集合的交集,保存在 destKey 集合中
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long intersectAndStore(String key, String otherKey, String destKey) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
        } catch (Exception e) {
            log.error("key 和 otherKey 两个集合的交集,保存在 destKey 集合 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param key otherKeys destKey
     * @return Long
     * @Description key 和 otherKeys 多个集合的交集,保存在 destKey 集合中
     * @author lz
     * @date 2018/10/8 13:52
     */
    public Long intersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
        } catch (Exception e) {
            log.error("key 和 otherKeys 多个集合的交集,保存在 destKey 集合中 失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

}
