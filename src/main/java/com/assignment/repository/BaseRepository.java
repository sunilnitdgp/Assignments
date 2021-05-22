package com.assignment.repository;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Log4j2
public class BaseRepository implements DbFields{
    @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public <T> T find(String query, Class<T> returnType, Map<String, Object> data) {
        T result = null;
        try {
            if (String.class.equals(returnType) || Date.class.equals(returnType)
                    || Number.class.isAssignableFrom(returnType))
                result = namedParameterJdbcTemplate.queryForObject(query, data, returnType);
            else
                result = find(query, data, new BeanPropertyRowMapper<T>(returnType));
        } catch (EmptyResultDataAccessException e) {
            log.debug("Record doesn't exist for the criteria given. Expected - {}, actual - {}", e.getExpectedSize(),
                    e.getActualSize());
        }
        return result;
    }

    public <T> T find(String query, Map<String, Object> data, RowMapper<T> rowMapper) {
        T result = null;
        try {
            result = namedParameterJdbcTemplate.queryForObject(query, data, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Record doesn't exist for the criteria given. Expected - {}, actual - {}", e.getExpectedSize(),
                    e.getActualSize());
        }
        return result;
    }

    public long saveAndGetPrimaryKey(final String query, SqlParameterSource input, String[] id) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        int result = namedParameterJdbcTemplate.update(query, input, holder, id);
        if (result != 1) { // Failed
            log.error("saveAndGetPrimaryKey and failed with result " + result);
        }
        long pk = holder.getKey() != null ? holder.getKey().longValue() : 0;
        log.debug("Record saved successfully with PK {}", pk);
        return pk;
    }

    public Long saveAndGetPrimaryKey(String sql, Map<String, Object> input, String[] id) {
        MapSqlParameterSource inputParam = new MapSqlParameterSource();
        for (Map.Entry<String, Object> pair : input.entrySet()) {
            inputParam.addValue(pair.getKey(), pair.getValue());
        }
        return saveAndGetPrimaryKey(sql, inputParam, id);
    }

    public <T> List<T> findAll(final String query, final Class<T> returnType, final Map<String, Object> paramMap) {
        List<T> result;
        if (String.class.equals(returnType) || Number.class.isAssignableFrom(returnType))
            result = namedParameterJdbcTemplate.queryForList(query, paramMap, returnType);
        else
            result = namedParameterJdbcTemplate.query(query, paramMap, new BeanPropertyRowMapper<T>(returnType));

        log.debug("Number of Records found {}", result.size());
        return result;
    }

    @SuppressWarnings("unchecked")
    public int[] batchUpdate(String sql, List<Map<String, ?>> batchValues) {
        return namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[batchValues.size()]));
    }

    public int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
        return namedParameterJdbcTemplate.batchUpdate(sql, batchValues);
    }
}
