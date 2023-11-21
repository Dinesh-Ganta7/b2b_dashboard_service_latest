package com.stg.b2b.dashboard.dto;

public interface PositionCountByRegionAndType {
    String getRegion();

    String getPositionType();

    int getNoOfVacancy();
}
