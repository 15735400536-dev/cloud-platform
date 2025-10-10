### 使用步骤
#### 1.添加cloud-platform-redis依赖
#### 2.增加nacos配置
```yaml
spring:
  # Redis配置（缓存中间件）
  redis:
    host: localhost
    port: 6379
    password:  # 无密码则留空
    database: 0  # 缓存数据库索引（建议与业务库区分）
    timeout: 3000ms  # 连接超时时间

  # 缓存配置（Spring Cache）
  cache:
    type: redis  # 指定缓存类型为Redis
    redis:
      time-to-live: 3600000ms  # 缓存默认过期时间（1小时）
      use-key-prefix: true     # 启用key前缀（避免不同缓存名称冲突）
      key-prefix: "cache:"     # 全局key前缀（最终key格式：cache:user:1001）
      cache-null-values: false # 不缓存null值（防止缓存穿透）
```
#### 2.启动类添加@EnableCaching  // 开启Spring Cache注解支持
#### 3.方法上添加注解
````java
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    // 1. 根据ID查询用户（缓存：查缓存→无则查库→存缓存）
    @Cacheable(value = "user", key = "#id", unless = "#result == null")
    public User getById(Long id) {
        // 若缓存无数据，则调用MyBatis-Plus的selectById查询数据库
        return baseMapper.selectById(id);
    }

    // 2. 查询所有用户（缓存：key固定为"all"）
    @Cacheable(value = "user", key = "'all'")
    public List<User> getAll() {
        return baseMapper.selectList(null);  // 查所有用户
    }

    // 3. 新增用户（缓存：新增后将结果存入缓存）
    @CachePut(value = "user", key = "#result.id")  // key为新增用户的id
    public User add(User user) {
        baseMapper.insert(user);  // 数据库新增（MyBatis-Plus会回填id）
        return user;  // 返回新增用户（含id），用于缓存
    }

    // 4. 更新用户（缓存：更新后覆盖旧缓存）
    @CachePut(value = "user", key = "#user.id")  // key为用户id
    public User update(User user) {
        baseMapper.updateById(user);  // 数据库更新
        // 建议返回更新后的最新数据（避免入参缺失字段导致缓存不完整）
        return baseMapper.selectById(user.getId());
    }

    // 5. 根据ID删除用户（缓存：删除对应缓存）
    @CacheEvict(value = "user", key = "#id")  // 清除key为id的缓存
    public void deleteById(Long id) {
        baseMapper.deleteById(id);  // 数据库删除
    }

    // 6. 批量删除用户（缓存：清除所有用户缓存）
    @CacheEvict(value = "user", allEntries = true)  // 清除"user"缓存下的所有数据
    public void deleteBatch(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);  // 数据库批量删除
    }
}
````