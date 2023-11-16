package com.sba.recordingserver.repository;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RidingSpeedMemoryRepository {
    private static RidingSpeedMemoryRepository instance = null;

    Map<String, List<Double>> memoryRepository = new HashMap<>();

    public static RidingSpeedMemoryRepository getInstance() {
        if (instance == null) {
            instance = new RidingSpeedMemoryRepository();
            System.out.println("created MemoryRepository instance");
        }
        return instance;
    }

    public void save(String id, Double speed) {
        if (memoryRepository.containsKey(id) == true)
        {
            memoryRepository.get(id).add(speed);
        }
        else {
            List<Double> list = new ArrayList();
            list.add(speed);
            memoryRepository.put(id,list);
        }
    }
    public void remove(String id) {
        memoryRepository.remove(id);
    }
    public String findById(String id) {
        if(!memoryRepository.containsKey(id))
            return null;
        List<Double> list = memoryRepository.get(id);
        String json = new Gson().toJson(list);
        memoryRepository.remove(id);
        return json;
    }
}

