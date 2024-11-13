package com.javadiscordprojet.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public TestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void insertTest(String testName) {
        String sql = "INSERT INTO test (testName) VALUES (?)";
        jdbcTemplate.update(sql, testName);
    }
}