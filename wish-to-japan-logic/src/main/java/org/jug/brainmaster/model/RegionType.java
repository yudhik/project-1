package org.jug.brainmaster.model;


//TODO: use prize list to populate region size
public enum RegionType {
  REGION_1("region_1"), REGION_2("region_2"), REGION_3("region_3");

  private String name;

  private RegionType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
