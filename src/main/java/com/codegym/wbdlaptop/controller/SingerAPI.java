package com.codegym.wbdlaptop.controller;

import com.codegym.wbdlaptop.message.response.ResponseMessage;
import com.codegym.wbdlaptop.model.Singer;
import com.codegym.wbdlaptop.model.Song;
import com.codegym.wbdlaptop.model.User;
import com.codegym.wbdlaptop.security.service.UserDetailsServiceImpl;
import com.codegym.wbdlaptop.service.Impl.SingerServiceImpl;
import com.codegym.wbdlaptop.service.Impl.SongServiceImpl;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class SingerAPI {
    @Autowired
    private SingerServiceImpl singerService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private SongServiceImpl songService;
    @GetMapping("/singer")
    public ResponseEntity<?> pageSinger(@PageableDefault(sort = "nameSinger", direction = Sort.Direction.ASC)Pageable pageable){
        Page<Singer> singers = singerService.findAll(pageable);
        if(singers.isEmpty()){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(singers, HttpStatus.OK);
    }
    @PostMapping("/singer")
    public ResponseEntity createSinger(@Valid @RequestBody Singer singer){
        if(singer.getNameSinger()==null||singer.getNameSinger()==""){
            return new ResponseEntity(new ResponseMessage("noname"), HttpStatus.OK);
        }
        if(singer.getAvatarSinger()==null||singer.getAvatarSinger()==""){
            return new ResponseEntity(new ResponseMessage("noavatar"), HttpStatus.OK);
        }
        if(singer.getInformation()==null||singer.getInformation()==""){
            return new ResponseEntity(new ResponseMessage("noinformation"),HttpStatus.OK);
        }
        if(singer.getGender()==null||singer.getGender()==""){
            return new ResponseEntity(new ResponseMessage("nogender"), HttpStatus.OK);
        }
        if(singer.getBirthday()==null||singer.getBirthday()==""){
            return new ResponseEntity(new ResponseMessage("nobirthday"), HttpStatus.OK);
        }
        if(singerService.existsByNameSinger(singer.getNameSinger())){
            return new ResponseEntity(new ResponseMessage("nosinger"), HttpStatus.OK);
        }
        singerService.save(singer);
        return new ResponseEntity(new ResponseMessage("yes"), HttpStatus.OK);
    }
    @PutMapping("/singer/{id}")
    public ResponseEntity updateSinger(@PathVariable Long id, @Valid @RequestBody Singer singer){
        Optional<Singer> singer1 = singerService.findById(id);
        if(!singer1.isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(singerService.existsByNameSinger(singer.getNameSinger())){
            return new ResponseEntity(new ResponseMessage("nosinger"), HttpStatus.OK);
        }
        if(singer.getNameSinger()==null||singer.getNameSinger()==""){
            return new ResponseEntity(new ResponseMessage("noname"),HttpStatus.OK);
        }
        singer1.get().setNameSinger(singer.getNameSinger());
        singer1.get().setAvatarSinger(singer.getAvatarSinger());
        singer1.get().setBirthday(singer.getBirthday());
        singer1.get().setGender(singer.getGender());
        singer1.get().setCreateBy(singer.getCreateBy());
        singer1.get().setInformation(singer.getInformation());
        singerService.save(singer1.get());
        return new ResponseEntity(new ResponseMessage("yes"), HttpStatus.OK);
    }
    @GetMapping("/singer/{id}")
    public ResponseEntity getSingerById(@PathVariable Long id){
        Optional<Singer> singer = singerService.findById(id);
        if(!singer.isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(singer, HttpStatus.OK);
    }
    @GetMapping("/singer-by-user")
    public ResponseEntity pageSingerByUser(@PageableDefault(sort = "nameSinger", direction = Sort.Direction.ASC)Pageable pageable){
        User user = userDetailsService.getCurrentUser();
        Page<Singer> singers = singerService.findAllByUserId(user.getId(),pageable);
        if(singers.isEmpty()){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(singers, HttpStatus.OK);
    }
    @GetMapping("/song-by-singer/{id}")
    public ResponseEntity pageSongBySinger(@PathVariable Long id,@PageableDefault(sort = "nameSong", direction = Sort.Direction.ASC)Pageable pageable){
        Optional<Singer> singer = singerService.findById(id);
        Page<Song> songPage = songService.findByNameSingerContaining(singer.get().getNameSinger(),pageable);
        return new ResponseEntity(songPage, HttpStatus.OK);
    }
    @GetMapping("/list-singer")
    public ResponseEntity<?> getListSinger(){
        List<Singer> singerList = singerService.findAllList();
        if(singerList.isEmpty()){
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(singerList, HttpStatus.OK);
    }
}
