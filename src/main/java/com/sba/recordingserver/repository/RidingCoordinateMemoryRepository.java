package com.sba.recordingserver.repository;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RidingCoordinateMemoryRepository {
    private static RidingCoordinateMemoryRepository instance = null;

    Map<String, List<Coordinate>> memoryRepository = new HashMap<>();

    public static RidingCoordinateMemoryRepository getInstance() {
        if (instance == null) {
            instance = new RidingCoordinateMemoryRepository();
            System.out.println("created MemoryRepository instance");
        }
        return instance;
    }

    public void save(String id, Double longitude, Double latitude) {
        if (memoryRepository.containsKey(id) == true)
        {
            memoryRepository.get(id).add(new Coordinate(longitude,latitude));
        }
        else {
            List<Coordinate> list = new ArrayList();
            list.add(new Coordinate(longitude, latitude));
            memoryRepository.put(id,list);
        }
    }
    public void remove(String id) {
        memoryRepository.remove(id);
    }
    public String findById(String id) {
        if(!memoryRepository.containsKey(id))
            return null;
        List<Coordinate> list = memoryRepository.get(id);
        String json = new Gson().toJson(list);
        memoryRepository.remove(id);
        return json;
    }
}

class Coordinate {
    Double longitude;
    Double latitude;
    public Coordinate(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
