---
--- 如该key不存在，则保存并设置过期时间
---
--- Created by martin.
--- DateTime: 2020-12-11 9:22
---
if redis.call('get', KEYS[1]) == false
then
    redis.call('set', KEYS[1], ARGV[1]) redis.call('expire', KEYS[1], tonumber(ARGV[2]))
    return 1
else
    return 0
end