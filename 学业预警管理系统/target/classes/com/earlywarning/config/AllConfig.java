package com.earlywarning.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.earlywarning.mybatis.MyBatisInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class AllConfig extends WebMvcConfigurerAdapter {

    /**
     * web登录验证相关配置
     */
    public final static String SESSION_KEY = "user";
    private static final Logger log = LoggerFactory.getLogger(AllConfig.class);
    @Autowired
    private Environment env;

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    /**
     * 创建数据源(数据源的名称：方法名可以取为XXXDataSource(),XXX为数据库名称,该名称也就是数据源的名称)
     */
    @Bean
    public DataSource pyrxDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("jdbc.driverClassName"));
        props.put("url", env.getProperty("jdbc.url"));
        props.put("username", env.getProperty("jdbc.username"));
        props.put("password", env.getProperty("jdbc.password"));
        return DruidDataSourceFactory.createDataSource(props);
    }

    /**
     * 添加拦截器
     */
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry
                .addInterceptor(getSecurityInterceptor());
        // 排除配置

        addInterceptor.excludePathPatterns("/**/**");
        addInterceptor.excludePathPatterns("/doc");
        addInterceptor.excludePathPatterns("/v2/**");
        addInterceptor.excludePathPatterns("/swagger-resources/**");

        // 拦截配置
        addInterceptor.addPathPatterns("/**");
        // 权限隔离
        log.debug("此处添加拦截器, Add interceptors here.");
    }

    /**
     * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
     * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource(
            @Qualifier("pyrxDataSource") DataSource myTestDbDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DatabaseType.MASTER, myTestDbDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        dataSource.setDefaultTargetDataSource(myTestDbDataSource);// 默认的datasource设置为myTestDbDataSource

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("pyrxDataSource") DataSource myTestDbDataSource)
            throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(this.dataSource(myTestDbDataSource));
        fb.setTypeAliasesPackage(env
                .getProperty("mybatis.type-aliases-package"));

        // 添加插件
        fb.setPlugins(new Interceptor[]{new MyBatisInterceptor()});

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        // 配置自动将下划线转换至驼峰表示法
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCallSettersOnNulls(true);
        try {
            fb.setConfiguration(configuration);
            fb.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResources(env.getProperty("mybatis.mapper-locations")));
            return fb.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(
            DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request,
                                 HttpServletResponse response, Object handler) throws Exception {
            HttpSession session = request.getSession();
            if (session.getAttribute(SESSION_KEY) != null) {
                return true;
            }
            // 跳转登录
            String url = request.getContextPath() + "/login";
            response.sendRedirect(url);
            return false;
        }

    }

}