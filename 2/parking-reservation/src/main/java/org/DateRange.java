package org;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class DateRange {

    private final LocalDateTime start;
    private final LocalDateTime end;

    public DateRange(LocalDateTime start, int days) {
        this.start = start;
        this.end = start.plusDays(days);
    }

    public boolean isDatesOverlaps(DateRange source) {
        return !(this.end.isEqual(source.start) || this.end.isBefore(source.start)
                || source.end.isEqual(this.start) || source.end.isBefore(this.start));
    }
}
