package ewm.main.compilation.controller;

import ewm.main.compilation.dto.CompilationDto;
import ewm.main.compilation.dto.NewCompilationDto;
import ewm.main.compilation.dto.UpdateCompilationRequest;
import ewm.main.compilation.service.CompilationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;

    @Autowired
    public AdminCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilation) {
        log.info("Creating new compilation - {}...", newCompilation.getTitle());
        return compilationService.createCompilation(newCompilation);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
        log.info("Compilation with id={} deleted", compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody @Valid UpdateCompilationRequest compilationRequest) {
        log.info("Updating Compilation with id={}...", compId);
        return compilationService.updateCompilation(compId, compilationRequest);
    }

}
