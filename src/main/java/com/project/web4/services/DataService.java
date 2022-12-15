package com.project.web4.services;

import com.project.web4.model.Data;
import com.project.web4.model.User;
import com.project.web4.repositories.DataRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DataService {
    @Autowired
    private final DataRepository dataRepository;

    @Transactional
    public void saveData(Data data) {
        dataRepository.save(data);
    }

    @Transactional
    public void deleteData(User user) {
        dataRepository.deleteAllByUser(user);
    }

    @Transactional
    public List<Data> getData(User user) {
        return dataRepository.getAllByUser(user);
    }
}
