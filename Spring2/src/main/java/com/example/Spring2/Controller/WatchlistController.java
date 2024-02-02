package com.example.Spring2.Controller;


import com.example.Spring2.entities.Watchlist;

import com.example.Spring2.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="controller/watchlist")
public class WatchlistController {

    private WatchlistService watchlistService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping
    public List<Watchlist> getWatchlist(){
        return watchlistService.getWatchlist();
    }

    @PostMapping("add")
    public void addWatchlist(@RequestBody Watchlist watchlist){
        watchlistService.addWatchlist(watchlist);
    }

    @GetMapping("email")
    public List<Watchlist> getwatchlistbyEmail(@PathVariable String email){
        return watchlistService.getWatchlistbyEmail(email);
    }

    @PostMapping("delete")
    public void deleteWatchlistLine(@RequestBody Watchlist watchlist){
        watchlistService.deleteWatchlistLine(watchlist);
    }
}
