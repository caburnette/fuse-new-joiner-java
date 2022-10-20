package org.galatea.starter.utils.clock;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class ClockWrapper {

  private static Clock clock = Clock.systemDefaultZone();
  private static ZoneId zoneId = ZoneId.systemDefault();

  public static void useFixedClockAt(String date) {
    LocalDate providedDate = LocalDate.parse(date);
    clock = Clock.fixed(providedDate.atStartOfDay().toInstant(ZoneOffset.UTC), zoneId);
  }

  public static void useSystemDefaultZoneClock() {
    clock = Clock.systemDefaultZone();
  }

  public static Clock getClock() {
    return clock;
  }
}
