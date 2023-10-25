package ewm.main.compilation.map;

import ewm.main.compilation.dto.CompilationDto;
import ewm.main.compilation.dto.NewCompilationDto;
import ewm.main.compilation.model.Compilation;
import ewm.main.event.map.EventMapper;
import ewm.main.event.model.Event;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto newCompilation, List<Event> events) {
        return new Compilation(events,
                newCompilation.getPinned() != null ? newCompilation.getPinned() : false,
                newCompilation.getTitle());
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(compilation.getEvents().stream().map(EventMapper::toEventShortDto)
                .collect(Collectors.toList()));
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }
}
