package com.javadiscordproject.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void insertTest(String testName) {
        String sql = "INSERT INTO test (testName) VALUES (?)";
        jdbcTemplate.update(sql, testName);
    }
}