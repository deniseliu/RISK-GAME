package edu.duke.ece651.risc.shared;

public class Army {
  private int commanderId;
  private int units;
  public Army(){
    commanderId = -1;
    units = -1;
  }
  public Army(int commanderId, int units){
    this.commanderId = commanderId;
    this.units = units;
  }
  public getCommanderId(){
    return commanderId;
  }
  public getUnits(){
    return units;
  }
  public setUnits(int units){
    this.units = units;
  }

}
