package com.project.web4.repositories;

import com.project.web4.model.Data;
import com.project.web4.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataRepository extends JpaRepository<Data, Integer> {
    void deleteAllByUser(User user);
    List<Data> getAllByUser(User user);
}
