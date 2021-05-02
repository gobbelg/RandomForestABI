package src.main.gov.va.vha09.grecc.raptat.gg.helpers;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public enum UniqueIDGenerator {
  INSTANCE;

  private static HashSet<String> assignedValues = new HashSet<>();


  private UniqueIDGenerator() {}


  public String getUnique() {
    String candidateValue;
    do {
      candidateValue = UUID.randomUUID().toString();
      candidateValue = candidateValue.substring(candidateValue.lastIndexOf("-") + 1);
    } while (UniqueIDGenerator.assignedValues.contains(candidateValue));
    UniqueIDGenerator.assignedValues.add(candidateValue);
    return candidateValue;
  }


  public void populateWithExisting(List<String> existingValues) {
    UniqueIDGenerator.assignedValues.addAll(existingValues);
  }


  public static void main(String[] args) {
    UniqueIDGenerator theGenerator = UniqueIDGenerator.INSTANCE;
    for (int i = 0; i < 10; i++) {
      String testValue = theGenerator.getUnique();
      System.out.println(i + ":" + testValue);
    }
  }
}
