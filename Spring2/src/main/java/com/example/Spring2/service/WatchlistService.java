package com.example.Spring2.service;



import com.example.Spring2.entities.Watchlist;
import com.example.Spring2.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WatchlistService {


    private WatchlistRepository watchlistRepository;

    @Autowired
    public WatchlistService(WatchlistRepository watchlistRepository){
        this.watchlistRepository=watchlistRepository;
    }

    public List<Watchlist> getWatchlist(){
        return watchlistRepository.findAll();
    }

    public void addWatchlist(Watchlist watchlist){
        watchlistRepository.save(watchlist);
    }

    public List<Watchlist> getWatchlistbyEmail(String email){
        List<Watchlist> alle=watchlistRepository.findAll();
        List<Watchlist>gesucht=new ArrayList<>();
        for(Watchlist wl:alle){
            if(wl.getEmailNutzer().equals(email)){
                gesucht.add(wl);
            }
        }
        return gesucht;
    }

    public void deleteWatchlistLine(Watchlist watchlist) {
        if (watchlistRepository.findById(watchlist.getWatchlistId()).isEmpty()) {
            System.out.println("Watchlisteintrag existiert nicht.");
        } else {
            watchlistRepository.deleteById(watchlist.getWatchlistId());
            System.out.println("Watchlisteintrag wurde gelöscht!");
        }
    }

}
