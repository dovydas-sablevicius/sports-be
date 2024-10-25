package com.sourcery.sport.user.dto;


import com.sourcery.sport.helpers.IntegerHelpers;

public class UserStatisticsDto {

  private final int participated;
  private final int participating;
  private final int won;
  public UserStatisticsDto(Integer participated,Integer participating, Integer won) {
    this.participated = IntegerHelpers.returnNumberOrZero(participated);
    this.participating = IntegerHelpers.returnNumberOrZero(participating);
    this.won = IntegerHelpers.returnNumberOrZero(won);
  }
}
