---
--- 如该key不存在，则保存并设置过期时间
---
--- Created by martin.
--- DateTime: 2020-12-11 15:22
---
local key1 = KEYS[1]
local value1 = ARGV[1]
redis.log(redis.LOG_DEBUG, "lock--start")
redis.log(redis.LOG_DEBUG, key1)
redis.log(redis.LOG_DEBUG, value1)
redis.log(redis.LOG_DEBUG, "lock--end")
if (redis.call('setnx', key1, value1) < 1)
then
    return 0;

else

    redis.call('pexpire', key1, tonumber(ARGV[2]))
    return 1
end