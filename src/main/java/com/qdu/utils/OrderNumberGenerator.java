package com.qdu.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@Service
public class OrderNumberGenerator {

    private final RedisTemplate<String, String> redisTemplate;

    public OrderNumberGenerator(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateTrainOrderNo(Long userId, String trainNumber, Date departureDate) {
        // 1. 日期部分（6位）
        String datePart = new SimpleDateFormat("yyMMdd").format(departureDate);

        // 2. 用户ID后四位（4位）
        String userIdPart = String.format("%04d", userId % 10000);

        // 3. 车次编号哈希（4位）
        String trainPart = getTrainNumberHash(trainNumber);

        // 4. Redis自增序列（2位）
        String seqKey = String.format("TRAIN_ORDER:%s:%s:%s", datePart, userIdPart, trainPart);
        Long sequence = redisTemplate.opsForValue().increment(seqKey);
        if (sequence == null || sequence == 1L) {
            redisTemplate.expire(seqKey, Duration.ofHours(48));
        }
        String seqPart = String.format("%02d", sequence % 100);

        return datePart + userIdPart + trainPart + seqPart;
    }

    private String getTrainNumberHash(String trainNumber) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(trainNumber.getBytes(StandardCharsets.UTF_8));
            int hashValue = ((hash[0] & 0xFF) << 8) | (hash[1] & 0xFF);
            return String.format("%04d", hashValue % 10000);
        } catch (Exception e) {
            return "0000";
        }
    }
}