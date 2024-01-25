package com.br.blogging.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/paginainicial")
public class PostController {
    @Autowired(required = true)
    public PostRepository postRepository;


    @PostMapping
    public ResponseEntity<String> newPost(@RequestBody PostModel post){
        if(post ==null|| post.getTitle() == null || post.getContent() ==null){
            return ResponseEntity.badRequest().body("Titulo e conteudo são obirgatórios");
        }
        postRepository.save(post);
        return ResponseEntity.ok("Novo post feito" + post) ;
    }

    @GetMapping
    public ResponseEntity getAllPost(){
        var allposts = postRepository.findAll();
        return ResponseEntity.ok(allposts);
    }

    @PutMapping("/")
    public ResponseEntity<String> updatePost(@RequestBody PostModel updatePostDTO) {
        if (updatePostDTO.getIdPost() == null){
            return ResponseEntity.badRequest().body("O ID é necessário para o update.");
        }
        Optional<PostModel> existingPost = postRepository.findById(updatePostDTO.getIdPost());

        if (existingPost.isPresent()) {
            var postToUpdate = this.postRepository.save(updatePostDTO);

            return ResponseEntity.ok("Post atualizado com sucesso." + postToUpdate);
        } else {
            return new ResponseEntity<>("Post não encontrado. Atualização falhou.", HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{idPost}")
    public ResponseEntity<String> deletePost(@PathVariable Long idPost) {
        if (idPost == null) {
            return ResponseEntity.badRequest().body("O ID do post é necessário para o delete");
        }
        System.out.println("Tentativa de excluir post com ID: " + idPost);

        Optional<PostModel> existingPost = postRepository.findById(idPost);
        if (existingPost.isPresent()) {
            postRepository.deleteById(idPost);
            return ResponseEntity.ok("Post excluído com sucesso.");
        } else {
            return new ResponseEntity<>("Post não encontrado. Excluir falhou.", HttpStatus.NOT_FOUND);
        }
    }

}
