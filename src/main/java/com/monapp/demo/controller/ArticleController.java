package com.monapp.demo.controller;

import com.monapp.demo.Entity.Article;
import com.monapp.demo.Entity.Comment;
import com.monapp.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.List;

@Controller
public class ArticleController {
    
    @Autowired
    private ArticleService articleService;
    
    // ========== PAGES HTML (Thymeleaf/JSP) ==========
    
    @GetMapping("/articles")
    public String showListPage(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "articles/list";
    }
    
    @GetMapping("/list")
    public String listArticles(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "articles/list";
    }
    
    @GetMapping("/form")
    public String showFormPage(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("isEdit", false);
        return "articles/form";
    }
    
    @GetMapping("/form/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Article article = articleService.getById(id);
        model.addAttribute("article", article);
        model.addAttribute("isEdit", true);
        return "articles/form";
    }
    
    @GetMapping("/view/{id}")
    public String showArticleDetail(@PathVariable Long id, Model model) {
        Article article = articleService.getArticleWithComments(id);
        List<Comment> comments = articleService.getCommentsByArticle(id);
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        return "articles/view";
    }
    
    // ========== API REST ==========
    
    @PostMapping("/api/articles")
    public RedirectView createArticle(@ModelAttribute Article article) {
        articleService.saveArticle(article);
        return new RedirectView("/view/" + article.getId());
    }
    
    @PostMapping("/api/articles/json")
    @ResponseBody
    public Article createArticleJson(@RequestBody Article article) {
        return articleService.saveArticle(article);
    }
    
    @GetMapping("/api/articles")
    @ResponseBody
    public List<Article> getAllArticlesApi() {
        return articleService.getAllArticles();
    }
    
    @GetMapping("/api/articles/{id}")
    @ResponseBody
    public Article getArticleByIdApi(@PathVariable Long id) {
        return articleService.getById(id);
    }
    
    @PutMapping("/api/articles/{id}")
    @ResponseBody
    public Article updateArticleApi(@PathVariable Long id, @RequestBody Article article) {
        return articleService.updateArticle(id, article);
    }
    
    @DeleteMapping("/api/articles/{id}")
    @ResponseBody
    public void deleteArticleApi(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }
    
    @PostMapping("/api/articles/{id}/comments")
    @ResponseBody
    public Comment addCommentApi(@PathVariable Long id, @RequestBody Comment comment) {
        return articleService.addComment(id, comment);
    }
    
    @GetMapping("/api/articles/{id}/comments")
    @ResponseBody
    public List<Comment> getCommentsApi(@PathVariable Long id) {
        return articleService.getCommentsByArticle(id);
    }
    
    // ========== TRAITEMENT DES FORMULAIRES ==========
    
    @PostMapping("/save-article")
    public RedirectView saveArticle(@ModelAttribute Article article) {
        Article savedArticle = articleService.saveArticle(article);
        return new RedirectView("/view/" + savedArticle.getId());
    }
    
    @PostMapping("/update-article/{id}")
    public RedirectView updateArticle(@PathVariable Long id, @ModelAttribute Article article) {
        article.setId(id);
        Article updatedArticle = articleService.saveArticle(article);
        return new RedirectView("/view/" + updatedArticle.getId());
    }
    
    @PostMapping("/delete-article/{id}")
    public RedirectView deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return new RedirectView("/list");
    }
    
    @PostMapping("/add-comment/{articleId}")
    public RedirectView addComment(@PathVariable Long articleId, 
         @RequestParam String author, 
         @RequestParam String text) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setText(text);
        articleService.addComment(articleId, comment);
        return new RedirectView("/view/" + articleId);
    }
    
    @PostMapping("/delete-comment/{articleId}/{commentId}")
    public RedirectView deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        articleService.deleteComment(commentId);
        return new RedirectView("/view/" + articleId);
    }
}