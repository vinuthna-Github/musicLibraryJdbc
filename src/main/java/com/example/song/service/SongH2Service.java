package com.example.song.service;

import com.example.song.repository.SongRepository;
import com.example.song.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.song.model.SongRowMapper ;

import java.util.*;

@Service
public class SongH2Service implements SongRepository{
    @Autowired
    private JdbcTemplate db ;
    @Override
    public ArrayList <Song> getSongs(){
        List<Song>songsList = db.query("select * from playlist ", new SongRowMapper());
        ArrayList <Song> songs = new ArrayList<>(songsList);
        return songs ;
    }
    @Override 
    public Song getSongById(int songId){
        try{
        Song song = db.queryForObject("select * from playlist where songId = ?", new SongRowMapper(),songId);
        return song ;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        
    }

    @Override
    public Song addSong(Song song){
        db.update("INSERT INTO playlist (songName,lyricist,singer,musicDirector) values(?,?,?,?)", song.getSongName(),song.getLyricist(), song.getSinger(),song.getMusicDirector());
        Song savedSong = db.queryForObject("select * from playlist WHERE songName=? and lyricist=? and singer=? and musicDirector=?",new SongRowMapper(),song.getSongName(),song.getLyricist(), song.getSinger(),song.getMusicDirector());
        return savedSong ;
    }
    @Override
    public Song updateSong(int songId,Song song){
        if(song.getSongName()!=null){
            db.update("Update playlist SET songName = ? WHERE songId = ?",song.getSongName(),songId);
        }
        if(song.getLyricist()!=null){
            db.update("Update playlist SET lyricist = ? WHERE songId = ?",song.getLyricist(),songId);
        }
        if(song.getSinger()!=null){
            db.update("Update playlist SET singer = ? WHERE songId = ?",song.getSinger(),songId);
        }
        if(song.getMusicDirector()!=null){
            db.update("Update playlist SET musicDirector = ? WHERE songId = ?",song.getMusicDirector(),songId);
        }
        return getSongById(songId);
        
    }
    @Override
    public void deleteSong(int songId){
        db.update("DELETE FROM playlist WHERE songId = ? ",songId);
    }
}