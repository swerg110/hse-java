package hse.java.lectures.lesson7.dau;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DauServiceImpl implements DauService {

    private final Clock clock;
    private final ConcurrentHashMap<LocalDate, ConcurrentHashMap<Integer, Set<Integer>>> data = new ConcurrentHashMap<>();

    public DauServiceImpl() {
        this(Clock.systemDefaultZone());
    }

    public DauServiceImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void postEvent(Event event) {
        LocalDate today = LocalDate.now(clock);
        data.computeIfAbsent(today, k -> new ConcurrentHashMap<>())
            .computeIfAbsent(event.authorId(), k -> ConcurrentHashMap.newKeySet())
            .add(event.userId());
    }

    @Override
    public Map<Integer, Long> getDauStatistics(List<Integer> authorIds) {
        LocalDate yesterday = LocalDate.now(clock).minusDays(1);
        Map<Integer, Long> result = new HashMap<>();
        var yesterdayData = data.getOrDefault(yesterday, new ConcurrentHashMap<>());
        for (int authorId : authorIds) {
            Set<Integer> users = yesterdayData.get(authorId);
            result.put(authorId, users == null ? 0L : (long) users.size());
        }
        return result;
    }

    @Override
    public Long getAuthorDauStatistics(int authorId) {
        LocalDate yesterday = LocalDate.now(clock).minusDays(1);
        var yesterdayData = data.getOrDefault(yesterday, new ConcurrentHashMap<>());
        Set<Integer> users = yesterdayData.get(authorId);
        return users == null ? 0L : (long) users.size();
    }
}
