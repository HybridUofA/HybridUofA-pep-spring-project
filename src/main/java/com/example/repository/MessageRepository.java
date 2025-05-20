package com.example.repository;

import com.example.entity.Message;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    public Optional<Message> getById(int id);
    public List<Message> getByPostedBy(int id);
}
