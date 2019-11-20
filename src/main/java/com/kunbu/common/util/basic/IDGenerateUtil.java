package com.kunbu.common.util.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局唯一ID（有序）
 * <br/> 1. 数据库自增ID（难以扩展，非分库分表可以使用。解决：每个库设置不同的auto_increment初始值，以及相同增长步长）
 * <br/> 2. 第三方ID，如redis和zookeeper（实现复杂，依赖严重）
 * <br/> 3. UUID（体积过大，查询效率低）
 * <br/> 4. Snowflake雪花算法，标准时64bit（即long），普通使用53bit（兼容前端js的最大整形53位，超出会丢失精度）
 * <p>
 * <br/>
 * https://blog.csdn.net/a724888/article/details/80784533
 * https://www.jianshu.com/p/2fd77375927d
 * https://www.liaoxuefeng.com/article/1280526512029729
 *
 * @program: bucks
 * @author: kunbu
 * @create: 2019-08-16 17:17
 **/
public class IDGenerateUtil {

    private static final Logger logger = LoggerFactory.getLogger(IDGenerateUtil.class);

    private static final String SPLITTER_MIDDLE = "-";
    private static final String WHITE_CHAR = "";

    /**
     * UUID
     *
     * @param
     * @return
     * @author kunbu
     * @time 2019/8/23 14:27
     **/
    public static String UUID() {
        return UUID.randomUUID().toString().replace(SPLITTER_MIDDLE, WHITE_CHAR);
    }

    /**
     * 53 bits unique id:
     * <p>
     * |--------|--------|--------|--------|--------|--------|--------|--------|
     * |00000000|00011111|11111111|11111111|11111111|11111111|11111111|11111111|
     * |--------|---xxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxx-----|--------|--------|
     * |--------|--------|--------|--------|--------|---xxxxx|xxxxxxxx|xxx-----|
     * |--------|--------|--------|--------|--------|--------|--------|---xxxxx|
     * <p>
     * Maximum ID = 11111_11111111_11111111_11111111_11111111_11111111_11111111
     * <p>
     * Maximum TS = 11111_11111111_11111111_11111111_111
     * <p>
     * Maximum NT = ----- -------- -------- -------- ---11111_11111111_111 = 65535
     * <p>
     * Maximum SH = ----- -------- -------- -------- -------- -------- ---11111 = 31
     * <p>
     * It can generate 64k unique id per IP and up to 2106-02-07T06:28:15Z.
     */
    public static long snowflakeId() {
        return nextId(System.currentTimeMillis() / 1000);
    }

    private static final Pattern PATTERN_LONG_ID = Pattern.compile("^([0-9]{15})([0-9a-f]{32})([0-9a-f]{3})$");

    private static final Pattern PATTERN_HOSTNAME = Pattern.compile("^.*\\D+([0-9]+)$");

    private static final long OFFSET = LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.of("Z")).toEpochSecond();

    private static final long MAX_NEXT = 0b11111_11111111_111L;

    private static final long SHARD_ID = getServerIdAsLong();

    private static long offset = 0;

    private static long lastEpoch = 0;

    private static synchronized long nextId(long epochSecond) {
        if (epochSecond < lastEpoch) {
            // warning: clock is turn back:
            logger.warn("clock is back: " + epochSecond + " from previous:" + lastEpoch);
            epochSecond = lastEpoch;
        }
        if (lastEpoch != epochSecond) {
            lastEpoch = epochSecond;
            reset();
        }
        offset++;
        long next = offset & MAX_NEXT;
        if (next == 0) {
            logger.warn("maximum id reached in 1 second in epoch: " + epochSecond);
            return nextId(epochSecond + 1);
        }
        return generateId(epochSecond, next, SHARD_ID);
    }

    private static void reset() {
        offset = 0;
    }

    private static long generateId(long epochSecond, long next, long shardId) {
        return ((epochSecond - OFFSET) << 21) | (next << 5) | shardId;
    }

    private static long getServerIdAsLong() {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            System.out.println(">>> hostname: " + hostname);
            Matcher matcher = PATTERN_HOSTNAME.matcher(hostname);
            if (matcher.matches()) {
                long n = Long.parseLong(matcher.group(1));
                if (n >= 0 && n < 8) {
                    logger.info("detect server id from host name {}: {}.", hostname, n);
                    return n;
                }
            }
        } catch (UnknownHostException e) {
            logger.warn("unable to get host name. set server id = 0.");
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(snowflakeId());
    }

}
