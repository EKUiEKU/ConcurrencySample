package top.xizai.study.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author: WSC
 * @DATE: 2022/12/19
 * @DESCRIBE: 看门狗 接口计时器 性能测试工具
 **/
public class WatchDog {
    private static ThreadLocal<Map<String, StringBuffer>> recordLogMap = new ThreadLocal<>();
    private static ThreadLocal<Map<String, Stack<Long>>> recordTimeMap = new ThreadLocal<>();

    /**
     * 开始
     */
    public static void start(){
        try {
            append("=========开始计时=========");
            pushLatelyCurrentTime();
        } catch (Exception e) {}
    }

    /**
     * 结束
     */
    public static void stop() {
        try {
            long end = System.currentTimeMillis();
            append("一共耗时：" + (end - getFirstRecordTime()) + "ms");
            append("=========结束计时=========");
            // 测试打印
            System.out.println(getResult());
            // 释放内存
            release();
        } catch (Exception e) {}
    }

    /**
     * 下一个计时
     * @param tag
     */
    public static void next(String tag) {
        try {
            Long now = System.currentTimeMillis();
            Long latelyTime = getLatelyTime();
            Long spend = now - latelyTime;
            append("tag：" + tag);
            append("计时结束,距离上一轮计时耗时：" + spend + "ms");
            append("=========第" + getRecordSize() +"轮计时结束=========");
            pushLatelyCurrentTime();
        } catch (Exception e) {}
    }

    public static String getResult() {
        try {
            String result = getCurrentLog().toString();
            return result;
        } catch (Exception e) {}

        return "";
    }

    public static StringBuffer getCurrentLog() {
        String callerReference = getCallerReference();
        Map<String, StringBuffer> map = recordLogMap.get();
        if (ObjectUtil.isEmpty(map)) {
            map = new HashMap<>();
            recordLogMap.set(map);
        }

        StringBuffer recordLog = map.get(callerReference);
        if (ObjectUtil.isEmpty(recordLog)) {
            recordLog = new StringBuffer();
            map.put(callerReference, recordLog);
        }

        return recordLog;
    }

    public static Long getLatelyTime() {
        String callerReference = getCallerReference();
        Map<String, Stack<Long>> map = recordTimeMap.get();
        if (ObjectUtil.isEmpty(map)) {
            map = new HashMap<>();
            recordTimeMap.set(map);
        }

        Stack<Long> timeStack = map.get(callerReference);
        if (ObjectUtil.isEmpty(timeStack)) {
            timeStack = new Stack<>();
            map.put(callerReference, timeStack);
        }

        return timeStack.size() != 0 ? timeStack.peek() : 0L;
    }

    public static void pushLatelyCurrentTime() {
        String callerReference = getCallerReference();
        Map<String, Stack<Long>> map = recordTimeMap.get();
        if (ObjectUtil.isEmpty(map)) {
            map = new HashMap<>();
            recordTimeMap.set(map);
        }

        Stack<Long> timeStack = map.get(callerReference);
        if (ObjectUtil.isEmpty(timeStack)) {
            timeStack = new Stack<>();
            map.put(callerReference, timeStack);
        }

        timeStack.push(System.currentTimeMillis());
    }

    public static Long getFirstRecordTime() {
        String callerReference = getCallerReference();
        Map<String, Stack<Long>> map = recordTimeMap.get();
        if (ObjectUtil.isEmpty(map)) {
            map = new HashMap<>();
            recordTimeMap.set(map);
        }

        Stack<Long> timeStack = map.get(callerReference);
        if (ObjectUtil.isEmpty(timeStack)) {
            timeStack = new Stack<>();
            map.put(callerReference, timeStack);
        }

        if (timeStack.size() == 0) {
            return 0L;
        }

        return timeStack.get(0);
    }


    public static Integer getRecordSize() {
        String callerReference = getCallerReference();
        Map<String, Stack<Long>> map = recordTimeMap.get();
        if (ObjectUtil.isEmpty(map)) {
            map = new HashMap<>();
            recordTimeMap.set(map);
        }

        Stack<Long> timeStack = map.get(callerReference);
        if (ObjectUtil.isEmpty(timeStack)) {
            timeStack = new Stack<>();
            map.put(callerReference, timeStack);
        }

        return timeStack.size();
    }

    /**
     * 获取调用方的位置
     * @return
     */
    public static String getCallerReference() {
        /**
         * 获取当前调用的堆栈信息
         */
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (trace.length < 2) {
            return "";
        }

        StackTraceElement caller = null;
        String s = WatchDog.class.getName();
        for (int i = 1; i < trace.length; i++) {
            caller = trace[i];
            if (!s.equals(caller.getClassName())) {
                break;
            }
        }
        return caller.getClassName() + "#" + caller.getMethodName();
    }

    public static String formatTime(Long time) {
        return DateUtil.format(new Date(time), "yyyy-MM-dd hh:mm:ss");
    }

    public static void append(String msg) {
        StringBuffer log = getCurrentLog();
        log.append("[").append(formatTime(System.currentTimeMillis())).append("] ")
                .append("[").append(getCallerReference()).append("]")
                .append(msg).append("\n");
    }

    public static void release() {
        String reference = getCallerReference();
        Map<String, StringBuffer> logMap = recordLogMap.get();
        if (ObjectUtil.isNotEmpty(logMap) && logMap.containsKey(reference)) {
            logMap.remove(reference);
        }

        Map<String, Stack<Long>> stackMap = recordTimeMap.get();
        if (ObjectUtil.isNotEmpty(stackMap) && stackMap.containsKey(reference)) {
            stackMap.remove(reference);
        }

        if (logMap.size() == 0) {
            recordLogMap.remove();
        }

        if (stackMap.size() == 0) {
            recordTimeMap.remove();
        }
    }
}
