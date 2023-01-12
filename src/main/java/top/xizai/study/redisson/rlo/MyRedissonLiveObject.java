package top.xizai.study.redisson.rlo;

import lombok.Getter;
import lombok.Setter;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;
import org.redisson.api.annotation.RIndex;

import java.util.List;
import java.util.Set;

/**
 * @author: WSC
 * @DATE: 2023/1/12
 * @DESCRIBE: rlo = Redisson Live Object
 **/
@Getter
@Setter
@REntity
public class MyRedissonLiveObject {
    @RId
    private String id;

    /**
     * 服务名称
     */
    @RIndex
    private String serviceName;

    /**
     * IP地址
     */
    private Set<String> services;
}
