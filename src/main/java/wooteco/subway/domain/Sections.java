package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Sections {

    private final Set<Section> sections;

    public Sections(Set<Section> sections) {
        this.sections = new HashSet<>(sections);
    }

    public Sections(Section section) {
        this(new HashSet<>(Collections.singletonList(section)));
    }

    public Set<Section> values() {
        return new HashSet<>(sections);
    }

    public Station firstStation() {
        return firstSection().getUpStation();
    }

    private Section firstSection() {
        return sections.stream()
            .filter(section -> sections.stream()
                .noneMatch(section1 -> section.getUpStation().equals(section1.getDownStation())))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("첫 번째 구간이 존재하지 않습니다."));
    }

    public Station lastStation() {
        return lastSection().getDownStation();
    }

    private Section lastSection() {
        return sections.stream()
            .filter(section -> sections.stream()
                .noneMatch(section1 -> section.getDownStation().equals(section1.getUpStation())))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("마지막 구간이 존재하지 않습니다."));
    }

    public int count() {
        return sections.size();
    }

    public boolean hasNotStation(Station station) {
        return sections.stream()
            .noneMatch(section -> section.hasStation(station));
    }

    public List<Section> sectionsWithStation(Station station) {
        return sections.stream()
            .filter(section -> section.hasStation(station))
            .collect(Collectors.toList());
    }

    public Distance totalDistance() {
        return new Distance(sections.stream()
            .mapToInt(section -> section.getDistance().value())
            .sum());
    }

    public List<Station> path() {
        Section now = firstSection();
        Section last = lastSection();
        List<Station> result = new ArrayList<>(Collections.singletonList(now.getUpStation()));

        while (!now.equals(last)) {
            result.add(now.getDownStation());
            now = nextSection(now);
        }
        result.add(now.getDownStation());
        return result;
    }

    private Section nextSection(Section now) {
        return sections.stream()
            .filter(section -> now.getDownStation().equals(section.getUpStation()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("다음 구간이 존재하지 않습니다."));
    }
}
