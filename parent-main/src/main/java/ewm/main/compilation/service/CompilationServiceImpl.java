package ewm.main.compilation.service;

import ewm.main.compilation.dto.CompilationDto;
import ewm.main.compilation.dto.NewCompilationDto;
import ewm.main.compilation.dto.UpdateCompilationRequest;
import ewm.main.compilation.map.CompilationMapper;
import ewm.main.compilation.model.Compilation;
import ewm.main.compilation.repository.CompilationRepositiry;
import ewm.main.error.NotFoundException;
import ewm.main.event.model.Event;
import ewm.main.event.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepositiry compilationRepositiry;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepositiry compilationRepositiry,
                                  EventRepository eventRepository) {
        this.compilationRepositiry = compilationRepositiry;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilation) {
        List<Event> events = Collections.emptyList();
        if (newCompilation.getEvents() != null) {
            events = eventRepository.findAllById(newCompilation.getEvents());
        }
        Compilation compilation = CompilationMapper.toCompilation(newCompilation, events);
        compilation = compilationRepositiry.save(compilation);
        log.info("Compilation saved with id={}.", compilation.getId());
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        try {
            compilationRepositiry.deleteById(compId);
            log.info("Compilation with id={} deleted", compId);
        } catch (EmptyResultDataAccessException e) {
            log.info("Compilation with id={} was not found", compId);
            throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilationRequest) {
        Compilation compilation = compilationRepositiry.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        if (compilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(compilationRequest.getEvents());
            compilation.setEvents(events);
        }
        if (compilationRequest.getPinned() != null) {
            compilation.setPinned(compilationRequest.getPinned());
        }
        if (compilationRequest.getTitle() != null) {
            compilation.setTitle(compilationRequest.getTitle());
        }
        compilationRepositiry.save(compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = compilationRepositiry.findAll();
        if (pinned != null) {
            compilations = compilations.stream().filter(c -> c.getPinned().equals(pinned)).collect(Collectors.toList());
        }

        int start;
        int end = from + size;

        if (from < compilations.size()) {
            start = from;
        } else return Collections.emptyList();
        if (end >= compilations.size()) {
            end = compilations.size();
        }
        compilations = compilations.subList(start, end);
        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepositiry.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        return CompilationMapper.toCompilationDto(compilation);
    }
}
