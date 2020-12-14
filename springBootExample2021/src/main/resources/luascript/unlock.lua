---
--- unlock
--- 加value判断 也就是ARGV[1]判断，是为了确保锁是否是当前线程持有，是当前线程持有，则可删除
--- Created by martin.
--- DateTime: 2020-12-11 16:22
---
--获取KEY
local key1 = KEYS[1]
local tmpKeys = redis.call('get', key1)
redis.log(redis.LOG_DEBUG, "unlock---1")
redis.log(redis.LOG_DEBUG, tmpKeys)
redis.log(redis.LOG_DEBUG, key1)
if (redis.call('get', key1) == ARGV[1])
then
    redis.log(redis.LOG_DEBUG, "unlock---2")
    return redis.call('del', key1)
else return 0
end