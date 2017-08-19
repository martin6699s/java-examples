环境配置：
本地环境数据库 建名为java_tools的数据库；


Spring Boot 事务：
关于事务管理器，不管是JPA还是JDBC等都实现自接口PlatformTransactionManager,
如果你添加的是 spring-boot-starter-jdbc 依赖，框架会默认注入 DataSourceTransactionManager 实例。
如果你添加的是 spring-boot-starter-data-jpa 依赖，框架会默认注入 JpaTransactionManager 实例。

而Mybatis 默认依赖了spring-boot-starter-jdbc 所以有注入的是DataSourceTransactionManager

默认是把@Transactional注解在类及类方法上，这是cglib代动态理方式;而注解在接口上是JDK动态代理;
因为在集成shiro时，默认时JDK代理，即代理接口, 所以都是代理接口，
导致注解在类方法上的@Transactional类无法使用 
编译时报'The bean 'RUserService' could not be injected as a 'com.example.transactionTest.service.RUserService' because it is a JDK dynamic proxy that implements:'
此时需要在shiro配置类上加上advisorAutoProxyCreator.setProxyTargetClass(true);

```
    @Bean
    @DependsOn({ "lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

```