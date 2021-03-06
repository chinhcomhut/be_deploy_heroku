package com.codegym.wbdlaptop.service;

import com.codegym.wbdlaptop.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ISongService {
    Boolean existsByNameSong(String nameSong);
    Page<Song> findAllByUserId(Long userId, Pageable pageable);
    List<Song> findByNameSongContaining(String nameSong);
    Page<Song> findByNameCategoryContaining(String nameCategory, Pageable pageable);
    Page<Song> findByNameSingerContaining(String nameSinger, Pageable pageable);
    Page<Song> findByNameBandContaining(String nameBand, Pageable pageable);
    Optional<Song> findById(Long id);
    void delete(Long id);
    Song save(Song song);
    Page<Song> findAll(Pageable pageable);
}
