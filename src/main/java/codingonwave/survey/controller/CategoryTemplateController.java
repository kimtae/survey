package codingonwave.survey.controller;

import codingonwave.survey.dto.CategoryTemplateDto;
import codingonwave.survey.repository.CategoryTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category-template")
public class CategoryTemplateController {

    private final CategoryTemplateRepository repository;

    @GetMapping
    public List<CategoryTemplateDto> getCategoryTemplates() {
        return repository.findAll();
    }

    @GetMapping("/{categoryTemplateId}")
    public CategoryTemplateDto getCategoryTemplate(@PathVariable("categoryTemplateId") Long categoryTemplateId) {
        return repository.findById(categoryTemplateId);
    }

    @PostMapping
    public void save(@RequestBody CategoryTemplateDto categoryTemplate) {
        repository.save(categoryTemplate);
    }

    @PatchMapping("/{categoryTemplateId}")
    public void update(@PathVariable("categoryTemplateId") Long categoryTemplateId,
                       @RequestParam("name") String newName) {

        repository.update(categoryTemplateId, newName);
    }

    @DeleteMapping("/{categoryTemplateId}")
    public void delete(@PathVariable("categoryTemplateId") Long categoryTemplateId) {
        repository.remove(categoryTemplateId);
    }

    @GetMapping("/remove")
    public void removeAll() {
        repository.removeAll();
    }
}
