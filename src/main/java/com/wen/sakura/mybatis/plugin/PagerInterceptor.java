package com.wen.sakura.mybatis.plugin;

import com.wen.sakura.mybatis.IPage;
import com.wen.sakura.mybatis.IPager;
import com.wen.sakura.mybatis.PagerCountSupport;
import com.wen.sakura.mybatis.SoftMethodCache;
import com.wen.sakura.util.Helper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huwenwen
 * @date 2019/9/27
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class})})
public class PagerInterceptor implements Interceptor {

    protected Field additionalParametersField;

    @Override
    public Object plugin(Object target) {
        try {
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Mybatis BoundSql版本问题，不存在additionalParameters", e);
        }
        return Plugin.wrap(target, this);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        String id = ms.getId();
        SoftMethodCache.MethodSignature signature = SoftMethodCache.getSignature(id);
        if (signature == null) {
            return invocation.proceed();
        }
        // 返回类型必须是 IPage 类型
        if (support(ms)) {
            // 入参必须有一个是 IPager 类型
            IPager pager = getPager(signature.getParameterTypes());
            if (pager == null) {
                return invocation.proceed();
            }
            Long total = count(invocation);
            List<?> result = new ArrayList<>();
            if (total > 0) {
                result = getList(invocation, pager, signature);
            }
            return wrapPage(signature, pager, total, result);
        }
        return invocation.proceed();
    }

    private List<?> getList(Invocation invocation, IPager pager, SoftMethodCache.MethodSignature signature) throws Throwable {
        int limit = pager.getLimit();
        int index = pager.getIndex();
        index = index < 1 ? 0 : index;
        Executor executor = (Executor) invocation.getTarget();
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object params = invocation.getArgs()[1];
        String sql = getLimitSql(ms.getBoundSql(params).getSql().trim(), pager.getOrderBy(), index, limit);
        ResultHandler<?> rhandler = (ResultHandler<?>) invocation.getArgs()[3];
        BoundSql newSql = newBoundSql(sql, invocation);
        CacheKey cacheKey = executor.createCacheKey(ms, params, RowBounds.DEFAULT, newSql);
        ResultType type = signature.annotationOf(ResultType.class);
        if (null != type) {
            Class<?> rt = type.value();
            ms = newLimitMappedStatement(ms, rt);
        }
        return executor.query(ms, params, RowBounds.DEFAULT, rhandler, cacheKey, newSql);
    }

    private Long count(Invocation invocation) throws Throwable {
        Executor executor = (Executor) invocation.getTarget();
        MappedStatement originms = (MappedStatement) invocation.getArgs()[0];
        Object params = invocation.getArgs()[1];
        ResultHandler<?> resultHandler = (ResultHandler<?>) invocation.getArgs()[3];

        String origin = originms.getBoundSql(params).getSql().trim();
        String cql = PagerCountSupport.dealCount(origin);

        BoundSql newSql = newBoundSql(cql, invocation);

        MappedStatement countms = newCountMappedStatement(originms);
        CacheKey countKey = executor.createCacheKey(countms, params, RowBounds.DEFAULT, newSql);

        Object cresult = executor.query(countms, params, RowBounds.DEFAULT, resultHandler, countKey, newSql);
        Long count = ((List<Long>) cresult).get(0);
        return count;
    }

    private BoundSql newBoundSql(String sql, Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object params = invocation.getArgs()[1];
        BoundSql bql = ms.getBoundSql(params);
        Map<String, Object> additionalParameters = (Map<String, Object>) additionalParametersField.get(bql);
        BoundSql newBound = new BoundSql(ms.getConfiguration(), sql, bql.getParameterMappings(), params);
        for (String key : additionalParameters.keySet()) {
            newBound.setAdditionalParameter(key, additionalParameters.get(key));
        }
        return newBound;
    }

    public static MappedStatement newCountMappedStatement(MappedStatement ms) {
        return newMappedStatement(ms, ms.getId() + "Count_Interceptor_Generate_Ignore", Long.class);
    }

    public static MappedStatement newLimitMappedStatement(MappedStatement ms, Class<?> resultType) {
        return newMappedStatement(ms, ms.getId() + "Limit_Interceptor_Generate_Ignore", resultType);
    }

    public static MappedStatement newMappedStatement(MappedStatement ms, String id, Class<?> resultType) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), id, ms.getSqlSource(),
                ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (null != ms.getKeyProperties() && ms.getKeyProperties().length > 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        List<ResultMap> resultMaps = new ArrayList<>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), resultType,
                new ArrayList<>(0)).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    private String getLimitSql(String originSql, String orderBy, int index, int limit) {
        String temp = originSql.toLowerCase();
        StringBuilder my = new StringBuilder(originSql);
        int lix = findKey(temp, "limit", -1);
        int wix = findKey(temp, "where", -1);
        int oix = findKey(temp, "order", -1);
        int bix = findKey(temp, "by", -1);
        boolean limited = lix > 5 &&
                wix > -1 &&
                lix > wix &&
                Character.isWhitespace(temp.charAt(lix - 1)) &&
                Character.isWhitespace(temp.charAt(lix + 5));
        boolean ordered = oix > 5 &&
                bix > 5 &&
                bix > oix &&
                Helper.isEmpty(temp.substring(oix + 5, bix)) &&
                Character.isWhitespace(temp.charAt(oix - 1)) &&
                Character.isWhitespace(temp.charAt(bix + 2));
        if (!limited) {
            if (!ordered && Helper.isNotEmpty(orderBy)) {
                my.append(" order by ").append(orderBy);
            }
            my.append(" limit ").append(index * limit).append(",").append(limit);
        }
        return my.toString();
    }

    private int findKey(String body, String key, int start) {
        int lb = body.length();
        int lk = key.length();
        int index = body.indexOf(key, start);
        if (index <= 5 || index >= lb - lk) {
            return -1;
        }
        boolean lw = Character.isWhitespace(body.charAt(index - 1));
        boolean rw = Character.isWhitespace(body.charAt(index + lk));
        if (lw && rw) {
            return index;
        }
        return findKey(body, key, index + lk);
    }

    private IPager getPager(Class<?>[] parameterTypes) {
        if (parameterTypes == null) {
            return null;
        }
        for (Class<?> parameterType : parameterTypes) {
            if (IPager.class.isAssignableFrom(parameterType)) {
                try {
                    return (IPager) parameterType.newInstance();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return null;
    }

    private <T> List<T> wrapPage(SoftMethodCache.MethodSignature signature, IPager local, Long total, Object result) {
        Class<?> reclass = signature.getReturnType();
        try {
            IPage page = (IPage) reclass.newInstance();
            page.setIndex(local.getIndex());
            page.setLimit(local.getLimit());
            page.setOrderBy(local.getOrderBy());
            page.setTotal(new BigDecimal(total).intValue());
            page.setData(result);
            List<T> localarray = new ArrayList<>(1);
            localarray.add((T) page);
            return localarray;
        } catch (Exception e) {
            throw new RuntimeException("Call constructor of IPage implement class fail.", e);
        }
    }

    protected boolean support(MappedStatement ms) {
        SoftMethodCache.MethodSignature signature = SoftMethodCache.getSignature(ms.getId());
        if (signature == null) {
            return false;
        }
        Class<?> returnType = signature.getReturnType();
        return IPage.class.isAssignableFrom(returnType);
    }
}
