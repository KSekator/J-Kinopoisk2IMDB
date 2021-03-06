package org.f0w.k2i.core.model.repository.mock;

import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MockImportProgressRepositoryImpl extends BaseMockRepository<ImportProgress>
        implements ImportProgressRepository {
    @Override
    public void saveAll(KinopoiskFile kinopoiskFile, List<Movie> movies, Config config) {
        movies.forEach(m -> save(new ImportProgress(kinopoiskFile, m, "listId", false, false)));
    }

    @Override
    public void deleteAll(KinopoiskFile kinopoiskFile) {
        storage.values()
                .stream()
                .filter(ip -> ip.getKinopoiskFile().equals(kinopoiskFile))
                .forEach(this::delete);
    }

    @Override
    public List<ImportProgress> findNotImportedOrNotRatedByFile(KinopoiskFile kinopoiskFile, String listId) {
        return storage.values()
                .stream()
                .filter(ip -> ip.getKinopoiskFile().equals(kinopoiskFile))
                .filter(ip -> ip.getListId().equals(listId))
                .filter(ip -> !ip.isRated() || !ip.isImported())
                .collect(Collectors.toList());
    }

    @Override
    public List<ImportProgress> findNotImportedByFile(KinopoiskFile kinopoiskFile, String listId) {
        return storage.values()
                .stream()
                .filter(ip -> ip.getKinopoiskFile().equals(kinopoiskFile))
                .filter(ip -> ip.getListId().equals(listId))
                .filter(ip -> !ip.isImported())
                .collect(Collectors.toList());
    }

    @Override
    public List<ImportProgress> findNotRatedByFile(KinopoiskFile kinopoiskFile) {
        return storage.values()
                .stream()
                .filter(ip -> ip.getKinopoiskFile().equals(kinopoiskFile))
                .filter(ip -> !ip.isRated())
                .collect(Collectors.toList());
    }
}
